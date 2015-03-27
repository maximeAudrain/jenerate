package org.jenerate.internal.domain.hashcode.impl;

import org.jenerate.internal.domain.hashcode.IInitMultNumbers;

/**
 * @author jiayun
 */
public class InitMultNumbersCustom implements IInitMultNumbers {

    private int initial;
    private int multiplier;

    @Override
    public void setNumbers(int initial, int multiplier) {
        this.initial = initial;
        this.multiplier = multiplier;
    }

    @Override
    public String getValue() {
        return String.valueOf(initial) + ", " + String.valueOf(multiplier);
    }

}
