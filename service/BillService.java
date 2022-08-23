package com.qf.CJDX_MANAGER.service;

import com.qf.CJDX_MANAGER.entity.Bill;

import java.util.List;

public interface BillService {
    /**
     * 获取订单的数量
     * @param queryProductName
     * @param queryProviderId
     * @return
     */
    int getTotalCount(String queryProductName, Integer queryProviderId,Integer queryIsPayment);
    /**
     * 获取订单列表
     * @param queryProductName
     * @param queryProviderId
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<Bill> getBillList(String queryProductName, Integer queryProviderId, Integer queryIsPayment, int currentPageNo, int pageSize);
    /**
     * 根据id查询账单
     * @param id
     * @return
     */
    Bill getBillById(int id);
    /**
     * 修改保存业务
     * @param bill
     * @return
     */
    boolean modify(Bill bill);
    /**
     * 添加订单
     * @param bill
     * @return
     */
    boolean add(Bill bill);
    /**
     * 删除订单
     * @param id
     * @return
     */
    boolean deleteBillById(int id);
}
