package cn.hwb.askanswer.common.database.repository;

import cn.hwb.askanswer.common.base.pojo.entity.BaseEntity;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import cn.hwb.common.security.token.user.UserTokenCertificate;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author wtk
 * @date 2022-02-03
 */
public class AuditMetaObjectHandler implements MetaObjectHandler {

    public static final Long ADMIN_ID = 0L;

    @Override
    public void insertFill(MetaObject metaObject) {
        boolean createTime = metaObject.hasSetter(BaseEntity.CREATE_TIME);
        boolean modifyTime = metaObject.hasSetter(BaseEntity.MODIFY_TIME);
        if (createTime || modifyTime) {
            Date now = new Date();
            if (createTime) {
                this.setFieldValByName(BaseEntity.CREATE_TIME, now, metaObject);
            }
            if (modifyTime) {
                this.setFieldValByName(BaseEntity.MODIFY_TIME, now, metaObject);
            }
        }
        Long userId = ADMIN_ID;
        UserTokenCertificate tokenCertificate = UserSecurityContextHolder.get();
        if (tokenCertificate != null) {
            userId = tokenCertificate.getUserId();
        }
        if (metaObject.hasSetter(BaseEntity.CREATOR)) {
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
