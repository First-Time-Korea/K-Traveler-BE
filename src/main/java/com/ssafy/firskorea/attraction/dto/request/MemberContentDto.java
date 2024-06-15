package com.ssafy.firskorea.attraction.dto.request;

import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.exception.KTravelerException;
import com.ssafy.firskorea.config.ValidationConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "MemberContentDto", description = "회원의 관광지에 대한 상태를 알아야 할 때 사용 ex) 북마크")
public class MemberContentDto {
    @Schema(required = true, description = "회원 아이디")
    private String memberId;
    @Schema(required = true, description = "관광지 아이디")
    private String contentId;

    public MemberContentDto(String memberId, String contentId) {
        validateMemberId(memberId);
        validateContentId(contentId);
        this.memberId = memberId;
        this.contentId = contentId;
    }

    private void validateMemberId(String memberId) {
        if (memberId != null && memberId.length() > ValidationConfig.MEMBER_ID_MIN_LENGTH.getIntValue() &&
                memberId.length() < ValidationConfig.MEMBER_ID_MAX_LENGTH.getIntValue()) {
            return;
        }
        throw new KTravelerException(RetConsts.ERR410);
    }

    private void validateContentId(String contentId) {
        if (contentId != null && contentId.matches(ValidationConfig.CONTENT_ID_FORMAT.getStringValue())) {
            return;
        }
        throw new KTravelerException(RetConsts.ERR410);
    }
}
