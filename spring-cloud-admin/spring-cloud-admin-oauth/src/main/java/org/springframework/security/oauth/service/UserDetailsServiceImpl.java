package org.springframework.security.oauth.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.admin.common.to.LoginTo;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth.feign.UaaFeign;
import org.springframework.stereotype.Component;

/**
 * @author zhouyi
 * @date 2021/1/12 10:57
 */
@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UaaFeign uaaFeign;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        // 根据用户名查询数据库的实体类
        LoginTo loginTo = new LoginTo();
        loginTo.setUserName(username);
        ResponseEntityVo<UserTo> responseEntityVo = null;
        try {
            responseEntityVo = uaaFeign.login(loginTo);
        } catch (Exception ex) {
            return null;
        }
        if (responseEntityVo == null || responseEntityVo.getCode() != HttpStatus.OK.value()) {
            return null;
        }
        UserTo user = responseEntityVo.getData();
        if (user == null) {
            return null;
        }
        // 封装数据库的实体类
        UserDetailsImpl userDetail = new UserDetailsImpl();
        userDetail.setId(user.getId());
        userDetail.setUsername(user.getUserName());
        userDetail.setPassword(user.getPassword());
        userDetail.setTenantId(user.getTenantId());
        return userDetail;
    }
}
