package adventofcode.year2020;

import adventofcode.Puzzle;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day14 extends Puzzle<BigInteger> {

    interface DockingInstruction {

        void execute(char[] bitmask, Map<Long, Long> memory);

    }

    public Day14() {
        super("\n", "");
    }

    @Override
    public BigInteger part1() {
        char[] bitmask = new char[36];
        Map<Long, Long> memory = new HashMap<>();

        mapInput(this::part1Decode).forEach(i -> i.execute(bitmask, memory));

        return sum(memory);
    }

    private BigInteger sum(Map<Long, Long> memory) {
        BigInteger total = BigInteger.valueOf(0);

        for (long l : memory.values()) {
            total = total.add(BigInteger.valueOf(l));
        }

        return total;
    }

    private DockingInstruction part1Decode(String instruction) {
        String operand = instruction.substring(instruction.indexOf('=') + 2);

        switch(instruction.charAt(1)) {
            case 'a':
                char[] newMask = operand.toCharArray();
                return (b, m) -> System.arraycopy(newMask, 0, b, 0, newMask.length);

            case 'e':
                long address = Long.parseLong(instruction.substring(4, instruction.indexOf(']')));
                long value = Long.parseLong(operand);

                return (b, m) -> m.put(address, applyBitmask(b, value));

            default:
                throw new RuntimeException("Invalid input");
        }
    }

    private long applyBitmask(char[] bitmask, long value) {
        BitSet valueBitSet = BitSet.valueOf(new long[] { value });

        for (int index = 0; index < bitmask.length; index++) {
            char c = bitmask[bitmask.length - 1 - index];

            if (c == 'X') {
                continue;
            }

            int m = Integer.parseInt(Character.toString(c));
            valueBitSet.set(index, m != 0);
        }

        return valueBitSet.toLongArray()[0];
    }

    @Override
    public BigInteger part2() {
        char[] bitmask = new char[36];
        Map<Long, Long> memory = new HashMap<>();

        mapInput(this::part2Decode).forEach(i -> i.execute(bitmask, memory));

        return sum(memory);
    }

    private DockingInstruction part2Decode(String instruction) {
        String operand = instruction.substring(instruction.indexOf('=') + 2);

        switch(instruction.charAt(1)) {
            case 'a':
                char[] newMask = operand.toCharArray();
                return (b, m) -> System.arraycopy(newMask, 0, b, 0, newMask.length);

            case 'e':
                long address = Long.parseLong(instruction.substring(4, instruction.indexOf(']')));
                long value = Long.parseLong(operand);

                return (b, m) -> m.putAll(applyBitmask1(b, address, value));

            default:
                throw new RuntimeException("Invalid input");
        }
    }

    private Map<Long, Long> applyBitmask1(char[] bitmask, long address, long value) {
        BitSet valueBitSet = BitSet.valueOf(new long[] { address });

        for (int index = 0; index < bitmask.length; index++) {
            char c = bitmask[bitmask.length - index - 1];

            if (c != 'X') {
                int m = Integer.parseInt(Character.toString(c));
                valueBitSet.set(index, valueBitSet.get(index) || m != 0);
            }
        }

        char[] bitmaskCopy = Arrays.copyOf(bitmask, bitmask.length);
        Set<Long> permutations = applyFloating(bitmaskCopy, 0, valueBitSet);

        return new HashMap<>(permutations.stream().collect(Collectors.toMap(Function.identity(), l -> value)));
    }

    private Set<Long> applyFloating(char[] bitmask, int fromIndex, BitSet address) {
        Set<Long> values = new HashSet<>();

        for (int index = fromIndex; index < bitmask.length; index++) {
            char c = bitmask[bitmask.length - 1 - index];

            if (c == 'X') {
                values.add(address.toLongArray()[0]);
                values.addAll(applyFloating(bitmask, index + 1, address));

                address.flip(index);

                values.add(address.toLongArray()[0]);
                values.addAll(applyFloating(bitmask, index + 1, address));
            }
        }

        return values;
    }
}