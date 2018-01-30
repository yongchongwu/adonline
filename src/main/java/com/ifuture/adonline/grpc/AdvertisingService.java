package com.ifuture.adonline.grpc;

import com.ifuture.adonline.service.AdsMaterialService;
import fpay.bills.grpc.AdvRequest;
import fpay.bills.grpc.AdvResponse;
import fpay.bills.grpc.AdvertisingServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.List;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@GRpcService(interceptors = {AuthenticationInterceptor.class}, applyGlobalInterceptors = true)
public class AdvertisingService extends AdvertisingServiceGrpc.AdvertisingServiceImplBase {

    private final Logger log = LoggerFactory.getLogger(AdvertisingService.class);

    @Autowired
    private AdsMaterialService adsMaterialService;

    @Override
    public void getAdvertisement(AdvRequest request, StreamObserver<AdvResponse> responseObserver) {
        if (StringUtils.isEmpty(request.getMaid())) {
            responseObserver.onError(Status.INVALID_ARGUMENT.asException());
        } else {
            try {
                List<String> values = adsMaterialService.getAdvertisement(request);
                AdvResponse response = AdvResponse.newBuilder().addAllAdid(values).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } catch (Exception ex) {
                responseObserver.onError(
                    Status.INTERNAL.withCause(ex).withDescription("err").asRuntimeException());
            }
        }
    }

}
