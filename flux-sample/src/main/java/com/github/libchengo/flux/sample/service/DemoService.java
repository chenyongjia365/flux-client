package com.github.libchengo.flux.sample.service;

import com.github.libchengo.flux.FxResponse;
import com.github.libchengo.flux.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author 陈哈哈 (yongjia-chen@outlook.com)
 */
public interface DemoService {

    @FxMapping(path = "/test/complex", method = FxMethod.GET, authorized = false)
    FxResponse hello(
            @NotEmpty @FxAttr("fluxgo.request_id") String requestId,
            @NotNull @Min(value = 100) @FxRequest(value = "group") Integer group,
            @NotNull @NotEmpty List<Integer> state
    );

    @FxMapping(path = "/test/pojo", method = FxMethod.GET, authorized = false)
    Object pojo(
            @NotNull @Min(value = 100) @FxRequest(value = "group") Integer group,
            @NotNull UserDO user
    );

    @FxMapping(path = "/test/{userId}", method = FxMethod.GET)
    Map<String, Object> helloDynamic(@FxPath(value = "userId") String userId,
                                     @FxRequest String queryId);
}
