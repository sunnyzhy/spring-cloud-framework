package org.springframework.cloud.admin.uaa.service;

import org.springframework.cloud.admin.common.utils.ResponseMonoUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.common.vo.ResponseTableVo;
import org.springframework.cloud.admin.uaa.mapper.BaseUserMapper;
import org.springframework.cloud.admin.uaa.model.BaseUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author zhy
 * @date 2021/7/2 16:02
 */
@Service
public class UserService {
    private final BaseUserMapper userMapper;

    public UserService(BaseUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Mono<ResponseEntityVo<BaseUser>> getUser(Integer id) {
        BaseUser user = userMapper.selectByPrimaryKey(id);
        return ResponseMonoUtil.ok(user);
    }

    public Mono<ResponseEntityVo<ResponseTableVo<BaseUser>>> getAllUser() {
        List<BaseUser> userList = userMapper.selectAll();
        ResponseTableVo<BaseUser> responseTableVo = new ResponseTableVo<>();
        responseTableVo.setRows(userList);
        responseTableVo.setCount(userList.size());
        return ResponseMonoUtil.ok(responseTableVo);
    }

}
