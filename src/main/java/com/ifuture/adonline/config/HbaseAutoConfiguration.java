package com.ifuture.adonline.config;

import com.ifuture.adonline.hbase.HbaseProperties;
import com.ifuture.adonline.hbase.HbaseTemplate;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * JThink@JThink
 *
 * @author JThink
 * @version 0.0.1
 * @desc hbase auto configuration
 * @date 2016-11-16 11:11:27
 */
@Configuration
//@EnableConfigurationProperties(HbaseProperties.class)
//@ConditionalOnClass(HbaseTemplate.class)
@ImportResource("classpath:/config/spring-hbase.xml")
public class HbaseAutoConfiguration {

    //@Autowired
    private HbaseProperties hbaseProperties;

    //@Bean
    //@ConditionalOnMissingBean(HbaseTemplate.class)
    public HbaseTemplate hbaseTemplate() {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", hbaseProperties.getQuorum());
        configuration.set("hbase.zookeeper.property.clientPort", hbaseProperties.getPort());
        return new HbaseTemplate(configuration);
    }
}
