package igorfv.com.passin.services;

import igorfv.com.passin.domain.attendee.Attendee;
import igorfv.com.passin.domain.event.Event;
import igorfv.com.passin.domain.event.exceptions.EventFullException;
import igorfv.com.passin.domain.event.exceptions.EventNotFoundException;
import igorfv.com.passin.dto.attendee.AttendeIdDTO;
import igorfv.com.passin.dto.attendee.AttendeeRequestDTO;
import igorfv.com.passin.dto.event.EventIdDTO;
import igorfv.com.passin.dto.event.EventRequestDTO;
import igorfv.com.passin.dto.event.EventResponseDTO;
import igorfv.com.passin.repository.EventRepository;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

  private final EventRepository eventRepository;
  private final AttendeeService attendeeService;

  public EventService(EventRepository eventRepository, AttendeeService attendeeService) {
    this.eventRepository = eventRepository;
    this.attendeeService = attendeeService;
  }

  @Transactional(readOnly = true)
  public EventResponseDTO getEvent(String eventId) {
    Event event = getEventById(eventId);
    List<Attendee> attendeList = attendeeService.getAllAttendeesFromEvent(eventId);

    return new EventResponseDTO(event, attendeList.size());
  }

  @Transactional
  public EventIdDTO createEvent(EventRequestDTO eventRequestDTO) {
    Event newEvent = new Event();

    newEvent.setTitle(eventRequestDTO.title());
    newEvent.setDetails(eventRequestDTO.details());
    newEvent.setMaximunAttendees(eventRequestDTO.maximunAttendees());
    newEvent.setSlug(createSlug(eventRequestDTO.title()));

    eventRepository.save(newEvent);

    return new EventIdDTO(newEvent.getId());
  }

  @Transactional
  public AttendeIdDTO registerAttendeeOnEvent(
      String eventId, AttendeeRequestDTO attendeeRequestDTO) {
    attendeeService.verifyAttendeeSubscription(eventId, attendeeRequestDTO.email());

    Event event = getEventById(eventId);
    List<Attendee> attendeList = attendeeService.getAllAttendeesFromEvent(eventId);

    if (event.getMaximunAttendees() <= attendeList.size())
      throw new EventFullException("Event is full");

    Attendee newAttendee = new Attendee();
    newAttendee.setName(attendeeRequestDTO.name());
    newAttendee.setEmail(attendeeRequestDTO.email());
    newAttendee.setEvent(event);
    newAttendee.setCreatedAt(LocalDateTime.now());

    attendeeService.registerAttendee(newAttendee);

    return new AttendeIdDTO(newAttendee.getId());
  }

  private String createSlug(String text) {
    String normalize = Normalizer.normalize(text, Form.NFD);
    return normalize
        .replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
        .replaceAll("[^\\w\\s]", "")
        .replaceAll("[\\s+]", "-")
        .toLowerCase();
  }

  private Event getEventById(String eventId) {
    return eventRepository
        .findById(eventId)
        .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
  }
}
