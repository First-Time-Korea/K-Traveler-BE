package com.ssafy.firskorea.plan.service;

import com.ssafy.firskorea.plan.dto.RegionDto;
import com.ssafy.firskorea.plan.mapper.PlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanMapper planMapper;

    @Override
    public List<RegionDto> getRegionList() throws SQLException {
        return planMapper.getRegionList();
    }
}
