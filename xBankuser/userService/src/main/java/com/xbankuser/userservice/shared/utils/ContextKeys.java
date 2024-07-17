package com.xbankuser.userservice.shared.utils;

import com.xbankuser.userservice.modules.auth.entiy.User;
import io.grpc.Context;

public class ContextKeys {
    public static final Context.Key<User> user = Context.key("user");
}

//public class ContextKeys {
//    public static final Context.Key<String> user = Context.key("user");
//}