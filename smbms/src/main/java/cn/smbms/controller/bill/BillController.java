package cn.smbms.controller.bill;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.bill.BillServiceImpl;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private ProviderService providerService;

    @Autowired
    private BillService billService;

    /**
     * 查询订单列表
     * @param queryProductName
     * @param queryProviderId
     * @param queryIsPayment
     * @param model
     * @return
     */
    @RequestMapping(value = "/billList.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String queryBillList(@RequestParam(value = "queryProductName", defaultValue = "") String queryProductName,
                                @RequestParam(value = "queryProviderId", defaultValue = "0") Integer queryProviderId,
                                @RequestParam(value = "queryIsPayment", defaultValue = "0") Integer queryIsPayment, Model model) {
        //查询供应商列表
        List<Provider> providerList = providerService.getProviderList("", "");
        model.addAttribute("providerList", providerList);

        Bill bill = new Bill();
        bill.setProductName(queryProductName);
        bill.setProviderId(queryProviderId);
        bill.setIsPayment(queryIsPayment);

        //查询订单列表
        List<Bill> billList = billService.getBillList(bill);

        model.addAttribute("billList", billList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        return "billlist";
    }

    /**
     * 跳转至添加订单界面
     * @param bill
     * @return
     */
    @RequestMapping("/addBill.html")
    public String addBill(@ModelAttribute("bill") Bill bill) {
        return "billadd";
    }

    /**
     * 添加订单
     * @param bill
     * @param result
     * @return
     */
    @RequestMapping(value = "/addBillSave.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String addBillSave(@ModelAttribute("bill") @Valid Bill bill, BindingResult result,HttpSession session) {

        if (result.hasErrors()) {
            return "billadd";
        }
        bill.setCreationDate(new Date());
        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        boolean flag = billService.add(bill);
        if (flag) {
            return "redirect:/bill/billList.html";
        } else {
            return "billadd";
        }
    }

    /**
     * 跳转至修改订单页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/toBillModify.html")
    public String getBillById(@RequestParam("billid") String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Bill bill = billService.getBillById(id);
            model.addAttribute("bill", bill);
            return "billmodify";
        } else {
            throw new RuntimeException("您访问的数据不存在");
        }
    }

    /**
     * 修改订单
     * @param bill
     * @param request
     * @return
     */
    @RequestMapping("/billModifySave.html")
    public String billModifySave(Bill bill, HttpServletRequest request) {

        bill.setModifyBy(((User) request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        boolean flag = billService.modify(bill);
        if (flag) {
            return "redirect:/bill/billList.html";
        } else {
            return "billmodify";
        }
    }

    /**
     * 查看订单信息
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/billView/{id}")
    public String viewBill(@PathVariable String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Bill bill = billService.getBillById(id);
            model.addAttribute("bill", bill);
            return "billview";
        } else {
            throw new RuntimeException("您访问的数据不存在");
        }
    }

    /**
     * 通过Ajax查看订单信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/billView.json", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String view(String id) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Bill bill = billService.getBillById(id);
            return JSON.toJSONString(bill);
        } else {
            return "null";
        }
    }

    /**
     * 通过Ajax获取供应商列表
     *
     * @return
     */
    @RequestMapping("/getProviderList.json")
    @ResponseBody
    public String getProviderList() {
        List<Provider> providerList = new ArrayList<Provider>();
        providerList = providerService.getProviderList("", "");

        //把providerList转换成json对象,返回
        return JSONArray.toJSONString(providerList);
    }

    @RequestMapping("/delBill.json")
    @ResponseBody
    public String delBill(@RequestParam("billid") String id) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(id)) {
            boolean flag = billService.deleteBillById(id);
            if (flag) {//删除成功
                resultMap.put("delResult", "true");
            } else {//删除失败
                resultMap.put("delResult", "false");
            }
        } else {
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象,返回
        return JSONArray.toJSONString(resultMap);
    }
}
