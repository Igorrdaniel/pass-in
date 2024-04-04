package igorfv.com.passin.controller;

import igorfv.com.passin.dto.attendee.AttendeIdDTO;
import igorfv.com.passin.dto.attendee.AttendeeRequestDTO;
import igorfv.com.passin.dto.attendee.AttendeesListResponseDTO;
import igorfv.com.passin.dto.event.EventIdDTO;
import igorfv.com.passin.dto.event.EventRequestDTO;
import igorfv.com.passin.dto.event.EventResponseDTO;
import igorfv.com.passin.services.AttendeeService;
import igorfv.com.passin.services.EventService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
public class EventController {

  private final EventService eventService;
  private final AttendeeService attendeeService;

  public EventController(EventService eventService, AttendeeService attendeeService) {
    this.eventService = eventService;
    this.attendeeService = attendeeService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {
    EventResponseDTO event = eventService.getEvent(id);
    return ResponseEntity.ok(event);
  }

  @GetMapping("/attendees/{id}")
  public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id) {
    AttendeesListResponseDTO attendeeListResponse = attendeeService.getEventsAttendee(id);
    return ResponseEntity.ok(attendeeListResponse);
  }

  @PostMapping
  public ResponseEntity<EventIdDTO> createEvent(
      @RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
    EventIdDTO eventIdDTO = eventService.createEvent(body);

    URI uri =
        uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

    return ResponseEntity.created(uri).body(eventIdDTO);
  }

  @PostMapping("/{eventId}/attendees")
  public ResponseEntity<AttendeIdDTO> registerParticipant(
      @PathVariable String eventId,
      @RequestBody AttendeeRequestDTO attendeeRequestDTO,
      UriComponentsBuilder uriComponentsBuilder) {

    AttendeIdDTO attendeIdDTO = eventService.registerAttendeeOnEvent(eventId, attendeeRequestDTO);

    URI uri =
        uriComponentsBuilder
            .path("/attendees/{attendeeId}/badge")
            .buildAndExpand(attendeIdDTO.attendeeId())
            .toUri();

    return ResponseEntity.created(uri).body(attendeIdDTO);
  }
}
