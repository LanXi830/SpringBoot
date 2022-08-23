package com.qf.CJDX_MANAGER.controller;


import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.qf.CJDX_MANAGER.entity.Provider;
import com.qf.CJDX_MANAGER.entity.Role;
import com.qf.CJDX_MANAGER.entity.User;
import com.qf.CJDX_MANAGER.service.ProviderService;
import com.qf.CJDX_MANAGER.utils.Constants;
import com.qf.CJDX_MANAGER.utils.PageSupport;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/sys/provider")
public class ProviderController {


        @Autowired(required = false)
        private ProviderService providerService;


        /**
         * 查询商家列表
         *
         * @return
         */
        @RequestMapping(value = "/list.html")
        public String getProviderList(Model model, @RequestParam(value = "queryProCode", required = false) String queryProCode,
                                  @RequestParam(value = "queryProName", required = false) String queryProName,
                                  @RequestParam(value = "pageIndex", required = false) String pageIndex) {
            // 用户列表
            List<Provider> providerList = null;
            // 设置页面容量
            int pageSize = Constants.pageSize;
            // 设置当前页
            int currentPageNo = 1;
            // 判断接受的用户名
            if (queryProCode == null) {
                queryProCode = "";
            }
            // 设置当前页
            if (pageIndex != null) {
                try {
                    currentPageNo = Integer.parseInt(pageIndex);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            // 设置商家的数量
            int totalCount = 0;
            try {
                // 根据编码、名称查询总记录数量
                totalCount = providerService.getTotalCount(queryProCode, queryProName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 总页数封装到分页工具类
            PageSupport pageSupport = new PageSupport();
            pageSupport.setCurrentPageNo(currentPageNo);
            pageSupport.setPageSize(pageSize);
            pageSupport.setTotalCount(totalCount);
            // 总页数
            int totalPageCount = pageSupport.getTotalPageCount();
            // 控制首页和尾页
            if (currentPageNo < 1)
                currentPageNo = 1;
            else if (currentPageNo > totalPageCount)
                currentPageNo = totalPageCount;
            // 查询用户数据列表
            try {
                providerList = providerService.getProviderList(queryProCode, queryProName, currentPageNo, pageSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.addAttribute("providerList", providerList);
            model.addAttribute("queryProCode", queryProCode);
            model.addAttribute("queryProName", queryProName);
            model.addAttribute("totalPageCount", totalPageCount);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("currentPageNo", currentPageNo);
            return "providerlist";
        }

        /**
         * 跳转至添加页面
         *
         * @return
         */
        @RequestMapping(value = "/add.html", method = RequestMethod.GET)
        public String addProvider(@ModelAttribute("provider") Provider provider) {
            return "provideradd";
        }

        /**
         * 商家新增功能
         * @return
         */
        @RequestMapping(value = "/addsave.html", method = RequestMethod.POST)
        public String addProviderSave(Provider provider, HttpSession session,
                                  HttpServletRequest request,
                                  @RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
            // 设置初始值
            String a_companyLicPicPath = null;
            String a_orgCodePicPath = null;
            String errorInfo = null;
            // 文件上传成功标识
            boolean flag = true;
            // 获取文件上传真实路径
            String path = request.getSession().
                    getServletContext().getRealPath
                    ("statics" + File.separator + "uploadfiles");
            // 文件上传
            for (int i = 0; i < attachs.length; i++) {
                MultipartFile attach = attachs[i];
                // 当上传的图片不为空
                if (!attach.isEmpty()) {
                    if (i == 0) {
                        // 初始化报错信息
                        errorInfo = "uploadFileError";
                    } else if (i == 1) {
                        errorInfo = "uploadWpError";
                    }
                    // 获取原文件名称
                    String oldFileName = attach.getOriginalFilename();
                    System.out.println("原文件名称:" + oldFileName);
                    // 获取原文件的后缀
                    String subfix = FilenameUtils.getExtension(oldFileName);
                    System.out.println("后缀:" + subfix);
                    // 设置文件上传的大小
                    int size = 50000000;// 500KB
                    if (attach.getSize() > size) {
                        request.setAttribute(errorInfo, "文件上传不得超过500KB");
                        flag = false;
                        // 判断文件类型
                    } else if (subfix.equalsIgnoreCase("jpg") ||
                            subfix.equalsIgnoreCase("png") ||
                            subfix.equalsIgnoreCase("jpeg") ||
                            subfix.equalsIgnoreCase("pneg")) {
                        // 给文件命名唯一的名称
                        String fileName = System.currentTimeMillis() +
                                UUID.randomUUID().toString() + "_persional.jpg";
                        System.out.println("新文件名称" + fileName);
                        // 创建文件
                        File targeFile = new File(path, fileName);
                        // 如果文件不存在则创建
                        if (!targeFile.exists()) {
                            targeFile.mkdirs();
                        }
                        // 执行保存操作
                        try {
                            attach.transferTo(targeFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            request.setAttribute(errorInfo, "上传失败!");
                            flag = false;
                        }
                        // 给工作照和证件照赋值
                        if (i == 0)
                            a_companyLicPicPath = path + File.separator + fileName;
                        if (i == 1)
                            a_orgCodePicPath = path + File.separator + fileName;
                        System.out.println("idPicPath" + a_companyLicPicPath);
                        System.out.println("workPicPath" + a_orgCodePicPath);
                    } else {
                        // 上传图片格式不正确
                        request.setAttribute(errorInfo, "上传图片格式不正确!");
                        flag = false;
                    }
                }
            }
            // 如果文件上传成功,保存用户
            if (flag) {
                provider.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
                provider.setCreationDate(new Date());
                provider.setCompanyLicPicPath(a_companyLicPicPath);
                provider.setOrgCodePicPath(a_orgCodePicPath);
                try {
                    if (providerService.add(provider))
                        return "redirect:/sys/provider/list.html";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "provideradd";
        }


    /**
     * 显示详情
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model, HttpServletRequest request){
        Provider provider=new Provider();
        try {
            provider = providerService.getProviderById(Integer.parseInt(id));
            if (provider.getCompanyLicPicPath() != null && !"".equals(provider.getCompanyLicPicPath())) {
                //截取路径
                String[] paths = provider.getCompanyLicPicPath().split("\\" + File.separator);
                provider.setCompanyLicPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
            }
            if (provider.getOrgCodePicPath() != null && !provider.getOrgCodePicPath().equals("")) {
                String[] paths = provider.getOrgCodePicPath().split("\\" + File.separator);
                provider.setOrgCodePicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(provider);
        return "providerview";
    }


    /**
     * 删除供应商
     * @param id
     * @return
     */
    @RequestMapping(value = "/del.json",method = RequestMethod.POST)
    @ResponseBody
    public Object deleteProviderById(@RequestParam("proid") String id ){

        HashMap<String,String> resultMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(id)){
            //说明id不存在
            resultMap.put("delResult","notexist");
        }else {
            try{
                //删除用户
                if (providerService.deleteProviderById(Integer.parseInt(id)))
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
     * 修改回显
     */
    @RequestMapping(value = "/modify/{id}")
    public String getProviderById(Model model, @PathVariable String id, HttpServletRequest request) {
        Provider provider = new Provider();
        try {
            provider = providerService.getProviderById(Integer.parseInt(id));
            //企业营业执照
            if (provider.getCompanyLicPicPath() != null && !provider.getCompanyLicPicPath().equals("")) {
                String[] paths = provider.getCompanyLicPicPath().split("\\" + File.separator);
                //获取照片名称
                provider.setCompanyLicPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
                System.out.println(provider.getCompanyLicPicPath());
            }
            //组织机构代码证
            if (provider.getOrgCodePicPath() != null && !provider.getOrgCodePicPath().equals("")) {
                String[] paths = provider.getOrgCodePicPath().split("\\" + File.separator);
                //获取照片名称
                provider.setOrgCodePicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(provider);
        return "providermodify";
    }


    /**保存修改商家
     * @param provider
     * @param session
     * @param request
     * @param attachs
     * @return
     */
    @RequestMapping(value = "/modifysave.html", method = RequestMethod.POST)
    public String modifyProvider(Provider provider, HttpSession session, HttpServletRequest request,
                                 @RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {

        System.out.println("进入modifyProvider\n attachs==========" + attachs);
        // 设置初始值
        String a_companyLicPicPath = null;
        String a_orgCodePicPath = null;
        String errorInfo = null;
        // 文件上传成功标识
        boolean flag = true;
        // 获取文件上传真实路径
        String path = request.getSession().
                getServletContext().getRealPath
                ("statics" + File.separator + "uploadfiles");
        System.out.println("path=================="+path);

        // 文件上传
        if (attachs != null) {
            for (int i = 0; i < attachs.length; i++) {
                MultipartFile attach = attachs[i];
                // 当上传的图片不为空
                if (!attach.isEmpty()) {
                    if (i == 0) {
                        // 初始化报错信息
                        errorInfo = "uploadFileError";
                    } else if (i == 1) {
                        errorInfo = "uploadWpError";
                    }
                    // 获取原文件名称
                    String oldFileName = attach.getOriginalFilename();
                    System.out.println("原文件名称:" + oldFileName);
                    // 获取原文件的后缀
                    String subfix = FilenameUtils.getExtension(oldFileName);
                    System.out.println("后缀:" + subfix);
                    // 设置文件上传的大小
                    int size = 50000000;// 500KB
                    if (attach.getSize() > size) {
                        request.setAttribute(errorInfo, "文件上传不得超过500KB");
                        flag = false;
                        // 判断文件类型
                    } else if (subfix.equalsIgnoreCase("jpg") ||
                            subfix.equalsIgnoreCase("png") ||
                            subfix.equalsIgnoreCase("jpeg") ||
                            subfix.equalsIgnoreCase("pneg")) {
                        // 给文件命名唯一的名称
                        String fileName = System.currentTimeMillis() +
                                UUID.randomUUID().toString() + "_persional.jpg";
                        System.out.println("新文件名称" + fileName);
                        // 创建文件
                        File targeFile = new File(path, fileName);
                        // 如果文件不存在则创建
                        if (!targeFile.exists()) {
                            targeFile.mkdirs();
                        }
                        // 执行保存操作
                        try {
                            attach.transferTo(targeFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            request.setAttribute(errorInfo, "上传失败!");
                            flag = false;
                        }
                        // 给工作照和证件照赋值
                        if (i == 0)
                            a_companyLicPicPath = path + File.separator + fileName;
                        if (i == 1)
                            a_orgCodePicPath = path + File.separator + fileName;
                        System.out.println("idPicPath" + a_companyLicPicPath);
                        System.out.println("workPicPath" + a_orgCodePicPath);
                    } else {
                        // 上传图片格式不正确
                        request.setAttribute(errorInfo, "上传图片格式不正确!");
                        flag = false;
                    }
                }
            }
        }
        // 如果文件上传成功,保存用户
        if (flag) {
            provider.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
            provider.setModifyDate(new Date());
            provider.setCompanyLicPicPath(a_companyLicPicPath);
            provider.setOrgCodePicPath(a_orgCodePicPath);
            try {
                if (providerService.modify(provider))
                    System.out.println("修改成功！！！！");
                return "redirect:/sys/provider/list.html";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "providerlist";
    }


}
