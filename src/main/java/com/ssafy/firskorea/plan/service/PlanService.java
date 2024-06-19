package com.ssafy.firskorea.plan.service;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.firskorea.plan.dto.request.PlanMemoDto;
import com.ssafy.firskorea.plan.dto.request.PlanMemberPgnoDto;
import com.ssafy.firskorea.plan.dto.request.PlanCreationDto;
import com.ssafy.firskorea.plan.dto.response.PaginatedPlansDto;
import com.ssafy.firskorea.plan.dto.response.PlanDetailsDto;

public interface PlanService {

	void createPlan(PlanCreationDto planCreationDto) throws SQLException;

	PaginatedPlansDto getPaginatedPlans(PlanMemberPgnoDto planMemberPgnoDto) throws SQLException;

	PlanDetailsDto getPlanDetails(int planId) throws SQLException;

	void updatePlanMemos(List<PlanMemoDto> memoList) throws SQLException;

	void deletePlan(String planId) throws SQLException;
}
