package com.ssafy.firskorea.plan.service;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.firskorea.plan.dto.RegionDto;
import com.ssafy.firskorea.plan.dto.request.PlanRequest;

public interface PlanService {
	List<RegionDto> getRegionList() throws SQLException;

	void registerPlanner(PlanRequest planRequest) throws SQLException;

}
