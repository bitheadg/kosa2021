package aa.schedule;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.quartz.JobDetail;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aa.exception.InternalServerException;
import aa.exception.ServiceException;
import aa.exception.ServiceExceptionCodes;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScheduleCockpitImpl implements ScheduleCockpit {

	@Autowired
    private Scheduler scheduler;
	
	private volatile Lock _LOCK = new ReentrantLock();
    /*
	@Autowired
    private IJobManagementService jobManagementService;

    public AddTaskResponse addTask(Task task) {
        String jobId = jobManagementService.createTask(task);
        log.info("Job added to Job Scheduler. [JOB-ID]: {}", jobId);
        return new AddTaskResponse(ScheduleResponse.ACK, jobId);
    }
    */
	
    public JobDetail associateNewJobNewTrigger(Task task, When when) {
        JobDetail jobDetails = PrepareJobTrigger.buildJob(task);
        when.setTaskId(task.getTaskId()); //linked jobId
        Trigger jobTrigger = PrepareJobTrigger.buildTrigger(when);
        try {
            log.debug("Job datamap: {}", jobDetails.getJobDataMap().get(ScheduleCode.JOB_INFO_JSON.toString()));
            log.debug("Trigger datamap: {}", jobTrigger.getJobDataMap().get(ScheduleCode.TRIGGER_INFO_JSON.toString()));
            //log.info("scheduler isStarted :" +scheduler.isStarted());
            scheduler.scheduleJob(jobDetails, jobTrigger);
        } catch (ObjectAlreadyExistsException oaee) {
            log.error("Job ID already exists. [JOB-ID]: {}", jobDetails.getKey());
            throw new ServiceException(
                    ServiceExceptionCodes.JOB_ID_ALREADY_PRESENT.code(),
                    ServiceExceptionCodes.JOB_ID_ALREADY_PRESENT.message());
        } catch (SchedulerException e) {
            log.error("Scheduler exception occurred. Exception: {}", e);
            throw new InternalServerException();
        }
        return jobDetails; //for reuse of job
    }

    //GenericResponse deleteTask(String taskId);

    @Override
    public ScheduleResponse startScheduler() {
        // acquire lock
        _LOCK.lock();
        try {
            // check if shutdown has not been called.
            if (scheduler.isShutdown()) {
                throw new ServiceException(ServiceExceptionCodes.SCHEDULER_HAS_BEEN_SHUTDOWN.code(),
                        ServiceExceptionCodes.SCHEDULER_HAS_BEEN_SHUTDOWN.message());
            }
            // only start the consumer if it is in stand by mode.
            if (!scheduler.isStarted() || scheduler.isInStandbyMode()) {
                log.info("Starting new threads to poll requests.");
                //RequestConsumers.startNewThreads();
                log.info("Starting quartz scheduler.");
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error("Error occurred while starting scheduler. Error: " + e);
			throw new InternalServerException();
        } finally {
            // release lock
            _LOCK.unlock();
        }
        return new ScheduleResponse(ScheduleResponseCode.ACK);
    }

    //GenericResponse stopScheduler(SchedulerRequest request);
}

