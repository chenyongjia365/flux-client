package net.bytepowered.flux.sample.service;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 陈哈哈 chenyongjia365@outlook.com
 */
public interface AuthenticationService {

    String getAppSecret(Map<String, Serializable> args);
}
