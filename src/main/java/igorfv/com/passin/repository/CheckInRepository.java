package igorfv.com.passin.repository;

import igorfv.com.passin.domain.checkin.CheckIn;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {

  Optional<CheckIn> findByAttendeeId(String attendeId);
}
