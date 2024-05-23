package com.ssafy.firskorea.attraction.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AttractionDto {
    private int contentId;
    private String title;
    private String addr1;
    private String addr2;
    private String firstImage;
    private String sidoCode;
    private String sidoName;
    private String gugunCode;
    private String themeCode;
    private String themeName;
    private String categoryCode;
    private String categoryName;
    private double latitude;
    private double longitude;
    private String overView;
    private int bookmarkId; //북마크 했으면 값 o, 없으면 null
}