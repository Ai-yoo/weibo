package com.du.weibo.dao;

import com.du.weibo.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author duzhentong
 * @Date 2018/8/29
 * @Time 9:39
 */

@Mapper
public interface MessageDao {

    String TABLE_NAME = "message";
    String INSERT_FIELDS = " user_id,content,created_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{userId},#{content},#{createdDate})"})
    int addMessage(Message message);


    @Select({"select ", SELECT_FIELDS, "from", TABLE_NAME,
            "order by created_date desc limit #{offset},#{limit}"})
    List<Message> getMessageList(@Param("offset") int offset,@Param("limit") int limit);
}
