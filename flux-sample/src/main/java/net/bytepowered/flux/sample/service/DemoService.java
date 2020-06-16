package net.bytepowered.flux.sample.service;

import net.bytepowered.flux.extension.FxResponse;
import net.bytepowered.flux.annotation.*;
import net.bytepowered.flux.annotation.*;

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
            @FxForm(value = "group") Integer group,
            List<Integer> state
    );

    @FxMapping(path = "/test/pojo2", method = FxMethod.GET, authorized = false)
    Object pojo(
            @FxParam(value = "group") Integer group,
            @NotNull UserDO user
    );

    @FxMapping(path = "/test/{userId}", method = FxMethod.GET)
    Map<String, Object> helloDynamic(
            @FxPath(value = "userId") String userId,
            @FxParam String queryId);
}
