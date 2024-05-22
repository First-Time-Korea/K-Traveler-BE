package com.ssafy.firskorea.attraction.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SearchDto {
    private String memberId;
    private String keyword;
    private int sidoCode;
    private Character themeCode;
    private String categoryCode;
    private int contentId;
}
