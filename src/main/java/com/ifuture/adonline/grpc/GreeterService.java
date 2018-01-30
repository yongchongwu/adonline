package com.ifuture.adonline.grpc;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GRpcService(interceptors = {AuthenticationInterceptor.class}, applyGlobalInterceptors = true)
public class GreeterService extends GreeterServiceGrpc.GreeterServiceImplBase {

    private final Logger log = LoggerFactory.getLogger(GreeterService.class);

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
