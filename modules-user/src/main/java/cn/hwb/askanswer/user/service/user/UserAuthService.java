package cn.hwb.askanswer.user.service.user;

import cn.hwb.askanswer.common.base.crypt.PasswordEncryptor;
import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.user.infrastructure.pojo.entity.UserEntity;
import cn.hwb.askanswer.user.infrastructure.pojo.request.UserLoginRequest;
import cn.hwb.askanswer.user.mapper.UserMapper;
import cn.hwb.common.security.token.user.UserTokenCertificate;
import cn.hwb.common.security.token.user.UserTokenService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthService extends ServiceImpl<UserMapper, UserEntity> {
    private final PasswordEncryptor passwordEncryptor;
    private final UserTokenService tokenService;

    public UserTokenCertificate login(UserLoginRequest request) {
        QueryWrapper<UserEntity> query = new QueryWrapper<>();
        query.eq("username", request.getUsername());
        UserEntity user = this.baseMapper.selectOne(query);
        if (user == null) {
            throw new NotFoundException("用户不存在");
        }
        if (!passwordEncryptor.match(request.getPassword(), user.getPassword())) {
            throw new BadRequestException(ResultCode.PASSWORD_NOT_MATCH);
        }
        UserTokenCertificate tokenCertificate = new UserTokenCertificate(
                user.getId(),
                user.getUsername(),
                user.getBirthday()
        );
        tokenService.completeTokenAndSave(tokenCertificate);
        return tokenCertificate;
    }

    public boolean verifyToken(String token) {
        UserTokenCertificate tokenCertificate = tokenService.getToken(token);
        return tokenCertificate != null;
    }

    public void invalidateToken(String token) {
        tokenService.invalidateToken(token);
    }
}
