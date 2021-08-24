package org.springframework.cloud.admin.uaa.service;

import org.springframework.beans.BeanUtils;
import org.springframework.cloud.admin.common.to.AutTo;
import org.springframework.cloud.admin.common.to.UserTo;
import org.springframework.cloud.admin.common.utils.ResponseMonoUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.uaa.mapper.BaseUserMapper;
import org.springframework.cloud.admin.uaa.model.BaseUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2021/6/24 10:42
 */
@Service
public class ApiService {
    private final BaseUserMapper userMapper;

    public ApiService(BaseUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 登录
     * @param autTo
     * @return
     */
    public Mono<ResponseEntityVo<UserTo>> aut(AutTo autTo) {
        BaseUser one = new BaseUser();
        one.setUserName(autTo.getUserName());
        BaseUser baseUser = userMapper.selectOne(one);
        if (baseUser == null) {
            return ResponseMonoUtil.ok(null);
        }
        UserTo user = new UserTo();
        BeanUtils.copyProperties(baseUser, user);
        return ResponseMonoUtil.ok(user);
    }
}
