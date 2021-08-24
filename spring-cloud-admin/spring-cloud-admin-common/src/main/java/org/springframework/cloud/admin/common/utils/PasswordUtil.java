package org.springframework.cloud.admin.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author zhy
 * @date 2021/6/25 17:50
 */
public class PasswordUtil {
    // strength 设置的值越大，计算越耗时
    private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    /**
     * 加密原始密码
     *
     * @param rawPassword
     * @return
     */
    public static String encode(String rawPassword) {
        String password = encoder.encode(rawPassword);
        return password;
    }

    /**
     * 比对原始密码与加密密码
     *
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        boolean value = encoder.matches(rawPassword, encodedPassword);
        return value;
    }
}
