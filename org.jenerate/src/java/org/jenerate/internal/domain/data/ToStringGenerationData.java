package org.jenerate.internal.domain.data;

import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangToStringStyle;

public interface ToStringGenerationData extends MethodGenerationData {

    CommonsLangToStringStyle getToStringStyle();
}
