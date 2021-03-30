package adventofcode.year2020;

import adventofcode.Puzzle;
import adventofcode.year2020.util.FieldRule;
import adventofcode.year2020.util.Ticket;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Terrible solution
 * Bad day, u?
 */
public final class Day16 extends Puzzle<Long> {

    private static final String YOUR_TICKET_KEY = "your ticket:";

    public Day16() {
        super("\n", "");
    }

    @Override
    public Long part1() {
        int ticketStartIndex = input.indexOf(YOUR_TICKET_KEY);

        Set<FieldRule> rules = mapInput(FieldRule::parse).limit(ticketStartIndex - 1).collect(Collectors.toSet());
        Stream<Ticket> tickets = mapInput(s -> s.split(",\\s*")).skip(ticketStartIndex + 1).filter(Objects::nonNull)
                .map(Ticket::parse)
                .filter(Objects::nonNull);

        return tickets.mapToLong(t -> t.filter(v -> rules.stream().noneMatch(r -> r.test(v))).sum()).sum();
    }

    @Override
    public Long part2() {
        int ticketStartIndex = input.indexOf(YOUR_TICKET_KEY);

        Set<FieldRule> rules = mapInput(FieldRule::parse).limit(ticketStartIndex - 1).collect(Collectors.toSet());

        Ticket myTicket = mapInput(s -> s.split(",\\s*")).skip(ticketStartIndex + 1).limit(1).findAny().map(Ticket::parse).get();

        Set<Ticket> tickets = mapInput(s -> s.split(",\\s*")).skip(ticketStartIndex + 2)
                .map(Ticket::parse)
                .filter(t -> isValid(t, rules))
                .collect(Collectors.toSet());

        Map<FieldRule, Integer> index = mapFields(new LinkedHashMap<>(), rules, tickets);

        Stream<Integer> departurePositions = index.keySet()
                .stream()
                .filter(r -> r.name().startsWith("departure"))
                .map(index::get);

        return departurePositions.mapToLong(i -> myTicket.getValues().get(i)).reduce((a,b) -> a*b).getAsLong();
    }

    private Map<FieldRule, Integer> mapFields(Map<FieldRule, Integer> index, Set<FieldRule> rules, Set<Ticket> tickets) {
        if (rules.isEmpty()) {
            return index;
        }

        for (int i = 0; i < 20; i++) {
            Set<FieldRule> matches = new HashSet<>();

            for (FieldRule rule : rules) {
                boolean match = true;

                for (Ticket t : tickets) {
                    int value = t.getValues().get(i);

                    if (!rule.test(value)) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    matches.add(rule);
                }
            }

            if (matches.size() == 1) {
                FieldRule rule = matches.iterator().next();

                index.put(rule, i);
                rules.remove(rule);
            }
        }

        return mapFields(index, rules, tickets);
    }

    private boolean isValid(Ticket ticket, Set<FieldRule> rules) {
        return ticket != null && ticket.getValues().stream().allMatch(v -> rules.stream().anyMatch(r -> r.test(v)));
    }

}