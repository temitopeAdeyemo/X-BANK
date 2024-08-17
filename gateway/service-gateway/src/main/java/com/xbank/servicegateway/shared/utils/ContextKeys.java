package com.xbank.servicegateway.shared.utils;

import io.grpc.Context;
import proto.getUser.proto.User;

public class ContextKeys {
    public static final Context.Key<String > token = Context.key("token");

    public static final Context.Key<User> user = Context.key("user");
}