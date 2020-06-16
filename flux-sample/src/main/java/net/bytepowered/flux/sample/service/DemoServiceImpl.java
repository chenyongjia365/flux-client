package net.bytepowered.flux.sample.service;

import net.bytepowered.flux.extension.FxResponse;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Service(group = "2019", version = "v1.0")
@Slf4j
public class DemoServiceImpl implements DemoService {

    @Override
    public FxResponse hello(String requestId, Integer group, List<Integer> state) {
        log.info("--> Calling !!!!");
        log.info("--> Attachments: {}", RpcContext.getContext().getAttachments());
        log.info("--> String attrLogId,: {}", requestId);
        log.info("--> Group: {}", group);
        log.info("--> State: {}", state);
        return FxResponse.bodyMap()
                .asSuccess()
                .build();
    }

    @Override
    public Object pojo(@NotNull @Min(value = 100) Integer group, @NotNull UserDO user) {
        log.info("--> Calling !!!!");
        log.info("--> Group: {}", group);
        log.info("--> User: {}", user);
        log.info("--> Attachments: {}", RpcContext.getContext().getAttachments());
        user.setId(Long.MAX_VALUE);
        return FxResponse.bodyMap()
                .body(user)
                .asSuccess()
                .build();
    }

    @Override
    public Map<String, Object> helloDynamic(String userId, String queryId) {
        Map<String, Object> out = new HashMap<>(2);
        out.put("userId", userId);
        out.put("queryId", queryId);
        UserDO user = new UserDO();
        user.setAge(19);
        user.setPassword("pass1234");
        user.setUsername("yongjia");
        out.put("user", user);
        out.put("timestamp", Instant.now());
        return out;
    }

    public static void main(String[] args) {
        final HashMap<String, Serializable> body = new HashMap<>(4);
        UserDO user = new UserDO();
        user.setUsername("yoojia");
        user.setPassword("yoojia");
        body.put("logId", "aaaa");
        body.put("group", "g001");
        body.put("user", user);
        FxResponse res = FxResponse.bodyMap()
                .body(body)
                .header("Server", "TestServer")
                .build();
        System.out.println(new GsonBuilder().create().toJson(res));
    }
}
