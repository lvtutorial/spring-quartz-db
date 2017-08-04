package com.hlv.job;

import java.text.ParseException;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hlv.entity.JobInfo;
import com.hlv.service.JobInfoService;

@Controller
public class JobListener {

	@Autowired
	private JobSchedulerHandle jobHandle;
	
	@Autowired
	private JobInfoService jobinfoService;
	
	@SuppressWarnings("unchecked")
    public void execute() throws SchedulerException, ParseException {
									
        // Get list of all active jobInfo
		System.out.println("JobListener is start");
		List<JobInfo> lstJobInfo = jobinfoService.listJobInfo();
		jobHandle.execSetOfJobs(lstJobInfo);
    }

}
