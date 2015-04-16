package org.jenerate.internal.domain.data;

import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangToStringStyle;

/**
 * Extension of the {@link MethodGenerationData} for the toString method generation
 * 
 * @author maudrain
 */
public interface ToStringGenerationData extends MethodGenerationData {

    /**
     * @return the toString style to be used for the code generation
     */
    CommonsLangToStringStyle getToStringStyle();
}
