package com.qiu.paper_management.service.Impl;

import com.qiu.paper_management.mapper.OtherMapper;
import com.qiu.paper_management.service.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtherServiceImpl implements OtherService {
    @Autowired
    OtherMapper otherMapper;
    @Override
    public void uploadFile(Integer id, String url) {
        otherMapper.uploadFile(id, url);
    }

    @Override
    public String findObjById(Integer id) {
        String url = otherMapper.findObjById(id);
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
