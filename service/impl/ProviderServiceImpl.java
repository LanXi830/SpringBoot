package com.qf.CJDX_MANAGER.service.impl;


import com.qf.CJDX_MANAGER.entity.Provider;
import com.qf.CJDX_MANAGER.mapper.ProviderMapper;
import com.qf.CJDX_MANAGER.service.ProviderService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class ProviderServiceImpl implements ProviderService {

    @Resource
    private ProviderMapper providerMapper;

    /**
     * 查询总记录数
     * @param queryProCode
     * @param queryProName
     * @return
     */
    @Override
    public int getTotalCount(String queryProCode, String queryProName) {
        return providerMapper.getTotalCount(queryProName,queryProCode);
    }

    /**
     * 查询供应商列表
     * @param queryProCode
     * @param queryProName
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<Provider> getProviderList(String queryProCode, String queryProName, int currentPageNo, int pageSize) {
        //计算分页的起始索引
        currentPageNo =(currentPageNo-1)* pageSize;

        return providerMapper.getProviderList(queryProName,queryProCode,currentPageNo,pageSize);
    }

    @Override
    public boolean add(Provider provider) {
        boolean flag = false;
        if (providerMapper.add(provider)>0){
            flag=true;
        }
        return flag;
    }

    @Override
    public boolean deleteProviderById(int id) {
        boolean flag = false;
        if (providerMapper.deleteProviderById(id)>0){
            flag=true;
        }
        return flag;
    }

    @Override
    public Provider getProviderById(int id) {
        return providerMapper.getProviderById(id);
    }

    @Override
    public boolean modify(Provider provider) {
        boolean flag = false;
        if (providerMapper.modify(provider) > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Provider> getAllList() {
        return providerMapper.getAllList();
    }


}
