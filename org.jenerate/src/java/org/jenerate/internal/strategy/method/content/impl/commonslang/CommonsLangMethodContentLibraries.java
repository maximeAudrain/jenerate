package org.jenerate.internal.strategy.method.content.impl.commonslang;

import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.content.MethodContent;

/**
 * Helper class to ease the construction of the commons-lang library objects to be used by the {@link MethodContent}
 * strategies. This class cannot be instantiated since it only holds static constants and static helper methods.
 * 
 * @author maudrain
 */
public final class CommonsLangMethodContentLibraries {

    public static final String COMMONS_LANG_PREFIX = "org.apache.commons.lang";
    public static final String BUILDER_STRING = ".builder.";

    public static final String DOT_STRING = ".";
    public static final String COMMONS_LANG3_ADDON = "3";
    public static final String EMPTY_STRING = "";

    public static final String TO_STRING_STYLE = "ToStringStyle";

    private static final String COMPARE_TO_BUILDER_POSTFIX = BUILDER_STRING + "CompareToBuilder";
    private static final String HASH_CODE_BUILDER_POSTFIX = BUILDER_STRING + "HashCodeBuilder";
    private static final String EQUALS_BUILDER_POSTFIX = BUILDER_STRING + "EqualsBuilder";
    private static final String TO_STRING_BUILDER_POSTFIX = BUILDER_STRING + "ToStringBuilder";

    private CommonsLangMethodContentLibraries() {
        /* Only static constants */
    }

    /**
     * @return the fully constructed CompareToBuilder library
     */
    public static String getCompareToBuilderLibrary(StrategyIdentifier strategyIdentifier) {
        boolean useCommonsLang3 = useCommonsLang3(strategyIdentifier);
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING)
                + COMPARE_TO_BUILDER_POSTFIX;
    }

    /**
     * @return the fully constructed HashCodeBuilder library
     */
    public static String getHashCodeBuilderLibrary(StrategyIdentifier strategyIdentifier) {
        boolean useCommonsLang3 = useCommonsLang3(strategyIdentifier);
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + HASH_CODE_BUILDER_POSTFIX;
    }

    /**
     * @return the fully constructed EqualsBuilder library
     */
    public static String getEqualsBuilderLibrary(StrategyIdentifier strategyIdentifier) {
        boolean useCommonsLang3 = useCommonsLang3(strategyIdentifier);
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + EQUALS_BUILDER_POSTFIX;
    }

    /**
     * @return the fully constructed ToStringBuilder library
     */
    public static String getToStringBuilderLibrary(StrategyIdentifier strategyIdentifier) {
        boolean useCommonsLang3 = useCommonsLang3(strategyIdentifier);
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + TO_STRING_BUILDER_POSTFIX;
    }

    /**
     * @return the fully constructed ToStringStyle library
     */
    public static String getToStringStyleLibrary(StrategyIdentifier strategyIdentifier) {
        boolean useCommonsLang3 = useCommonsLang3(strategyIdentifier);
        return COMMONS_LANG_PREFIX + (useCommonsLang3 ? COMMONS_LANG3_ADDON : EMPTY_STRING) + BUILDER_STRING
                + TO_STRING_STYLE;
    }

    private static boolean useCommonsLang3(StrategyIdentifier strategyIdentifier) {
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(strategyIdentifier)) {
            return true;
        }
        return false;
    }

}
