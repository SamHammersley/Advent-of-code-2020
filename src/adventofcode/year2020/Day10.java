package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.*;
import java.util.stream.Collectors;

public class Day10 extends Puzzle<Long> {

    public Day10() {
        super("\n", "");
    }

    private List<Integer> getExhaustive() {
        List<Integer> adapterRatings = mapInput(Integer::parseInt).sorted(Integer::compareTo).collect(Collectors.toList());

        adapterRatings.add(0, 0);
        adapterRatings.add(adapterRatings.get(adapterRatings.size() - 1) + 3);

        return adapterRatings;
    }

    @Override
    public Long part1() {
        List<Integer> exhaustive = getExhaustive();

        long ones = 0;
        int threes = 0;

        for (int i = 0; i < exhaustive.size() - 1; i++) {
            int difference = exhaustive.get(i + 1) - exhaustive.get(i);

            if (difference == 1) {
                ones++;

            } else if (difference == 3) {
                threes++;
            }
        }

        return ones * threes;
    }

    @Override
    public Long part2() {
        List<Integer> exhaustive = getExhaustive();

        long total = 1;

        for (int i = 0; i < exhaustive.size() - 1; i++) {
            int options = countOptions(i, exhaustive);

            total *= options;
            i += (options / 2);
        }

        return total;
    }

    private final Map<Integer, Integer> cached = new HashMap<>();

    private int countOptions(int index, List<Integer> list) {
        if (cached.containsKey(index)) {
            return cached.get(index);
        }

        List<Integer> options = new ArrayList<>();

        for (int i = index + 1; i < list.size(); i++) {
            if (list.get(i) - list.get(index) > 3) {
                break;
            }

            options.add(i);
        }

        if (options.size() == 1) {
            return options.size();
        }

        int total = 0;

        for (int option : options) {
            total += countOptions(option, list);
        }

        cached.putIfAbsent(index, total);
        return total;
    }

    /**
     * Correct but not efficient enough for larger inputs.
     */
    private Set<List<Integer>> apply(List<Integer> l, Set<List<Integer>> configurations) {
        if (!configurations.add(l)) {
            return configurations;
        }

        Set<Integer> ignoreIndices = new LinkedHashSet<>();
        for (int i = 1; i < l.size() - 1; i++) {
            if (l.get(i + 1) - l.get(i - 1) <= 3) {
                ignoreIndices.add(i);
            }
        }

        if (ignoreIndices.isEmpty()) {
            return configurations;
        }

        for (int ignore : ignoreIndices) {
            List<Integer> excluding = exclude(l, ignore);

            configurations.addAll(apply(excluding, configurations));
        }

        return configurations;
    }

    private List<Integer> exclude(List<Integer> l, int index) {
        List<Integer> copyList = new ArrayList<>(l.size() - 1);

        for (int i = 0; i < l.size(); i++) {
            if (i == index) {
                continue;
            }
            copyList.add(l.get(i));
        }

        return copyList;
    }

}