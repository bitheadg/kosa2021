package aa.spring;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import aa.schedule.ScheduleCockpit;
import aa.schedule.ScheduleCode;
import aa.schedule.Task;

@Controller
public class BootMVCController {
 
	@Autowired
    private ScheduleCockpit requestService;
	
	@GetMapping("/form")
	public String form() {
		return "mvc"; // this will look a jsp for under spring.mvc.view.prefix if specified
	}
	
    @GetMapping("/read") // is mapped to physical spring.mvc.view.prefix
    public String mvcMethod(HttpServletRequest req, HttpServletResponse res){
    	String cr = "Boot MVC Controller response!";
    	req.setAttribute("message", cr);
    	return "mvc"; // spring.mvc.view.suffix=.jsp under spring.mvc.view.prefix
    }
    
    
    //@GetMapping({"/", "/hello"})
    //@GetMapping(value="/hello")
    @GetMapping({"/hello"})
    public String hello(@RequestParam(value = "message", defaultValue = "World") String name, Model model) {
    	model.addAttribute("message", "MessageRelayTest");
        return "mvc";
    }
    
    
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String formattedDate = dateFormat.format(date);
        model.addAttribute("serverTime", formattedDate );
        return "server";
    }
    
}