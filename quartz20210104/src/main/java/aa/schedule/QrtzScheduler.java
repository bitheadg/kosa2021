package aa.schedule;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

//@Slf4j
@Configuration
@ConditionalOnExpression("'${switch.spring.schedulerFactory}'=='false'")
public class QrtzScheduler {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        log.info("Quartz PostConstruct");
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        log.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public Scheduler scheduler() throws SchedulerException {
        log.debug("Getting a handle to the StdScheduler");
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        return scheduler;
    }
    /* or you can define in application-context.xml
    <bean id="scheduler"
    class="org.quartz.impl.StdSchedulerFactory"
    factory-method="getDefaultScheduler"/>
    
    @Bean //this works, too, why???
    public Scheduler scheduler2(SchedulerFactoryBean factory) throws SchedulerException {
        log.debug("Getting a handle to the Scheduler");
        Scheduler scheduler = factory.getScheduler();
        return scheduler;
    }
    */

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(springBeanJobFactory());
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }

    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /*
    we don't want job and trigger built when loading spring, ^
    @Bean
    public JobDetail jobDetail() {
        return newJob().ofType(DelegateJob.class).storeDurably().withIdentity(JobKey.jobKey("Qrtz_Job_Detail")).withDescription("Invoke Sample Job service...").build();
    }

    @Bean
	public Trigger trigger(JobDetail job) { //this will be invoked when loading springboot

        //simple repetitive type
    	//int frequencyInSec = 10;
        //log.info("Configuring trigger to fire every {} seconds", frequencyInSec);
        //return newTrigger().forJob(job).withIdentity(TriggerKey.triggerKey("Qrtz_Trigger")).withDescription("Sample trigger").withSchedule(simpleSchedule().withIntervalInSeconds(frequencyInSec).repeatForever()).build();
        //
        
        //cron type; Seconds        Minutes        Hours        Day-of-Month        Month        Day-of-Week        Year (optional field)
        //e.g.)0 0/5 * * * ? 
    	log.info("job key: {}, job desc: {}", job.getKey(), job.getDescription());
    	//09:33:25.288 [restartedMain] INFO  a.s.QrtzScheduler$$EnhancerBySpringCGLIB$$5c90f4b3 - job key: DEFAULT.Qrtz_Job_Detail, job desc: Invoke Sample Job service...
        return newTrigger().forJob(job).withIdentity(TriggerKey.triggerKey("Qrtz_CronTrigger")).withDescription("Sample trigger").withSchedule(cronSchedule("0 0/5 * * * ?")).build();
    }
    we don't want job and trigger built when loading spring, $
	*/
    
}
