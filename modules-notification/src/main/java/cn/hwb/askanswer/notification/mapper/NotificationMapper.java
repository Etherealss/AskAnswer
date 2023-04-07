package cn.hwb.askanswer.notification.mapper;

import cn.hwb.askanswer.notification.infrastructure.pojo.entity.NotificationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author hwb
 */
@Mapper
@Repository
public interface NotificationMapper extends BaseMapper<NotificationEntity> {

}