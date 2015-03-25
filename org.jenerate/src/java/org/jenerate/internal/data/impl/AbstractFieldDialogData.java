package org.jenerate.internal.data.impl;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.jenerate.internal.data.FieldDialogData;

public abstract class AbstractFieldDialogData implements FieldDialogData {

    private final IField[] checkedFields;
    private final IJavaElement elementPosition;
    private final boolean appendSuper;
    private final boolean generateComment;
    private final boolean useGettersInsteadOfFields;
    private final boolean useBlockInIfStatements;

    protected AbstractFieldDialogData(@SuppressWarnings("rawtypes") Builder builder) {
        this.checkedFields = builder.builderCheckedFields;
        this.elementPosition = builder.builderElementPosition;
        this.appendSuper = builder.builderAppendSuper;
        this.generateComment = builder.builderGenerateComment;
        this.useGettersInsteadOfFields = builder.builderUseGettersInsteadOfFields;
        this.useBlockInIfStatements = builder.builderUseBlockInIfStatements;
    }

    @Override
    public IField[] getCheckedFields() {
        return checkedFields;
    }

    @Override
    public IJavaElement getElementPosition() {
        return elementPosition;
    }

    @Override
    public boolean getAppendSuper() {
        return appendSuper;
    }

    @Override
    public boolean getGenerateComment() {
        return generateComment;
    }

    @Override
    public boolean getUseGettersInsteadOfFields() {
        return useGettersInsteadOfFields;
    }

    @Override
    public boolean getUseBlockInIfStatements() {
        return useBlockInIfStatements;
    }

    public abstract static class Builder<T extends Builder<T>> {

        private IField[] builderCheckedFields;
        private IJavaElement builderElementPosition;
        private boolean builderAppendSuper;
        private boolean builderGenerateComment;
        private boolean builderUseGettersInsteadOfFields;
        private boolean builderUseBlockInIfStatements;

        public T withCheckedFields(IField[] checkedFields) {
            this.builderCheckedFields = checkedFields;
            return getThis();
        }

        public T withElementPosition(IJavaElement elementPosition) {
            this.builderElementPosition = elementPosition;
            return getThis();
        }

        public T withAppendSuper(boolean appendSuper) {
            this.builderAppendSuper = appendSuper;
            return getThis();
        }

        public T withGenerateComment(boolean generateComment) {
            this.builderGenerateComment = generateComment;
            return getThis();
        }

        public T withUseGettersInsteadOfFields(boolean useGettersInsteadOfFields) {
            this.builderUseGettersInsteadOfFields = useGettersInsteadOfFields;
            return getThis();
        }

        public T withUseBlockInIfStatements(boolean useBlockInIfStatements) {
            this.builderUseBlockInIfStatements = useBlockInIfStatements;
            return getThis();
        }

        public abstract T getThis();
    }
}
