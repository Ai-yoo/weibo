package com.du.weibo.dao;

import com.du.weibo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author duzhentong
 * @Date 2018/8/29
 * @Time 9:36
 */

@Mapper
public interface UserDao {

    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name,password ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;


    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{name},#{password})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where name=#{name}"})
    User selectByName(String name);
}
