package rs.raf.rafcloudbackend.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.raf.rafcloudbackend.dto.MachineWsDto;
import rs.raf.rafcloudbackend.model.ErrorLog;
import rs.raf.rafcloudbackend.model.Machine;
import rs.raf.rafcloudbackend.model.MachineState;
import rs.raf.rafcloudbackend.repository.ErrorLogRepository;
import rs.raf.rafcloudbackend.repository.MachineRepository;

import java.util.Arrays;

@Service
public class MachineAsyncService {

  private final MachineRepository machineRepository;
  private final SimpMessagingTemplate messagingTemplate;
  private final ErrorLogRepository errorLogRepository;


  public MachineAsyncService(
    MachineRepository machineRepository,
    SimpMessagingTemplate messagingTemplate,
    ErrorLogRepository errorLogRepository
  ) {
    this.machineRepository = machineRepository;
    this.messagingTemplate = messagingTemplate;
    this.errorLogRepository = errorLogRepository;
  }


  @Async
  public void startAsync(Long machineId) {
    try {
      Thread.sleep(10_000);

      Machine m = machineRepository.findById(machineId).orElseThrow();
      m.setState(MachineState.RUNNING);
      machineRepository.save(m);

      messagingTemplate.convertAndSend(
        "/topic/machines",
        new MachineWsDto(
          m.getId(),
          m.getName(),
          m.getState().name()
        )
      );


    } catch (Exception e) {
      ErrorLog errorLog = new ErrorLog(
        e.getMessage(),
        Arrays.toString(e.getStackTrace())
      );
      errorLogRepository.save(errorLog);
    }

  }


  @Async
  public void restartAsync(Long machineId) {
    try {
      Thread.sleep(10_000);

      Machine m = machineRepository.findById(machineId).orElseThrow();
      m.setState(MachineState.RUNNING);
      machineRepository.save(m);

      // ⬇️ šaljemo ENTITY (ili jednostavan DTO)
      messagingTemplate.convertAndSend(
        "/topic/machines",
        new MachineWsDto(
          m.getId(),
          m.getName(),
          m.getState().name()
        )
      );


    } catch (Exception e) {
      ErrorLog errorLog = new ErrorLog(
        e.getMessage(),
        Arrays.toString(e.getStackTrace())
      );
      errorLogRepository.save(errorLog);
    }

  }

  @Async
  public void stopAsync(Long machineId) {
    try {
      Thread.sleep(10_000);

      Machine m = machineRepository.findById(machineId).orElseThrow();
      m.setState(MachineState.STOPPED);
      machineRepository.save(m);

      messagingTemplate.convertAndSend(
        "/topic/machines",
        new MachineWsDto(
          m.getId(),
          m.getName(),
          m.getState().name()
        )
      );


    } catch (Exception e) {
      ErrorLog errorLog = new ErrorLog(
        e.getMessage(),
        Arrays.toString(e.getStackTrace())
      );
      errorLogRepository.save(errorLog);
    }

  }

}
