package com.hlv.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import com.hlv.entity.JobInfo;
import com.hlv.service.JobInfoService;

//@Service
public class JobExecution implements Job {

	//@Autowired //can not declare here, so will be null
	//private JobInfoService jobinfoService;
	
	private static final Logger logger = Logger.getLogger(JobExecution.class);
	
	@Override
	public void execute(JobExecutionContext context)
		throws JobExecutionException {
		System.out.println("These Jobs is runing");
		//job send email
		try {
			JobInfo jobInfo = (JobInfo) context.getJobDetail().getJobDataMap().get("JOB_INFO");
			//SchedulerContext schedulerContext = context.getScheduler().getContext();				
			//JobInfoService jobInfoService = (JobInfoService)schedulerContext.get("INFO_JOB_SERVICE");
			
			//call job
			if (jobInfo.getJobname().equals("JOB1")) {
				Job1 job1 = new Job1();
				job1.run();
			}
			else if  (jobInfo.getJobname().equals("JOB2")) {
				Job2 job2 = new Job2();
				job2.run();
			}
			
        } catch (Exception e) {
            logger.error("sendEmail: " + e.getMessage());
        }			
	}

}
