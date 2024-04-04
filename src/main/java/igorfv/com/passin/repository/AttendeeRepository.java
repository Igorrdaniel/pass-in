package igorfv.com.passin.repository;

import igorfv.com.passin.domain.attendee.Attendee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, String> {

  List<Attendee> findByEventId(String eventId);

  Optional<Attendee> findByEventIdAndEmail(String eventId, String email);
}
