package cn.hwb.askanswer.common.base.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

/**
 * 带审计字段的基础实体类
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class BaseEntity implements Serializable {
    @TableField(value = CREATOR, fill = FieldFill.INSERT)
    @CreatedBy
    Long creator;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @CreatedDate
    Date createTime;

    @TableField(value = "modify_time", fill = FieldFill.INSERT_UPDATE)
    @LastModifiedDate
    Date modifyTime;

    public static final String CREATOR = "creator";
    public static final String CREATE_TIME = "createTime";
    public static final String MODIFY_TIME = "modifyTime";
}
