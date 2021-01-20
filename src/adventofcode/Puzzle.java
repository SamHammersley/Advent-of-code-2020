package adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class Puzzle<R> {

    protected final List<String> input = new LinkedList<>();

    private final String inputDelimiter;

    private final String pattern;

    public Puzzle(String inputDelimiter, String pattern) {
        this.inputDelimiter = inputDelimiter;
        this.pattern = pattern;
    }

    final void loadInput(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             Scanner scanner = new Scanner(reader)) {

            scanner.useDelimiter(inputDelimiter);
            scanner.forEachRemaining(input::add);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected final <T> Stream<T> mapInput(Function<String, T> mappingFunction) {
        return input.stream().map(mappingFunction);
    }

    public abstract R part1();

    public abstract R part2();

}