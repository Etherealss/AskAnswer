package cn.hwb.askanswer.user.infrastructure.pojo.entity;

import cn.hwb.askanswer.common.base.pojo.entity.IdentifiedEntity;
import cn.hwb.askanswer.common.database.repository.List2JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

/**
 * @author wtk
 * @date 2023-03-21
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends IdentifiedEntity {

    @TableField(value = "username")
    String username;

    @TableField(value = "password")
    String password;

    @TableField(value = "avatar")
    String avatar;

    @TableField(value = "birthday")
    Date birthday;

    @TableField(value = "signature")
    String signature;

    @TableField(value = "roles", typeHandler = List2JsonTypeHandler.class)
    List<String> roles;
}