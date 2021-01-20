package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Day1 extends Puzzle<Integer> {

    public Day1() {
        super("\n", "beeepboop bananasoup");
    }

    @Override
    public Integer part1() {
        List<Integer> mapped = mapInput(Integer::parseInt).sorted().collect(Collectors.toList());

        int upperIndex = mapped.size() - 1, lowerIndex = 0;

        while (upperIndex - lowerIndex > 1) {
            int upper = mapped.get(upperIndex);
            int lower = mapped.get(lowerIndex);
            int sum = upper + lower;

            if (sum < 2020) {
                lowerIndex++;

            } else if (sum > 2020) {
                upperIndex--;

            } else {
                return upper * lower;
            }
        }

        throw new RuntimeException("No valid pair found!");
    }

    @Override
    public Integer part2() {
        List<Integer> mapped = mapInput(Integer::parseInt).sorted().collect(Collectors.toList());

        for (int i = 0; i < mapped.size(); i++) {
            for (int j = mapped.size() - 1; j > i + 1; j--) {
                int sum = mapped.get(i) + mapped.get(j);

                if (sum >= 2020) {
                    continue;
                }

                List<Integer> sub = mapped.subList(i + 1, j);
                int searchIndex = Collections.binarySearch(sub, 2020 - sum);

                if (searchIndex >= 0) {
                    return mapped.get(i) * mapped.get(j) * (2020 - sum);
                }
            }
        }

        throw new RuntimeException("No valid triple found!");
    }
}