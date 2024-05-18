package com.qiu.paper_management.controller;

import com.qiu.paper_management.pojo.Result;
import com.qiu.paper_management.service.OtherService;
import com.qiu.paper_management.utils.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@CrossOrigin
@RestController
public class OtherController {
    @Autowired
    OtherService otherService;
    @PostMapping("/upload")
    public Result<String> Upload(MultipartFile file, Integer id) throws Exception {
        String originalFilename = file.getOriginalFilename();
        // 将文件存储到oss云服务器上，UUID保证名字唯一，不因文件名重复而发生覆盖
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf('.'));
//        file.transferTo(new File("C:\\Users\\Qiu\\Desktop\\files\\" + filename));
        String originalObj = otherService.findObjById(id);
        String url = OssUtil.uploadFile(originalObj, filename, file.getInputStream());
        otherService.uploadFile(id, url);
        return Result.success(url);
    }
}
