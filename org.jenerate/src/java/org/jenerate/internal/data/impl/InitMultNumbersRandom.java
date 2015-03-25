package org.jenerate.internal.data.impl;

import java.util.Random;

import org.jenerate.internal.data.IInitMultNumbers;

/**
 * @author jiayun
 */
public class InitMultNumbersRandom implements IInitMultNumbers {

    private static Random random = new Random();

    @Override
    public void setNumbers(int initial, int multiplier) {
        /* Nothing to be done here */
    }

    @Override
    public String getValue() {

        int initial = random.nextInt();
        int multiplier = random.nextInt();

        initial = initial % 2 == 0 ? initial + 1 : initial;
        multiplier = multiplier % 2 == 0 ? multiplier + 1 : multiplier;

        return String.valueOf(initial) + ", " + String.valueOf(multiplier);
    }

}
