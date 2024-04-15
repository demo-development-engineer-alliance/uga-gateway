package com.pengzexuan.uga.common.exceptions;


import com.pengzexuan.uga.common.enums.UgaResponseCode;
import lombok.Getter;

/**
 * UgaBaseException
 * <p>网关最基础的异常定义类</p>
 *
 * @author pengzexuan
 * @since 1.0
 */
@Getter
@SuppressWarnings({"unused"})
public class UgaBaseException extends RuntimeException {

    private static final long serialVersionUID = -5658789202563433456L;

    public UgaBaseException(UgaResponseCode code) {
        this.code = code;
    }

    protected final UgaResponseCode code;

    public UgaBaseException(String message, UgaResponseCode code) {
        super(message);
        this.code = code;
    }

    public UgaBaseException(String message, Throwable cause, UgaResponseCode code) {
        super(message, cause);
        this.code = code;
    }

    public UgaBaseException(UgaResponseCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public UgaBaseException(String message, Throwable cause,
                            boolean enableSuppression, boolean writableStackTrace, UgaResponseCode code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

}
