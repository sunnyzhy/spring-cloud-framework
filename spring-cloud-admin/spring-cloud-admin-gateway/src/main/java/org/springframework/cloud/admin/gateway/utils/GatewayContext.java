package org.springframework.cloud.admin.gateway.utils;


import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author zhy
 * @date 2021/12/29 14:25
 */
@Data
public class GatewayContext {
    public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";

    private HttpHeaders headers;

    private String cacheBody;

    private MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

    private String ipAddress;

    private String path;
}
