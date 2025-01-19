package com.auroral.controller;

import com.auroral.entity.ResponseResult;
import com.auroral.utils.AliOssUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @RequestMapping("/upload")
    public ResponseResult upload(MultipartFile file) throws Exception {
        //把文件的内容存储到本地磁盘上
        String originalFilename = file.getOriginalFilename();
        //保证文件的名字是唯一的，从而防止文件覆盖
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
//        file.transferTo(new File("/Users/超级无敌巨重要的资料/frontend/SpringBoot+Vue项目实战/big-event/file/"+filename));
        String url = AliOssUtil.uploadFile(filename,file.getInputStream());
        return ResponseResult.okResult(url);
    }
}
