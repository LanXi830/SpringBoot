package com.qf.CJDX_MANAGER.service;

import com.qf.CJDX_MANAGER.entity.Provider;

import java.util.List;


public interface ProviderService {

    /**
     * 查询总页数
     * @param queryProCode
     * @param queryProName
     * @return
     */
    int getTotalCount(String queryProCode, String queryProName);

    /**
     * 查询供应商列表
     * @param queryProCode
     * @param queryProName
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<Provider> getProviderList(String queryProCode, String queryProName, int currentPageNo, int pageSize);


    /**
     * 添加狗供应商
     * @param provider
     * @return
     */
    boolean add(Provider provider);

    /**
     * 删除供应商
     * @param id
     * @return
     */
    boolean deleteProviderById(int id);

    Provider getProviderById(int id);

    /**
     * 保存修改商家
     * @param provider
     * @return
     */
    boolean modify(Provider provider);

    //直接获取所以列表
    List<Provider> getAllList();
}
