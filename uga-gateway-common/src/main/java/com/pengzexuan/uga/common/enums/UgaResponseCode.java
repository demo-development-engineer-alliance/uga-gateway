package com.pengzexuan.uga.common.enums;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;

/**
 * 
 * Uga Response Code
 *
 * @author pengzexuan
 * @since 1.0
 */
@Getter
@SuppressWarnings("unused")
public enum UgaResponseCode {

    SUCCESS(HttpResponseStatus.OK, 0, "success"),
    INTERNAL_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR, 1000, "Internal gateway error"),
    SERVICE_UNAVAILABLE(HttpResponseStatus.SERVICE_UNAVAILABLE, 2000, "The service is temporarily unavailable. Please try again later"),

    REQUEST_PARSE_ERROR(HttpResponseStatus.BAD_REQUEST, 10000, "Request parsing error, uniqueId parameter must exist in the header"),
    REQUEST_PARSE_ERROR_NO_UNIQUE(HttpResponseStatus.BAD_REQUEST, 10001, "Request parsing error, uniqueId parameter must exist in the header"),
    PATH_NO_MATCHED(HttpResponseStatus.NOT_FOUND,10002, "No matching path found, request failed quickly"),
    SERVICE_DEFINITION_NOT_FOUND(HttpResponseStatus.NOT_FOUND,10003, "No corresponding service definition found"),
    SERVICE_INVOKER_NOT_FOUND(HttpResponseStatus.NOT_FOUND,10004, "No corresponding calling instance found"),
    SERVICE_INSTANCE_NOT_FOUND(HttpResponseStatus.NOT_FOUND,10005, "No corresponding service instance found"),
    FILTER_CONFIG_PARSE_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR,10006, "Filter configuration parsing exception"),

    HTTP_RESPONSE_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR, 10030, "Service return exception"),

    DUBBO_DISPATCH_CONFIG_EMPTY(HttpResponseStatus.INTERNAL_SERVER_ERROR, 10016, "Routing configuration cannot be empty"),
    DUBBO_PARAMETER_TYPE_EMPTY(HttpResponseStatus.BAD_REQUEST, 10017, "The requested parameter type cannot be empty"),
    DUBBO_PARAMETER_VALUE_ERROR(HttpResponseStatus.BAD_REQUEST, 10018, "Request parameter parsing error"),
    DUBBO_METHOD_NOT_FOUNT(HttpResponseStatus.NOT_FOUND, 10021, "Method does not exist"),
    DUBBO_CONNECT_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR, 10022, "Downstream service encountered an exception, please try again later"),
    DUBBO_REQUEST_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR, 10028, "Service request exception"),
    DUBBO_RESPONSE_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR, 10029, "Service return exception"),
    VERIFICATION_FAILED(HttpResponseStatus.BAD_REQUEST,10030, "Request parameter validation failed"),
    BLACKLIST(HttpResponseStatus.FORBIDDEN,10004, "Request IP to be on blacklist"),
    WHITELIST(HttpResponseStatus.FORBIDDEN,10005, "Request IP not on whitelist")

    ;

    private final HttpResponseStatus httpResponseStatus;
    private final Integer code;
    private final String message;

    UgaResponseCode(HttpResponseStatus httpResponseStatus, Integer code, String msg) {
        this.httpResponseStatus = httpResponseStatus;
        this.code = code;
        this.message = msg;
    }

}
