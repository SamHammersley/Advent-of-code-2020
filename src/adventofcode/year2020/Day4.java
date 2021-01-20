package adventofcode.year2020;

import adventofcode.Puzzle;
import adventofcode.year2020.util.BoundIntegerRule;
import adventofcode.year2020.util.HeightRule;
import adventofcode.year2020.util.RegexValidationRule;

import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day4 extends Puzzle<Integer> {

    public Day4() {
        super("\n\n", "");
    }

    private static final Map<String, Predicate<String>> FIELD_VALIDATION_RULES = Map.of(
        "byr", new RegexValidationRule("\\d{4}").and(new BoundIntegerRule(1920, 2002)),
        "iyr", new RegexValidationRule("\\d{4}").and(new BoundIntegerRule(2010, 2020)),
        "eyr", new RegexValidationRule("\\d{4}").and(new BoundIntegerRule(2020, 2030)),
        "hgt", new RegexValidationRule("\\d{2,3}(cm|in)").and(new HeightRule()),
        "hcl", new RegexValidationRule("#[\\d|a-f]{6}"),
        "ecl", new RegexValidationRule("amb|blu|brn|gry|grn|hzl|oth"),
        "pid", new RegexValidationRule("\\d{9}")
    );

    private boolean hasAllRequiredFields(String passport) {
        for (String field : FIELD_VALIDATION_RULES.keySet()) {
            if (!passport.contains(field)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Integer part1() {
        int validCount = 0;

        for (String passport : input) {
            if (hasAllRequiredFields(passport)) {
                validCount++;
            }
        }

        return validCount;
    }

    private boolean validPassportEntry(String key, String value) {
        Predicate<String> pred = FIELD_VALIDATION_RULES.get(key);

        if (pred == null) { // cid should always pass
            return true;
        }

        return pred.test(value);
    }

    @Override
    public Integer part2() {
        Pattern keyValuePattern = Pattern.compile("([a-z]{3}):(\\S+)");
        int validCount = 0;

        for (String passport : input) {
            Matcher matcher = keyValuePattern.matcher(passport);

            if (hasAllRequiredFields(passport) && matcher.results().allMatch(r -> validPassportEntry(r.group(1), r.group(2)))) {
                validCount++;
            }
        }

        return validCount;
    }

}