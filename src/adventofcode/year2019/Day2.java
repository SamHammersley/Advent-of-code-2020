package adventofcode.year2019;

import adventofcode.Puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Day2 extends Puzzle<Integer> {

    public Day2() {
        super(",", "");
    }

    private int interpret(List<Integer> program, int noun, int verb) {
        program.set(1, noun);
        program.set(2, verb);

        boolean halt = false;

        for (int instructionPointer = 0; !halt && instructionPointer < program.size() - 4; instructionPointer += 4) {
            int opcode = program.get(instructionPointer);
            int operand1 = program.get(program.get(instructionPointer + 1));
            int operand2 = program.get(program.get(instructionPointer + 2));
            int resultPosition = program.get(instructionPointer + 3);

            switch(opcode) {
                case 1:
                    program.set(resultPosition, operand1 + operand2);
                    break;
                case 2:
                    program.set(resultPosition, operand1 * operand2);
                    break;
                case 99:
                    halt = true;
                    break;
            }
        }

        return program.get(0);
    }

    @Override
    public Integer part1() {
        List<Integer> program = mapInput(Integer::parseInt).collect(Collectors.toList());

        return interpret(program, 12, 2);
    }

    @Override
    public Integer part2() {
        String output = "";

        for (int i = 0; i <= 99; i++) {
            for (int j = 0; j <= 99; j++) {
                List<Integer> programCopy = mapInput(Integer::parseInt).collect(Collectors.toList());

                if (interpret(programCopy, i, j) == 19690720) {
                    output = i + "" + j;
                }
            }
        }

        return Integer.parseInt(output);
    }
}