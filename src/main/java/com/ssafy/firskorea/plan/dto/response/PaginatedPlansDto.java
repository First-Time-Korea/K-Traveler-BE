package com.ssafy.firskorea.plan.dto.response;

import com.ssafy.firskorea.plan.dto.PlanInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.*;

@Getter
@AllArgsConstructor
public class PaginatedPlansDto {
    private List<PlanInfoDto> planInfos;
    private int currentPage;
    private int totalPageCount;
}

