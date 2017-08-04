package com.hlv.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import com.hlv.bean.JobInfoBean;
import com.hlv.dao.JobInfoDAO;
import com.hlv.entity.JobInfo;
import com.hlv.job.JobSchedulerHandle;
import com.hlv.service.JobInfoService;

@Service("JobInfoService")
@Transactional(readOnly = true)
public class JobInfoServiceImpl implements JobInfoService {

	@Autowired
	private JobInfoDAO JobInfoDAO;
	
	@Override
	//@Transactional
	public JobInfo findId(Long id) {		
		return JobInfoDAO.find(id);
	}
	
	@Override
	//@Transactional
	public JobInfo getSingleJob() {		
		return JobInfoDAO.getSingleJob();
	}
	
	@Override
	//@Transactional
	public JobInfoBean findJobInfo(JobInfoBean bean) {
		//return JobInfoDAO.findJobInfos(p);
		JobInfo jobinfo = bean.getEntity();
		Search search = new Search(JobInfo.class);
		if (jobinfo != null) {		
			Filter filter1 = Filter.ilike("jobname",  "%" + (jobinfo.getJobname()==null? "" : jobinfo.getJobname()) + "%");			
			search.addFilters(filter1);
		}
		else {			
			search.addFilterILike("jobname", "%%");
		}
		
		search.addSort("id", true);
		search.setMaxResults(bean.getLimit());
		search.setPage(bean.getPage()-1);
		//search.setPage(bean.getPage());
		SearchResult<JobInfo> searchResult=JobInfoDAO.searchAndCount(search);
		
		bean.setListResult(searchResult.getResult());
		//bean.setPage(bean.getPage()-1);
		//bean.setPage(bean.getPage());
		bean.setTotal(searchResult.getTotalCount());
		return bean;
	}
	
	@Override
	@Transactional
	public void addJobInfo(JobInfo p) {
		this.JobInfoDAO.addJobInfo(p);		
	}
	
	@Override
	@Transactional
	public void update(JobInfo p) {
		this.JobInfoDAO.updateJobInfo(p);
	}


	@Override
	@Transactional
	public void updateJobInfo(JobInfo p) {
		this.JobInfoDAO.updateJobInfo(p);
	}

	@Override
	@Transactional
	public void updateList(List<JobInfo> p) {
		this.JobInfoDAO.updateJobSchedules(p);
	}
	
	@Override
	@Transactional
	public List<JobInfo> listJobInfo() {		
		return this.JobInfoDAO.listJobInfo();
	}

	@Override
	@Transactional
	public void removeJobInfo(Long id) {
		//this.JobInfoDAO.removeJobInfo(id);
		this.JobInfoDAO.removeById(id);
	}
	
	@Autowired
    JobSchedulerHandle jobSchedulerHandle;
	
	//business function
	@Override
	@Transactional
	public void transUpdateList(List<JobInfo> p) throws Exception {
		this.JobInfoDAO.updateJobSchedules(p);
		jobSchedulerHandle.execSetOfJobs(p);
	}	
}
