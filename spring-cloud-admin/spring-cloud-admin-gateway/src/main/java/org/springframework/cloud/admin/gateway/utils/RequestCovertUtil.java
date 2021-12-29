package org.springframework.cloud.admin.gateway.utils;

import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author zhy
 * @date 2021/12/29 14:25
 */
@Slf4j
public class RequestCovertUtil {
    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    /**
     * ReadFormData
     *
     * @param exchange
     * @param chain
     * @return
     */
    public static Mono<Void> readFormData(ServerWebExchange exchange, GatewayFilterChain chain,
                                          GatewayContext gatewayContext) {
        final ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        Mono<Void> mono = exchange.getFormData().doOnNext(multiValueMap -> {
            gatewayContext.setFormData(multiValueMap);
        }).then(Mono.defer(() -> {
            Charset charset = headers.getContentType().getCharset();
            charset = charset == null ? StandardCharsets.UTF_8 : charset;
            String charsetName = charset.name();
            MultiValueMap<String, String> formData = gatewayContext.getFormData();
            /**
             * formData is empty just return
             */
            if (null == formData || formData.isEmpty()) {
                return chain.filter(exchange);
            }
            StringBuilder formDataBodyBuilder = new StringBuilder();
            String entryKey;
            List<String> entryValue;
            try {
                /**
                 * repackage form data
                 */
                for (Map.Entry<String, List<String>> entry : formData.entrySet()) {
                    entryKey = entry.getKey();
                    entryValue = entry.getValue();
                    if (entryValue.size() > 1) {
                        for (String value : entryValue) {
                            formDataBodyBuilder.append(entryKey).append("=")
                                    .append(URLEncoder.encode(value, charsetName)).append("&");
                        }
                    } else {
                        formDataBodyBuilder.append(entryKey).append("=")
                                .append(URLEncoder.encode(entryValue.get(0), charsetName)).append("&");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                log.warn(e.getMessage(), e);
            }
            /**
             * substring with the last char '&'
             */
            String formDataBodyString = "";
            if (formDataBodyBuilder.length() > 0) {
                formDataBodyString = formDataBodyBuilder.substring(0, formDataBodyBuilder.length() - 1);
            }

            byte[] bodyBytes = formDataBodyString.getBytes(charset);
            int contentLength = bodyBytes.length;
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(request) {
                @Override
                public HttpHeaders getHeaders() {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(super.getHeaders());
                    if (contentLength > 0) {
                        httpHeaders.setContentLength(contentLength);
                    } else {
                        httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                    }
                    return httpHeaders;
                }

                @Override
                public Flux<DataBuffer> getBody() {
                    return DataBufferUtils.read(new ByteArrayResource(bodyBytes),
                            new NettyDataBufferFactory(ByteBufAllocator.DEFAULT), contentLength);
                }
            };
            ServerWebExchange mutateExchange = exchange.mutate().request(decorator).build();
            return chain.filter(mutateExchange);
        }));
        return mono;
    }

    /**
     * ReadJsonBody
     *
     * @param exchange
     * @param chain
     * @return
     */
    public static Mono<Void> readJsonData(ServerWebExchange exchange, GatewayFilterChain chain, GatewayContext gatewayContext) {
        Mono<Void> mono = DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                DataBufferUtils.retain(buffer);
                return Mono.just(buffer);
            });

            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return cachedFlux;
                }
            };

            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            return ServerRequest.create(mutatedExchange, messageReaders).bodyToMono(String.class)
                    .doOnNext(objectValue -> {
                        gatewayContext.setCacheBody(objectValue);
                    }).then(chain.filter(mutatedExchange));
        });
        return mono;
    }

}
