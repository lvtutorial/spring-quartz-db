package com.hlv.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.hlv.dao.JobInfoDAO;
import com.hlv.entity.JobInfo;

@Repository
public class JobInfoDAOImpl extends GenericDAOImpl<JobInfo, Long> implements JobInfoDAO {

	@Override
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
		
	@Override
	public void addJobInfo(JobInfo p) {
		this._persist(p);
	}

	@Override
	public void updateJobInfo(JobInfo p) {
		this._update(p);
	}
	
	@Override
	public void updateJobSchedules(List<JobInfo> plstJobSchedule) {
        if (plstJobSchedule != null && plstJobSchedule.size() > 0) {
            for (JobInfo entity: plstJobSchedule) {
                this._update(entity);
            }
        }
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<JobInfo> listJobInfo() {
		return findAll();
	}	
	
	@Override
	public JobInfo getSingleJob() {
		return findAll().get(0);
	}	
}
