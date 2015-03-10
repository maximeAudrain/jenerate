package org.jenerate.internal.lang.generators;

/**
 * @author jiayun
 */
public class CustomInitMultNumbers implements IInitMultNumbers {

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
