package igorfv.com.passin.services;

import igorfv.com.passin.domain.attendee.Attendee;
import igorfv.com.passin.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import igorfv.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import igorfv.com.passin.domain.checkin.CheckIn;
import igorfv.com.passin.dto.attendee.AttendeBadgeResponseDTO;
import igorfv.com.passin.dto.attendee.AttendeeBadgeDTO;
import igorfv.com.passin.dto.attendee.AttendeeDetailsDTO;
import igorfv.com.passin.dto.attendee.AttendeesListResponseDTO;
import igorfv.com.passin.repository.AttendeeRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AttendeeService {

  private final AttendeeRepository attendeeRepository;
  private final CheckInService checkInService;

  public AttendeeService(AttendeeRepository attendeeRepository, CheckInService checkInService) {
    this.attendeeRepository = attendeeRepository;
    this.checkInService = checkInService;
  }

  public List<Attendee> getAllAttendeesFromEvent(String eventId) {
    return attendeeRepository.findByEventId(eventId);
  }

  @Transactional(readOnly = true)
  public AttendeesListResponseDTO getEventsAttendee(String eventId) {
    List<Attendee> attendeeList = getAllAttendeesFromEvent(eventId);

    List<AttendeeDetailsDTO> attendeeDetailsDTOList =
        attendeeList.stream()
            .map(
                attendee -> {
                  Optional<CheckIn> checkIn = checkInService.getCheckIn(attendee.getId());
                  LocalDateTime checkInAt =
                      checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
                  return new AttendeeDetailsDTO(
                      attendee.getId(),
                      attendee.getName(),
                      attendee.getEmail(),
                      attendee.getCreatedAt(),
                      checkInAt);
                })
            .toList();

    return new AttendeesListResponseDTO(attendeeDetailsDTOList);
  }

  @Transactional
  public Attendee registerAttendee(Attendee newAttendee) {
    attendeeRepository.save(newAttendee);
    return newAttendee;
  }

  @Transactional(readOnly = true)
  public void verifyAttendeeSubscription(String eventId, String email) {
    Optional<Attendee> attendeRegistered = attendeeRepository.findByEventIdAndEmail(eventId, email);

    if (attendeRegistered.isPresent())
      throw new AttendeeAlreadyRegisteredException("Attendee already registered");
  }

  @Transactional(readOnly = true)
  public AttendeBadgeResponseDTO getAttendeeBadge(
      String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
    Attendee attendee = getAttendee(attendeeId);

    var uri =
        uriComponentsBuilder
            .path("/attendees/{attendeeId}/check-in")
            .buildAndExpand(attendeeId)
            .toUri()
            .toString();

    AttendeeBadgeDTO attendeeBadgeDTO =
        new AttendeeBadgeDTO(
            attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());

    return new AttendeBadgeResponseDTO(attendeeBadgeDTO);
  }

  public void checkInAttendee(String attendeeId) {
    Attendee attendee = getAttendee(attendeeId);
    checkInService.registerCheckIn(attendee);
  }

  private Attendee getAttendee(String attendeeId) {
    return attendeeRepository
        .findById(attendeeId)
        .orElseThrow(
            () -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
  }
}
