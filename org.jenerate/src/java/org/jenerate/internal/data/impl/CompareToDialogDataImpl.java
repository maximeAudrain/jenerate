package org.jenerate.internal.data.impl;

import org.jenerate.internal.data.CompareToDialogData;

public class CompareToDialogDataImpl extends AbstractFieldDialogData implements CompareToDialogData {

    private CompareToDialogDataImpl(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbstractFieldDialogData.Builder<Builder> {

        @Override
        public Builder getThis() {
            return this;
        }

        public CompareToDialogData build() {
            return new CompareToDialogDataImpl(getThis());
        }
    }

}
