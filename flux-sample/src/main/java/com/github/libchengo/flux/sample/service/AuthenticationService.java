package com.github.libchengo.flux.sample.service;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 陈哈哈 yongjia-chen@outlook.com
 */
public interface AuthenticationService {

    String getAppSecret(Map<String, Serializable> args);
}
