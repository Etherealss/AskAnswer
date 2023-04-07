package cn.hwb.askanswer.common.base.validation.entity;

import cn.hwb.askanswer.common.base.exception.service.NotFoundException;

/**
 * 检查实体是否存在的接口
 * @author hwb
 */
public interface EntityValidator {
    void validate(Long id) throws NotFoundException;
}