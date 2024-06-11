package com.ssafy.firskorea.common.exception;

import com.ssafy.firskorea.common.consts.RetConsts;
import lombok.Getter;

@Getter
public class KTravelerException extends RuntimeException {
    private RetConsts retConsts;

    public KTravelerException(RetConsts retConsts) {
        super(retConsts.getDescription());
        this.retConsts = retConsts;
    }
}
