package com.qiu.paper_management.service;

public interface OtherService {
    void uploadFile(Integer id, String url);

    // 找出原先文件在服务器上的pdf名字，注意只需要名字!
    String findObjById(Integer id);
}
