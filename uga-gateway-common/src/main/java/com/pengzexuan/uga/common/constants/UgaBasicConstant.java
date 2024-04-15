package com.pengzexuan.uga.common.constants;

import org.jetbrains.annotations.Contract;

import java.util.regex.Pattern;

/**
 * UgaBasicConstant
 * <p>基础常量类</p>
 *
 * @author pengzexuan
 * @since 1.0
 */
@SuppressWarnings({"unused"})
public class UgaBasicConstant {

    private UgaBasicConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String PATH_SEPARATOR = "/";

    public static final String PATH_PATTERN = "/**";

    public static final String QUESTION_SEPARATOR = "?";

    public static final String ASTERISK_SEPARATOR = "*";

    public static final String AND_SEPARATOR = "&";

    public static final String EQUAL_SEPARATOR = "=";

    public static final String BLANK_SEPARATOR_1 = "";

    public static final String BLANK_SEPARATOR_2 = " ";

    public static final String COMMA_SEPARATOR = ",";

    public static final String SEMICOLON_SEPARATOR = ";";

    public static final String DOLLAR_SEPARATOR = "$";

    public static final String PIPELINE_SEPARATOR = "|";

    public static final String BAR_SEPARATOR = "-";

    public static final String COLON_SEPARATOR = ":";

    public static final String DIT_SEPARATOR = ".";

    @SuppressWarnings("HttpUrlsUsage")
    public static final String HTTP_PREFIX_SEPARATOR = "http://";

    public static final String HTTPS_PREFIX_SEPARATOR = "https://";

    public static final String HTTP_FORWARD_SEPARATOR = "X-Forwarded-For";

    public static final Pattern PARAM_PATTERN = Pattern.compile("\\{(.*?)}");

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String ENABLE = "Y";

    public static final String DISABLE = "N";
}
