package org.springframework.cloud.admin.common.utils;

import java.util.UUID;

/**
 * @author zhy
 * @date 2021/6/25 11:40
 */
public class UuidUtil {
    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
