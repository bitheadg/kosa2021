package aa.schedule;

import lombok.Data;

@Data
//@Alias("scheduleVo")
public class ScheduleVo {
    private String jobId;
    private String triggerId;
    private String group_id;
    private String user_id;
    //@NotEmpty(message = "Please enter schedule memo")
    private String datetimeOnce; //yyyymmddhhmm
    private String datetimeCronExpression;
    private String location;
    private String memo;
    private int schedule_cnt;
}