package aa.schedule;

import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DelegatedServiceImpl implements DelegatedService {

	//without queue
	@Override
	public Boolean delegatedExecution(Map<String, Object> jobDataMap) {
		log.info("Task {}: {} start at "+new java.util.Date(), jobDataMap.get("METADATA"), jobDataMap.get(ScheduleCode.JOB_ID.toString()));
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("Task {}: {} ended at "+new java.util.Date(), jobDataMap.get("METADATA"), jobDataMap.get(ScheduleCode.JOB_ID.toString()));
		return true;
	}
}
