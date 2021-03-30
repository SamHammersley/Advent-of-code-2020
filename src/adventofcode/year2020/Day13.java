package adventofcode.year2020;

import adventofcode.Puzzle;

import java.math.BigInteger;
import java.util.*;

public class Day13 extends Puzzle<BigInteger> {

    public Day13() {
        super("\n", "");
    }

    private long nextMultiple(long id, long timestamp) {
        return id * (1 + timestamp / id);
    }

    @Override
    public BigInteger part1() {
        int earliestTimestamp = Integer.parseInt(input.get(0));
        long closestId = -1;
        long waitTime = Integer.MAX_VALUE;

        for (String s : input.get(1).split(",")) {
            if (s.equalsIgnoreCase("x")) {
                continue;
            }
            long id = mapToInt(s);
            long r = nextMultiple(id, earliestTimestamp);

            if (r - earliestTimestamp < waitTime) {
                waitTime = r - earliestTimestamp;
                closestId = id;
            }
        }

        return BigInteger.valueOf(closestId * waitTime);
    }

    private Map<Long, Long> getRemainders(String[] ids) {
        Map<Long, Long> index = new LinkedHashMap<>();
        index.put(mapToInt(ids[0]), 0L);

        for (int i = 1; i < ids.length; i++) {
            if (ids[i].equalsIgnoreCase("x")) {
                continue;
            }

            long id = mapToInt(ids[i]);
            index.put(id, nextMultiple(id, i) - i);
        }

        return index;
    }

    @Override
    public BigInteger part2() {
        String[] ids = input.get(1).split(",");
        Map<Long, Long> index = getRemainders(ids);

        long product = index.keySet().stream().reduce((a, b) -> a * b).get();
        BigInteger solution = BigInteger.ZERO;

        for (Map.Entry<Long, Long> entry : index.entrySet()) {
            long unitSolution = calcUnitSolution(entry.getKey(), product);

            solution = solution.add(BigInteger.valueOf((unitSolution * entry.getValue()) % product));
        }

        return solution.mod(BigInteger.valueOf(product));
    }

    private long calcUnitSolution(long mod, long product) {
        long coefficient = product / mod;
        long reducedCoefficient = coefficient % mod;
        int multiplier = 1;

        if (reducedCoefficient > 1) {
            for (; multiplier < mod && (reducedCoefficient * multiplier) % mod != 1; multiplier++);
        }

        return coefficient * multiplier;
    }

    private static Long mapToInt(String s) {
        try {
            return Long.parseLong(s);
        } catch(Exception e) {
            return null;
        }
    }

}