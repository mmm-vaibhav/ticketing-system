package com.ticketing.ticket.model.domain;

import java.util.Set;

public enum TicketStatus {

    OPEN, IN_PROGRESS, RESOLVED, CLOSED, CANCELLED;

    private static final Set<Transition> VALID_TRANSITIONS = Set.of(
            new Transition(OPEN, Set.of(IN_PROGRESS, RESOLVED, CANCELLED)),
            new Transition(IN_PROGRESS, Set.of(RESOLVED, CLOSED, CANCELLED)),
            new Transition(RESOLVED, Set.of(CLOSED, IN_PROGRESS)),
            new Transition(CLOSED, Set.of()),
            new Transition(CANCELLED, Set.of())
    );


    public boolean canTransitionTo(TicketStatus target) {
        return VALID_TRANSITIONS.stream()
                .filter(t -> t.from() == this)
                .anyMatch(t -> t.to().contains(target));
    }

    public boolean isTerminal() {
        return this == CLOSED || this == CANCELLED;
    }

}

record Transition(TicketStatus from, Set<TicketStatus> to) {}




