package org.jenerate.internal.domain.data;

import org.jenerate.internal.domain.method.content.tostring.ToStringStyle;

public interface ToStringGenerationData extends MethodGenerationData {

    ToStringStyle getToStringStyle();
}
