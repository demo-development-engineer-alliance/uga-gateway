
package com.pengzexuan.uga.common.exceptions;


import com.pengzexuan.uga.common.enums.UgaResponseCode;

/**
 * Uga ResponseException
 *
 * <p>所有的响应异常基础定义</p>
 *
 * @author pengzexuan
 * @since 1.0
 */
@SuppressWarnings({"unused"})
public class UgaResponseException extends UgaBaseException {


    public UgaResponseException() {
        this(UgaResponseCode.INTERNAL_ERROR);
    }

    public UgaResponseException(UgaResponseCode code) {
        super(code.getMessage(), code);
    }

    public UgaResponseException(Throwable cause, UgaResponseCode code) {
        super(code.getMessage(), cause, code);
    }

}
