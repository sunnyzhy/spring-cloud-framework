package org.springframework.cloud.admin.minio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.admin.minio.service.MinioService;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author zhy
 * @date 2022/1/11 17:34
 */
@RestController
@RequestMapping(value = "/minio")
@RequiredArgsConstructor
public class MinioController {
    private final MinioService minioService;

    @PostMapping(value = "/upload")
    public Mono<String> upload(@RequestPart("file") FilePart file) {
        return minioService.upload(file);
    }
}
