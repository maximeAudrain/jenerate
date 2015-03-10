package org.jenerate.internal.data.impl;

import java.util.Arrays;

import org.eclipse.jdt.core.IField;
import org.jenerate.internal.data.MethodGenerationData;

/**
 * Abstract implementation of the {@link MethodGenerationData}. This class is meant to be immutable.
 * 
 * @author maudrain
 */
public class AbstractMethodGenerationData implements MethodGenerationData {

    private final IField[] fields;
    private final boolean appendSuper;
    private final boolean generateComment;

    public AbstractMethodGenerationData(IField[] fields, boolean appendSuper, boolean generateComment) {
        this.fields = fields;
        this.appendSuper = appendSuper;
        this.generateComment = generateComment;
    }

    @Override
    public IField[] getFields() {
        return Arrays.copyOf(fields, fields.length);
    }

    @Override
    public boolean isAppendSuper() {
        return appendSuper;
    }

    @Override
    public boolean isGenerateComment() {
        return generateComment;
    }

}
