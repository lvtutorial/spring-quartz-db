package com.hlv.service;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;

import com.hlv.bean.JobInfoBean;
import com.hlv.entity.JobInfo;

 
public interface JobInfoService {
 
    public JobInfo findId(Long id);
    public JobInfo getSingleJob();
    public void addJobInfo(JobInfo p);
    public void updateJobInfo(JobInfo p);
    public void update(JobInfo p);
    public void updateList(List<JobInfo> p);
    public JobInfoBean findJobInfo(JobInfoBean bean);
    public List<JobInfo> listJobInfo();
    public void removeJobInfo(Long id);
    public void transUpdateList(List<JobInfo> p) throws Exception;
}