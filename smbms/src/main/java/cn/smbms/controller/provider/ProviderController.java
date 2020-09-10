package cn.smbms.controller.provider;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    /**
     * 查询供应商列表
     * @param queryProName
     * @param queryProCode
     * @param model
     * @return
     */
    @RequestMapping(value = "/providerList.html",method = {RequestMethod.GET,RequestMethod.POST})
    public String queryProviderList(@RequestParam(value = "queryProName", defaultValue = "") String queryProName,
                                    @RequestParam(value = "queryProCode", defaultValue = "") String queryProCode, Model model) {

        List<Provider> providerList = providerService.getProviderList(queryProName, queryProCode);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);
        return "providerlist";
    }

    /**
     * 跳转至添加供应商界面
     * @return
     */
    @RequestMapping("/addProvider.html")
    public String addProvider(@ModelAttribute("provider") Provider provider){
        return "provideradd";
    }

    /**
     *添加供应商
     * @param provider
     * @param session
     * @return
     */
    @RequestMapping("/addProviderSave.html")
    public String addProviderSave(@ModelAttribute("provider") @Valid Provider provider, BindingResult result, HttpSession session,
                                  @RequestParam(value = "a_idPicPath",required = false) MultipartFile multipartFile,
                                  @RequestParam(value = "a_workPicPath",required = false) MultipartFile workMultipartFile,
                                  Model model){
        //上传组织机构代码证
        String saveFile = null;
        if(!multipartFile.isEmpty()){
            //获取上传文件的原名和大小
            String oldName = multipartFile.getOriginalFilename();
            //获取上传文件的后缀名
            String ext = FilenameUtils.getExtension(oldName);
            long size = multipartFile.getSize();
            if(size > 500*1024){
                model.addAttribute("uploadFileError","上传图片的大小不能超过500KB");
                return "provideradd";
            }else{
                String[] types = {"jpg","png","gif","jpeg"};
                if(!Arrays.asList(types).contains(ext)){
                    model.addAttribute("uploadFileError","上传的图片格式必须是jpg,png,gif,jpeg");
                    return "provideradd";
                }else{//正式上传
                    String targetFile = session.getServletContext().getRealPath("staticResource"+ File.separator +"upload");
                    String fileName = System.currentTimeMillis()+ RandomUtils.nextInt(100000)+"_Person."+ext;
                    File file = new File(targetFile,fileName);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    try {
                        multipartFile.transferTo(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("uploadFileError","上传失败,请及时联系管理员");
                        return "provideradd";
                    }
                    saveFile = targetFile+File.separator+fileName;
                }
            }
        }
        //上传企业营业执照
        String workSavePath = null;
        if(!workMultipartFile.isEmpty()){
            //获取上传文件的原名和大小
            String oldName = workMultipartFile.getOriginalFilename();
            //获取上传文件的后缀名
            String ext = FilenameUtils.getExtension(oldName);
            long size = workMultipartFile.getSize();
            if(size > 500*1024){
                model.addAttribute("uploadWorkFileError","上传图片的大小不能超过500KB");
                return "provideradd";
            }else{
                String[] types = {"jpg","png","gif","jpeg"};
                if(!Arrays.asList(types).contains(ext)){
                    model.addAttribute("uploadWorkFileError","上传图片的格式必须是jpg,png,gif,jpeg");
                    return "provideradd";
                }else{//正式上传
                    String targetFile = session.getServletContext().getRealPath("staticResource"+File.separator+"upload");
                    String fileName = System.currentTimeMillis()+RandomUtils.nextInt(100000)+"_Person."+ext;
                    File file = new File(targetFile,fileName);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    try {
                        workMultipartFile.transferTo(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("uploadWorkFileError","上传失败,请及时联系管理员");
                        return "provideradd";
                    }
                    workSavePath = targetFile+File.separator+fileName;
                }
            }
        }
        if(result.hasErrors()){
            return "provideradd";
        }
        provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        provider.setIdPicPath(saveFile);
        provider.setWorkPicPath(workSavePath);
        boolean flag = providerService.add(provider);
        if(flag){
            return "redirect:/provider/providerList.html";
        }else{
            return "provideradd";
        }
    }

    /**
     * 跳转至供应商修改页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/toProviderModify.html")
    public String getProviderById(@RequestParam("proid") String id, Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            Provider provider =  providerService.getProviderById(id);
            model.addAttribute("provider", provider);
            return "providermodify";
        }else{
            throw new RuntimeException("您访问的数据不存在");
        }
    }

    /**
     * 修改供应商列表
     * @param provider
     * @param request
     * @return
     */
    @RequestMapping("/providerModifySave.html")
    public String providerModifySave(Provider provider, HttpServletRequest request){
        provider.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        boolean flag = providerService.modify(provider);
        if(flag){
            return "redirect:/provider/providerList.html";
        }else{
            return "providermodify";
        }
    }

    /**
     * 查看供应商信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/providerView/{id}")
    public String viewProvider(@PathVariable String id,Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            Provider provider =  providerService.getProviderById(id);
            model.addAttribute("provider", provider);
            return "providerview";
        }else{
            throw new RuntimeException("您访问的数据不存在");
        }
    }

    /**
     * 通过Ajax查看供应商信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/providerView.json",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String view(String id){
        if(!StringUtils.isNullOrEmpty(id)){
            Provider provider =  providerService.getProviderById(id);
            return JSON.toJSONString(provider);
        }else{
            return "null";
        }
    }
    /**
     * 判断供应商编码是否存在
     * @param proCode
     * @return
     */
    @RequestMapping("proCodeExists.html")
    @ResponseBody
    public String proCodeExists(@RequestParam(value = "proCode",defaultValue = "") String proCode){

        HashMap<String,String> resultMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(proCode)){
            resultMap.put("proCode","empty");
        }else{
            Provider provider = providerService.selectProCodeExists(proCode);
            if(null != provider){
                resultMap.put("proCode","exist");
            }else {
                resultMap.put("proCode","notExist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 删除供应商
     * @param id
     * @return
     */
    @RequestMapping("/delProvider.json")
    @ResponseBody
    public String delProvider(@RequestParam("proid") String id){
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            int flag = providerService.deleteProviderById(id);
            if(flag == 0){//删除成功
                resultMap.put("delResult", "true");
            }else if(flag == -1){//删除失败
                resultMap.put("delResult", "false");
            }else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象,返回
       return JSONArray.toJSONString(resultMap);
    }
}
