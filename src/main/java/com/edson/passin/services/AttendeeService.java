package com.edson.passin.services;

import com.edson.passin.domain.attendee.Attendee;
import com.edson.passin.domain.attendee.exceptions.AttendeeAlreadyRegistered;
import com.edson.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.edson.passin.domain.checkin.CheckIn;
import com.edson.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.edson.passin.dto.attendee.AttendeeDetails;
import com.edson.passin.dto.attendee.AttendeesResponseDTO;
import com.edson.passin.dto.attendee.AttendeeBadgeDTO;
import com.edson.passin.repositories.AttendeeRepository;
import com.edson.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInRepository checkInRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findAllByEventId(eventId);
    }

    public AttendeesResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendees = this.getAllAttendeesFromEvent(eventId);
        List<AttendeeDetails> attendeeDetails = attendees.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();
        return new AttendeesResponseDTO(attendeeDetails);
    }

    public Attendee registerAttendee(Attendee newAttendee) {
        return this.attendeeRepository.save(newAttendee);
    }

    public void verifyAttendeeSubscription(String email, String eventId) {
        Optional<Attendee> registered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if(registered.isPresent()) {
            throw new AttendeeAlreadyRegistered("Attendee is already registered");
        }
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        Attendee attendee = this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID " + attendeeId));
        var uri = uriComponentsBuilder.path("/v1/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();
        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }
}
