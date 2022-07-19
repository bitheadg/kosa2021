package aa;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import aa.schedule.ScheduleCockpit;
import aa.schedule.ScheduleCockpitImpl;
import aa.schedule.ScheduleCode;
import aa.schedule.Task;

@SpringBootApplication
//@ImportResource("application-context.xml")
public class Application extends SpringBootServletInitializer { //to make use of Spring Framework¡¯s Servlet 3.0 support and lets you configure your application when it is launched by the servlet container.

	public static void main(String[] args) {
		//SpringApplication.run(Application.class, args).getBean(ScheduleCockpit.class).startScheduler();
		
		//quartz test with command
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		ScheduleCockpit sc = context.getBean(ScheduleCockpit.class);
		sc.startScheduler();
        
		//quartz test
		/*
		Task task = new Task(ScheduleCode.CRON);
		task.setTaskId("OOOOO");
        task.setCronExp("0 0/1 * * * ?");
        task.setTaskType("AutoWebCheckin");
        sc.associateJobTrigger(task);
        */
		
		//SpringApplication.run(Application.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
			
		};
	}

}