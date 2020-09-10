package cn.smbms.dao.provider;

import cn.smbms.pojo.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderMapper {

    /**
     * 增加供应商
     * @param provider
     * @return
     */
    int add(Provider provider);


    /**
     * 通过供应商名称、编码获取供应商列表-模糊查询-providerList
     * @param proName
     * @return
     */
    List<Provider> getProviderList(@Param("proName") String proName,
                                   @Param("proCode") String proCode);

    /**
     * 通过proId删除Provider
     * @param delId
     * @return
     */
    int deleteProviderById(String delId);


    /**
     * 通过proId获取Provider
     * @param id
     * @return
     */
    Provider getProviderById(String id);

    /**
     * 修改用户信息
     * @return
     */
   int modify(Provider provider);

    /**
     * 判断供应商编码是否存在
     * @param proCode
     * @return
     */
    Provider proCodeExists(String proCode);
}
