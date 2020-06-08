package com.github.libchengo.flux.sample.service;

import com.github.libchengo.flux.FxResponse;
import com.github.libchengo.flux.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
public interface DemoService {

    @FxMapping(path = "/test/complex", method = FxMethod.GET, authorized = false)
    FxResponse hello(
            @FxAttr("fluxgo.request_id") String requestId,
            @FxRequest(value = "group") Integer group,
            List<Integer> state
    );

    @FxMapping(path = "/test/pojo2", method = FxMethod.GET, authorized = false)
    Object pojo(
            @FxRequest(value = "group") Integer group,
            @NotNull UserDO user
    );

    @FxMapping(path = "/test/{userId}", method = FxMethod.GET)
    Map<String, Object> helloDynamic(
            @FxPath(value = "userId") String userId,
            @FxRequest String queryId);
}
