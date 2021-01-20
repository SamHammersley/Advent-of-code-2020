package adventofcode.year2020.util;

import java.util.function.Predicate;

public final class RegexValidationRule implements Predicate<String> {

    private final String pattern;

    public RegexValidationRule(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean test(String s) {
        return s.matches(pattern);
    }
}
