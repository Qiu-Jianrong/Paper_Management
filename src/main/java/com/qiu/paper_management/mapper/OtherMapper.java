package com.qiu.paper_management.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OtherMapper {
    @Update("update paper_management.article set paper_pdf=#{url}, update_time=now() where id=#{id}")
    void uploadFile(Integer id, String url);

    @Select("select paper_pdf from paper_management.article where id=#{id};")
    String findObjById(Integer id);
}
