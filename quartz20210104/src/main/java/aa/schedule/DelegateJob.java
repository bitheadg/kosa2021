package aa.schedule;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/*
 * Job interface having just one method �� execute. 
It must be implemented by the class that contains the actual work to be done, i.e. the task. 
When a job's trigger fires, the scheduler invokes the execute method, passing it a JobExecutionContext object.
https://www.baeldung.com/spring-quartz-schedule
*/

@Slf4j
public class DelegateJob implements Job {

	//delegate the task to this service
	//async, q-based
    //private ITaskQueuingService taskQueuingService = TaskQueuingServiceProvider.getInstance().getTaskQueuingService();
	//sync-based
	@Autowired
	private DelegatedService ds = new DelegatedServiceImpl();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException { //actual task
    	//log.info("Job ** {} ** fired @ {} with Trigger ** {}", context.getJobDetail().getJobDataMap().get(ScheduleCode.JOB_INFO_JSON.toString()), context.getFireTime(), context.getTrigger().getJobDataMap().get(ScheduleCode.TRIGGER_INFO_JSON.toString()));
    	log.info("Job({}) ** fired @ {} with Trigger ** {}", context.getJobDetail().getKey(), context.getFireTime(), context.getTrigger().getJobDataMap().get(ScheduleCode.TRIGGER_INFO_JSON.toString()));
        //int maxRefireLimit = Integer.parseInt(Configuration.getGlobalProperty(ConfigurationConstants.MAX_REFIRE_LIMIT));
        //log.info("Adding task to execution queue. Try count #{}", context.getRefireCount());
        //if (context.getRefireCount() < maxRefireLimit) { //is this method repeatedly???
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap(); //boilerplate
            
            //Boolean success = taskQueuingService.addTaskToExecutionQueue(jobDataMap);
            Boolean success = ds.delegatedExecution(jobDataMap);
            if (!success) {
                log.error("Could not push task to queue. Retrying.. [Task-ID]: {}", context.getJobDetail().getKey());
                JobExecutionException jee = new JobExecutionException("Could not push task to queue. Trying again.");
                jee.setRefireImmediately(true);
                throw jee;
            }
    }
}