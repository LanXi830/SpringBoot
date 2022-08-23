package com.qf.CJDX_MANAGER.mapper;

import com.qf.CJDX_MANAGER.entity.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderMapper {

    /**
     * 总页数
     * @param queryProName
     * @param queryProCode
     * @return
     */
    int getTotalCount(@Param("queryProName") String queryProName,@Param("queryProCode") String queryProCode);

    /**
     * 查询
     * @param queryProName
     * @param queryProCode
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<Provider> getProviderList(@Param("queryProName") String queryProName,
                                   @Param("queryProCode") String queryProCode,
                                   @Param("currentPageNo") int currentPageNo,
                                   @Param("pageSize") int pageSize);

    /**
     * 添加
     * @param provider
     * @return
     */
    int add(Provider provider);

    int deleteProviderById(int id);

    Provider getProviderById(int id);

    int modify(Provider provider);

    List<Provider> getAllList();

}
