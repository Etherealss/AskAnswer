package cn.hwb.user.service.user;

import cn.hwb.common.base.pojo.entity.IdentifiedEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class SysUser extends IdentifiedEntity {

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
}