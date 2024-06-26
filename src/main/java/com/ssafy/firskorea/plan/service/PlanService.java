package com.ssafy.firskorea.plan.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.firskorea.plan.dto.PlanMemoDto;
import com.ssafy.firskorea.plan.dto.RegionDto;
import com.ssafy.firskorea.plan.dto.request.PlanRequest;
import com.ssafy.firskorea.plan.dto.response.PlanResponse;

public interface PlanService {
	List<RegionDto> getRegionList() throws SQLException;

	void registerPlanner(PlanRequest planRequest) throws SQLException;
	
	Map<String, Object> getPlanInfos(Map<String, String> map) throws Exception;
	
	byte[] getPlanFile(String src) throws Exception;

	PlanResponse getCompletePlanner(int planId) throws SQLException;

	void updateMemo(List<PlanMemoDto> memoList) throws SQLException;

	void deletePlan(String planId) throws SQLException;
}
