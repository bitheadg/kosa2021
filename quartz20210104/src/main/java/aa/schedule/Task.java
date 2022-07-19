package aa.schedule;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Task {
    private String taskId;
    private String taskType;
    private String taskMetadata;
    private int priority;
    
    public Task(String taskId) {
        this.taskId = taskId;
    }
    /*
    private ScheduleCode schedulingType;
    //private Retry retry;
    private int count;
    private int maxRetries;
    private int base;
    private int delay;
    
    
    private LocalDateTime executionTime;
    private String cronExp;

    public Task(ScheduleCode type) {
        this.schedulingType = type;
    }
    */
}
