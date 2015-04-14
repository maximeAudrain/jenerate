package org.jenerate.internal.strategy.method.content.impl.commonslang;

import java.util.EnumSet;

import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;

/**
 * Defines the different styles for the commons lang ToStringBuilder
 *
 * @author maudrain
 */
public enum CommonsLangToStringStyle {
    NO_STYLE("Person@182f0db[name=John Doe,age=33,smoker=false]"),

    DEFAULT_STYLE("Person@182f0db[name=John Doe,age=33,smoker=false]"),
    MULTI_LINE_STYLE("Person@182f0db[\n   name=John Doe\n   age=33\n   smoker=false\n ]"),
    NO_FIELD_NAMES_STYLE("Person@182f0db[John Doe,33,false]"),
    SHORT_PREFIX_STYLE("Person[name=John Doe,age=33,smoker=false]"),
    SIMPLE_STYLE("John Doe,33,false");
    private static final String PREFIX = "Example:\n";

    /**
     * tooltip to show in the toString dialog. Based on the examples in
     * http://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/builder/ToStringStyle.html
     */
    private final String toolTip;

    private CommonsLangToStringStyle(String toolTip) {
        this.toolTip = toolTip;

    }

    public String getFullStyle() {
        return CommonsLangMethodContentLibraries.TO_STRING_STYLE + CommonsLangMethodContentLibraries.DOT_STRING
                + name();
    }

    public String getFullLibraryString(StrategyIdentifier strategyIdentifier) {
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG.equals(strategyIdentifier)) {
            return CommonsLangMethodContentLibraries.COMMONS_LANG_PREFIX
                    + CommonsLangMethodContentLibraries.BUILDER_STRING + getFullStyle();
        }
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(strategyIdentifier)) {
            return CommonsLangMethodContentLibraries.COMMONS_LANG_PREFIX
                    + CommonsLangMethodContentLibraries.COMMONS_LANG3_ADDON
                    + CommonsLangMethodContentLibraries.BUILDER_STRING + getFullStyle();
        }
        throw new UnsupportedOperationException(
                "The full library string for the toString style is not currently handled for strategy '"
                        + strategyIdentifier + "' ");
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

    public String getToolTip() {
        return PREFIX + toolTip;
    }
}
