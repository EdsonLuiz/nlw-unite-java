package com.edson.passin.controllers;

import com.edson.passin.dto.event.EventIdDTO;
import com.edson.passin.dto.event.EventRequestDTO;
import com.edson.passin.dto.event.EventResponseDTO;
import com.edson.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService service;
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {
        EventResponseDTO eventResponseDTO = this.service.getEventDetail(id);
        return ResponseEntity.ok(eventResponseDTO);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        EventIdDTO eventIdDTO = this.service.createEvent(body);
        var uri = uriComponentsBuilder.path("/v1/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();
        return ResponseEntity.created(uri).body(eventIdDTO);
    }
}
