package com.github.libchengo.flux.sample.service;

import com.github.libchengo.flux.annotation.FxRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Data
public class UserDO implements Serializable {

    @FxRequest
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotNull
    private Integer age;

    private Long id;
}
