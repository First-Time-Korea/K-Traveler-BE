package com.ssafy.firskorea.domain.attraction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor
public class PaginatedAttractionsDto {
    private List<AttractionDto> attractions;
    private int currentPage;
    private int totalPageCount;
}
