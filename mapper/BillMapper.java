package com.qf.CJDX_MANAGER.mapper;


import com.qf.CJDX_MANAGER.entity.Bill;

import java.util.List;

public interface BillMapper {

    /**
     * 获取账单页数
     * @param queryProductName
     * @param queryProviderId
     * @param queryIsPayment
     * @return
     */
    int getTotalCount(String queryProductName, Integer queryProviderId, Integer queryIsPayment);


    /**
     * 账单查询业务
     * @param queryProductName
     * @param queryProviderId
     * @param queryIsPayment
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<Bill> getBillList(String queryProductName, Integer queryProviderId, Integer queryIsPayment, int currentPageNo, int pageSize);

    /**
     * 修改回显数据
     * @param id
     * @return
     */
    Bill getBillById(int id);

    /**
     * 修改保存
     * @param bill
     * @return
     */
    int modify(Bill bill);

    /**
     * 添加订单
     * @param bill
     * @return
     */
    int add(Bill bill);

    /**
     * 删除订单
     * @param id
     * @return
     */
    int deleteBillById(int id);
}
