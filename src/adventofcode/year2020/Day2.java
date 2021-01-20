package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day2 extends Puzzle<Long> {

    public Day2() {
        super("\n", "maybe do later");
    }

    @Override
    public Long part1() {
        OccurrencesPolicy occurrencesPolicy = new OccurrencesPolicy();

        return mapInput(PasswordInput::parseInput).filter(occurrencesPolicy::satisfies).count();
    }

    @Override
    public Long part2() {
        PositionPolicy positionPolicy = new PositionPolicy();

        return mapInput(PasswordInput::parseInput).filter(positionPolicy::satisfies).count();
    }

    static class PasswordInput {

        private static final Pattern INPUT_PATTERN = Pattern.compile("(\\d+-\\d+) ([a-z]): (\\w+)");

        private final String password;

        private final int lower;

        private final int upper;

        private final char character;

        public PasswordInput(String password, int lower, int upper, char character) {
            this.password = password;
            this.upper = upper;
            this.lower = lower;
            this.character = character;
        }

        @Override
        public String toString() {
            return upper + "-" + lower + " " + character + ": " + password;
        }

        public static PasswordInput parseInput(String input) {
            Matcher matcher = INPUT_PATTERN.matcher(input);

            if (!matcher.find()) {
                throw new RuntimeException("Malformed input");
            }

            String[] range = matcher.group(1).split("-");
            int lower = Integer.parseInt(range[0]);
            int upper = Integer.parseInt(range[1]);

            String character = matcher.group(2);
            String password = matcher.group(3);

            return new PasswordInput(password, lower, upper, character.charAt(0));
        }

    }

    private interface PasswordPolicy {

        boolean satisfies(PasswordInput input);

    }

    static class OccurrencesPolicy implements PasswordPolicy {

        @Override
        public boolean satisfies(PasswordInput input) {
            int occurrences = countOccurrences(input.character, input.password);

            return occurrences >= input.lower && occurrences <= input.upper;
        }

        private int countOccurrences(char character, String password) {
            int occurrences = 0;

            for (char c : password.toCharArray()) {
                if (c == character) {
                    occurrences++;
                }
            }

            return occurrences;
        }

    }

    static class PositionPolicy implements PasswordPolicy {

        @Override
        public boolean satisfies(PasswordInput input) {
            char firstPos = input.password.charAt(input.lower - 1);
            char secondPos = input.password.charAt(input.upper - 1);

            return (firstPos == input.character && secondPos != input.character)
                    || (firstPos != input.character && secondPos == input.character);
        }

    }

}