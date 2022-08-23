package com.qf.CJDX_MANAGER.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.qf.CJDX_MANAGER.entity.Role;
import com.qf.CJDX_MANAGER.entity.User;
import com.qf.CJDX_MANAGER.service.RoleService;
import com.qf.CJDX_MANAGER.utils.Constants;
import com.qf.CJDX_MANAGER.utils.PageSupport;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/sys/role")
public class RoleController {
    @Autowired(required = false)
    private RoleService roleService;
    //查询用户
    @RequestMapping("/list.html")
    public String getRoleList(Model model){
        //角色列表
        List<Role> roleList = null;

        //查询用户的数据列表
        try{
            //查询角色的列表
            roleList = roleService.getRoleList();
        }catch (Exception e){
            e.printStackTrace();
        }

        model.addAttribute("roleList",roleList);
        return "rolelist";
    }

    /**
     * 动态获取角色数据
     * @return
     */
    @RequestMapping(value = "/rolelist.json",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public List<Role> getRoleList(){
        //初始化角色集合
        List<Role> roleList = null;
        try {
            roleList = roleService.getRoleList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return roleList;
    }

    /**
     * 验证角色存在
     * @param roleCode
     */
    @RequestMapping(value = "/rcexist.json",method = RequestMethod.GET)
    @ResponseBody
    public Object RoleCodeIsExist(@RequestParam String roleCode){
        HashMap<String,String> resultMap = new HashMap<>();

        if (StringUtils.isNullOrEmpty(roleCode)){
            resultMap.put("roleCode","noexist");
        }else {
            //客户端传递的roleCode是否有值
            Role role = null;
            try {
                role = roleService.selectRoleCodeExist(roleCode);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (role!=null) resultMap.put("roleCode","exist");
            else  resultMap.put("roleCode","noexist");

        }
        return JSONArray.toJSONString(resultMap);
    }


    //跳转添加页面
    @RequestMapping(value = "/add.html",method = RequestMethod.GET)
    public String addRole(@ModelAttribute("role") Role role){
        return "roleadd";
    }


    //新增角色
    @RequestMapping(value = "/addsave.html",method = RequestMethod.POST)
    public String addRoleSave(Role role, HttpSession session){

        System.out.println(role);
            role.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            role.setCreationDate(new Date());

            try{
                if(roleService.add(role))
                    return "redirect:/sys/role/list.html";
            }catch (Exception e) {
                e.printStackTrace();
        }
        return "roleadd";
    }


    /**
     * 修改回显
     */
    @RequestMapping(value = "/modify/{id}")
    public String getUserById(Model model, @PathVariable String id) {
        Role role = new Role();
        try {
            role = roleService.getRoleById(Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(role);
        return "rolemodify";
    }

    /**
     * 保存修改
     * @param role
     * @return
     */
    @RequestMapping(value = "/modifysave.html", method = RequestMethod.POST)
    public String modifyUser(Role role,HttpSession session) {
        role.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        role.setModifyDate(new Date());
        try{
            if (roleService.modify(role)){
                return "redirect:/sys/role/list.html";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "rolelist";
    }

    /**
     * 删除单个角色
     * @param id
     * @return
     */
    @RequestMapping(value = "/delrole.json",method = RequestMethod.GET)
    @ResponseBody
    public Object deleteRole(@RequestParam("roleid") String id){
        HashMap<String,String> resultMap=new HashMap<>();
        //判断该角色是否有系统用户引用
        if(StringUtils.isNullOrEmpty(id)){
            resultMap.put("delResult","notexist");
        }else{
            try{
                //删除用户
                if(roleService.deleteRoleById(Integer.parseInt(id))){
                    //删除成功
                    resultMap.put("delResult","true");
                }else{
                    //删除失败
                    resultMap.put("delResult","false");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return JSONArray.toJSON(resultMap);
    }

}
