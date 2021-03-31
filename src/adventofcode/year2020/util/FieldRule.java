package adventofcode.year2020.util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FieldRule {

    private final String name;

    private final int lowerLowerBound, lowerUpperBound;

    private final int higherLowerBound, higherUpperBound;

    public FieldRule(String name, int lowerLowerBound, int lowerUpperBound, int higherLowerBound, int higherUpperBound) {
        this.name = name;
        this.lowerLowerBound = lowerLowerBound;
        this.lowerUpperBound = lowerUpperBound;
        this.higherLowerBound = higherLowerBound;
        this.higherUpperBound = higherUpperBound;
    }

    public boolean test(int value) {
        return (value >= lowerLowerBound && value <= lowerUpperBound)
                || (value >= higherLowerBound && value <= higherUpperBound);
    }

    @Override
    public String toString() {
        return name + ":" + lowerLowerBound + "-" +lowerUpperBound + " or " + higherLowerBound + "-" + higherUpperBound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lowerLowerBound, lowerUpperBound, higherLowerBound, higherUpperBound);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FieldRule)) {
            return false;
        }
        FieldRule other = (FieldRule) object;

        return name.equals(other.name)
                && lowerLowerBound == other.lowerLowerBound
                && lowerUpperBound == other.lowerUpperBound
                && higherLowerBound == other.higherLowerBound
                && higherUpperBound == other.higherUpperBound;
    }

    private static final Pattern NUMBER_RANGE_PATTERN = Pattern.compile("(\\d+)-(\\d+)\\s*or\\s*(\\d+)-(\\d+)");

    public static FieldRule parse(String input) {
        String[] parts = input.split(":");

        String ranges = parts[1].trim();
        Matcher matcher = NUMBER_RANGE_PATTERN.matcher(ranges);

        if (!matcher.find()) {
            throw new RuntimeException("Malformed input.");
        }

        return new FieldRule(parts[0],
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4)));
    }

    public String name() {
        return name;
    }
}