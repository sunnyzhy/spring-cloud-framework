package org.springframework.cloud.admin.gateway.constant;

/**
 * @author zhy
 * @date 2021/6/25 11:31
 */
public enum CLAIM_KEY {
    TOKEN_ID(0, "token_id", "租户ID"),
    USER_ID(1, "user_id", "用户ID"),
    USER_NAME(2, "user_name", "用户名"),
    TENANT_ID(5, "tenant_id", "租户ID");

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
