package org.springframework.cloud.admin.minio.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zhy
 * @date 2022/1/17 11:25
 */@Data
public class DeleteVo {
     private List<String> fileNameList;
}
