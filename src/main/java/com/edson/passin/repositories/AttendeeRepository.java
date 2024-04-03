package com.edson.passin.repositories;

import com.edson.passin.domain.attendee.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
    List<Attendee> findByEventId(String eventId);
    List<Attendee> findAllByEventId(String eventId);
}
