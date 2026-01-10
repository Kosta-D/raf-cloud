package rs.raf.rafcloudbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.rafcloudbackend.model.ScheduledOperationError;

public interface ScheduledOperationErrorRepository
  extends JpaRepository<ScheduledOperationError, Long> {
}
