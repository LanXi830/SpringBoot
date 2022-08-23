package com.qf.CJDX_MANAGER.service;

import com.qf.CJDX_MANAGER.entity.User;

import java.util.List;

/**
 * @Author:wjp
 * @DATE：2022/7/15 8:49
 * @Description:
 * @Version1.8
 */
public interface UserService {

    /**
     *
     * @param userCode 用户编码
     * @param userPassword 用户密码
     * @return 用户对象
     */
    User login (String userCode, String userPassword );

    /**
     * 查询用户列表
     * @param queryname
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<User> getUserList(String queryname, Integer queryUserRole, int currentPageNo, int pageSize);

    /**
     * 查询总页面数
     * @param queryname
     * @param queryUserRole
     * @return
     */
    int getTotalCount(String queryname, Integer queryUserRole);

    boolean add(User user);

    User selectUserCodeExist(String userCode);

    User getUserById(int id);

    boolean modify(User user);
    /**
     * 通过id删除
     * @param id
     * @return
     */
    boolean deleteUserById(int id);
    /**
     * 更改密码
     * @param id
     * @param newpassword
     * @return
     */
    boolean updataPwd(Integer id, String newpassword);
}
