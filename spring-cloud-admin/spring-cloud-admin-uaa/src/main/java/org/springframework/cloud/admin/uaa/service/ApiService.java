package org.springframework.cloud.admin.uaa.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.admin.common.to.LoginTo;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.utils.ResponseUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.uaa.mapper.BaseUserMapper;
import org.springframework.cloud.admin.uaa.model.BaseUser;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @date 2021/6/24 10:42
 */
@Service
@AllArgsConstructor
public class ApiService {
    private final BaseUserMapper userMapper;

    /**
     * 登录
     *
     * @param loginTo
     * @return
     */
    public ResponseEntityVo<UserTo> login(LoginTo loginTo) {
        BaseUser one = new BaseUser();
        one.setUserName(loginTo.getUserName());
        BaseUser baseUser = userMapper.selectOne(one);
        if (baseUser == null) {
            return ResponseUtil.ok(null);
        }
        UserTo user = new UserTo();
        BeanUtils.copyProperties(baseUser, user);
        return ResponseUtil.ok(user);
    }
}
