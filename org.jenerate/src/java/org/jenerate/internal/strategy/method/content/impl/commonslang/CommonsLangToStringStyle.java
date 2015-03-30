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

    public String getFullStyle() {
        return CommonsLangMethodContentLibraries.TO_STRING_STYLE + CommonsLangMethodContentLibraries.DOT_STRING
                + name();
    }

    public String getFullLibraryString(boolean useCommonsLang3) {
        return CommonsLangMethodContentLibraries.COMMONS_LANG_PREFIX
                + (useCommonsLang3 ? CommonsLangMethodContentLibraries.COMMONS_LANG3_ADDON
                        : CommonsLangMethodContentLibraries.EMPTY_STRING)
                + CommonsLangMethodContentLibraries.BUILDER_STRING + getFullStyle();
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
