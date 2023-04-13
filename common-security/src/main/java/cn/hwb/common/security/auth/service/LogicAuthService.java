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
        // @RequiredRoles注解中可以设置多个角色，
        // 此处判断是只需要一个角色满足即可，还是所有角色都要满足
        if (requiredRoles.logical() == Logical.AND) {
            // 与操作：必须具有所有权限
            checkRoleAnd(requiredRoles.value());
        } else {
            // 或操作：只需要满足某一个权限
            checkRoleOr(requiredRoles.value());
        }
    }

    /**
     * 验证用户是否含有指定角色，必须全部拥有
     * @param roles 要求
     */
    public void checkRoleAnd(String... roles) {
        List<String> curUserRoles = getCurUserRoles();
        for (String role : roles) {
            if (!hasRole(curUserRoles, role)) {
                throw new NotRoleException(role);
            }
        }
    }

    /**
     * 验证用户是否含有指定角色，只需包含其中一个
     * @param roles 角色标识数组
     */
    public void checkRoleOr(String... roles) {
        List<String> roleList = getCurUserRoles();
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
    public List<String> getCurUserRoles() {
        UserTokenCertificate loginUser = UserSecurityContextHolder.require();
        return loginUser.getRoles();
    }

    /**
     * 判断是否包含角色
     * @param curUserRoles 当前用户拥有的身份
     * @param targetRole 需要验证的身份
     * @return 用户是否具备某角色权限
     */
    public boolean hasRole(Collection<String> curUserRoles, String targetRole) {
        for (String curUserRole : curUserRoles) {
            if (StringUtils.hasText(curUserRole)) {
                // 使用 Spring 提供的 PatternMatchUtils 进行匹配
                if (AuthConstants.ADMIN.equals(curUserRole) ||
                        PatternMatchUtils.simpleMatch(curUserRole, targetRole)) {
                    // 任何一个身份匹配到就返回true
                    return true;
                }
            }
        }
        return false;
    }
}
