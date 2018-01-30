package com.ifuture.adonline.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ifuture.adonline.quartz.dto.JobDto;
import com.ifuture.adonline.service.QuartzJobService;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class QuartzJobResource {

    private final Logger log = LoggerFactory.getLogger(QuartzJobResource.class);

    @Autowired
    private QuartzJobService quartzJobService;

    @GetMapping(value = "/jobs")
    @Timed
    public ResponseEntity ListJobDto() {
        try {
            return new ResponseEntity<>(quartzJobService.listJobDto(), HttpStatus.OK);
        } catch (SchedulerException e) {
            log.error("获取运行 JOB 异常", e);
            return new ResponseEntity<>("获取运行 JOB 异常", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(value = "/jobs/{jobName}/{jobGroup}")
    @Timed
    public ResponseEntity getJobDto(@PathVariable("jobName") String jobName,
        @PathVariable("jobGroup") String jobGroup) {
        try {
            return new ResponseEntity<>(quartzJobService.getJobDto(jobName, jobGroup),
                HttpStatus.OK);
        } catch (SchedulerException e) {
            log.error("获取运行 JOB 异常", e);
            return new ResponseEntity<>("获取运行 JOB 异常", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping(value = "/jobs")
    @Timed
    public ResponseEntity<JobDto> createJob(@Valid @RequestBody JobDto jobDto)
        throws URISyntaxException {
        quartzJobService.createJob(jobDto);
        return ResponseEntity
            .created(new URI("/api/jobs/" + jobDto.getJobName() + "/" + jobDto.getJobGroup()))
            .body(jobDto);
    }

    @PutMapping(value = "/jobs")
    @Timed
    public ResponseEntity<JobDto> updateJob(@Valid @RequestBody JobDto jobDto)
        throws URISyntaxException {
        quartzJobService.updateJob(jobDto);
        return ResponseEntity.ok().body(jobDto);
    }

    @PostMapping(value = "/jobs/resumeJob/{jobName}/{jobGroup}")
    @Timed
    public ResponseEntity resumeJob(@PathVariable("jobName") String jobName,
        @PathVariable("jobGroup") String jobGroup)
        throws URISyntaxException {
        quartzJobService.resumeJob(jobName, jobGroup);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/jobs/pauseJob/{jobName}/{jobGroup}")
    @Timed
    public ResponseEntity pauseJob(@PathVariable("jobName") String jobName,
        @PathVariable("jobGroup") String jobGroup)
        throws URISyntaxException {
        quartzJobService.pauseJob(jobName, jobGroup);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/jobs/{jobName}/{jobGroup}")
    @Timed
    public ResponseEntity<Void> deleteJob(@PathVariable("jobName") String jobName,
        @PathVariable("jobGroup") String jobGroup) throws URISyntaxException {
        quartzJobService.deleteJob(jobName, jobGroup);
        return ResponseEntity.ok().build();
    }

}
