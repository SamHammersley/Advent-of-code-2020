package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day15 extends Puzzle<Integer> {

    public Day15() {
        super(",", "");
    }

    private int nthNumber(int n, List<Integer> input, Map<Integer, Integer> index) {
        int previous = input.get(input.size() - 1);

        for (int i = index.size(); i < n; i++) {
            if (index.containsKey(previous) && index.get(previous) != i) {
                int lastCall = index.get(previous);

                index.put(previous, i);
                previous = i - lastCall;

            } else {
                index.putIfAbsent(previous, i);

                previous = 0;
            }
        }

        return previous;
    }

    @Override
    public Integer part1() {
        List<Integer> ints = mapInput(Integer::parseInt).collect(Collectors.toList());

        Map<Integer, Integer> index = IntStream.range(0, ints.size())
                .boxed()
                .collect(Collectors.toMap(ints::get, i -> i + 1));

        return nthNumber(2020, ints, index);
    }

    @Override
    public Integer part2() {
        List<Integer> ints = mapInput(Integer::parseInt).collect(Collectors.toList());

        Map<Integer, Integer> index = IntStream.range(0, ints.size())
                .boxed()
                .collect(Collectors.toMap(ints::get, i -> i + 1));

        return nthNumber(30_000_000, ints, index);
    }
}