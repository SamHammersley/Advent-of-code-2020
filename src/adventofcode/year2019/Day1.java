package adventofcode.year2019;

import adventofcode.Puzzle;

public final class Day1 extends Puzzle<Integer> {

    public Day1() {
        super("\n", "");
    }

    @Override
    public Integer part1() {
        return mapInput(Integer::parseInt).mapToInt(m ->  m / 3 - 2).sum();
    }

    @Override
    public Integer part2() {
        return mapInput(Integer::parseInt).mapToInt(this::calculateFuelReq).sum();
    }

    private int calculateFuelReq(int mass) {
        int fuelReq = mass / 3 - 2;

        if (fuelReq < 0) {
            return 0;
        }

        return fuelReq + calculateFuelReq(fuelReq);
    }
}