package rs.raf.rafcloudbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.rafcloudbackend.model.ScheduledOperation;
import rs.raf.rafcloudbackend.model.ScheduledOperationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledOperationRepository
  extends JpaRepository<ScheduledOperation, Long> {

  List<ScheduledOperation> findByStatusAndScheduledAtBefore(
    ScheduledOperationStatus status,
    LocalDateTime time
  );
}
