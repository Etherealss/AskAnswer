package cn.hwb.askanswer.user.mapper;

import cn.hwb.askanswer.user.infrastructure.pojo.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<UserEntity> {
}
