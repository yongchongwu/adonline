package com.ifuture.adonline.grpc;

import io.grpc.MethodDescriptor;
import io.grpc.MethodDescriptor.MethodType;

class MetricsGrpcMethod {

    private final String serviceName;
    private final String methodName;
    private final MethodType type;

    static MetricsGrpcMethod of(MethodDescriptor<?, ?> method) {
        String serviceName = MethodDescriptor.extractFullServiceName(method.getFullMethodName());
        String methodName = method.getFullMethodName().substring(serviceName.length() + 1);
        return new MetricsGrpcMethod(serviceName, methodName, method.getType());
    }

    private MetricsGrpcMethod(String serviceName, String methodName, MethodType type) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.type = type;
    }

    String serviceName() {
        return serviceName;
    }

    String methodName() {
        return methodName;
    }

    String type() {
        return type.toString();
    }

    boolean streamsRequests() {
        return type == MethodType.CLIENT_STREAMING || type == MethodType.BIDI_STREAMING;
    }

    boolean streamsResponses() {
        return type == MethodType.SERVER_STREAMING || type == MethodType.BIDI_STREAMING;
    }
}
