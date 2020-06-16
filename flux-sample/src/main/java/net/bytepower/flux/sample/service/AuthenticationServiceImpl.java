package net.bytepower.flux.sample.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 陈哈哈 chenyongjia365@outlook.com
 */
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public String getAppSecret(Map<String, Serializable> args) {
        final Serializable appKey = args.getOrDefault("appKey", args.toString());
        final String appSecret = "As_000" + appKey;
        log.info("->> 加载App密钥: key={}, secret={}", appKey, appSecret);
        return appSecret;
    }
}
