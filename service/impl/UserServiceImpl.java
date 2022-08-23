package com.qf.CJDX_MANAGER.service.impl;

import com.qf.CJDX_MANAGER.mapper.UserMapper;
import com.qf.CJDX_MANAGER.entity.User;
import com.qf.CJDX_MANAGER.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User login(String userCode, String userPassword) {
        User user = userMapper.login(userCode,userPassword);
        if (user != null){
            if (!user.getUserPassword().equals(userPassword)){
                user = null;
            }
        }
        return user;
    }

    /**
     *查询用户列表
     * @param queryname
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<User> getUserList(String queryname, Integer queryUserRole, int currentPageNo, int pageSize) {
        //计算分页的起始索引
        currentPageNo =(currentPageNo-1)* pageSize;

        return userMapper.getUserList(queryname,queryUserRole,currentPageNo,pageSize);
    }

    /**
     * 查询总记录数
     * @param queryname
     * @param queryUserRole
     * @return
     */
    @Override
    public int getTotalCount(String queryname, Integer queryUserRole) {
        return userMapper.getTotalCount(queryname,queryUserRole);
    }

    @Override
    public boolean add(User user) {
        boolean flag = false;
        if (userMapper.add(user)>0){
            flag=true;
        }
        return flag;
    }

    @Override
    public User selectUserCodeExist(String userCode) {
        return userMapper.selectUserCodeExist(userCode);

    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }


    //修改保存用户
    @Override
    public boolean modify(User user) {
        boolean flag = false;
        if (userMapper.modify(user)>0){
            flag=true;
        }
        return flag;
    }

    @Override
    public boolean deleteUserById(int id) {
        boolean flag = false;
        if (userMapper.deleteUserById(id)>0){
            flag=true;
        }
        return flag;
    }

    @Override
    public boolean updataPwd(Integer id, String newpassword) {
        boolean flag = false;
        if (userMapper.updataPwd(id,newpassword)>0){
            flag=true;
        }
        return flag;
    }
}
