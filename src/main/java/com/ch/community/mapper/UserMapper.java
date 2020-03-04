package com.ch.community.mapper;

import com.ch.community.module.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {
    @Insert("insert into user(Account_id,name,token,gmt_create,gmt_modify) values (#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified})")
    void insertUser(User user);

    @Select("select * from user where token=#{token}")
    User findByToken();
}
