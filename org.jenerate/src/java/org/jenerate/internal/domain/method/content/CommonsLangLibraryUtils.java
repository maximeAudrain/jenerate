package org.jenerate.internal.domain.method.content;


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

    private static final String COMMONS_LANG3_ADDON = "3";
    private static final String EMPTY_STRING = "";

    private CommonsLangLibraryUtils() {
        /* Only static constants */
    }

    /**
     * @return the full constructed CompareToBuilder library
     */
    public static String getCompareToBuilderLibrary(boolean useCommonsLang3) {
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + COMPARE_TO_BUILDER_POSTFIX;
    }

    /**
     * @return the full constructed HashCodeBuilder library
     */
    public static String getHashCodeBuilderLibrary(boolean useCommonsLang3) {
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + HASH_CODE_BUILDER_POSTFIX;
    }

    /**
     * @return the full constructed EqualsBuilder library
     */
    public static String getEqualsBuilderLibrary(boolean useCommonsLang3) {
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + EQUALS_BUILDER_POSTFIX;
    }

    /**
     * @return the full constructed ToStringBuilder library
     */
    public static String getToStringBuilderLibrary(boolean useCommonsLang3) {
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + TO_STRING_BUILDER_POSTFIX;
    }

}
