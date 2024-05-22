package com.ssafy.firskorea.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegionDto {
    private String sidoCode;
    private String sidoName;
//    private String gugunName;
//    private String gugunCode;
    private String sidoDescription;
    private String sidoImage;
}
