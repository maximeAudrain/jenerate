package org.jenerate.internal.domain.data;

import org.jenerate.internal.domain.hashcode.IInitMultNumbers;

public interface EqualsHashCodeGenerationData extends MethodGenerationData {

	boolean getCompareReferences();

	IInitMultNumbers getInitMultNumbers();

	boolean getClassComparison();

}
