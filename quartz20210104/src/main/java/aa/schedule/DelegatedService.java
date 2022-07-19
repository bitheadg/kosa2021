package aa.schedule;

import java.util.Map;

public interface DelegatedService {
    //Boolean addTaskToExecutionQueue(Map<String, Object> jobDataMap);
	
	Boolean delegatedExecution(Map<String, Object> jobDataMap);
}
