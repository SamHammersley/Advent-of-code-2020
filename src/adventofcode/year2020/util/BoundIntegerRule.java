package adventofcode.year2020.util;

import java.util.function.Predicate;

public final class BoundIntegerRule implements Predicate<String> {

    private final int lowerBound;

    private final int upperBound;

    public BoundIntegerRule(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public boolean test(String s) {
        int intValue = Integer.parseInt(s);

        return intValue >= lowerBound && intValue <= upperBound;
    }
}