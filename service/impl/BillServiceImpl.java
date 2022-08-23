package com.qf.CJDX_MANAGER.service.impl;

import com.qf.CJDX_MANAGER.entity.Bill;
import com.qf.CJDX_MANAGER.mapper.BillMapper;
import com.qf.CJDX_MANAGER.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BillServiceImpl implements BillService {

    @Autowired(required = false)
    private BillMapper billMapper;

    /**
     * 总条数查询
     * @param queryProductName
     * @param queryProviderId
     * @param queryIsPayment
     * @return
     */
    @Override
    public int getTotalCount(String queryProductName, Integer queryProviderId,Integer queryIsPayment) {
        return billMapper.getTotalCount(queryProductName,queryProviderId,queryIsPayment);
    }

    /**
     * 模糊查询订单列表
     * @param queryProductName
     * @param queryProviderId
     * @param queryIsPayment
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<Bill> getBillList(String queryProductName, Integer queryProviderId,Integer queryIsPayment, int currentPageNo, int pageSize) {
        currentPageNo=(currentPageNo-1)*pageSize;
        return billMapper.getBillList(queryProductName,queryProviderId,queryIsPayment,currentPageNo,pageSize);
    }

    /**
     * 查询订单
     * @param id
     * @return
     */
    @Override
    public Bill getBillById(int id) {
        return billMapper.getBillById(id);
    }

    /**
     * 修改保存订单
     * @param bill
     * @return
     */
    @Override
    public boolean modify(Bill bill) {
        boolean flag = false;
        if (billMapper.modify(bill) > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 添加订单
     * @param bill
     * @return
     */
    @Override
    public boolean add(Bill bill) {
        boolean flag = false;
        System.out.println("bill==="+bill);
        if (billMapper.add(bill)>0){
            flag = true;
        }
        return flag;
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    @Override
    public boolean deleteBillById(int id) {
        boolean flag = false;
        if (billMapper.deleteBillById(id) > 0)
            flag = true;

        return flag;
    }
}
