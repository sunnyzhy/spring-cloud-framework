# spring-cloud-admin-minio

## MinIO 分布式部署

[minio下载](https://min.io/download 'minio下载')

### 1 准备 4 台服务器

|节点|应用目录|文件存储目录|脚本目录|日志存储目录|
|---|---|---|---|---|
|192.168.0.100|/usr/local/minio/bin|/usr/local/minio/upload|/usr/local/minio/run|/usr/local/minio/log|
|192.168.0.101|/usr/local/minio/bin|/usr/local/minio/upload|/usr/local/minio/run|/usr/local/minio/log|
|192.168.0.102|/usr/local/minio/bin|/usr/local/minio/upload|/usr/local/minio/run|/usr/local/minio/log|
|192.168.0.103|/usr/local/minio/bin|/usr/local/minio/upload|/usr/local/minio/run|/usr/local/minio/log|

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
export MINIO_ACCESS_KEY=admin
export MINIO_SECRET_KEY=password
/usr/local/minio/bin/minio server --address "192.168.0.100:9000" \
http://192.168.0.100:9000/usr/local/minio/upload \
http://192.168.0.101:9000/usr/local/minio/upload \
http://192.168.0.102:9000/usr/local/minio/upload \
http://192.168.0.103:9000/usr/local/minio/upload \
> /usr/local/minio/log/run.log
```

#### 5.2 节点 192.168.0.101 的脚本内容

```bash
#!/bin/bash
export MINIO_ACCESS_KEY=admin
export MINIO_SECRET_KEY=password
/usr/local/minio/bin/minio server --address "192.168.0.101:9000" \
http://192.168.0.100:9000/usr/local/minio/upload \
http://192.168.0.101:9000/usr/local/minio/upload \
http://192.168.0.102:9000/usr/local/minio/upload \
http://192.168.0.103:9000/usr/local/minio/upload \
> /usr/local/minio/log/run.log
```

#### 5.3 节点 192.168.0.102 的脚本内容

```bash
#!/bin/bash
export MINIO_ACCESS_KEY=admin
export MINIO_SECRET_KEY=password
/usr/local/minio/bin/minio server --address "192.168.0.102:9000" \
http://192.168.0.100:9000/usr/local/minio/upload \
http://192.168.0.101:9000/usr/local/minio/upload \
http://192.168.0.102:9000/usr/local/minio/upload \
http://192.168.0.103:9000/usr/local/minio/upload \
> /usr/local/minio/log/run.log
```

#### 5.4 节点 192.168.0.103 的脚本内容

```bash
#!/bin/bash
export MINIO_ACCESS_KEY=admin
export MINIO_SECRET_KEY=password
/usr/local/minio/bin/minio server --address "192.168.0.103:9000" \
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

在 nginx.conf 文件里增加如下配置:

```conf
   upstream lb_minio {
        server 192.168.0.100:9000;
        server 192.168.0.101:9000;
        server 192.168.0.102:9000;
        server 192.168.0.103:9000;
    }

    server {
        listen       80;
        
        location /minio {
            proxy_pass http://lb_minio/;
        }
    }    
```

此时，minio 对外的 ENDPOINT 为: ```http://${NGINX_HOST}/minio```

### FAQ

如果服务启动失败，则:

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
