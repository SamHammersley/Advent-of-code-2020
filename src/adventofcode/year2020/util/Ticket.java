package adventofcode.year2020.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ticket {

    private final List<Integer> values;

    private Ticket(List<Integer> values) {
        this.values = values;
    }

    public List<Integer> getValues() {
        return values;
    }

    public IntStream filter(Predicate<Integer> predicate) {
        return values.stream().filter(predicate).mapToInt(i -> i);
    }

    @Override
    public String toString() {
        return values.toString();
    }

    public static Ticket parse(String[] input) {
        if (input.length <= 1) {
            return null;
        }

        List<Integer> values = Arrays.stream(input)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        return new Ticket(values);
    }

}