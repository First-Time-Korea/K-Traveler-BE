package com.ssafy.firskorea.domain.plan.service;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.firskorea.domain.plan.dto.request.PlanCreationDto;
import com.ssafy.firskorea.domain.plan.dto.request.PlanMemberPgnoDto;
import com.ssafy.firskorea.domain.plan.dto.request.PlanMemoDto;
import com.ssafy.firskorea.domain.plan.dto.response.PaginatedPlansDto;
import com.ssafy.firskorea.domain.plan.dto.response.PlanDetailsDto;

public interface PlanService {

	void createPlan(PlanCreationDto planCreationDto) throws SQLException;
	void createPlanV2(PlanCreationDto planCreationDto) throws SQLException, JsonProcessingException;

	PaginatedPlansDto getPaginatedPlans(PlanMemberPgnoDto planMemberPgnoDto) throws SQLException;

	PlanDetailsDto getPlanDetails(int planId) throws SQLException;

	void updatePlanMemos(List<PlanMemoDto> memoList) throws SQLException;

	void deletePlan(String planId) throws SQLException;
}
