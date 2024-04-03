package com.edson.passin.controllers;

import com.edson.passin.dto.attendee.AttendeeIdDTO;
import com.edson.passin.dto.attendee.AttendeeRequestDTO;
import com.edson.passin.dto.attendee.AttendeesResponseDTO;
import com.edson.passin.dto.event.EventIdDTO;
import com.edson.passin.dto.event.EventRequestDTO;
import com.edson.passin.dto.event.EventResponseDTO;
import com.edson.passin.services.AttendeeService;
import com.edson.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventservice;
    private final AttendeeService attendeeService;
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {
        EventResponseDTO eventResponseDTO = this.eventservice.getEventDetail(id);
        return ResponseEntity.ok(eventResponseDTO);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        EventIdDTO eventIdDTO = this.eventservice.createEvent(body);
        var uri = uriComponentsBuilder.path("/v1/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();
        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesResponseDTO> getEventAttendees(@PathVariable String id) {
        AttendeesResponseDTO attendeesResponseDTO = this.attendeeService.getEventsAttendee(id);
        return ResponseEntity.ok(attendeesResponseDTO);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        AttendeeIdDTO attendeeIdDTO = this.eventservice.registerAttendeeOnEvent(eventId, body);
        var uri = uriComponentsBuilder.path("/v1/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.attendeeId()).toUri();
        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }

}
