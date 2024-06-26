package com.ssafy.firskorea.plan.mapper;

import com.ssafy.firskorea.plan.dto.PlanFileDto;
import com.ssafy.firskorea.plan.dto.RegionDto;
import com.ssafy.firskorea.plan.dto.response.PlanInfoDto;

import org.apache.ibatis.annotations.Mapper;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.firskorea.plan.dto.PlanMemoDto;
import com.ssafy.firskorea.plan.dto.response.AttractionForPlan;
import com.ssafy.firskorea.plan.dto.response.PlanAndAttractionDto;

@Mapper
public interface PlanMapper {
	List<RegionDto> getRegionList() throws SQLException;

	int insertPlan(Map<String, Object> plan) throws SQLException;

	int insertPlanAndAttraction(Map<String, Object> planAndAttraction) throws SQLException;

	void insertPlanFile(PlanFileDto dto) throws SQLException;

	List<PlanInfoDto> getPlanInfos(Map<String, Object> map) throws Exception;

	int getTotalPlanCount(String memberId) throws Exception;

	List<PlanAndAttractionDto> getPlanAndAttractions(int planId) throws SQLException;

	AttractionForPlan getAttractionForPlan(PlanAndAttractionDto dto) throws SQLException;

	void updateMemo(PlanMemoDto memoDto) throws SQLException;

    void deletePlan(String planId) throws SQLException;
}
