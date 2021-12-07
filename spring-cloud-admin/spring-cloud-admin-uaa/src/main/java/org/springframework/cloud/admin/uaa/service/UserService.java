package org.springframework.cloud.admin.uaa.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.admin.common.utils.ResponseMonoUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.cloud.admin.common.vo.ResponseTableVo;
import org.springframework.cloud.admin.uaa.mapper.BaseUserMapper;
import org.springframework.cloud.admin.uaa.model.BaseUser;
import org.springframework.cloud.admin.uaa.vo.UserVo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhy
 * @date 2021/7/2 16:02
 */
@Service
@AllArgsConstructor
public class UserService {
    private final BaseUserMapper userMapper;

    public Mono<ResponseEntityVo<UserVo>> getUser(Integer id) {
        BaseUser user = userMapper.selectByPrimaryKey(id);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return ResponseMonoUtil.ok(userVo);
    }

    public Mono<ResponseEntityVo<ResponseTableVo<UserVo>>> getAllUser() {
        List<BaseUser> userList = userMapper.selectAll();
        List<UserVo> list = new ArrayList<>();
        userList.forEach(x -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(x, userVo);
            list.add(userVo);
        });
        ResponseTableVo<UserVo> responseTableVo = new ResponseTableVo<>();
        responseTableVo.setRows(list);
        responseTableVo.setCount(list.size());
        return ResponseMonoUtil.ok(responseTableVo);
    }

}
