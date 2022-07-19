package aa.schedule;

public enum ScheduleCode {
	CRON,
	ONCE,
    JOB_ID("JOB_ID"),
    TRIGGER_ID("TRIGGER_ID"),
    DEFAULT_GROUP_ID("DEFAULT_GROUP_ID"), //The name portion of the key of a job or trigger must be unique within the group - or in other words, the complete key (or identifier) of a job or trigger is the compound of the name and group.
    ADD_JOB("ADD_JOB"),
    DEL_JOB("DEL_JOB"),
    JOB_INFO_JSON("JOB_INFO_JSON"),
    TRIGGER_INFO_JSON("TRIGGER_INFO_JSON"),
    JOB_PREFIX("J_"),
    TRIGGER_PREFIX("T_")
    ;
    
    ScheduleCode() {
		
	}
    
    private String value;

    
    ScheduleCode(String value) {
        this.value = value;
    }

	/* when do we use this???
    public static ScheduleCode forName(String val) {
        for (ScheduleCode schedulerMode : SchedulerMode.values()) {
            if (schedulerMode.value.equalsIgnoreCase(val)) {
                return schedulerMode;
            }
        }
        return null;
    }
    */

    @Override
    public String toString() {
        return this.value;
    }
}