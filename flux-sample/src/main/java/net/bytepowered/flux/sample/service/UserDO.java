package net.bytepowered.flux.sample.service;

import net.bytepowered.flux.annotation.FxForm;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Data
public class UserDO implements Serializable {

    @FxForm
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotNull
    private Integer age;

    private Long id;
}
