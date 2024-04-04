package igorfv.com.passin.services;

import igorfv.com.passin.domain.attendee.Attendee;
import igorfv.com.passin.domain.checkin.CheckIn;
import igorfv.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import igorfv.com.passin.repository.CheckInRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckInService {

  private final CheckInRepository checkInRepository;

  public CheckInService(CheckInRepository checkInRepository) {
    this.checkInRepository = checkInRepository;
  }

  @Transactional
  public void registerCheckIn(Attendee attendee) {
    verifyCheckInExist(attendee.getId());

    CheckIn newCheckIn = new CheckIn();
    newCheckIn.setAttendee(attendee);
    newCheckIn.setCreatedAt(LocalDateTime.now());

    checkInRepository.save(newCheckIn);
  }

  public Optional<CheckIn> getCheckIn(String attendeeId) {
    return checkInRepository.findByAttendeeId(attendeeId);
  }

  private void verifyCheckInExist(String attendeeId) {
    Optional<CheckIn> checkIn = getCheckIn(attendeeId);

    if (checkIn.isPresent()) throw new CheckInAlreadyExistsException("Attende already checked in");
  }
}
