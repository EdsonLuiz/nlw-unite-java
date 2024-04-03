package com.edson.passin.dto.event;

import com.edson.passin.domain.event.Event;
import com.edson.passin.dto.event.EventDetailDTO;

public class EventResponseDTO {
    EventDetailDTO event;

    public EventResponseDTO(Event event, Integer numberOfAttendees) {
        this.event = new EventDetailDTO(event.getId(), event.getTitle(), event.getDetails(), event.getSlug(), event.getMaximumAttendees(), numberOfAttendees);
    }
}
