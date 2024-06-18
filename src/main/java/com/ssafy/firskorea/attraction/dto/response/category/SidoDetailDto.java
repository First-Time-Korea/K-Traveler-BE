package com.ssafy.firskorea.attraction.dto.response.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class SidoDetailDto {
    private String sidoCode;
    private String sidoName;
    private String sidoDescription;
    private String sidoImage;
}
