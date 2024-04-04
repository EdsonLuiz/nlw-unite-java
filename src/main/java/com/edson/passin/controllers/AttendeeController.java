package com.edson.passin.controllers;

import com.edson.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.edson.passin.services.AttendeeService;
import com.edson.passin.services.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/v1/attendees")
@RequiredArgsConstructor
public class AttendeeController {
    private final AttendeeService attendeeService;
    @GetMapping("/{attendeeId}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        AttendeeBadgeResponseDTO attendeeBadgeResponseDTO = this.attendeeService.getAttendeeBadge(attendeeId, uriComponentsBuilder);
        return ResponseEntity.ok(attendeeBadgeResponseDTO);
    }

    @PostMapping("/{attendeeId}/check-in")
    public ResponseEntity registerCheckIn(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        this.attendeeService.checkInAttendee(attendeeId);
        var uri = uriComponentsBuilder.path("/v1/attendees/{attendeeId}/badge").buildAndExpand(attendeeId).toUri();
        return ResponseEntity.created(uri).build();
    }
}
