package igorfv.com.passin.controller;

import igorfv.com.passin.dto.attendee.AttendeBadgeResponseDTO;
import igorfv.com.passin.services.AttendeeService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/attendees")
public class AttendeeController {

  private final AttendeeService attendeeService;

  public AttendeeController(AttendeeService attendeeService) {
    this.attendeeService = attendeeService;
  }

  @GetMapping("/{attendeeId}/badge")
  public ResponseEntity<AttendeBadgeResponseDTO> getAttendeeBadge(
      @PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
    AttendeBadgeResponseDTO attendeeBadge =
        attendeeService.getAttendeeBadge(attendeeId, uriComponentsBuilder);
    return ResponseEntity.ok(attendeeBadge);
  }

  @PostMapping("/{attendeeId}/check-in")
  public ResponseEntity registerCheckIn(
      @PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
    attendeeService.checkInAttendee(attendeeId);

    URI uri = uriComponentsBuilder.path("{attendeeId}/badge").buildAndExpand(attendeeId).toUri();

    return ResponseEntity.created(uri).build();
  }
}
