package com.qf.CJDX_MANAGER.mapper;

import com.qf.CJDX_MANAGER.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    List<Role> getRoleList();

    int add(Role role);

    Role selectRoleCodeExist(@Param("roleCode") String roleCode);

    Role getRoleById(int id);

    int modify(Role role);


    /**
     * 删除单个用户
     * @param id
     * @return
     */
    int deleteRoleById(int id);

    /**
     * 查找使用该角色的用户数量
     * @param id
     * @return
     */
    int selectUserNum(int id);


}
