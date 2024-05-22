package com.ssafy.firskorea.plan.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.firskorea.plan.dto.PlanFileDto;
import com.ssafy.firskorea.plan.dto.PlanMemoDto;
import com.ssafy.firskorea.plan.dto.RegionDto;
import com.ssafy.firskorea.plan.dto.request.AttractionPerDate;
import com.ssafy.firskorea.plan.dto.request.PlanRequest;
import com.ssafy.firskorea.plan.dto.response.AttractionForPlan;
import com.ssafy.firskorea.plan.dto.response.PlanAndAttractionDto;
import com.ssafy.firskorea.plan.dto.response.PlanResponse;
import com.ssafy.firskorea.plan.mapper.PlanMapper;

@Service
public class PlanServiceImpl implements PlanService {
	@Autowired
	private PlanMapper planMapper;

	@Override
	public List<RegionDto> getRegionList() throws SQLException {
		return planMapper.getRegionList();
	}

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
	@Transactional
	public PlanResponse getCompletePlanner(int planId) throws SQLException {
		PlanResponse planResponse = new PlanResponse(planId);

		// planId랑 paaId랑 contentId를 가져온다.
		List<PlanAndAttractionDto> paaDtos = planMapper.getPlanAndAttractions(planId);
		if (paaDtos != null) {
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

}
