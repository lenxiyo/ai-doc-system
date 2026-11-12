package com.lvent.doc.mapper;

import com.lvent.doc.model.po.DocPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DocMapper {

    @Insert("insert into doc (user_id, file_name, create_time, content) values (#{userId},#{fileName},#{createTime},#{content});")
    Integer save(DocPO docPO);
}
