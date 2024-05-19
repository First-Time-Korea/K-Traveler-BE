package com.ssafy.firskorea.plan.service;

import com.ssafy.firskorea.plan.dto.RegionDto;

import java.sql.SQLException;
import java.util.List;

public interface PlanService {
    List<RegionDto> getRegionList() throws SQLException;
}
