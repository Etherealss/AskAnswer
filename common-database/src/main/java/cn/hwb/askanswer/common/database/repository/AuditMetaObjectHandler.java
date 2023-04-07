package cn.hwb.askanswer.common.database.repository;

import cn.hwb.askanswer.common.base.pojo.entity.BaseEntity;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import cn.hwb.common.security.token.user.UserTokenCertificate;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 自动填充审计字段
 * @author hwb
 */
public class AuditMetaObjectHandler implements MetaObjectHandler {

    public static final Long ADMIN_ID = 0L;

    @Override
    public void insertFill(MetaObject metaObject) {
        // 判断是否有createTime和modifyTime的setter方法
        boolean createTime = metaObject.hasSetter(BaseEntity.CREATE_TIME);
        boolean modifyTime = metaObject.hasSetter(BaseEntity.MODIFY_TIME);
        if (createTime || modifyTime) {
            Date now = new Date();
            if (createTime) {
                // 存在 createTime 的setter方法，添加创建时间字段
                this.setFieldValByName(BaseEntity.CREATE_TIME, now, metaObject);
            }
            if (modifyTime) {
                this.setFieldValByName(BaseEntity.MODIFY_TIME, now, metaObject);
            }
        }
        UserTokenCertificate tokenCertificate = UserSecurityContextHolder.get();
        // token存在，则使用当前用户的ID，否则使用默认adminID
        Long userId = ADMIN_ID;
        if (tokenCertificate != null) {
            userId = tokenCertificate.getUserId();
        }
        // 判断是否有 creator 的setter方法
        if (metaObject.hasSetter(BaseEntity.CREATOR)) {
            // 自动添加 creator 字段
            this.setFieldValByName(BaseEntity.CREATOR, userId, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(BaseEntity.MODIFY_TIME)) {
            this.setFieldValByName(BaseEntity.MODIFY_TIME, new Date(), metaObject);
        }
    }
}
