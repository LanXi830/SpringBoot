package com.qf.CJDX_MANAGER.service.impl;

import com.qf.CJDX_MANAGER.entity.Role;
import com.qf.CJDX_MANAGER.mapper.RoleMapper;
import com.qf.CJDX_MANAGER.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired(required = false)
    private RoleMapper roleMapper;

    /**
     * 查询所有角色信息
     * @return
     */
    @Override
    public List<Role> getRoleList() {
        return roleMapper.getRoleList();
    }

    @Override
    public boolean add(Role role) {
        boolean flag = false;
        if (roleMapper.add(role)>0){
            flag=true;
        }
        return flag;
    }

    @Override
    public Role selectRoleCodeExist(String roleCode) {
        return roleMapper.selectRoleCodeExist(roleCode);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Role getRoleById(int id) {
        return roleMapper.getRoleById(id);
    }

    @Override
    public boolean modify(Role role) {
        boolean flag = false;
        if (roleMapper.modify(role) > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Override
    public boolean deleteRoleById(int id) {
        boolean flag=false;
        if(roleMapper.selectUserNum(id)==0){
            if(roleMapper.deleteRoleById(id)>0)
                flag=true;
        }
        return flag;
    }
}
