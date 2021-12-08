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
      Mono<ResponseEntityVo<UserTo>> aut(@RequestBody AutTo loginTo);
   }
   ```

4. 业务模块的 Controller
   ```java
   @RestController
   @RequestMapping(value = "/api")
   public class ApiController {
      /**
      * AUT = Authentication，认证
      * @param loginTo
      * @return
      */
      @PostMapping(value = "/aut")
      public Mono<ResponseEntityVo<UserTo>> aut(@RequestBody AutTo loginTo) {
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

### 生成公钥文件

#### 1 生成密钥对

```bash
# keytool -genkeypair -alias oauth2-auth-key -keyalg RSA -keypass zhy123 -keystore /usr/local/jks/oauth2.keystore -storepass zhy123
What is your first and last name?
  [Unknown]:  zhy
What is the name of your organizational unit?
  [Unknown]:  zhy
What is the name of your organization?
  [Unknown]:  zhy
What is the name of your City or Locality?
  [Unknown]:  zhy
What is the name of your State or Province?
  [Unknown]:  zhy
What is the two-letter country code for this unit?
  [Unknown]:  cn
Is CN=zhy, OU=zhy, O=zhy, L=zhy, ST, C=cn correct?
  [no]:  y

# ls
oauth2.keystore
```

#### 2 获取公钥

从文件 ```oauth2.keystore``` 中提取公钥。

```bash
# keytool -list -rfc --keystore /usr/local/jks/oauth2.keystore | openssl x509 -inform pem -pubkey
Enter keystore password:  zhy123
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAok+s2/9lSn9CbZUOJtWN
yuhk6TXs8lHPD9sjE3SzR3JUGCetIeoJkpAeFpKRGre3RhPPNZb3q3Yn/qE4JGfz
6VQf6aBOfSs6wXbc8Ge8vhQJY51JgGkWly5iMWvfRop/wcn9sYbzgaUS6/np4qpX
Gt5/NVa6ISPe8C/V6FmfFf8VIUgix0ahWNGTtygH/B8HUOT6ZF90E4EWeP+gyplk
IsjyoiWMowGs9HG9uGBo0c3Z7GOQnvUO03Iobpd2tHKkkMG9JW7bpxC297NiL/I4
6WzMVZpeimJwNz24eWceTCGCySA+8Y9qknwZzaaiXwwXKGbjzxzi9QKZYlW1CP5H
9wIDAQAB
-----END PUBLIC KEY-----
-----BEGIN CERTIFICATE-----
MIIDRzCCAi+gAwIBAgIETkueFTANBgkqhkiG9w0BAQsFADBUMQwwCgYDVQQGEwN6
aHkxDDAKBgNVBAgTA3podDEMMAoGA1UEBxMDemh5MQwwCgYDVQQKEwN6aHkxDDAK
BgNVBAsTA3poeTEMMAoGA1UEAxMDemh5MB4XDTIxMDEwOTA4MzQ0NVoXDTIxMDQw
OTA4MzQ0NVowVDEMMAoGA1UEBhMDemh5MQwwCgYDVQQIEwN6aHQxDDAKBgNVBAcT
A3poeTEMMAoGA1UEChMDemh5MQwwCgYDVQQLEwN6aHkxDDAKBgNVBAMTA3poeTCC
ASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAKJPrNv/ZUp/Qm2VDibVjcro
ZOk17PJRzw/bIxN0s0dyVBgnrSHqCZKQHhaSkRq3t0YTzzWW96t2J/6hOCRn8+lU
H+mgTn0rOsF23PBnvL4UCWOdSYBpFpcuYjFr30aKf8HJ/bGG84GlEuv56eKqVxre
fzVWuiEj3vAv1ehZnxX/FSFIIsdGoVjRk7coB/wfB1Dk+mRfdBOBFnj/oMqZZCLI
8qIljKMBrPRxvbhgaNHN2exjkJ71DtNyKG6XdrRypJDBvSVu26cQtvezYi/yOOls
zFWaXopicDc9uHlnHkwhgskgPvGPapJ8Gc2mol8MFyhm488c4vUCmWJVtQj+R/cC
AwEAAaMhMB8wHQYDVR0OBBYEFBLzQgbLVjR6KE8vzph8803Ml+POMA0GCSqGSIb3
DQEBCwUAA4IBAQAa1uoGMJu5zDCMdJmXVrFO2Ocp+GuY5Xm5gQtP53WRlD0bUCOy
0jStiaDDtY/WW5LGeSrfBNwkBUJNU0Qj7fMYYJ4uC3WCRyjLAEXuYWmL/wLM9q1O
U0H6RaupWwETkcG8wSxCjB4jzeMLo415MuGJHt2mEDDyKp8xpv1Aq5FXAPE7JKbt
E9VWPho7weMr51VZuWB9BCKEYMOxWkHLKTfJGyFc0Y1iJEwV+Umv29G3VMxz39tM
1cNVguo9JsbkUWdnlSliQSdRZsaG1CzifBoE2LLyTEZJbGz0MLOrHTR93DtgCc+L
kShaCyr6NGGcR5N6DNeY+vdevHFBTYqh32CM
-----END CERTIFICATE-----
```

#### 3 保存公钥

把 ```-----BEGIN PUBLIC KEY-----``` 和 ```-----END PUBLIC KEY-----``` 之间的内容保存到文件 ```public.key```

***注意: 文件 ```public.key``` 的首尾不能有空格或者换行符。***

```bash
# touch public.key

# ls
oauth2.keystore  public.key

# vim public.key
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAok+s2/9lSn9CbZUOJtWN
yuhk6TXs8lHPD9sjE3SzR3JUGCetIeoJkpAeFpKRGre3RhPPNZb3q3Yn/qE4JGfz
6VQf6aBOfSs6wXbc8Ge8vhQJY51JgGkWly5iMWvfRop/wcn9sYbzgaUS6/np4qpX
Gt5/NVa6ISPe8C/V6FmfFf8VIUgix0ahWNGTtygH/B8HUOT6ZF90E4EWeP+gyplk
IsjyoiWMowGs9HG9uGBo0c3Z7GOQnvUO03Iobpd2tHKkkMG9JW7bpxC297NiL/I4
6WzMVZpeimJwNz24eWceTCGCySA+8Y9qknwZzaaiXwwXKGbjzxzi9QKZYlW1CP5H
9wIDAQAB

# cat public.key 
MMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAok+s2/9lSn9CbZUOJtWN
yuhk6TXs8lHPD9sjE3SzR3JUGCetIeoJkpAeFpKRGre3RhPPNZb3q3Yn/qE4JGfz
6VQf6aBOfSs6wXbc8Ge8vhQJY51JgGkWly5iMWvfRop/wcn9sYbzgaUS6/np4qpX
Gt5/NVa6ISPe8C/V6FmfFf8VIUgix0ahWNGTtygH/B8HUOT6ZF90E4EWeP+gyplk
IsjyoiWMowGs9HG9uGBo0c3Z7GOQnvUO03Iobpd2tHKkkMG9JW7bpxC297NiL/I4
6WzMVZpeimJwNz24eWceTCGCySA+8Y9qknwZzaaiXwwXKGbjzxzi9QKZYlW1CP5H
9wIDAQAB
```

#### 4 复制公钥文件

把文件 ```public.key``` 复制到项目的 ```resources``` 目录下。
