package com.ifuture.adonline.task;

import com.ifuture.adonline.service.AdsMaterialService;
import com.ifuture.adonline.util.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private AdsMaterialService adsMaterialService;

    /**
     * @Scheduled(fixedRate = 5000)表示每隔5000ms，Spring scheduling会调用一次该方法，不论该方法的执行时间是多少
     * @Scheduled(fixedDelay = 5000)表示当方法执行完毕5000ms后，Spring scheduling会再次调用该方法
     * @Scheduled(cron = "0 0 0,8,20 1/1 * ?")定时任务表达式
     */
    public void collectAdsMaterials() {
        log.info("collect start at：" + DateTools.todayStr(DateTools.FORMAT_YMDHMS));
        adsMaterialService.collectAdsMaterials();
    }

}
