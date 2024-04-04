package igorfv.com.passin.config;

import igorfv.com.passin.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import igorfv.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import igorfv.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import igorfv.com.passin.domain.event.exceptions.EventFullException;
import igorfv.com.passin.domain.event.exceptions.EventNotFoundException;
import igorfv.com.passin.dto.general.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity handleEventNotFound(EventNotFoundException exception) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(EventFullException.class)
  public ResponseEntity<ErrorResponseDTO> handleEventFull(EventFullException exception) {
    return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
  }

  @ExceptionHandler(AttendeeNotFoundException.class)
  public ResponseEntity handleAttendeNotFound(AttendeeNotFoundException exception) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(AttendeeAlreadyRegisteredException.class)
  public ResponseEntity handleAttendeeAlreadyExist(AttendeeAlreadyRegisteredException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler(CheckInAlreadyExistsException.class)
  public ResponseEntity handleCheckInAlreadyExist(CheckInAlreadyExistsException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }
}
