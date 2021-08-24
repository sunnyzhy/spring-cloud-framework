# spring-cloud-gateway

## FAQ
### openfeign
由于 Spring Cloud Gateway 是基于 Spring 5、Spring Boot 2.X 和 Reactor 开发的响应式组件，运用了大量的异步实现。

而 FeignClient 属于同步调用，所以需要解决以下问题才能正常调用 Feign。

1. No qualifying bean of type 'org.springframework.boot.autoconfigure.http.HttpMessageConverters' available
   - 原因:
      - 在项目启动过程中，并不会创建 HttpMessageConverters 实例。
   - 解决方法:
      - 需要手动创建相应的 Bean 注入到 Spring 容器。
      - 源码: .\org\springframework\boot\spring-boot-autoconfigure\2.5.1\spring-boot-autoconfigure-2.5.1.jar!\org\springframework\boot\autoconfigure\http\HttpMessageConvertersAutoConfiguration.class
        ```java
        @Bean
        @ConditionalOnMissingBean
        public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
            return new HttpMessageConverters((Collection)converters.orderedStream().collect(Collectors.toList()));
        }
        ```

2. block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-x
    - 原因:
        - FeignClient 属于同步调用。
    - 解决方法:
        - 同步转异步，如果需要获取返回结果，可以通过 Future 方式获取。

### reactivefeign

Reactive Feign 属于响应式编码，可结合 webflux 使用。

1. 添加依赖
   ```xml
   <dependency>
      <groupId>com.playtika.reactivefeign</groupId>
      <artifactId>feign-reactor-cloud</artifactId>
   </dependency>
   <dependency>
      <groupId>com.playtika.reactivefeign</groupId>
      <artifactId>feign-reactor-spring-configuration</artifactId>
   </dependency>
   <dependency>
      <groupId>com.playtika.reactivefeign</groupId>
      <artifactId>feign-reactor-webclient</artifactId>
   </dependency>
   ```

2. 在启动类上添加注解
   ```java
   @EnableReactiveFeignClients
   ```

3. 定义 Feign 接口
   ```java
   @ReactiveFeignClient(value = "${feign.remote.application.uaa:spring-cloud-uaa}")
   public interface UaaFeign {
      @PostMapping(value = "/api/aut")
      Mono<ResponseEntityVo<UserTo>> aut(@RequestBody AutTo autTo);
   }
   ```

4. 业务模块的 Controller
   ```java
   @RestController
   @RequestMapping(value = "/api")
   public class ApiController {
      /**
      * AUT = Authentication，认证
      * @param autTo
      * @return
      */
      @PostMapping(value = "/aut")
      public Mono<ResponseEntityVo<UserTo>> aut(@RequestBody AutTo autTo) {
         return null;
      }
   }
   ```

### BCryptPasswordEncoder#strength
源码路径: .\org\springframework\security\spring-security-crypto\5.5.1\spring-security-crypto-5.5.1.jar!\org\springframework\security\crypto\bcrypt\BCryptPasswordEncoder.class

源码:
```java
public BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion version, int strength, SecureRandom random) {
    this.BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");
    this.logger = LogFactory.getLog(this.getClass());
    if (strength == -1 || strength >= 4 && strength <= 31) {
        this.version = version;
        this.strength = strength == -1 ? 10 : strength;
        this.random = random;
    } else {
        throw new IllegalArgumentException("Bad strength");
    }
}
```
注意:

1. strength 的取值范围 [4,31]，默认是 10
2. strength 取值越大，计算越耗时
