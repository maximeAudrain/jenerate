package org.jenerate.internal.domain.method.content.tostring;

import java.util.EnumSet;

public enum ToStringStyle {

    NO_STYLE,

    DEFAULT_STYLE,
    MULTI_LINE_STYLE,
    NO_FIELD_NAMES_STYLE,
    SHORT_PREFIX_STYLE,
    SIMPLE_STYLE;

    public static final String DOT_STRING = ".";
    private static final String COMMONS_LANG_PREFIX = "org.apache.commons.lang";
    private static final String BUILDER_STRING = ".builder.";
    private static final String TO_STRING_STYLE = "ToStringStyle";
    private static final String COMMONS_LANG3_ADDON = "3";
    private static final String EMPTY_STRING = "";

    public String getFullStyle() {
        return TO_STRING_STYLE + DOT_STRING + name();
    }

    public String getFullLibraryString(boolean useCommonsLang3) {
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + BUILDER_STRING
                + getFullStyle();
    }

    public static String getToStringStyleLibrary(boolean useCommonsLang3) {
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + BUILDER_STRING
                + TO_STRING_STYLE;
    }

    public static ToStringStyle getToStringStyle(String fullLibraryString) {
        EnumSet<ToStringStyle> enumSet = EnumSet.allOf(ToStringStyle.class);
        for (ToStringStyle style : enumSet) {
            if (fullLibraryString.contains(style.name())) {
                return style;
            }
        }
        return NO_STYLE;
    }
}
