package org.springframework.cloud.admin.minio.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author zhy
 * @date 2022/1/11 16:40
 */
public class MinioPolicyUtil {
    public static String config(String path, String bucketName) throws IOException {
        StringBuilder result = new StringBuilder();
        Resource resource = new ClassPathResource(path);
        InputStream in = resource.getInputStream();
        // 读取文件上的数据。
        // 将字节流向字符流的转换。
        // 读取
        InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
        // 创建字符流缓冲区
        // 缓冲
        BufferedReader bufr = new BufferedReader(isr);

        String line = null;
        while ((line = bufr.readLine()) != null) {
            result.append(System.lineSeparator() + line);
        }
        isr.close();
        JSONObject parse = (JSONObject) JSONObject.parse(result.toString());
        JSONArray statement = (JSONArray) parse.get("Statement");
        JSONObject o = (JSONObject) statement.get(0);
        o.remove("Resource");
        o.fluentPut("Resource", "arn:aws:s3:::" + bucketName);
        JSONObject o1 = (JSONObject) statement.get(1);
        o1.remove("Resource");
        o1.fluentPut("Resource", "arn:aws:s3:::" + bucketName + "/*");
        return parse.toString();
    }
}
