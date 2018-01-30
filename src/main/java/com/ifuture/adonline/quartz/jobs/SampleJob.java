package com.ifuture.adonline.quartz.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class SampleJob implements Job {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("execute SampleJob：" + dateFormat.format(new Date()));
    }
}
