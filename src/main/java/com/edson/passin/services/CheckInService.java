package com.edson.passin.services;

import com.edson.passin.domain.attendee.Attendee;
import com.edson.passin.domain.checkin.CheckIn;
import com.edson.passin.domain.checkin.exceptions.AttendeeAlreadyExistsException;
import com.edson.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkInRepository;

    public void registerCheckIn(Attendee attendee) {
        this.verifyIfCheckInExists(attendee.getId());
        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());
        this.checkInRepository.save(newCheckIn);
    }

    private void verifyIfCheckInExists(String attendeeId) {
        Optional<CheckIn> checkIn = this.getCheckIn(attendeeId);
        if(checkIn.isPresent()) throw new AttendeeAlreadyExistsException("Attendee already exists");
    }

    public Optional<CheckIn> getCheckIn(String attendeeId) {
        return this.checkInRepository.findByAttendeeId(attendeeId);
    }
}
