package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.EnumSet;

/**
 * Defines the different styles for the commons lang ToStringBuilder
 * 
 * @author maudrain
 */
public enum CommonsLangToStringStyle {

    NO_STYLE,

    DEFAULT_STYLE,
    MULTI_LINE_STYLE,
    NO_FIELD_NAMES_STYLE,
    SHORT_PREFIX_STYLE,
    SIMPLE_STYLE;

    public static final String DOT_STRING = ".";
    private static final String COMMONS_LANG_PREFIX = "org.apache.commons.lang";
    private static final String BUILDER_STRING = ".builder.";
    private static final String TO_STRING_STYLE = "CommonsLangToStringStyle";
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

    public static CommonsLangToStringStyle getToStringStyle(String fullLibraryString) {
        EnumSet<CommonsLangToStringStyle> enumSet = EnumSet.allOf(CommonsLangToStringStyle.class);
        for (CommonsLangToStringStyle style : enumSet) {
            if (fullLibraryString.contains(style.name())) {
                return style;
            }
        }
        return NO_STYLE;
    }
}
