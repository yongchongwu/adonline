package com.ifuture.adonline.grpc;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@GRpcGlobalInterceptor
@Order(10)
public class LoggingInterceptor implements ServerInterceptor {

    private final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
        Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        log.info("LoggingInterceptor:"+serverCall.getMethodDescriptor().getFullMethodName());

        return serverCallHandler.startCall(serverCall, metadata);
    }
}
