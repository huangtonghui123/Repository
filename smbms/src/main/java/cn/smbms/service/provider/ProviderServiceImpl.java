package cn.smbms.service.provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.bill.BillDao;
import cn.smbms.dao.bill.BillDaoImpl;
import cn.smbms.dao.bill.BillMapper;
import cn.smbms.dao.provider.ProviderDao;
import cn.smbms.dao.provider.ProviderDaoImpl;
import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("providerService")
public class ProviderServiceImpl implements ProviderService {

	/*@Resource
	private ProviderDao providerDao;

	@Resource
	private BillDao  billDao;*/

    @Autowired
    private ProviderMapper providerMapper;

    @Autowired
    private BillMapper billMapper;

    /*public ProviderServiceImpl(){
        providerDao = new ProviderDaoImpl();
        billDao = new BillDaoImpl();
    }*/

    @Override
    public boolean add(Provider provider) {
        boolean flag = false;
        if (providerMapper.add(provider) > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
	@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Provider> getProviderList(String proName, String proCode) {
        List<Provider> providerList = providerMapper.getProviderList(proName, proCode);
        return providerList;
    }

    /**
     * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
     * 若订单表中无该供应商的订单数据，则可以删除
     * 若有该供应商的订单数据，则不可以删除
     * 返回值billCount
     * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
     * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
     * <p>
     * ---判断
     * 如果billCount = -1 失败
     * 若billCount >= 0 成功
     */
    @Override
    public int deleteProviderById(String delId) {
        int billCount = -1;
        billCount = billMapper.getBillCountByProviderId(delId);
        if (billCount == 0) {
            providerMapper.deleteProviderById(delId);
        }
        return billCount;
    }

    @Override
	@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Provider getProviderById(String id) {
        Provider provider = providerMapper.getProviderById(id);
        return provider;
    }

    @Override
    public boolean modify(Provider provider) {
        boolean flag = false;
        if (providerMapper.modify(provider) > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
	@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Provider selectProCodeExists(String proCode) {
        Provider provider = providerMapper.proCodeExists(proCode);
        return provider;
    }

}
