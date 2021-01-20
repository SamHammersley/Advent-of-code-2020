package adventofcode;

import java.util.Scanner;

public final class EntryPoint {

    private static final String CLASS_NAME_TEMPLATE = "adventofcode.year%d.Day%d";

    private static final String DATA_DIRECTORY = "./data/";

    public static void main(String[] args) {
        System.out.println("Please enter the year and day:\n");

        try (Scanner scanner = new Scanner(System.in)) {
            int year = scanner.nextInt();
            int day = scanner.nextInt();

            String className = String.format(CLASS_NAME_TEMPLATE, year, day);
            Class<?> puzzleClass = EntryPoint.class.getClassLoader().loadClass(className);
            Puzzle<?> puzzle = (Puzzle<?>) puzzleClass.getConstructor().newInstance();

            puzzle.loadInput(DATA_DIRECTORY + year + "/day" + day);

            System.out.println("Answers for day " + day);
            System.out.println("Part 1: " + puzzle.part1());
            System.out.println("Part 2: " + puzzle.part2());

        } catch(Exception e) {
            throw new RuntimeException("Exception thrown", e);
        }
    }

}