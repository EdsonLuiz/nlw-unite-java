package com.edson.passin.services;

import com.edson.passin.domain.attendee.Attendee;
import com.edson.passin.domain.event.Event;
import com.edson.passin.domain.event.exceptions.EventFullException;
import com.edson.passin.domain.event.exceptions.EventNotFoundException;
import com.edson.passin.dto.attendee.AttendeeIdDTO;
import com.edson.passin.dto.attendee.AttendeeRequestDTO;
import com.edson.passin.dto.event.EventIdDTO;
import com.edson.passin.dto.event.EventRequestDTO;
import com.edson.passin.dto.event.EventResponseDTO;
import com.edson.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId) {
        Event event = getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event newEvent = new Event();
        newEvent.setTitle(eventRequestDTO.title());
        newEvent.setDetails(eventRequestDTO.details());
        newEvent.setMaximumAttendees(eventRequestDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventRequestDTO.title()));
        this.eventRepository.save(newEvent);
        return new EventIdDTO(newEvent.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO) {
         this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);
        Event event = getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        if(event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullException("Event is full");
        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);
        return new AttendeeIdDTO(newAttendee.getId());
    }

    private Event getEventById(String eventId) {
        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException( "Event not found with ID " + eventId));
    }

    private String createSlug(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return  normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\s\\w]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }
}
