package com.qf.CJDX_MANAGER.controller;


import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.qf.CJDX_MANAGER.entity.Role;
import com.qf.CJDX_MANAGER.entity.User;
import com.qf.CJDX_MANAGER.service.RoleService;
import com.qf.CJDX_MANAGER.service.UserService;
import com.qf.CJDX_MANAGER.utils.Constants;
import com.qf.CJDX_MANAGER.utils.GetAge;
import com.qf.CJDX_MANAGER.utils.PageSupport;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sound.midi.SysexMessage;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/sys/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private RoleService roleService;


    //查询用户
    @RequestMapping("/list.html")
    public String getUserList(Model model,
                              @RequestParam(value = "queryname",required = false)String queryname,
                              @RequestParam(value = "queryUserRole",required = false)String queryUserRole,
                              @RequestParam(value = "pageIndex",required = false)String pageIndex){
        //设置初始值
        Integer _queryUserRole = null;
        //用户列表
        List<User> userList = null;
        //角色列表
        List<Role> roleList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //设置当前页
        int currentPageNo = 1;
        //判断接收的用户名
        if(queryname==null){
            queryname="";
        }
        //判断角色标识
        if (queryUserRole!=null&&!queryUserRole.equals("")){
            _queryUserRole =Integer.parseInt(queryUserRole);
        }
        //设置当前页
        if(pageIndex != null){
            try{
                currentPageNo= Integer.parseInt(pageIndex);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        //设置用户总数量
        int totalCount = 0;
        try {
            //根据用户名称、角色id查询总记录数量
         totalCount = userService.getTotalCount(queryname,_queryUserRole);
        }catch (Exception e){
            e.printStackTrace();
        }
        //总页数封装分页
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        //总页数
        int totalPageCount = pageSupport.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo<1)
            currentPageNo=1;
        else if(currentPageNo>totalPageCount)
            currentPageNo=totalPageCount;

        //查询用户的数据列表
        try{
            userList = userService.getUserList(queryname,_queryUserRole,currentPageNo,pageSize);
            //通过生日获得年龄
            for (int i = 0; i <userList.size(); i++) {
                User user = userList.get(i);
                user.setAge(GetAge.getAge(user.getBirthday()));
            }

            //查询角色的列表
           roleList = roleService.getRoleList();
        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("userList",userList);
        model.addAttribute("roleList",roleList);
        model.addAttribute("queryUserName",queryname);
        model.addAttribute("queryUserRole",queryUserRole);
        model.addAttribute("totalPageCount",totalPageCount);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);


        return "userlist";
    }


    @RequestMapping(value = "/add.html",method = RequestMethod.GET)
    public String addUser(@ModelAttribute("user") User user){
        return "useradd";
    }



    //新增用户
    @RequestMapping(value = "/addsave",method = RequestMethod.POST)
    public String addUserSave(User user, HttpSession session,
                              HttpServletRequest request,
                              @RequestParam(value = "attachs",required = false) MultipartFile[] attachs){
        //设置初始值
        String idPicPath=null;
        String workPicPath=null;
        String errorInfo=null;
        //文件上传成功标识
        boolean flag=true;
        //或者文件上传真实路径
        String path=request.getSession().getServletContext().getRealPath
                ("statics"+ File.separator+"uploadfiles");
        System.out.println(path+"-------------");

        //文件上传
        for(int i=0;i<attachs.length;i++){
            MultipartFile attach=attachs[i];
            //当上传的图片不为空
            if(!attach.isEmpty()){
                if (i==0){
                    //初始化报错信息
                    errorInfo="uploadFileError";
                }else if(i==1){
                    errorInfo="uploadWpError";
                }
                //获取原文件名称
                String oldFileName=attach.getOriginalFilename();
                System.out.println("原文件名称："+oldFileName);
                //获取原文件的后缀
                String subfix = FilenameUtils.getExtension(oldFileName);
                System.out.println("后缀："+subfix);
                //设置文件上传的大小
                int size=50000000;//500kb
                if(attach.getSize()>size){
                    request.setAttribute(errorInfo,"文件上传不得超过500kb");
                    flag=false;
                    //判断文件类型
                }else if (subfix.equalsIgnoreCase("jpg")||
                        subfix.equalsIgnoreCase("png")||
                        subfix.equalsIgnoreCase("jepg")||
                        subfix.equalsIgnoreCase("pneg")){
                    //给文件命名唯一的名称
                    String fileName= System.currentTimeMillis()+
                            UUID.randomUUID().toString() +"_persional.jpg";
                    System.out.println("新文件名称"+fileName);
                    //创建文件
                    File targeFile=new File(path,fileName);
                    //如果文件不存在则创建
                    if(!targeFile.exists()){
                        targeFile.mkdir();
                    }
                    //执行保存操作
                    try{
                        attach.transferTo(targeFile);
                    }catch (Exception e){
                        e.printStackTrace();
                        request.setAttribute(errorInfo,"上传失败");
                        flag=false;
                    }
                    //给工作照和证件照赋值
                    if(i==0)
                        idPicPath=path+File.separator+fileName;
                    if (i==1)
                        workPicPath=path+File.separator+fileName;
                    System.out.println("idPicPath"+idPicPath);
                    System.out.println("workPicPath"+workPicPath);
                }else {
                    //上传图片格式不正确
                    request.setAttribute(errorInfo,"上传图片格式不正确");
                    flag=false;
                }
            }
        }
        //如果文件上传成功，保存用户
        if(flag){
            user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            user.setCreationDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            try{
                if(userService.add(user))
                    return "redirect:/sys/user/list.html";
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "useradd";
    }

    /**
     * 验证用户编码
     * @param userCode
     */
    @RequestMapping(value = "/ucexist.json",method = RequestMethod.GET)
    @ResponseBody
    public Object UserCodeIsExist(@RequestParam String userCode){
        HashMap<String,String> resultMap = new HashMap<>();

        if (StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode","noexist");
        }else {
            //客户端传递的userCode是否有值
            User user = null;
            try {
             user = userService.selectUserCodeExist(userCode);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (user!=null) resultMap.put("userCode","exist");
            else  resultMap.put("userCode","noexist");

        }
         return JSONArray.toJSONString(resultMap);
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


    //回显数据
    @RequestMapping(value = "/modify/{id}")
    public String getUserById(Model model,@PathVariable String id,HttpServletRequest request){
        User user = new User();
        try {
            user= userService.getUserById(Integer.parseInt(id));
            //证件照工作照
            if (user.getIdPicPath() !=null && !user.getIdPicPath().equals("")){
                String[] paths = user.getIdPicPath().split("\\" + File.separator);
                //获取照片名称
                user.setIdPicPath(request.getContextPath()+"/statics/uploadfiles/"+paths[paths.length-1]);

            }
            //证件照工作照
            if (user.getWorkPicPath() !=null && !user.getWorkPicPath().equals("")){
                String[] paths = user.getWorkPicPath().split("\\" + File.separator);
                //获取照片名称
                user.setWorkPicPath(request.getContextPath()+"/statics/uploadfiles/"+paths[paths.length-1]);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute(user);
        return "usermodify";
    }


    //修改保存
    @RequestMapping(value = "/modifysave.html",method = RequestMethod.POST)
    public String modifyUserSave(User user,  HttpSession session,
                                 HttpServletRequest request,
                                 @RequestParam(value = "attachs",required = false)MultipartFile[] attachs ){
        //设置初始值
        String idPicPath=null;
        String workPicPath=null;
        String errorInfo=null;
        //文件上传成功标识
        boolean flag=true;
        //或者文件上传真实路径
        String path=request.getSession().getServletContext().getRealPath
                ("statics"+ File.separator+"uploadfiles");
        System.out.println(path+"-------------");

        if (attachs!=null){
            //文件上传
            for(int i=0;i<attachs.length;i++){
                MultipartFile attach=attachs[i];
                //当上传的图片不为空
                if(!attach.isEmpty()){
                    if (i==0){
                        //初始化报错信息
                        errorInfo="uploadFileError";
                    }else if(i==1){
                        errorInfo="uploadWpError";
                    }
                    //获取原文件名称
                    String oldFileName=attach.getOriginalFilename();
                    System.out.println("原文件名称："+oldFileName);
                    //获取原文件的后缀
                    String subfix = FilenameUtils.getExtension(oldFileName);
                    System.out.println("后缀："+subfix);
                    //设置文件上传的大小
                    int size=50000000;//500kb
                    if(attach.getSize()>size){
                        request.setAttribute(errorInfo,"文件上传不得超过500kb");
                        flag=false;
                        //判断文件类型
                    }else if (subfix.equalsIgnoreCase("jpg")||
                            subfix.equalsIgnoreCase("png")||
                            subfix.equalsIgnoreCase("jepg")||
                            subfix.equalsIgnoreCase("pneg")){
                        //给文件命名唯一的名称
                        String fileName= System.currentTimeMillis()+
                                UUID.randomUUID().toString() +"_persional.jpg";
                        System.out.println("新文件名称"+fileName);
                        //创建文件
                        File targeFile=new File(path,fileName);
                        //如果文件不存在则创建
                        if(!targeFile.exists()){
                            targeFile.mkdir();
                        }
                        //执行保存操作
                        try{
                            attach.transferTo(targeFile);
                        }catch (Exception e){
                            e.printStackTrace();
                            request.setAttribute(errorInfo,"上传失败");
                            flag=false;
                        }
                        //给工作照和证件照赋值
                        if(i==0)
                            idPicPath=path+File.separator+fileName;
                        if (i==1)
                            workPicPath=path+File.separator+fileName;
                    }else {
                        //上传图片格式不正确
                        request.setAttribute(errorInfo,"上传图片格式不正确");
                        flag=false;
                    }
                }
            }

        }

        //修改用户
        if (flag){
            user.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            user.setModifyDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            try{
                if (userService.modify(user)){
                    return "redirect:/sys/user/list.html";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "usermodify";
    }

    /**
     * 查看用户
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("view/{id}")
    public String view (@PathVariable String id,Model model,HttpServletRequest request){
        User user = new User();
        try {
            user = userService.getUserById(Integer.parseInt(id));
            if (user.getIdPicPath()!=null && !"".equals(user.equals(user.getIdPicPath()))){
                String[] path = user.getIdPicPath().split("\\"+File.separator);
                user.setIdPicPath(request.getContextPath()+"/statics/uploadfiles/"+path[path.length-1]);
            }
            if (user.getWorkPicPath()!=null && !"".equals(user.equals(user.getWorkPicPath()))){
                String[] path = user.getWorkPicPath().split("\\"+File.separator);
                user.setWorkPicPath(request.getContextPath()+"/statics/uploadfiles/"+path[path.length-1]);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute(user);
        return "userview";
    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/deluser.json",method = RequestMethod.GET)
    @ResponseBody
    public Object deleteUser(@RequestParam String id ){
        HashMap<String,String> resultMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(id)){
            //说明id不存在
            resultMap.put("delResult","notexist");
        }else {
            try{
                //删除用户
                if (userService.deleteUserById(Integer.parseInt(id)))
                    //删除成功
                    resultMap.put("delResult","true");
                else
                    //删除失败
                    resultMap.put("delResult","false");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return JSONArray.toJSON(resultMap);
    }

    /**
     * 验证用户是否登录失败
     * @return
     */
    @RequestMapping("/pwdmodify.html")
    public String pwdModify(HttpSession session){
        //判断用户是否过期
        if (session.getAttribute(Constants.USER_SESSION)==null)
            //重定向页面
            return "redirect:/login.jsp";

        return "pwdmodify";
    }

    /**
     * 验证旧密码是否正确
     * @return
     */
    @RequestMapping("/pwdmodify.json")
    @ResponseBody
    public Object getPwdById(@RequestParam String oldpassword,
                             HttpSession session){
        HashMap<String,String> resultMap = new HashMap<>();
        //判断用户是否过期
        if (session.getAttribute(Constants.USER_SESSION)==null){
            resultMap.put("result","sessionerror");
        }else if (StringUtils.isNullOrEmpty(oldpassword)){
            resultMap.put("result","error");
        }else{
            String userPassword = ((User)(session.getAttribute(Constants.USER_SESSION))).getUserPassword();

            if (oldpassword.equals(userPassword))
                resultMap.put("result","true");
            else
                resultMap.put("result","false");
        }

        return JSONArray.toJSON(resultMap);
    }

    /**
     * 密码修改
     * @param newpassword
     * @param session
     * @param request
     * @return
     */
    @RequestMapping("/pwdsave.html")
    public String pwdSave(@RequestParam(value = "newpassword")String newpassword,
                          HttpSession session,HttpServletRequest request){
        //修改是否成功
        boolean flag = false;
        Object obj = session.getAttribute(Constants.USER_SESSION);
        if (obj !=null&&!StringUtils.isNullOrEmpty(newpassword)){
            try{
                //调用更新密码的操作
              flag = userService.updataPwd(((User)obj).getId(),newpassword);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (flag) {
            //更新密码成功
            request.setAttribute(Constants.SYS_MESSAGE,"修改密码成功，请退出之后重新登录");
            //用户数据清空
            session.removeAttribute(Constants.USER_SESSION);
            return "redirect:/login.jsp";
        }else {
            //失败
            request.setAttribute(Constants.SYS_MESSAGE,"修改密码失败，别几把改了");
        }

        return "pwdmodify";
    }


}
