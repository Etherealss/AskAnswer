package cn.hwb.askanswer.common.base.exception.service;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;

/**
 * @author hwb
 */
public class NotCreatorException extends BadRequestException {
    public NotCreatorException() {
        super(ResultCode.NOT_AUTHOR);
    }

    public NotCreatorException(String message) {
        super(ResultCode.NOT_AUTHOR, message);
    }

    public NotCreatorException(Long userId, Long targetId) {
        super(ResultCode.NOT_AUTHOR, "用户'" + userId + "'不是'" + targetId + "'的创建者");
    }
}
