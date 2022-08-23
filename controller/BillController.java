package com.qf.CJDX_MANAGER.controller;


import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.qf.CJDX_MANAGER.entity.Bill;
import com.qf.CJDX_MANAGER.entity.Provider;
import com.qf.CJDX_MANAGER.entity.User;
import com.qf.CJDX_MANAGER.service.BillService;
import com.qf.CJDX_MANAGER.service.ProviderService;
import com.qf.CJDX_MANAGER.utils.Constants;
import com.qf.CJDX_MANAGER.utils.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/sys/bill")
public class BillController {

    @Autowired(required = false)
    private ProviderService providerService;
    @Autowired(required = false)
    private BillService billService;

    /**
     * 查询订单列表
     * @param model
     * @param queryProductName
     * @param queryProviderId
     * @param queryIsPayment
     * @param pageIndex
     * @return
     */
    @RequestMapping("/list.html")
    public String getBillList(Model model,
                              @RequestParam(value = "queryProductName",required = false)String queryProductName,
                              @RequestParam(value = "queryProviderId",required = false)String queryProviderId,
                              @RequestParam(value = "queryIsPayment",required = false)String queryIsPayment,
                              @RequestParam(value = "pageIndex", required = false) String pageIndex){
        //设置初始值
        Integer _queryProviderId = null;
        //订单列表
        List<Bill> billList = null;
        //供应商列表
        List<Provider> providerList = null;
        //是否付款
        Integer _queryIsPayment = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //设置当前页
        int currentPageNo = 1;
        //判断接收的产品名
        if (queryProductName == null) {
            queryProductName = "";
        }
        if (queryProviderId != null && !queryProviderId.equals("")) {
            _queryProviderId = Integer.parseInt(queryProviderId);
        }
        if (queryIsPayment != null && !queryIsPayment.equals("")) {
            _queryIsPayment = Integer.parseInt(queryIsPayment);
        }
        //设置当前页
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.parseInt(pageIndex);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        //设置用户的数量
        int totalCount = 0;
        try {
            totalCount = billService.getTotalCount(queryProductName, _queryProviderId,_queryIsPayment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //总页数封装到分页工具类
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        //总页数
        int totalPageCount = pageSupport.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        //查询订单列表
        try {
            billList = billService.getBillList(queryProductName, _queryProviderId,_queryIsPayment, currentPageNo, pageSize);
            providerList = providerService.getAllList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("billList", billList);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);

        return "billlist";
    }
    /**
     * 添加订单的页面
     * @param bill
     * @return
     */
    @RequestMapping(value = "/add.html",method = RequestMethod.GET)
    public String addUser(@ModelAttribute("bill") Bill bill){
        return "billadd";}

    /**
     * 添加订单
     * @param bill
     * @param session
     * @return
     */
    @RequestMapping(value = "/addsave.html",method = RequestMethod.POST)
    public String addRoleSave(Bill bill, HttpSession session){

        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());

        try{
            if(billService.add(bill))
                return "redirect:/sys/bill/list.html";
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "billadd";
    }

    /**
     * 修改回显
     */
    @RequestMapping(value = "/modify/{id}")
    public String getBillById(Model model, @PathVariable String id) {
        Bill bill = new Bill();
        try {
            bill = billService.getBillById(Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(bill);
        return "billmodify";
    }

    /**
     * 动态获取供应商数据
     * @return
     */
    @RequestMapping(value = "/providerlist.json",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public List<Provider> getProviderList(){
        //初始化角色集合
        List<Provider> providerList = null;
        try {
            providerList = providerService.getAllList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return providerList;
    }

    /**
     * 保存修改信息
     * @param bill
     * @param session
     * @return
     */
    @RequestMapping(value = "/modifysave.html",method = RequestMethod.POST)
    public String modifyBillSave(Bill bill, HttpSession session){

        bill.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        try{
            if (billService.modify(bill)){
                return "redirect:/sys/bill/list.html";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "billmodify";
    }

    /**
     * 显示详情
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        Bill bill=new Bill();
        try {
            bill = billService.getBillById(Integer.parseInt(id));

            System.out.println(bill.getProviderId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(bill);
        return "billview";
    }

    /**
     * 删除订单
     * @param id 订单id
     * @return
     */
    @RequestMapping(value = "/delbill.json", method = RequestMethod.GET)
    @ResponseBody
    public Object deleteUser(@RequestParam String id) {
        HashMap<String, String> resultMap = new HashMap<>();
        Bill bill = null;

        if (StringUtils.isNullOrEmpty(id)) {
            resultMap.put("delResult", "notexist");
        } else {
            try {
                //查看订单是否未支付
                bill = billService.getBillById(Integer.parseInt(id));
                //1:未支付
                if (bill.getIsPayment() == 1) {
                    resultMap.put("delResult", "false");
                } else {
                    if (billService.deleteBillById(Integer.parseInt(id))) {
                        resultMap.put("delResult", "true");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

}
