package aa.schedule;

import org.quartz.JobDetail;

public interface ScheduleCockpit {

    //AddTaskResponse addTask(Task task);
	JobDetail associateNewJobNewTrigger(Task task, When when);
	
    //GenericResponse deleteTask(String taskId);

	ScheduleResponse startScheduler();

    //GenericResponse stopScheduler(SchedulerRequest request);
}

