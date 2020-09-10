package cn.smbms.service.bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.bill.BillDao;
import cn.smbms.dao.bill.BillDaoImpl;
import cn.smbms.dao.bill.BillMapper;
import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("billService")
@Transactional
public class BillServiceImpl implements BillService {

	/*@Autowired
	private BillDao billDao;*/

    @Autowired
    private BillMapper billMapper;

    /*public BillServiceImpl(){
        billDao = new BillDaoImpl();
    }*/
    @Override
    public boolean add(Bill bill) {
        boolean flag = false;
        if (billMapper.add(bill) > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Bill> getBillList(Bill bill) {
        List<Bill> billList = billMapper.getBillList(bill);
        return billList;
    }

    @Override
    public boolean deleteBillById(String delId) {
        boolean flag = false;
        if (billMapper.deleteBillById(delId) > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Bill getBillById(String id) {
        Bill bill = billMapper.getBillById(id);
        return bill;
    }

    @Override
    public boolean modify(Bill bill) {
        boolean flag = false;
        if (billMapper.modify(bill) > 0) {
            flag = true;
        }
        return flag;
    }

}
