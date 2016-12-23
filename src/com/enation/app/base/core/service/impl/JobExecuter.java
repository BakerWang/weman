package com.enation.app.base.core.service.impl;

import javax.annotation.Resource;

import com.enation.app.api.service.SendMessageService;
import com.enation.app.base.core.plugin.job.JobExecutePluginsBundle;
import com.enation.app.base.core.service.IJobExecuter;

/**
 * 任务执行器
 * @author kingapex
 *
 */ 
public class JobExecuter implements IJobExecuter {
	private JobExecutePluginsBundle jobExecutePluginsBundle;

	@Resource
	private SendMessageService sendMessageService;
	
	@Override
	public void everyHour() {
		try {
			System.out.println("testet");
			sendMessageService.saveAndSendMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		jobExecutePluginsBundle.everyHourExcecute();
	}

	@Override 
	public void everyDay() {
		try{
			//this.jobExecutePluginsBundle.everyDayExcecute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void everyMonth() {
		jobExecutePluginsBundle.everyMonthExcecute();
	}

	public JobExecutePluginsBundle getJobExecutePluginsBundle() {
		return jobExecutePluginsBundle;
	}

	public void setJobExecutePluginsBundle(
			JobExecutePluginsBundle jobExecutePluginsBundle) {
		this.jobExecutePluginsBundle = jobExecutePluginsBundle;
	}
	

}
