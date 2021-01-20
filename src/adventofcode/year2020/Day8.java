package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.*;

public final class Day8 extends Puzzle<Integer> {

    public Day8() {
        super("\n", "");
    }

    static class Instruction {

        private final String operation;

        private final int operand;

        public Instruction(String operation, int operand) {
            this.operation = operation;
            this.operand = operand;
        }

        static Instruction parse(String input) {
            String[] split = input.split(" ");

            return new Instruction(split[0], Integer.parseInt(split[1]));
        }

        @Override
        public String toString() {
            return operation + " " + operand;
        }

    }

    @Override
    public Integer part1() {
        Instruction[] instructions = mapInput(Instruction::parse).toArray(Instruction[]::new);

        return executeInstructions(instructions) & 0x7FFFFFFF;
    }

    @Override
    public Integer part2() {
        Instruction[] instructions = mapInput(Instruction::parse).toArray(Instruction[]::new);

        for (int i = 0; i < instructions.length; i++) {
            Instruction[] instrCopy = Arrays.copyOf(instructions, instructions.length);
            Instruction instruction = instructions[i];
            String newOperation = instruction.operation;

            if (instruction.operation.equalsIgnoreCase("jmp")) {
                newOperation = "nop";
            } else if (instruction.operation.equalsIgnoreCase("nop")) {
                newOperation = "jmp";
            }

            instrCopy[i] = new Instruction(newOperation, instruction.operand);

            int accumulator = executeInstructions(instrCopy);
            boolean terminatedEarly = (accumulator >>> 31) == 1;

            if (!terminatedEarly) {
                return accumulator & 0x7FFFFFFF;
            }
        }

        throw new RuntimeException("Unable to find corrupted instruction");
    }

    private int executeInstructions(Instruction[] instructions) {
        int accumulator = 0;
        Set<Integer> visited = new HashSet<>();

        for (int pointer = 0; pointer < instructions.length; pointer++) {
            Instruction instruction = instructions[pointer];

            if (!visited.add(pointer)) {
                return (1 << 31) | (accumulator & 0x7FFFFFFF);
            }

            switch(instruction.operation) {
                case "acc":
                    accumulator += instruction.operand;
                    break;
                case "jmp":
                    pointer += instruction.operand - 1;
                    break;
            }
        }

        return accumulator & 0x7FFFFFFF;
    }

}