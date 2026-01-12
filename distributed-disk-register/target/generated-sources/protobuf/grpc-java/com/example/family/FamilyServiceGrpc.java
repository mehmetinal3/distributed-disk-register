package com.example.family;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * --- SERVİS TANIMLARI ---
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: family.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FamilyServiceGrpc {

  private FamilyServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.example.family.FamilyService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.family.NodeInfo,
      com.example.family.JoinResponse> getJoinMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Join",
      requestType = com.example.family.NodeInfo.class,
      responseType = com.example.family.JoinResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.family.NodeInfo,
      com.example.family.JoinResponse> getJoinMethod() {
    io.grpc.MethodDescriptor<com.example.family.NodeInfo, com.example.family.JoinResponse> getJoinMethod;
    if ((getJoinMethod = FamilyServiceGrpc.getJoinMethod) == null) {
      synchronized (FamilyServiceGrpc.class) {
        if ((getJoinMethod = FamilyServiceGrpc.getJoinMethod) == null) {
          FamilyServiceGrpc.getJoinMethod = getJoinMethod =
              io.grpc.MethodDescriptor.<com.example.family.NodeInfo, com.example.family.JoinResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Join"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.family.NodeInfo.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.family.JoinResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FamilyServiceMethodDescriptorSupplier("Join"))
              .build();
        }
      }
    }
    return getJoinMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.family.StoreRequest,
      com.example.family.StoreResponse> getStoreMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StoreMessage",
      requestType = com.example.family.StoreRequest.class,
      responseType = com.example.family.StoreResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.family.StoreRequest,
      com.example.family.StoreResponse> getStoreMessageMethod() {
    io.grpc.MethodDescriptor<com.example.family.StoreRequest, com.example.family.StoreResponse> getStoreMessageMethod;
    if ((getStoreMessageMethod = FamilyServiceGrpc.getStoreMessageMethod) == null) {
      synchronized (FamilyServiceGrpc.class) {
        if ((getStoreMessageMethod = FamilyServiceGrpc.getStoreMessageMethod) == null) {
          FamilyServiceGrpc.getStoreMessageMethod = getStoreMessageMethod =
              io.grpc.MethodDescriptor.<com.example.family.StoreRequest, com.example.family.StoreResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StoreMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.family.StoreRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.family.StoreResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FamilyServiceMethodDescriptorSupplier("StoreMessage"))
              .build();
        }
      }
    }
    return getStoreMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.family.GetRequest,
      com.example.family.GetResponse> getGetMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMessage",
      requestType = com.example.family.GetRequest.class,
      responseType = com.example.family.GetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.family.GetRequest,
      com.example.family.GetResponse> getGetMessageMethod() {
    io.grpc.MethodDescriptor<com.example.family.GetRequest, com.example.family.GetResponse> getGetMessageMethod;
    if ((getGetMessageMethod = FamilyServiceGrpc.getGetMessageMethod) == null) {
      synchronized (FamilyServiceGrpc.class) {
        if ((getGetMessageMethod = FamilyServiceGrpc.getGetMessageMethod) == null) {
          FamilyServiceGrpc.getGetMessageMethod = getGetMessageMethod =
              io.grpc.MethodDescriptor.<com.example.family.GetRequest, com.example.family.GetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.family.GetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.family.GetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FamilyServiceMethodDescriptorSupplier("GetMessage"))
              .build();
        }
      }
    }
    return getGetMessageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FamilyServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FamilyServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FamilyServiceStub>() {
        @java.lang.Override
        public FamilyServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FamilyServiceStub(channel, callOptions);
        }
      };
    return FamilyServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FamilyServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FamilyServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FamilyServiceBlockingStub>() {
        @java.lang.Override
        public FamilyServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FamilyServiceBlockingStub(channel, callOptions);
        }
      };
    return FamilyServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FamilyServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FamilyServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FamilyServiceFutureStub>() {
        @java.lang.Override
        public FamilyServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FamilyServiceFutureStub(channel, callOptions);
        }
      };
    return FamilyServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * --- SERVİS TANIMLARI ---
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * 1. Yeni üye sisteme katılırken (Değişmedi)
     * </pre>
     */
    default void join(com.example.family.NodeInfo request,
        io.grpc.stub.StreamObserver<com.example.family.JoinResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getJoinMethod(), responseObserver);
    }

    /**
     * <pre>
     * 2. YENİ: Lider, üyeye "Bunu diske kaydet" der
     * </pre>
     */
    default void storeMessage(com.example.family.StoreRequest request,
        io.grpc.stub.StreamObserver<com.example.family.StoreResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStoreMessageMethod(), responseObserver);
    }

    /**
     * <pre>
     * 3. YENİ: Lider, üyeden "Şu ID'li mesaj sende mi?" diye sorar
     * </pre>
     */
    default void getMessage(com.example.family.GetRequest request,
        io.grpc.stub.StreamObserver<com.example.family.GetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMessageMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service FamilyService.
   * <pre>
   * --- SERVİS TANIMLARI ---
   * </pre>
   */
  public static abstract class FamilyServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FamilyServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service FamilyService.
   * <pre>
   * --- SERVİS TANIMLARI ---
   * </pre>
   */
  public static final class FamilyServiceStub
      extends io.grpc.stub.AbstractAsyncStub<FamilyServiceStub> {
    private FamilyServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FamilyServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FamilyServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1. Yeni üye sisteme katılırken (Değişmedi)
     * </pre>
     */
    public void join(com.example.family.NodeInfo request,
        io.grpc.stub.StreamObserver<com.example.family.JoinResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getJoinMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 2. YENİ: Lider, üyeye "Bunu diske kaydet" der
     * </pre>
     */
    public void storeMessage(com.example.family.StoreRequest request,
        io.grpc.stub.StreamObserver<com.example.family.StoreResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStoreMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 3. YENİ: Lider, üyeden "Şu ID'li mesaj sende mi?" diye sorar
     * </pre>
     */
    public void getMessage(com.example.family.GetRequest request,
        io.grpc.stub.StreamObserver<com.example.family.GetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMessageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service FamilyService.
   * <pre>
   * --- SERVİS TANIMLARI ---
   * </pre>
   */
  public static final class FamilyServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FamilyServiceBlockingStub> {
    private FamilyServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FamilyServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FamilyServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1. Yeni üye sisteme katılırken (Değişmedi)
     * </pre>
     */
    public com.example.family.JoinResponse join(com.example.family.NodeInfo request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getJoinMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 2. YENİ: Lider, üyeye "Bunu diske kaydet" der
     * </pre>
     */
    public com.example.family.StoreResponse storeMessage(com.example.family.StoreRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStoreMessageMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 3. YENİ: Lider, üyeden "Şu ID'li mesaj sende mi?" diye sorar
     * </pre>
     */
    public com.example.family.GetResponse getMessage(com.example.family.GetRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service FamilyService.
   * <pre>
   * --- SERVİS TANIMLARI ---
   * </pre>
   */
  public static final class FamilyServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<FamilyServiceFutureStub> {
    private FamilyServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FamilyServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FamilyServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1. Yeni üye sisteme katılırken (Değişmedi)
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.family.JoinResponse> join(
        com.example.family.NodeInfo request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getJoinMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 2. YENİ: Lider, üyeye "Bunu diske kaydet" der
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.family.StoreResponse> storeMessage(
        com.example.family.StoreRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStoreMessageMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 3. YENİ: Lider, üyeden "Şu ID'li mesaj sende mi?" diye sorar
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.family.GetResponse> getMessage(
        com.example.family.GetRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMessageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_JOIN = 0;
  private static final int METHODID_STORE_MESSAGE = 1;
  private static final int METHODID_GET_MESSAGE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_JOIN:
          serviceImpl.join((com.example.family.NodeInfo) request,
              (io.grpc.stub.StreamObserver<com.example.family.JoinResponse>) responseObserver);
          break;
        case METHODID_STORE_MESSAGE:
          serviceImpl.storeMessage((com.example.family.StoreRequest) request,
              (io.grpc.stub.StreamObserver<com.example.family.StoreResponse>) responseObserver);
          break;
        case METHODID_GET_MESSAGE:
          serviceImpl.getMessage((com.example.family.GetRequest) request,
              (io.grpc.stub.StreamObserver<com.example.family.GetResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getJoinMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.family.NodeInfo,
              com.example.family.JoinResponse>(
                service, METHODID_JOIN)))
        .addMethod(
          getStoreMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.family.StoreRequest,
              com.example.family.StoreResponse>(
                service, METHODID_STORE_MESSAGE)))
        .addMethod(
          getGetMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.family.GetRequest,
              com.example.family.GetResponse>(
                service, METHODID_GET_MESSAGE)))
        .build();
  }

  private static abstract class FamilyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FamilyServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.family.FamilyProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FamilyService");
    }
  }

  private static final class FamilyServiceFileDescriptorSupplier
      extends FamilyServiceBaseDescriptorSupplier {
    FamilyServiceFileDescriptorSupplier() {}
  }

  private static final class FamilyServiceMethodDescriptorSupplier
      extends FamilyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FamilyServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FamilyServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FamilyServiceFileDescriptorSupplier())
              .addMethod(getJoinMethod())
              .addMethod(getStoreMessageMethod())
              .addMethod(getGetMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
