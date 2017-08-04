package com.hlv.dao;


import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.hlv.entity.JobInfo;
 
public interface JobInfoDAO extends GenericDAO<JobInfo, Long> {
 
    public void addJobInfo(JobInfo p);
    public void updateJobInfo(JobInfo p);
    public void updateJobSchedules(List<JobInfo> plstJobSchedule);
    public List<JobInfo> listJobInfo();
    public JobInfo getSingleJob();
}