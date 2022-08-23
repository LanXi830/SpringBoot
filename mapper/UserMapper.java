package com.qf.CJDX_MANAGER.mapper;

import com.qf.CJDX_MANAGER.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author:wjp
 * @DATE：2022/7/15 9:16
 * @Description:
 * @Version1.8
 */
public interface UserMapper {
    /**
     * 用户登录
     * @param userCode
     * @param userPassword
     * @return
     */
    User login(@Param("userCode") String userCode, @Param("userPassword") String userPassword);

    List<User> getUserList(@Param("queryname") String queryname,
                           @Param("queryUserRole") Integer queryUserRole,
                           @Param("currentPageNo") int currentPageNo,
                           @Param("pageSize") int pageSize);

    int getTotalCount(@Param("queryname") String queryname, @Param("userRole") Integer queryUserRole);


    User selectUserCodeExist(@Param("userCode") String userCode);

    int add(User user);

    User getUserById(int id);

    int modify(User user);

    int deleteUserById(@Param("id") int id);

    int updataPwd(@Param("id")Integer id, @Param("newpassword")String newpassword);

}
