package com.ssafy.firskorea.plan.mapper;

import com.ssafy.firskorea.plan.dto.request.PlanThumbnailDto;
import com.ssafy.firskorea.plan.dto.response.PlanInfoDto;

import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.firskorea.plan.dto.request.PlanMemoDto;
import com.ssafy.firskorea.plan.dto.response.PlanAttractionDetailsDto;
import com.ssafy.firskorea.plan.dto.response.PlanAndAttractionDto;

@Mapper
public interface PlanMapper {

    int insertPlan(Map<String, Object> plan) throws SQLException;

    int insertPlanAndAttraction(Map<String, Object> planAndAttraction) throws SQLException;

    void insertPlanThumbnail(PlanThumbnailDto dto) throws SQLException;

    List<PlanAndAttractionDto> getPlanAndAttractions(int planId) throws SQLException;

    PlanAttractionDetailsDto planAttractionDetails(PlanAndAttractionDto dto) throws SQLException;

    void updatePlanMemo(PlanMemoDto memoDto) throws SQLException;

    List<PlanInfoDto> retrievePaginatedPlans(Map<String, Object> map) throws SQLException;

    int getTotalPlanCount(String memberId) throws SQLException;

    void deletePlan(String planId) throws SQLException;
}
