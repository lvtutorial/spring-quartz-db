package com.hlv.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlv.bean.JobInfoBean;
import com.hlv.bean.Message;
import com.hlv.entity.JobInfo;
import com.hlv.job.JobSchedulerHandle;
import com.hlv.service.JobInfoService;
 
@Controller
@SessionAttributes("jobinfo")
//@RequestMapping("/")
public class JobInfoController {
	
	private static final String get_job_info = "jobinfos";
	private static final String get_job_edit = "jobinfoEdit";
     
	@Autowired
	private JobInfoService jobinfoService;

	@Autowired
    JobSchedulerHandle jobSchedulerHandle;

	@Autowired
	private MessageSource msgSrc;
	
	private static final Logger logger = Logger.getLogger(JobInfoController.class);	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "redirect:/jobinfos";
	}

    @RequestMapping(value = "/jobinfos", method = {RequestMethod.GET, RequestMethod.POST})
    public String searchJobInfo(@ModelAttribute("bean") @Valid JobInfoBean bean, Model model) {  
      //  model.addAttribute("JobInfo", new JobInfoBean());    	
    	bean.setLimit(10);
        model.addAttribute("bean", this.jobinfoService.findJobInfo(bean));        
        return get_job_info;
    }
    
    //new JobInfo
    @RequestMapping(value = "/jobinfos/edit", method = RequestMethod.GET)
    public String editJobInfo(Model model){   
        model.addAttribute("jobinfo", new JobInfoBean());
        return get_job_edit;
    }
    
    @RequestMapping(value = "/jobinfos/edit/{id}", method = RequestMethod.GET)
    public String editJobInfo(@PathVariable("id") Long id, Model model){    	
    	
    	JobInfo job = this.jobinfoService.findId(id);
    	JobInfoBean bean = ConvertEntityToJobInfoBean(job);
        model.addAttribute("jobinfo", bean);
        return get_job_edit;
    }    
       
    @RequestMapping(params = "saveEdit", value= "/jobinfos/edit", method = RequestMethod.POST)
    public String editJobInfo(@ModelAttribute("jobinfo") @Valid JobInfoBean jobinfoBean, BindingResult bindingResult, Model model,
    		 RedirectAttributes redirectAttributes,
    		 @RequestParam(value="lstRightRole", required=false) List<Long> lstRightRole){
    	    
    	jobinfoBean.clearMessages();
    	if (bindingResult.hasErrors()) {
            //add message
    		jobinfoBean.addMessage(Message.ERROR,	bindingResult.getFieldError("entity").getDefaultMessage());
    		model.addAttribute("bean", jobinfoBean);
            return get_job_edit;
        }
    	try {
	    	Date date = new Date();
	    	JobInfo jobinfo = jobinfoBean.getEntity();	    
	    	if (jobinfo.getEnable() && jobinfo.getId() != null) {
	    		jobinfoBean.addMessage(Message.ERROR,	"This job is running, You need to stop this job to edit.");
	    		model.addAttribute("bean", jobinfoBean);
	    		return get_job_edit;// + jobinfo.getId();
	    	}
	    	
	        if(jobinfo.getId() == null){
	            //new JobInfo, add it		        	
	        	this.jobinfoService.addJobInfo(jobinfo); //run here then insert in DB, not like seam         
	        }else{
	        	this.jobinfoService.updateJobInfo(jobinfo);        	          
	        }  
	        jobinfoBean.addMessage(Message.SUCCESS, msgSrc.getMessage("alert.save.success", null, null));
	        model.addAttribute("bean", jobinfoBean);
			redirectAttributes.addFlashAttribute("bean", jobinfoBean);
    	}
    	catch (Exception ex) {
    		//JobInfoBean.addMessage(Message.ERROR,	msgSrc.getMessage("msg.save.fail", null, locale));
    		logger.info("edit JobInfo: " + ex.toString());
    		jobinfoBean.addMessage(Message.ERROR,	"edit JobInfo: " + ex.toString());
			model.addAttribute("bean", jobinfoBean);
			return get_job_edit;
    	}
        return "redirect:/jobinfos";       
    }        
    
    @RequestMapping(params = "runjob", value= "/jobinfos/edit", method = RequestMethod.POST)
    public String runjob(@ModelAttribute("jobinfo") @Valid JobInfoBean jobinfoBean, BindingResult bindingResult, Model model,
    		 RedirectAttributes redirectAttributes, @RequestParam("runjob") String jobname ){
    	
    	jobinfoBean.clearMessages();
    	if (bindingResult.hasErrors()) {
            //add message
    		jobinfoBean.addMessage(Message.ERROR,	bindingResult.getFieldError("entity").getDefaultMessage());
    		model.addAttribute("bean", jobinfoBean);
            return get_job_edit;
        }
    	try {
	    	JobInfo jobinfo = jobinfoBean.getEntity();	    	 
	        if(jobinfo.getId() != null){
	            List<JobInfo> lstJobInfo = new ArrayList<JobInfo>();		
	            jobinfo.setEnable(!jobinfo.getEnable());					
				lstJobInfo.add(jobinfo);
				//need update 2 function to one function to handle transaction --> remove @Transactional in service and set here
				//jobinfoService.updateList(lstJobInfo);
				//jobSchedulerHandle.execSetOfJobs(lstJobInfo);
				jobinfoService.transUpdateList(lstJobInfo);
	       	          
	        }  
	        jobinfoBean.addMessage(Message.SUCCESS, msgSrc.getMessage("alert.job.success", null, null));
	        model.addAttribute("bean", jobinfoBean);
			redirectAttributes.addFlashAttribute("bean", jobinfoBean);
    	}
    	catch (Exception ex) {
    		//JobInfoBean.addMessage(Message.ERROR,	msgSrc.getMessage("msg.save.fail", null, locale));
    		logger.info("Run JobInfo: " + ex.toString());
    		jobinfoBean.addMessage(Message.ERROR,	"Run JobInfo: " + ex.toString());
			model.addAttribute("bean", jobinfoBean);
			return get_job_edit;
    	}
        return "redirect:/jobinfos";       
    }
          
    //start or stop jobs
    //@Transactional
    @RequestMapping(value="/run-job",method = RequestMethod.POST)
    public void startStopJob(@RequestParam(value="oEntity",required=false,defaultValue="") String sObject,
    						HttpServletResponse response) throws IOException{
		    
    	JSONObject _json = new JSONObject();	
    	ObjectMapper mapper = new ObjectMapper();
    	if(sObject!=null) {
			try {
				List<JobInfo> lstJobInfo = new ArrayList<JobInfo>();		
	    		JobInfo job = (JobInfo) mapper.readValue(sObject, JobInfo.class);
				job.setEnable(!job.getEnable());					
				lstJobInfo.add(job);
				//need update 2 function to one function to handle transaction --> remove @Transactional in service and set here
				jobinfoService.updateList(lstJobInfo);
				jobSchedulerHandle.execSetOfJobs(lstJobInfo);
				_json.put("error", "OK");
				_json.put("ojob", job);
			} catch (Exception ex) {
				_json.put("error", ex.toString());
			}				
		}       
		response.getWriter().println(_json.toString());
	}
  	  
    private JobInfoBean ConvertEntityToJobInfoBean(JobInfo job)
    {
    	JobInfoBean bean = new JobInfoBean();
    	bean.setEntity(job);
    	return bean;
    }   
   
}
