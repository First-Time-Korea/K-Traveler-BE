package com.ssafy.firskorea.attraction.dto.request;

import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.exception.KTravelerException;
import com.ssafy.firskorea.config.ValidationConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "SidoPgnoDto", description = "해당 지역의 전체 관광지 목록을 조회할 때 페이지네이션")
public class SidoPgnoDto {
    @Schema(required = true, description = "지역 시도 코드")
    private String sidocode;
    @Schema(description = "페이지 번호")
    private String pgno;

    public SidoPgnoDto(String sidocode, String pgno) {
        validateSidoCode(sidocode);
        validatePgno(pgno);
        this.sidocode = sidocode;
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

    private void validateSidoCode(String sidocode) {
        if (sidocode != null && sidocode.matches(ValidationConfig.SIDO_CODE_FORMAT.getStringValue())) {
            return;
        }
        throw new KTravelerException(RetConsts.ERR410);
    }
}
