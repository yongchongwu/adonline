package com.ifuture.adonline.grpc;

import com.codahale.metrics.MetricRegistry;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
@GRpcGlobalInterceptor
public class MetricsGrpcInterceptor implements ServerInterceptor {

    private final Logger log = LoggerFactory.getLogger(MetricsGrpcInterceptor.class);

    @Autowired
    private MetricRegistry metricRegistry;

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
        Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

        log.info("MetricsGrpcInterceptor:" + serverCall.getMethodDescriptor().getFullMethodName());

        MethodDescriptor<ReqT, RespT> method = serverCall.getMethodDescriptor();

        ServerCall<ReqT, RespT> metricsServerCall = new MetricsServerCall(serverCall,
            MetricsGrpcMethod.of(method), metricRegistry);

        return new MetricsServerCallListener<>(
            serverCallHandler.startCall(metricsServerCall, metadata), MetricsGrpcMethod.of(method),
            metricRegistry);
    }
}
