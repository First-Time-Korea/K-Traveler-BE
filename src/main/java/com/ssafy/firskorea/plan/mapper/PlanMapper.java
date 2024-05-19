package com.ssafy.firskorea.plan.mapper;

import com.ssafy.firskorea.plan.dto.RegionDto;
import org.apache.ibatis.annotations.Mapper;
import java.sql.SQLException;
import java.util.List;

@Mapper
public interface PlanMapper {
    List<RegionDto> getRegionList() throws SQLException;
}
