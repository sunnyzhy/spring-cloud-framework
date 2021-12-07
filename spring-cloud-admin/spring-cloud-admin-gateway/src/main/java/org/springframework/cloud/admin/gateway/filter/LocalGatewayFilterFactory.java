package org.springframework.cloud.admin.gateway.filter;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhy
 * @date 2021/12/2 11:06
 */
@Component
@AllArgsConstructor
public class LocalGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private final GatewayFilterImpl gatewayFilter;

    @Override
    public GatewayFilter apply(Object config) {
        return gatewayFilter;
    }
}
