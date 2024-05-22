package com.ssafy.firskorea.plan.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.firskorea.plan.dto.PlanFileDto;
import com.ssafy.firskorea.plan.dto.RegionDto;
import com.ssafy.firskorea.plan.dto.request.AttractionPerDate;
import com.ssafy.firskorea.plan.dto.request.PlanRequest;
import com.ssafy.firskorea.plan.dto.response.PlanInfoDto;
import com.ssafy.firskorea.plan.mapper.PlanMapper;
import com.ssafy.firskorea.util.SizeConstant;

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
	public Map<String, Object> getPlanInfos(Map<String, String> map) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		// 여행 계획 정보 리스트 가져오기
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("memberId", map.get("memberId"));
		int pgNo = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
		param.put("start", start);
		param.put("listsize", SizeConstant.LIST_SIZE);
		
		List<PlanInfoDto> planInfos = planMapper.getPlanInfos(param);
		
		result.put("planInfos", planInfos);
		
		// 페이지네비게이션 계산하기
		int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int sizePerPage = SizeConstant.LIST_SIZE;
		int totalArticleCount = planMapper.getTotalPlanCount(map.get("memberId"));
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		result.put("currentPage", currentPage);
		result.put("totalPageCount", totalPageCount);

		return result;
	}

}
