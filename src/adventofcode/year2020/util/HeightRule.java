package adventofcode.year2020.util;

import java.util.function.Predicate;

public final class HeightRule implements Predicate<String> {

    @Override
    public boolean test(String height) {
        String unit = height.substring(height.length() - 2);
        int value = Integer.parseInt(height.substring(0, height.length() - 2));

        return unit.equalsIgnoreCase("cm") ? value >= 150 && value <= 193 : value >= 59 && value <= 76;
    }
}