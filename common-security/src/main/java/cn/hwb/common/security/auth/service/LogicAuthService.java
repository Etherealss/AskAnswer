package cn.hwb.common.security.auth.service;

import cn.hwb.common.security.auth.annotation.RequiredRoles;
import cn.hwb.common.security.auth.enums.AuthConstants;
import cn.hwb.common.security.auth.enums.Logical;
import cn.hwb.common.security.auth.exception.NotRoleException;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import cn.hwb.common.security.token.user.UserTokenCertificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * 逻辑校验是否包含必要的权限、角色
 * @author hwb
 */
@Component
@Slf4j
public class LogicAuthService {

    /**
     * 根据注解(@RequiresRoles)鉴权
     * @param requiredRoles 注解对象
     */
    public void checkRole(RequiredRoles requiredRoles) {
        if (requiredRoles.logical() == Logical.AND) {
            checkRoleAnd(requiredRoles.value());
        } else {
            checkRoleOr(requiredRoles.value());
        }
    }

    /**
     * 验证用户是否含有指定角色，必须全部拥有
     * @param roles 角色标识数组
     */
    public void checkRoleAnd(String... roles) {
        List<String> roleList = getRoleList();
        for (String role : roles) {
            if (!hasRole(roleList, role)) {
                throw new NotRoleException(role);
            }
        }
    }

    /**
     * 验证用户是否含有指定角色，只需包含其中一个
     * @param roles 角色标识数组
     */
    public void checkRoleOr(String... roles) {
        List<String> roleList = getRoleList();
        for (String role : roles) {
            if (hasRole(roleList, role)) {
                return;
            }
        }
        if (roles.length > 0) {
            throw new NotRoleException(roles);
        }
    }


    /**
     * 获取当前账号的角色列表
     * @return 角色列表
     */
    public List<String> getRoleList() {
        UserTokenCertificate loginUser = UserSecurityContextHolder.require();
        return loginUser.getRoles();
    }

    /**
     * 判断是否包含角色
     * @param roles 角色列表
     * @param role 角色
     * @return 用户是否具备某角色权限
     */
    public boolean hasRole(Collection<String> roles, String role) {
        return roles.stream()
                .filter(StringUtils::hasText)
                .anyMatch(x -> AuthConstants.SUPER_ADMIN.contains(x) ||
                        PatternMatchUtils.simpleMatch(x, role));
    }
}
