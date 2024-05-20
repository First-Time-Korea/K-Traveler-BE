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

}
