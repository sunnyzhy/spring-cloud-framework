package org.springframework.cloud.admin.gateway.constant;

/**
 * @author zhy
 * @date 2021/6/25 11:31
 */
public enum CLAIM_KEY {
    TOKEN_ID(0, "TOKEN_ID", "租户ID"),
    USER_ID(1, "USER_ID", "用户ID"),
    USER_NAME(2, "USER_NAME", "用户名"),
    TENANT_ID(5, "TENANT_ID", "租户ID");

    private final int value;
    private final String name;
    private final String description;


    CLAIM_KEY(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
