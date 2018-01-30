package com.ifuture.adonline.service;

import com.ifuture.adonline.quartz.JobType;
import com.ifuture.adonline.quartz.dto.JobDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@SuppressWarnings("unchecked")
public class QuartzJobService {

    private final Logger log = LoggerFactory.getLogger(QuartzJobService.class);

    @Autowired(required = false)
    private Scheduler scheduler;

    /**
     * 启动任务调度器
     */
    public void start() {
        try{
            scheduler.start();
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }
    /**
     * 任务列表
     */
    public List<JobDto> listJobDto() throws SchedulerException {
        List<JobDto> jobDtoList = new ArrayList<JobDto>();
        for (String groupJob : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

                    String jobType = "", cronExpression = "", createTime = "";
                    Long repeatInterval = null;
                    Integer repeatCount = null;

                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        jobType = JobType.CRON;
                        cronExpression = cronTrigger.getCronExpression();
                        createTime = cronTrigger.getDescription();
                    } else if (trigger instanceof SimpleTrigger) {
                        SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
                        jobType = JobType.SIMPLE;
                        repeatInterval = simpleTrigger.getRepeatInterval();
                        repeatCount = simpleTrigger.getRepeatCount();
                        createTime = simpleTrigger.getDescription();
                    }
                    JobDto jobDto = new JobDto();
                    jobDto.setJobType(jobType);
                    jobDto.setJobName(jobKey.getName());
                    jobDto.setJobGroup(jobKey.getGroup());
                    jobDto.setJobDescription(jobDetail.getDescription());
                    jobDto.setJobStatus(triggerState.name());
                    jobDto.setRepeatInterval(repeatInterval);
                    jobDto.setRepeatCount(repeatCount);
                    jobDto.setCronExpression(cronExpression);
                    jobDto.setCreateTime(createTime);
                    jobDto.setPreviousFireTime(trigger.getPreviousFireTime());
                    jobDto.setNextFireTime(trigger.getNextFireTime());
                    jobDtoList.add(jobDto);
                }
            }
        }
        return jobDtoList;
    }

    /**
     * 获得指定任务
     */
    public JobDto getJobDto(String jobName, String jobGroup) throws SchedulerException {
        jobGroup = StringUtils.isEmpty(jobGroup) ? Scheduler.DEFAULT_GROUP : jobGroup;
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (Objects.nonNull(jobDetail)) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            JobDto jobDto = null;
            for (Trigger trigger : triggers) {
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

                String jobType = "", cronExpression = "", createTime = "";
                Long repeatInterval = null;
                Integer repeatCount = null;

                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    jobType = JobType.CRON;
                    cronExpression = cronTrigger.getCronExpression();
                    createTime = cronTrigger.getDescription();
                } else if (trigger instanceof SimpleTrigger) {
                    SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
                    jobType = JobType.SIMPLE;
                    repeatInterval = simpleTrigger.getRepeatInterval();
                    repeatCount = simpleTrigger.getRepeatCount();
                    createTime = simpleTrigger.getDescription();
                }
                jobDto = new JobDto();
                jobDto.setJobType(jobType);
                jobDto.setJobName(jobKey.getName());
                jobDto.setJobGroup(jobKey.getGroup());
                jobDto.setJobDescription(jobDetail.getDescription());
                jobDto.setJobStatus(triggerState.name());
                jobDto.setRepeatInterval(repeatInterval);
                jobDto.setRepeatCount(repeatCount);
                jobDto.setCronExpression(cronExpression);
                jobDto.setCreateTime(createTime);
                jobDto.setPreviousFireTime(trigger.getPreviousFireTime());
                jobDto.setNextFireTime(trigger.getNextFireTime());
            }
            return jobDto;
        }
        return null;
    }

    /**
     * 增加定时任务
     */
    public void createJob(JobDto jobDto) {
        String jobType = jobDto.getJobType(),
            jobName = jobDto.getJobName(),
            jobGroup = jobDto.getJobGroup(),
            cronExpression = jobDto.getCronExpression(),
            jobDescription = jobDto.getJobDescription(),
            createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        Long repeatInterval = jobDto.getRepeatInterval();
        Integer repeatCount = jobDto.getRepeatCount();
        Boolean replace = jobDto.getReplace();
        try {
            jobGroup = StringUtils.isEmpty(jobGroup) ? Scheduler.DEFAULT_GROUP : jobGroup;
            if (checkExists(jobName, jobGroup)) {
                log.info("===> AddJob fail, job already exist, jobGroup:{}, jobName:{}", jobGroup,
                    jobName);
                throw new SchedulerException(
                    String.format("Job已经存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
            }
            //TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            //JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

            ScheduleBuilder schedBuilder = null;

            if (JobType.CRON.equals(jobType)) {
                schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
                    .withMisfireHandlingInstructionDoNothing();
            } else {
                schedBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMilliseconds(repeatInterval).withRepeatCount(repeatCount==null?0:repeatCount);
            }
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .withDescription(createTime).withSchedule(schedBuilder).build();

            Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobName);

            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, jobGroup)
                .withDescription(jobDescription).build();

            scheduler.scheduleJob(jobDetail,trigger);

            //scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改定时任务
     */
    public void updateJob(JobDto jobDto) {
        String jobType = jobDto.getJobType(),
            jobName = jobDto.getJobName(),
            jobGroup = jobDto.getJobGroup(),
            cronExpression = jobDto.getCronExpression(),
            jobDescription = jobDto.getJobDescription(),
            createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        Long repeatInterval = jobDto.getRepeatInterval();
        Integer repeatCount = jobDto.getRepeatCount();
        Boolean replace = jobDto.getReplace();
        try {
            jobGroup = StringUtils.isEmpty(jobGroup) ? Scheduler.DEFAULT_GROUP : jobGroup;
            if (!checkExists(jobName, jobGroup)) {
                throw new SchedulerException(
                    String.format("Job不存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
            }
            //TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = new JobKey(jobName, jobGroup);

            ScheduleBuilder schedBuilder = null;

            if (JobType.CRON.equals(jobType)) {
                schedBuilder = CronScheduleBuilder
                    .cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
            } else {
                schedBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMilliseconds(repeatInterval).withRepeatCount(repeatCount==null?0:repeatCount);

            }
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .withDescription(createTime).withSchedule(schedBuilder).build();
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDetail.getJobBuilder().withDescription(jobDescription);

            HashSet<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(trigger);
            scheduler.scheduleJob(jobDetail, triggerSet, true);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除定时任务
     */
    public void deleteJob(String jobName, String jobGroup) {
        jobGroup = StringUtils.isEmpty(jobGroup) ? Scheduler.DEFAULT_GROUP : jobGroup;
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                log.info("===> delete, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证是否存在
     */
    private boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
        jobGroup = StringUtils.isEmpty(jobGroup) ? Scheduler.DEFAULT_GROUP : jobGroup;
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }

    /**
     * 重新开始任务
     */
    public void resumeJob(String jobName, String jobGroup) {

        jobGroup = StringUtils.isEmpty(jobGroup) ? Scheduler.DEFAULT_GROUP : jobGroup;

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.resumeTrigger(triggerKey);
                log.info("===> Resume success, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停定时任务
     */
    public void pauseJob(String jobName, String jobGroup) {

        jobGroup = StringUtils.isEmpty(jobGroup) ? Scheduler.DEFAULT_GROUP : jobGroup;

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                log.info("===> Pause success, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启用所有任务
     */
    public boolean enableAll() {
        try {
            scheduler.resumeAll();
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 停用所有任务
     */
    public boolean disableAll() {
        try {
            scheduler.pauseAll();
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 立即触发任务
     */
    public boolean triggerNow(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.triggerJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取jobDetail
     */
    public JobDetail getJobDetailByKey(String jobName, String jobGroup) {
        JobDetail jd = null;
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            jd = scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jd;
    }

    /**
     * 获取job trigger
     */
    public List<Trigger> getTriggerByKey(String jobName, String jobGroup) {
        List<Trigger> triggerList = new ArrayList<Trigger>();
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            triggerList = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return triggerList;
    }
}
