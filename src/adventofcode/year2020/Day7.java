package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Day7 extends Puzzle<Integer> {

    static class Item {

        private final String description;

        private final int quantity;

        public Item(String description, int quantity) {
            this.description = description;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return description + " x" + quantity;
        }

    }

    static class LuggageRule {

        private final String key;

        private final Item[] items;

        public LuggageRule(String key, Item[] items) {
            this.key = key;
            this.items = items;
        }

        public String getKey() {
            return key;
        }

        public boolean contains(String description) {
            for (Item item : items) {
                if (item.description.equalsIgnoreCase(description)) {
                    return true;
                }
            }

            return false;
        }

        public boolean isEmpty() {
            return items.length == 0;
        }

        @Override
        public String toString() {
            return key + " bags contain " + Arrays.toString(items);
        }

        private static final Pattern BAG_DESC_PATTERN = Pattern.compile("(\\d) ([a-z]+ [a-z]+)");

        static LuggageRule parse(String rule) {
            String[] parts = rule.split(" bags contain ");
            Matcher matcher = BAG_DESC_PATTERN.matcher(parts[1]);
            List<Item> items = new ArrayList<>();

            while (matcher.find()) {
                int quantity = Integer.parseInt(matcher.group(1));
                String description = matcher.group(2);

                items.add(new Item(description, quantity));
            }

            return new LuggageRule(parts[0], items.toArray(new Item[0]));
        }

    }

    public Day7() {
        super("\n", "");
    }

    @Override
    public Integer part1() {
        Map<String, LuggageRule> rules = mapInput(LuggageRule::parse).collect(Collectors.toMap(LuggageRule::getKey, Function.identity()));
        int total = 0;

        for (LuggageRule rule : rules.values()) {
            total += canHold(rules, rule, "shiny gold") ? 1 : 0;
        }

        return total;
    }

    private final Set<String> approved = new HashSet<>();

    private boolean canHold(Map<String, LuggageRule> rules, LuggageRule rule, String key) {
        if (rule.contains(key) || approved.contains(rule.key)) {
            return true;
        }

        for (Item item : rule.items) {
            if (canHold(rules, rules.get(item.description), key)) {
                approved.add(rule.key);
                return true;
            }
        }

        return false;
    }

    @Override
    public Integer part2() {
        Map<String, LuggageRule> rules = mapInput(LuggageRule::parse).collect(Collectors.toMap(LuggageRule::getKey, Function.identity()));

        LuggageRule start = rules.get("shiny gold");

        return countContained(rules, start) - 1;
    }

    private int countContained(Map<String, LuggageRule> rules, LuggageRule rule) {
        int quantity = 1;

        for (Item item : rule.items) {
            quantity += item.quantity * countContained(rules, rules.get(item.description));
        }

        return quantity;

    }

}