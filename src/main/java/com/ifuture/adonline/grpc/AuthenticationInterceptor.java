package com.ifuture.adonline.grpc;


import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationInterceptor implements ServerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private final Metadata.Key<String> token = Metadata.Key
        .of("access_token", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
        Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        logger.info(
            "AuthenticationInterceptor:" + serverCall.getMethodDescriptor().getFullMethodName());
        logger.info("header received from client:" + metadata);
        //return serverCallHandler.startCall(serverCall, metadata);
        return serverCallHandler.startCall(new SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
            @Override
            public void sendHeaders(Metadata responseHeaders) {
                responseHeaders.put(token, "A2D05E5ED2414B1F8C6AEB19F40EF77C");
                super.sendHeaders(responseHeaders);
            }
        }, metadata);
    }
}
