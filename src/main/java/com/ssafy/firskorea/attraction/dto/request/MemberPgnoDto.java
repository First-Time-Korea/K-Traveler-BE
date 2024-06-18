package com.ssafy.firskorea.attraction.dto.request;

import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.exception.KTravelerException;
import com.ssafy.firskorea.config.ValidationConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "MemberPgnoDto", description = "회원의 북마크된 관광지 목록을 조회할 때 페이지네이션")
public class MemberPgnoDto {
    @Schema(required = true, description = "회원 아이디")
    private String memberId;
    @Schema(description = "페이지 번호")
    private String pgno;

    public MemberPgnoDto(String memberId, String pgno) {
        validateMemberId(memberId);
        validatePgno(pgno);
        this.memberId = memberId;
        this.pgno = pgno;
    }

    private void validatePgno(String pgno) {
        try {
            if (pgno != null) {
                Integer.parseInt(pgno);
            }
        } catch (NumberFormatException e) {
            throw new KTravelerException(RetConsts.ERR410);
        }
    }

    private void validateMemberId(String memberId) {
        if (memberId != null && memberId.length() > ValidationConfig.MEMBER_ID_MIN_LENGTH.getIntValue() &&
                memberId.length() < ValidationConfig.MEMBER_ID_MAX_LENGTH.getIntValue()) {
            return;
        }
        throw new KTravelerException(RetConsts.ERR410);
    }
}
