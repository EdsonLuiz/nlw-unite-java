package com.edson.passin.services;

import com.edson.passin.domain.attendee.Attendee;
import com.edson.passin.domain.event.Event;
import com.edson.passin.dto.EventResponseDTO;
import com.edson.passin.repositories.AttendeeRepository;
import com.edson.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;

    public EventResponseDTO getEventDetail(String eventId) {
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found with ID " + eventId));
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

}
