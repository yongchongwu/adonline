package com.ifuture.adonline.quartz;

import com.ifuture.adonline.service.QuartzJobService;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动定时任务
 */
@Component
public class QuartzJobRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(QuartzJobRunner.class);

    private static final String JOB_NAME = "com.ifuture.adonline.quartz.jobs.SampleJob";
    private static final String JOB_GROUP = Scheduler.DEFAULT_GROUP;

    @Autowired
    private QuartzJobService quartzJobService;

    @Override
    public void run(String... strings) throws Exception {

        /*JobDto jobDto = new JobDto();
        jobDto.setJobType(JobType.SIMPLE);
        jobDto.setJobName(JOB_NAME);
        jobDto.setJobGroup(JOB_GROUP);
        jobDto.setJobDescription("测试简单任务");
        jobDto.setRepeatInterval(5000L);
        jobDto.setRepeatCount(-1);
        jobDto.setReplace(true);
        quartzJobService.createJob(jobDto);*/
    }
}
