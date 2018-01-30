package com.ifuture.adonline.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Component;

@Component
public class HbaseHealthIndicator extends AbstractHealthIndicator {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Override
    protected void doHealthCheck(Builder builder) throws Exception {
        Configuration copyOfConf = HBaseConfiguration.create(hbaseTemplate.getConfiguration());
        builder.withDetail("hbase.zookeeper.quorum",
            copyOfConf.get("hbase.zookeeper.quorum"));
        builder.withDetail("hbase.zookeeper.property.clientPort",
            copyOfConf.get("hbase.zookeeper.property.clientPort"));
        try {
            HBaseAdmin.checkHBaseAvailable(copyOfConf);
            builder.up();
        } catch (Exception e) {
            builder.down(e);
        }
    }
}
