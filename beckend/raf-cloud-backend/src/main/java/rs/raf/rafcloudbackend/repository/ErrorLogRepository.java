package rs.raf.rafcloudbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.rafcloudbackend.model.ErrorLog;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
