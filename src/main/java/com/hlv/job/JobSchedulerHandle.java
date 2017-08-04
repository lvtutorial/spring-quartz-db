package com.hlv.job;

import java.text.ParseException;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;
import com.hlv.entity.JobInfo;
import com.hlv.service.JobInfoService;


@Service
public class JobSchedulerHandle {

	/*@Autowired
	private JobInfoService jobinfoService;
	*/

	
	private static final String CONST_JOB_GROUP_NAME = "GROUP_JOB";
	
	@SuppressWarnings("unchecked")
    public void execSetOfJobs(List<JobInfo> lstJobInfo) throws SchedulerException, ParseException {
											
		//init scheduler
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        //int i = 1/0;        
        
				
		for(JobInfo _job : lstJobInfo)
	    {
			//get name of job
            //String jobName = _job.getJobname();
			String jobName = _job.getId().toString();
	        
	        //define job key
            JobKey jobKey = new JobKey(jobName, CONST_JOB_GROUP_NAME);
            //define job detail
            JobDetail jobDetail = JobBuilder.newJob(JobExecution.class).withIdentity(jobKey).build();
            jobDetail.getJobDataMap().put("JOB_INFO", _job);
            
            //time
            String conQuartz = generateCronDefault(_job.getJobschedule());
            //test http://www.cronmaker.com/
            //conQuartz = "0 0/3 * 1/1 * ? *"; //every 3 mins
            //String conQuartz = "0 * * * * ? *"; //every minute
            if (conQuartz != null) {           
            	//create trigger
	            Trigger trigger = TriggerBuilder.newTrigger()
	                    .withIdentity(jobName, CONST_JOB_GROUP_NAME)
	                    .withSchedule(CronScheduleBuilder.cronSchedule(conQuartz)
	                           /* SimpleScheduleBuilder.simpleSchedule()
	                            .withIntervalInMinutes(2)*/)
	                    .build();            
	            
	            SchedulerContext schedulerContext = new StdSchedulerFactory().getScheduler().getContext();
	            //set some needed value to context and able to get back in job
	            // put jobInfoDao to context to using in JobExecution
	            //set class use to share           
	            /*if(!schedulerContext.containsKey("INFO_JOB_SERVICE")){
	                schedulerContext.put("INFO_JOB_SERVICE", jobinfoService);
	            }*/
	            /*********end job**********/
	            	           	          
	            //start scheduler
	            if(!scheduler.isStarted()){
	                scheduler.start();
	            }
	            
	            //schedule job trigger
	            if (_job != null) {
	                if (_job.getEnable()) {
	
	                    if (scheduler.getJobDetail(jobKey) == null) {
	                        scheduler.scheduleJob(jobDetail, trigger);
	                    } else {
	                        List<Trigger> lstTrigger = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
	                        Trigger oldTrigger = lstTrigger.get(0);
	                        scheduler.rescheduleJob(oldTrigger.getKey(), trigger);
	                    	//scheduler.rescheduleJob(trigger.getKey(), trigger);
	                    }
	                } else {
	                    scheduler.deleteJob(jobKey);
	                }
	            }
            }
	    }		    	       
    }
	
	private String generateCronDefault(String str) {
		try {
			String[] strArr = str.split(" ");
			String cron = generateCronExpression("0", strArr[0], strArr[1], strArr[2], strArr[3], "?", strArr[4]);
			return cron;
		} catch (Exception ex) {
			return null;
		}

	}
	
	private String generateCronExpression(final String seconds,
			final String minutes, final String hours, final String dayOfMonth,
			final String month, final String dayOfWeek, final String year) {
		return String.format("%1$s %2$s %3$s %4$s %5$s %6$s %7$s", seconds,
				minutes, hours, dayOfMonth, month, dayOfWeek, year);
	}

}
