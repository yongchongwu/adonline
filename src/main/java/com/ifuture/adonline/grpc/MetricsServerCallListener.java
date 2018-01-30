package com.ifuture.adonline.grpc;

import com.codahale.metrics.MetricRegistry;
import io.grpc.ForwardingServerCallListener;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;

public class MetricsServerCallListener<R> extends ForwardingServerCallListener<R> {

    private final ServerCall.Listener<R> delegate;
    private final MetricsGrpcMethod grpcMethod;
    private final MetricRegistry metricRegistry;

    public MetricsServerCallListener(ServerCall.Listener<R> delegate, MetricsGrpcMethod grpcMethod,
        MetricRegistry metricRegistry) {
        this.delegate = delegate;
        this.grpcMethod = grpcMethod;
        this.metricRegistry = metricRegistry;
    }

    @Override
    protected Listener<R> delegate() {
        return delegate;
    }

    @Override
    public void onMessage(R message) {
        if (grpcMethod.streamsRequests()) {

        }
        super.onMessage(message);
    }
}
