package com.ssafy.firskorea.plan.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.dto.response.PaginatedAttractionsDto;
import com.ssafy.firskorea.plan.dto.request.MemberPgnoDto;
import com.ssafy.firskorea.plan.dto.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.firskorea.plan.dto.PlanFileDto;
import com.ssafy.firskorea.plan.dto.PlanMemoDto;
import com.ssafy.firskorea.plan.dto.request.AttractionPerDate;
import com.ssafy.firskorea.plan.dto.request.PlanRequest;
import com.ssafy.firskorea.plan.mapper.PlanMapper;
import com.ssafy.firskorea.util.SizeConstant;

@Service
public class PlanServiceImpl implements PlanService {
	@Autowired
	private PlanMapper planMapper;

	@Value("${planThumbFile.path.upload-images}")
	private String uploadImagesPath;

	@Override
	@Transactional
	public void registerPlanner(PlanRequest dto) throws SQLException {
		Map<String, Object> plan = new HashMap<>();
		plan.put("memberId", dto.getMemberId());
		plan.put("title", dto.getTitle());
		planMapper.insertPlan(plan);

		String planId = String.valueOf(plan.get("id"));
		PlanFileDto fileDto = dto.getPlanFileDto();
		if (fileDto != null) {
			fileDto.setPlanId(planId);
			planMapper.insertPlanFile(fileDto);
		}

		for (AttractionPerDate apd : dto.getAttractionsPerDate()) {
			LocalDateTime date = apd.getDate();
			for (String contentId : apd.getContentId()) {
				Map<String, Object> pnaMap = new HashMap<>();
				pnaMap.put("planId", planId);
				pnaMap.put("date", date);
				pnaMap.put("contentId", contentId);
				planMapper.insertPlanAndAttraction(pnaMap);
			}
		}
	}

	@Override
	public PaginatedPlansDto getPlanInfos(MemberPgnoDto memberPgnoDto) throws Exception {
		int pgNo = Integer.parseInt(memberPgnoDto.getPgno() == null ? "1" : memberPgnoDto.getPgno());
		int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;

		String memberId = memberPgnoDto.getMemberId();
		List<PlanInfoDto> planInfos = planMapper.getPlanInfos(Map.of(
				"memberId", memberId,
				"start", start,
				"listsize", SizeConstant.LIST_SIZE
		));

		int currentPage = Integer.parseInt(memberPgnoDto.getPgno() == null ? "1" : memberPgnoDto.getPgno());
		int sizePerPage = SizeConstant.LIST_SIZE;
		int totalAttractions = planMapper.getTotalPlanCount(memberPgnoDto.getMemberId());
		int totalPageCount = (totalAttractions - 1) / sizePerPage + 1;
		return new PaginatedPlansDto(planInfos, currentPage, totalPageCount);
	}

	@Override
	@Transactional
	public PlanResponse getCompletePlanner(int planId) throws SQLException {
		PlanResponse planResponse = new PlanResponse(planId);

		// planId랑 paaId랑 contentId를 가져온다.
		List<PlanAndAttractionDto> paaDtos = planMapper.getPlanAndAttractions(planId);
		if (paaDtos.size() != 0) {
			String planTitle = paaDtos.get(0).getPlanTitle();
			planResponse.setPlanTitle(planTitle);
		}

		List<AttractionForPlan> attractions = new ArrayList<>();
		// paaId랑 contentId를 기반으로 Attraction 정보를 가져온다.
		for (PlanAndAttractionDto dto : paaDtos) {
			attractions.add(planMapper.getAttractionForPlan(dto));
		}

		Map<String, List<AttractionForPlan>> map = new HashMap<>();
		for (AttractionForPlan afp : attractions) {
			String dateKey = afp.getDate().toString();
			map.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(afp);
		}

		planResponse.setAttractions(map);
		return planResponse;
	}

	@Override
	@Transactional
	public void updateMemo(List<PlanMemoDto> memoList) throws SQLException {
		for (PlanMemoDto memoDto : memoList) {
			planMapper.updateMemo(memoDto);
		}
	}

	@Override
	public void deletePlan(String planId) throws SQLException {
		planMapper.deletePlan(planId);
	}

}
