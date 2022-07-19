package aa.schedule;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.time.ZoneId;
import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrepareJobTrigger {
	
    public static JobDetail buildJob(Task task) {

        String jobId = task.getTaskId();
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(ScheduleCode.JOB_ID.toString(), jobId);
        jobDataMap.put(ScheduleCode.JOB_INFO_JSON.toString(), ScheduleTool.toJson(task));
        jobDataMap.put("METADATA",task.getTaskMetadata());
        
        //newJob().ofType(DelegateJob.class).storeDurably().withIdentity(JobKey.jobKey("Qrtz_Job_Detail")).withDescription("Invoke Sample Job service...").build();
        JobDetail jobDetail = JobBuilder.newJob(DelegateJob.class)
        		.storeDurably() //Job을 수행할 트리거가 존재하지 않더라도, Job 인스턴스가 유지 되도록 할 경우 //Creating a job without associated triggers is a valid use-case: you have a piece of logic and later you will attach one or more triggers to execute it at different points in time.
                .withIdentity(jobId, ScheduleCode.DEFAULT_GROUP_ID.toString())
                .usingJobData(jobDataMap)
                .build();
        
        return jobDetail;
    }

    public static Trigger buildTrigger(When when) {
    	
    	String triggerId = ScheduleTool.generateRandomUUID(ScheduleCode.TRIGGER_PREFIX.toString());
    	
    	JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(ScheduleCode.TRIGGER_ID.toString(), triggerId);
        jobDataMap.put(ScheduleCode.TRIGGER_INFO_JSON.toString(), ScheduleTool.toJson(when));
        
        Trigger jobTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerId, ScheduleCode.DEFAULT_GROUP_ID.toString())
                .withDescription("buildTriggerOnNewJob")
                .startAt(getStartTime(when)) //if specified //.startNow()
                .withSchedule(getScheduleBuilder(when))
                .withPriority(when.getPriority())
                .usingJobData(jobDataMap)
                .build();

        return jobTrigger;
    }
    
    public static Trigger buildTriggerOnExistingJob(JobDetail existingJob, When when ) {
    	String triggerId = ScheduleTool.generateRandomUUID(ScheduleCode.TRIGGER_PREFIX.toString());
    	
    	JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(ScheduleCode.TRIGGER_ID.toString(), triggerId);
        jobDataMap.put(ScheduleCode.TRIGGER_INFO_JSON.toString(), ScheduleTool.toJson(when));
        
    	Trigger trigger = TriggerBuilder.newTrigger()
    			.withIdentity(triggerId, ScheduleCode.DEFAULT_GROUP_ID.toString())
    		    .withDescription("buildTriggerOnExistingJob")
    		    .startAt(getStartTime(when)) //if specified //.startNow()
                .withSchedule(getScheduleBuilder(when))
                .withPriority(when.getPriority())
    		    .forJob(existingJob)
    		    .usingJobData(jobDataMap)
    		    .build();
    	return trigger;
    }

    /*
    public static Trigger buildTrigger(Task task) {
        Trigger jobTrigger = TriggerBuilder.newTrigger()
                .withIdentity(ScheduleTool.generateRandomUUID(ScheduleCode.TRIGGER_PREFIX.toString()), ScheduleCode.DEFAULT_GROUP_ID.toString())
                .startAt(getStartTime(task)) //if specified //.startNow()
                .withSchedule(getScheduleBuilder(task))
                .withPriority(task.getPriority())
                .build();

        return jobTrigger;
    }
    
    private static Date getStartTime(Task task) {
        switch (task.getSchedulingType()) {
            case CRON:
            default:
                return new Date(); //now
            case ONCE:
                return Date.from(task.getExecutionTime().atZone(ZoneId.systemDefault()).toInstant()); //???
        }
    }
    
    private static ScheduleBuilder getScheduleBuilder(Task task) {
        switch (task.getSchedulingType()) {
            case CRON:
                return cronSchedule(task.getCronExp());
            case ONCE:
            default:
                return simpleSchedule().withMisfireHandlingInstructionFireNow();
        }
    }
    */
    
    private static Date getStartTime(When when) {
        switch (when.getSchedulingType()) {
            case CRON:
            default:
                return new Date(); //now
            case ONCE:
                return Date.from(when.getExecutionTime().atZone(ZoneId.systemDefault()).toInstant()); //???
        }
    }
    
    private static ScheduleBuilder getScheduleBuilder(When when) {
        switch (when.getSchedulingType()) {
            case CRON:
                return cronSchedule(when.getCronExp());
            case ONCE:
            default:
                return simpleSchedule().withMisfireHandlingInstructionFireNow();
        }
    }
    
}
