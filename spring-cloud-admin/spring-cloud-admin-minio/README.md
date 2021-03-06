# spring-cloud-admin-minio

## minio 分布式部署

[minio下载](https://min.io/download 'minio下载')

### 1 准备 4 台服务器

|节点|应用目录|文件存储目录|脚本目录|日志存储目录|
|---|---|---|---|---|
|192.168.0.100|/usr/local/minio/bin|/usr/local/minio/upload|/usr/local/minio/run|/usr/local/minio/log|
|192.168.0.101|/usr/local/minio/bin|/usr/local/minio/upload|/usr/local/minio/run|/usr/local/minio/log|
|192.168.0.102|/usr/local/minio/bin|/usr/local/minio/upload|/usr/local/minio/run|/usr/local/minio/log|
|192.168.0.103|/usr/local/minio/bin|/usr/local/minio/upload|/usr/local/minio/run|/usr/local/minio/log|

***注: 在服务器数量少于 4 台的情况下，可以做伪集群部署，此时只需要修改端口号即可:***

```
192.168.0.100:9000
192.168.0.100:9001
192.168.0.100:9002
192.168.0.100:9003
```

### 2 创建目录

4 台服务器都执行以下命令:

```bash
# mkdir -p /usr/local/minio/{bin,upload,run,log}
```

### 3 下载 minio

把下载的 minio 分别存放到 4 台服务器的 ```/usr/local/minio/bin``` 目录。

### 4 创建启动脚本

4 台服务器都执行以下命令:

```bash
# vim /usr/local/minio/run/minio-run.sh
```

### 5 编辑启动脚本

#### 5.1 节点 192.168.0.100 的脚本内容

```bash
#!/bin/bash
export MINIO_ROOT_USER=admin
export MINIO_ROOT_PASSWORD=password
/usr/local/minio/bin/minio server --address "192.168.0.100:9000" --console-address ":7000" \
http://192.168.0.100:9000/usr/local/minio/upload \
http://192.168.0.101:9000/usr/local/minio/upload \
http://192.168.0.102:9000/usr/local/minio/upload \
http://192.168.0.103:9000/usr/local/minio/upload \
> /usr/local/minio/log/run.log
```

#### 5.2 节点 192.168.0.101 的脚本内容

```bash
#!/bin/bash
export MINIO_ROOT_USER=admin
export MINIO_ROOT_PASSWORD=password
/usr/local/minio/bin/minio server --address "192.168.0.101:9000" --console-address ":7000" \
http://192.168.0.100:9000/usr/local/minio/upload \
http://192.168.0.101:9000/usr/local/minio/upload \
http://192.168.0.102:9000/usr/local/minio/upload \
http://192.168.0.103:9000/usr/local/minio/upload \
> /usr/local/minio/log/run.log
```

#### 5.3 节点 192.168.0.102 的脚本内容

```bash
#!/bin/bash
export MINIO_ROOT_USER=admin
export MINIO_ROOT_PASSWORD=password
/usr/local/minio/bin/minio server --address "192.168.0.102:9000" --console-address ":7000" \
http://192.168.0.100:9000/usr/local/minio/upload \
http://192.168.0.101:9000/usr/local/minio/upload \
http://192.168.0.102:9000/usr/local/minio/upload \
http://192.168.0.103:9000/usr/local/minio/upload \
> /usr/local/minio/log/run.log
```

#### 5.4 节点 192.168.0.103 的脚本内容

```bash
#!/bin/bash
export MINIO_ROOT_USER=admin
export MINIO_ROOT_PASSWORD=password
/usr/local/minio/bin/minio server --address "192.168.0.103:9000" --console-address ":7000" \
http://192.168.0.100:9000/usr/local/minio/upload \
http://192.168.0.101:9000/usr/local/minio/upload \
http://192.168.0.102:9000/usr/local/minio/upload \
http://192.168.0.103:9000/usr/local/minio/upload \
> /usr/local/minio/log/run.log
```

### 6 授权 minio 目录

4 台服务器都执行以下命令:

```bash
# chmod +x -R /usr/local/minio
```

### 7 将 minio 加入系统服务

4 台服务器都执行以下命令:

```bash
# vim /usr/lib/systemd/system/minio.service
[Unit]
Description=Minio service
Documentation=https://docs.minio.io/

[Service]
WorkingDirectory=/usr/local/minio/run/
ExecStart=/usr/local/minio/run/minio-run.sh

Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target

# chmod +x /usr/lib/systemd/system/minio.service
```

### 8 启动 minio 服务

4 台服务器都执行以下命令:

```bash
# systemctl daemon-reload

# systemctl start minio
```

### 9 使用 nginx 负载均衡

在日志里增加 ```"$upstream_status" "$upstream_addr"``` 以便打印真实的服务端 IP 地址:

```xml
http {
    include       mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"'
					  '"$upstream_status" "$upstream_addr"';
}
```

#### 9.1 后台调用 endpoint

在 nginx.conf 文件里增加如下配置:

```conf
   upstream lb_minio {
        server 192.168.0.100:9000;
        server 192.168.0.101:9000;
        server 192.168.0.102:9000;
        server 192.168.0.103:9000;
    }

    server {
        listen 9000;
        server_name localhost;
		access_log  logs/minio.access.log  main;
		error_log  logs/minio.error.log;
		
        location / {
            proxy_set_header   X-Real-IP $remote_addr;
            proxy_set_header   X-Forwarded-Host  $host:$server_port;
            proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header   X-Forwarded-Proto  $http_x_forwarded_proto;
            proxy_set_header   Host $http_host;
   
            proxy_pass http://lb_minio;
        }
    } 
```

此时，minio 对外的 ENDPOINT 为: ```http://${NGINX_HOST}:9000```

#### 9.2 前端访问 console

在 nginx.conf 文件里增加如下配置:

```conf
    upstream lb_minio_console {
		server 192.168.0.100:7000;
		server 192.168.0.101:7000;
		server 192.168.0.102:7000;
		server 192.168.0.103:7000;
    }

    server {
        listen 7000;
        server_name localhost;
		access_log  logs/minio.access.log  main;
		error_log  logs/minio.error.log;
		
        location / {
            proxy_pass http://lb_minio_console;
        }
    }
```

此时，在浏览器的地址栏输入 ```http://${NGINX_HOST}:7000``` 即可访问 minio 的 Console 页面。

### FAQ

#### minio 服务启动失败

- 查看服务日志:

   ```bash
   # journalctl -u minio
   ```

- ERROR Unable to initialize backend: format.json file: expected format-type: fs, found: xl

   删除文件存储目录里的 ```.minio.sys```文件:
  
   ```bash
  # ls -la
  total 0
  drwxr-xr-x. 3 root root  24 Jan 12 11:53 .
  drwxr-xr-x. 6 root root  53 Jan 12 11:08 ..
  drwxr-xr-x. 9 root root 125 Jan 12 14:14 .minio.sys
  
  # rm -rf .minio.sys
   ```

## minio 开发需注意的事项

```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.3.4</version>
</dependency>
```

### 升级 okhttp3

okhttp3 的版本不能低于 4.8.1，升级的时候，只需要在父模块的 ```properties``` 里配置 okhttp3 版本即可。

```xml
    <properties>
        <okhttp3.version>4.9.3</okhttp3.version>
    </properties>
```

源码: ```.\io\minio\minio\8.3.4\minio-8.3.4.jar!\io\minio\S3Base.class```

```java
  static {
    try {
      RequestBody.create(new byte[0], (MediaType)null);
    } catch (NoSuchMethodError var1) {
      throw new RuntimeException("Unsupported OkHttp library found. Must use okhttp >= 4.8.1", var1);
    }

    DEFAULT_CONNECTION_TIMEOUT = TimeUnit.MINUTES.toMillis(5L);
    TRACE_QUERY_PARAMS = ImmutableSet.of("retention", "legal-hold", "tagging", "uploadId");
  }
```

### 命名 bucket 的名称

bucket 的名称必须满足正则表达式: ```^[a-z0-9][a-z0-9\\.\\-]+[a-z0-9]$```

源码: ```.\io\minio\minio\8.3.4\minio-8.3.4.jar!\io\minio\BucketArgs.class```

```java
  protected void validateBucketName(String name) {
    this.validateNotNull(name, "bucket name");
    if (name.length() >= 3 && name.length() <= 63) {
      String msg;
      if (name.contains("..")) {
        msg = "bucket name cannot contain successive periods. For more information refer http://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html";
        throw new IllegalArgumentException(name + " : " + msg);
      } else if (!name.matches("^[a-z0-9][a-z0-9\\.\\-]+[a-z0-9]$")) {
        msg = "bucket name does not follow Amazon S3 standards. For more information refer http://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html";
        throw new IllegalArgumentException(name + " : " + msg);
      }
    } else {
      throw new IllegalArgumentException(name + " : bucket name must be at least 3 and no more than 63 characters long");
    }
  }
```

正确的命名:

```java
.bucket("example.minio")
.bucket("example-minio")
.bucket("example.minio-bucket")
.bucket("example-minio.bucket")
```

### 命名 object 的名称

object 方法会根据层级自动创建目录。

```java
.object("example.jpg")
.object("/example.jpg")
.object("level1/level2/example.jpg")
.object("/level1/level2/example.jpg")
```

### 指定 ContentType

上传文件的时候，尽量指定 ContentType，如果不指定的话，ContentType 默认为: ```application/octet-stream``` 

源码: ```.\io\minio\minio\8.3.4\minio-8.3.4.jar!\io\minio\PutObjectArgs.class```

```java
    public String contentType() throws IOException {
        String contentType = super.contentType();
        return contentType != null ? contentType : "application/octet-stream";
    }
```

- FilePart(webflux)

   ```java
   List<String> contentTypeList = file.headers().get("Content-Type");
   .contentType((contentTypeList == null || contentTypeList.isEmpty()) ? null : contentTypeList.get(0))
   ```

- MultipartFile(web)

   ```java
   .contentType(file.getContentType())
   ```

### nginx 传输文件的限制

nginx 默认文件传输的大小是 1M，传输超过 1M 的文件会报异常: ```413 Request Entity Too Large```，此时可以在 ```http{ }``` 或 ```server{ }``` 或 ```location{ }``` 中添加以下配置:

```conf
	client_body_buffer_size 10m;
	client_max_body_size 10m;
```

- http{ }: 作用于全局 nginx
- server{ }: 作用于当前 server
- location{ }: 作用于当前路由
