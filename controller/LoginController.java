package com.qf.CJDX_MANAGER.controller;


import com.qf.CJDX_MANAGER.entity.User;
import com.qf.CJDX_MANAGER.service.UserService;
import com.qf.CJDX_MANAGER.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author:wjp
 * @DATE：2022/7/15 8:33
 * @Description: 处理用户登录以及注销功能
 * @Version1.8
 */

@Controller
public class LoginController {
    //创建打印日志的对象
    private Logger logger = Logger.getLogger(LoginController.class);

    @Resource
    UserService userService;
    //逻辑功能代码

    @RequestMapping(value = "/dologin",method = RequestMethod.POST)
    public ModelAndView doLogin(@RequestParam String userCode, @RequestParam String userPassword, HttpSession session, HttpServletRequest request){
        logger.debug("doLogin....................");
        User user = userService.login(userCode, userPassword);
        if (user !=null){
            //成功，用户信息存入session
            session.setAttribute(Constants.USER_SESSION,user);
            ModelAndView mav = new ModelAndView("/frame");
            //跳转首页
            return mav;
        }else {
            //重新登录
            request.setAttribute("error","用户名或密码错误！");
            ModelAndView mav = new ModelAndView("redirect:/login.jsp");
            return mav;
        }
    }

    //用户注销功能
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session, HttpServletRequest request){
        //清除用户信息
        session.removeAttribute(Constants.USER_SESSION);
        ModelAndView mav = new ModelAndView("redirect:/login.jsp");
        return mav;
    }



}
