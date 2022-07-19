package aa.spring;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import aa.schedule.ScheduleCockpit;
import aa.schedule.ScheduleCode;
import aa.schedule.ScheduleTool;
import aa.schedule.ScheduleVo;
import aa.schedule.Task;
import aa.schedule.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SecuredController {
 
	@Autowired
    private ScheduleCockpit scheduleCockpit;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String write(Model model) {
		model.addAttribute("message", "Test");
	    model.addAttribute("scheduleVo", new ScheduleVo());
	    return "mvc"; //input-form jsp
	}
	@RequestMapping(value = "/schedule/post", method = RequestMethod.POST) //input �깭洹몄?�� name �냽�꽦?�� BoardDTO �씤�뒪�꽩�뒪�쓽 �냽�꽦 �씠?��꾩쓣 ?��꾧탳�빐 �옄�룞�쑝濡� 洹� 媛�?�쓣 諛붿?���뵫
	public String post(@Valid @ModelAttribute("scheduleVo") ScheduleVo scheduleVo, BindingResult bindingResult, ModelMap model) {
	    if (bindingResult.hasErrors()) { // 諛붿?���뵫 �뿉�윭 泥섎?�� ?��붾뱶
	        return "mvc";
	    }
    	
	    //quartz test- cron, ^
    	Task task = new Task(ScheduleTool.generateRandomUUID("J_"));
    	task.setTaskMetadata(scheduleVo.getMemo());
    	
    	/*
    	Calendar date = Calendar.getInstance();
    	long t= date.getTimeInMillis();
    	Date afterOneMin=new Date(t + (60000));
    	ScheduleTool st = new ScheduleTool(afterOneMin);
    	String aSpecificTimeInCronExpression = st.generateCronExpression();
    	*/
    	
    	String aSpecificTimeInCronExpression = scheduleVo.getDatetimeCronExpression();
    	log.info("aSpecificTimeInCronExpression: "+aSpecificTimeInCronExpression);
    	//task.setCronExp("0 0/1 * * * ?");
    	When when = new When(ScheduleCode.CRON);
    	when.setCronExp(aSpecificTimeInCronExpression);
    	when.setWhenId(ScheduleTool.generateRandomUUID("T_"));
    	//ScheduleCockpit sc = new ScheduleCockpitImpl();
    	JobDetail jd = scheduleCockpit.associateNewJobNewTrigger(task, when);
    	//quartz test- cron, $
	    
	    return "redirect:/"; // PRG(Post Redirect Get pattern //to avoid double POST Request by F5
	}
    
    @RequestMapping(value = "/sa_TestAddJobTrigger", method = RequestMethod.GET)
    public void associateNewJobNewTrigger(HttpServletRequest req, HttpServletResponse res){
    	//quartz test- cron, ^
    	Task task = new Task(ScheduleTool.generateRandomUUID("J_"));
    	//task.setTaskId(ScheduleTool.generateRandomUUID("J_"));
    	
    	Calendar date = Calendar.getInstance();
    	long t= date.getTimeInMillis();
    	Date afterOneMin=new Date(t + (60000));
    	
    	ScheduleTool st = new ScheduleTool(afterOneMin);
    	String aSpecificTimeInCronExpression = st.generateCronExpression();
    	log.debug("aSpecificTimeInCronExpression: "+aSpecificTimeInCronExpression);
    	//task.setCronExp("0 0/1 * * * ?");
    	When when = new When(ScheduleCode.CRON);
    	when.setCronExp(aSpecificTimeInCronExpression);
    	when.setWhenId(ScheduleTool.generateRandomUUID("T_"));
    	//ScheduleCockpit sc = new ScheduleCockpitImpl();
    	JobDetail jd = scheduleCockpit.associateNewJobNewTrigger(task, when);
    	//quartz test- cron, $
    	
    	//quartz test- one time, ^
    	Task task1 = new Task(ScheduleTool.generateRandomUUID("J_"));
    	When when1 = new When(ScheduleCode.ONCE);
    	when1.setExecutionTime(LocalDateTime.now());
    	String newTriggerId = ScheduleTool.generateRandomUUID("T_");
    	when1.setWhenId(newTriggerId);
    	jd = scheduleCockpit.associateNewJobNewTrigger(task1, when1);
    	//quartz test- one time, $
    	
    	    	
    }


}