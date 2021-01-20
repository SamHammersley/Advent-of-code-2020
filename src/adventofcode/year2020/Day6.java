package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.HashMap;
import java.util.Map;

public final class Day6 extends Puzzle<Integer> {

    public Day6() {
        super("\n\n", "");
    }

    @Override
    public Integer part1() {
        return mapInput(s -> s.replace("\n", "").chars().distinct().count()).mapToInt(Long::intValue).sum();
    }

    @Override
    public Integer part2() {
        return input.stream().mapToInt(this::calculate).sum();
    }

    private int calculate(String groupAnswers) {
        String[] split = groupAnswers.split("\n"); // split by new line, get each person's answer
        int requiredCharCount = split.length;
        Map<Character, Integer> occurances = new HashMap<>();

        for (String answer : split) { // for each persons answer (now we must find letters that are present in each of the answers)
            for (char character : answer.toCharArray()) {
                occurances.putIfAbsent(character, 0);
                occurances.computeIfPresent(character, (c, i) -> i + 1);
            }
        }

        return (int) occurances.entrySet().stream().filter(e -> e.getValue() == requiredCharCount).count();
    }

}