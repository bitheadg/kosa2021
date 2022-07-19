package aa.schedule;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class When { //only about trigger-related
    private String whenId;
    private String taskId; //related jobId
    private ScheduleCode schedulingType;
    
    //private Retry retry;
    private int count;
    private int maxRetries;
    private int base;
    private int delay;
    
    private int priority;
    private LocalDateTime executionTime;
    private String cronExp;

    public When(ScheduleCode type) {
        this.schedulingType = type;
    }
}
