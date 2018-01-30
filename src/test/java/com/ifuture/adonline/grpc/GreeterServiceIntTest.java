package com.ifuture.adonline.grpc;

import com.ifuture.adonline.AdonlineApp;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdonlineApp.class)
public class GreeterServiceIntTest {

    private Server mockServer;
    private GreeterServiceGrpc.GreeterServiceBlockingStub stub;

    @Before
    public void setUp() throws IOException {
        GreeterService greeterGrpcService = new GreeterService();
        String uniqueServerName = "Mock server for " + GreeterService.class;
        mockServer = InProcessServerBuilder
            .forName(uniqueServerName).directExecutor().addService(greeterGrpcService).build()
            .start();
        InProcessChannelBuilder channelBuilder =
            InProcessChannelBuilder.forName(uniqueServerName).directExecutor();
        stub = GreeterServiceGrpc.newBlockingStub(channelBuilder.build());
    }

    @After
    public void tearDown() {
        mockServer.shutdownNow();
    }

    @Test
    public void testSayHello() {
        String msg = stub.sayHello(HelloRequest.newBuilder().setName("Jack").build()).getMessage();
        System.out.println(msg);
    }

}
