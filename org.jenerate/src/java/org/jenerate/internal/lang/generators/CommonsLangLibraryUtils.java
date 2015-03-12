package org.jenerate.internal.lang.generators;


/**
 * Helper class to ease the construction of the commons-lang library objects to be used by the generator classes. This
 * class cannot be instantiated since it only holds static constants and static helper methods.
 * 
 * @author maudrain
 */
public final class CommonsLangLibraryUtils {

    public static final String DOT_STRING = ".";

    private static final String COMMONS_LANG_PREFIX = "org.apache.commons.lang";

    private static final String BUILDER_STRING = ".builder.";
    private static final String COMPARE_TO_BUILDER_POSTFIX = BUILDER_STRING + "CompareToBuilder";
    private static final String HASH_CODE_BUILDER_POSTFIX = BUILDER_STRING + "HashCodeBuilder";
    private static final String EQUALS_BUILDER_POSTFIX = BUILDER_STRING + "EqualsBuilder";
    private static final String TO_STRING_BUILDER_POSTFIX = BUILDER_STRING + "ToStringBuilder";
    private static final String TO_STRING_STYLE_POSTFIX = BUILDER_STRING + "ToStringStyle";

    private static final String COMMONS_LANG3_ADDON = "3";
    private static final String EMPTY_STRING = "";

    private static final String DEFAULT_STYLE_POSTFIX = DOT_STRING + "DEFAULT_STYLE";
    private static final String MULTI_LINE_STYLE_POSTFIX = DOT_STRING + "MULTI_LINE_STYLE";
    private static final String NO_FIELD_NAMES_STYLE_POSTFIX = DOT_STRING + "NO_FIELD_NAMES_STYLE";
    private static final String SHORT_PREFIX_STYLE_POSTFIX = DOT_STRING + "SHORT_PREFIX_STYLE";
    private static final String SIMPLE_STYLE_POSTFIX = DOT_STRING + "SIMPLE_STYLE";

    private CommonsLangLibraryUtils() {
        /* Only static constants */
    }

    /**
     * @return the full constructed CompareToBuilder library
     */
    public static String getCompareToBuilderLibrary(boolean useCommonsLang3) {
        return createLibraryFullPath(COMPARE_TO_BUILDER_POSTFIX, useCommonsLang3);
    }

    /**
     * @return the full constructed HashCodeBuilder library
     */
    public static String getHashCodeBuilderLibrary(boolean useCommonsLang3) {
        return createLibraryFullPath(HASH_CODE_BUILDER_POSTFIX, useCommonsLang3);
    }

    /**
     * @return the full constructed EqualsBuilder library
     */
    public static String getEqualsBuilderLibrary(boolean useCommonsLang3) {
        return createLibraryFullPath(EQUALS_BUILDER_POSTFIX, useCommonsLang3);
    }

    /**
     * @return the full constructed ToStringBuilder library
     */
    public static String getToStringBuilderLibrary(boolean useCommonsLang3) {
        return createLibraryFullPath(TO_STRING_BUILDER_POSTFIX, useCommonsLang3);
    }

    /**
     * @return the full constructed ToStringStyle library
     */
    public static String getToStringStyleLibrary(boolean useCommonsLang3) {
        return createLibraryFullPath(TO_STRING_STYLE_POSTFIX, useCommonsLang3);
    }

    /**
     * @return the full constructed ToStringStyle.DEFAULT_STYLE library
     */
    public static String getToStringStyleLibraryDefaultStyle(boolean useCommonsLang3) {
        return createLibraryFullPath(TO_STRING_STYLE_POSTFIX, useCommonsLang3) + DEFAULT_STYLE_POSTFIX;
    }

    /**
     * @return an array containing all the fully constructed styles of the ToStringStyle library
     */
    public static String[] createToStringStyles(boolean useCommonsLang3) {
        String toStringStyleLibrary = getToStringStyleLibrary(useCommonsLang3);
        return new String[] { toStringStyleLibrary + DEFAULT_STYLE_POSTFIX,
                toStringStyleLibrary + MULTI_LINE_STYLE_POSTFIX, toStringStyleLibrary + NO_FIELD_NAMES_STYLE_POSTFIX,
                toStringStyleLibrary + SHORT_PREFIX_STYLE_POSTFIX, toStringStyleLibrary + SIMPLE_STYLE_POSTFIX };
    }

    private static String createLibraryFullPath(String libraryToImportPostfix, boolean useCommonsLang3) {
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + libraryToImportPostfix;
    }

}
