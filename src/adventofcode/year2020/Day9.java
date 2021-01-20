package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.*;
import java.util.stream.Collectors;

public class Day9 extends Puzzle<Long> {

    public Day9() {
        super("\n", "");
    }

    private boolean findPair(long key, List<Long> numbers) {
        int upperIndex = numbers.size() - 1, lowerIndex = 0;

        while (upperIndex - lowerIndex >= 1) {
            long upper = numbers.get(upperIndex);
            long lower = numbers.get(lowerIndex);
            long sum = upper + lower;

            if (sum < key) {
                lowerIndex++;

            } else if (sum > key) {
                upperIndex--;

            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public Long part1() {
        List<Long> input = mapInput(Long::parseLong).collect(Collectors.toList());
        List<Long> preamble = new ArrayList<>(input.subList(0, 25));
        preamble.sort(Long::compareTo);

        for (int i = 25; i < input.size(); i++) {
            long key = input.get(i);

            if (!findPair(key, preamble)) {
                return key;
            }

            preamble.remove(input.get(i - 25));

            int search = Collections.binarySearch(preamble, key);
            int insertionIndex = Math.max(search, -1 * search - 1);

            preamble.add(insertionIndex, key);
        }

        throw new RuntimeException("Unable to find a number which has no sum in immediately previous 25 numbers");
    }

    @Override
    public Long part2() {
        long key = part1();
        List<Long> input = mapInput(Long::parseLong).collect(Collectors.toList());

        int lowerIndex = 0;
        int upperIndex = 1;

        while (upperIndex < input.size()) {
            List<Long> range = input.subList(lowerIndex, upperIndex + 1);
            long sum = range.stream().reduce(Long::sum).get();

            if (sum < key) {
                upperIndex++;

            } else if (sum > key) {
                lowerIndex++;

            } else {
                range.sort(Long::compareTo);

                return range.get(0) + range.get(range.size() - 1);
            }
        }

        throw new RuntimeException("Unable to find contiguous set of numbers that sum to " + key);
    }
}