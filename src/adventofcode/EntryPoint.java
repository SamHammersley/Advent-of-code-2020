package adventofcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public final class EntryPoint {

    private static final String CLASS_NAME_TEMPLATE = "adventofcode.year%s.Day%s";

    private static final String DATA_DIRECTORY = "./data/";

    public static void main(String[] args) {
        System.out.println("Please enter the year and day:");
        Puzzle<?> puzzle;
        int year = -1, day = -1;

        try (Scanner scanner = new Scanner(System.in)) {
            year = scanner.nextInt();
            day = scanner.nextInt();
            puzzle = loadPuzzle(year, day);

        } catch(Exception e) {
            System.err.println("Failed to load puzzle for year " + year + ", day " + day);
            e.printStackTrace();
            puzzle = loadPrevious();
        }

        System.out.println();
        System.out.println("Part 1: " + puzzle.part1());
        System.out.println("Part 2: " + puzzle.part2());
    }

    private static void writePrevious(int year, int day) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("./data/previous"))) {
            writer.write(year + "," + day);

        } catch (IOException e) {
            throw new RuntimeException("Failed to write to previous", e);
        }
    }

    private static Puzzle<?> loadPrevious() {
        try (BufferedReader reader = Files.newBufferedReader(Path.of("./data/previous"))) {
            String[] parts = reader.readLine().split(",");

            return loadPuzzle(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));

        } catch (Exception e) {
            throw new RuntimeException("Failed to get previous puzzle!", e);
        }
    }

    private static Puzzle<?> loadPuzzle(int year, int day) throws Exception {
        String className = String.format(CLASS_NAME_TEMPLATE, year, day);
        Class<?> puzzleClass = EntryPoint.class.getClassLoader().loadClass(className);

        Puzzle<?> puzzle = (Puzzle<?>) puzzleClass.getConstructor().newInstance();
        puzzle.loadInput(DATA_DIRECTORY + year + "/day" + day);

        System.out.println("Answers for year " + year + " day " + day);

        writePrevious(year, day);
        return puzzle;
    }

}