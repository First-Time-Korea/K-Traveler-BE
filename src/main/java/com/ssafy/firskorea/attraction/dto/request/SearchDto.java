package com.ssafy.firskorea.attraction.dto.request;

import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.exception.KTravelerException;
import com.ssafy.firskorea.config.ValidationConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "SearchDto", description = "조건으로 목록을 필터링 할 때 사용")
public class SearchDto {
    @Schema(description = "회원 아이디", nullable = true) // nullable 속성 추가
    private String memberId;

    @Schema(description = "검색 키워드", nullable = true)
    private String keyword;

    @Schema(description = "지역 시도 코드", required = true, defaultValue = "1")
    private String sidoCode;

    @Schema(description = "테마  코드", required = true, defaultValue = "A")
    private Character themeCode;

    @Schema(description = "테마의 하위 카테고리 코드", nullable = true)
    private String categoryCode;

    @Schema(description = "관광지 아이디", nullable = true)
    private String contentId;

    public SearchDto(String memberId, String keyword, String sidoCode, Character themeCode, String categoryCode, String contentId) {
        this.memberId = memberId;
        this.keyword = keyword;
        this.sidoCode = sidoCode;
        this.themeCode = themeCode;
        this.categoryCode = categoryCode;
        this.contentId = contentId;

        if (memberId != null) validateMemberId(memberId);
        else this.memberId = "";
        if (contentId != null) validateContentId(contentId);
        else this.contentId = "";
        if (categoryCode != null) validateCategoryCode(categoryCode);
        else this.categoryCode = "";
        if (keyword == null) this.keyword = "";

        validateThemeCode(String.valueOf(themeCode));
        validateSidoCode(sidoCode);
    }

    private void validateMemberId(String memberId) {
        if (memberId.length() > ValidationConfig.MEMBER_ID_MIN_LENGTH.getIntValue() &&
                memberId.length() < ValidationConfig.MEMBER_ID_MAX_LENGTH.getIntValue()) {
            return;
        }
        throw new KTravelerException(RetConsts.ERR410);
    }

    private void validateSidoCode(String sidoCode) {
        if (sidoCode.matches(ValidationConfig.SIDO_CODE_FORMAT.getStringValue())) {
            return;
        }
        throw new KTravelerException(RetConsts.ERR410);
    }

    private void validateContentId(String contentId) {
        if (contentId.matches(ValidationConfig.CONTENT_ID_FORMAT.getStringValue())) {
            return;
        }
        throw new KTravelerException(RetConsts.ERR410);
    }

    private void validateCategoryCode(String categoryCode) {
        if (categoryCode != null && categoryCode.matches(ValidationConfig.CATEGORY_CODE_FORMAT.getStringValue())) {
            return;
        }
        throw new KTravelerException(RetConsts.ERR410);
    }

    private void validateThemeCode(String themeCode) {
        if (themeCode != null && themeCode.matches(ValidationConfig.THEME_CODE_FORMAT.getStringValue())) {
            return;
        }
        throw new KTravelerException(RetConsts.ERR410);
    }
}
