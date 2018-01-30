package com.ifuture.adonline.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;

public class GreeterServiceTest {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel mChannel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext(true)
            .build();
        GreeterServiceGrpc.GreeterServiceBlockingStub stub=GreeterServiceGrpc.newBlockingStub(mChannel);
        HelloRequest request =HelloRequest.newBuilder().setName("Jack").build();
        try {
            HelloReply reply = stub.sayHello(request);
            System.out.println(reply.getMessage());
        }finally {
            //mChannel.shutdownNow();
            mChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
