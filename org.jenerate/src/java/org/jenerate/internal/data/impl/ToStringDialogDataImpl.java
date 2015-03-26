package org.jenerate.internal.data.impl;

import org.jenerate.internal.data.ToStringDialogData;
import org.jenerate.internal.domain.method.content.tostring.ToStringStyle;

public class ToStringDialogDataImpl extends AbstractFieldDialogData implements ToStringDialogData {

    private final ToStringStyle toStringStyle;

    private ToStringDialogDataImpl(Builder builder) {
        super(builder);
        this.toStringStyle = builder.builderToStringStyle;
    }

    @Override
    public ToStringStyle getToStringStyle() {
        return toStringStyle;
    }

    public static class Builder extends AbstractFieldDialogData.Builder<Builder> {

        private ToStringStyle builderToStringStyle;

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder withToStringStyle(ToStringStyle toStringStyle) {
            this.builderToStringStyle = toStringStyle;
            return getThis();
        }

        public ToStringDialogData build() {
            return new ToStringDialogDataImpl(getThis());
        }

    }
}
