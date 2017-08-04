package com.hlv.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@Entity
@Table(name = "JOB_INFO")
@SequenceGenerator(name = "jobinfosequence", sequenceName = "JOBINFO_SEQ", allocationSize = 1)
public class JobInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobinfosequence")
	private Long id;

	@Column(name = "JOB_NAME", length = 255, unique = true)	
	private String jobname;

	@Column(name = "JOB_SCHEDULE", length = 100) 
	private String jobschedule; //use cron expression	
	
	@Column(name = "ENABLE", nullable = false, columnDefinition="NUMBER(1) default 0")
	private Boolean enable = false;	
	
	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getJobschedule() {
		return jobschedule;
	}

	public void setJobschedule(String jobschedule) {
		this.jobschedule = jobschedule;
	}
}
