package com.qf.CJDX_MANAGER.service;

import com.qf.CJDX_MANAGER.entity.Role;

import java.util.List;

public interface RoleService {
    /**
     * 查询所以角色信息
     * @return
     */
    List<Role> getRoleList();
    /**
     * 添加角色
     * @param role
     * @return
     */
    boolean add(Role role);
    /**
     * 验证角色编码存在
     * @param roleCode
     * @return
     */
    Role selectRoleCodeExist(String roleCode);

    /**
     * 修改回显 id 获取用户信息
     * @param id
     * @return
     */
    Role getRoleById(int id);
    /**
     * 保存修改
     * @param role
     * @return
     */
    boolean modify(Role role);
    /**
     * 删除角色信息
     * @param id
     * @return
     */
    boolean deleteRoleById(int id);
}
