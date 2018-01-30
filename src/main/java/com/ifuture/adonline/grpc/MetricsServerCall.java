package com.ifuture.adonline.grpc;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsServerCall<R, S> extends ForwardingServerCall.SimpleForwardingServerCall<R, S> {

    private final Logger log = LoggerFactory.getLogger(MetricsServerCall.class);

    private final MetricsGrpcMethod grpcMethod;
    private final MetricRegistry metricRegistry;
    private final Timer.Context context;

    public MetricsServerCall(ServerCall<R, S> delegate, MetricsGrpcMethod grpcMethod,
        MetricRegistry metricRegistry) {
        super(delegate);
        this.grpcMethod = grpcMethod;
        this.metricRegistry = metricRegistry;
        final Timer timer = metricRegistry.timer(
            MetricRegistry.name(grpcMethod.serviceName(), grpcMethod.methodName(), "[grpc]"));
        this.context = timer.time();
        reportStartMetrics();
    }

    @Override
    public void close(Status status, Metadata trailers) {
        reportEndMetrics(status);
        super.close(status, trailers);
    }

    @Override
    public void sendMessage(S message) {
        if (grpcMethod.streamsResponses()) {

        }
        super.sendMessage(message);
    }

    private void reportStartMetrics() {
        log.debug("reportStartMetrics...");
    }

    private void reportEndMetrics(Status status) {
        if (null != this.context) {
            context.stop();
        }
        log.debug("reportEndMetrics...");
    }
}
