package cn.smbms.controller.user;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.BaseController;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
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
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 查询用户列表并分页
     * @param queryUserName
     * @param queryUserRole
     * @param currentPageNo
     * @param model
     * @return
     */
    @RequestMapping(value = "/userList.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String queryUserList(@RequestParam(value = "queryName", defaultValue = "") String queryUserName,
                                @RequestParam(value = "queryUserRole", defaultValue = "0") Integer queryUserRole,
                                @RequestParam(value = "pageIndex", defaultValue = "1") Integer currentPageNo, Model model) {
        //查询用户列表
        List<User> userList = null;

        //设置页面容量
        int pageSize = Constants.pageSize;

        //总数量（表）
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);

        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        userList = userService.getUserList(queryUserName, queryUserRole, (currentPageNo - 1)* pageSize, pageSize);

        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("userList", userList);
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "userlist";
    }

    /**
     * 跳转至添加用户界面
     * @param user
     * @return
     */
    @RequestMapping("/addUser.html")
    public String addUser(@ModelAttribute("user") User user) {
        return "useradd";
    }

    /**
     * 添加用户
     * @param user
     * @param session
     * @return
     */
    @RequestMapping("/addUserSave.html")
    public String addUserSave(@ModelAttribute("user") @Valid User user, BindingResult result, HttpSession session,
                              @RequestParam(value = "a_idPicPath", required = false) MultipartFile multipartFile,
                              @RequestParam(value = "a_workPicPath", required = false) MultipartFile workMultipartFile,
                              Model model) {

        String savePath = null;
        if (!multipartFile.isEmpty()) {//上传准备工作
            //获取文件的原名和大小
            String oldName = multipartFile.getOriginalFilename();
            //获取文件的后缀名
            String ext = FilenameUtils.getExtension(oldName);
            long size = multipartFile.getSize();
            //上传图片大小不能超过500KB
            if (size > 500 * 1024) {
                model.addAttribute("uploadFileError", "上传图片的大小不能超过500KB");
                return "useradd";
            } else {
                String[] types = {"jpg", "gif", "png", "jpeg"};
                if (!Arrays.asList(types).contains(ext)) {
                    model.addAttribute("uploadFileError", "上传的图片格式不满足要求,必须是jpg,gif,png,jpeg格式");
                    return "useradd";
                } else {
                    //正式上传
                    String targetPath = session.getServletContext().getRealPath("staticResource" + File.separator + "upload");
                    //修改上传文件名
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt(100000) + "_Person." + ext;
                    File saveFile = new File(targetPath, fileName);
                    if (!saveFile.exists()) {
                        saveFile.mkdirs();
                    }
                    try {
                        multipartFile.transferTo(saveFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("uploadFileError", "上传失败,请及时联系管理员");
                        return "useradd";
                    }
                    savePath = targetPath + File.separator + fileName;
                }
            }
        }

        String workSavePath = null;
        if (!workMultipartFile.isEmpty()) {
            //获取上传文件的原名和文件大小
            String oldName = workMultipartFile.getOriginalFilename();
            //获取上传文件的后缀名
            String ext = FilenameUtils.getExtension(oldName);
            long size = workMultipartFile.getSize();
            if (size > 500 * 1024) {
                model.addAttribute("uploadWorkFileError", "上传图片的大小不能超过500KB");
                return "useradd";
            } else {
                String[] types = {"jpg", "gif", "png", "jpeg"};
                if (!Arrays.asList(types).contains(ext)) {
                    model.addAttribute("uploadWorkFileError", "上传的图片格式不满足要求,必须是jpg,gif,png,jpeg格式");
                    return "useradd";
                } else {
                    //正式上传
                    String targetFile = session.getServletContext().getRealPath("staticResource" + File.separator + "upload");
                    //修改上传的文件名
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt(100000) + "_Person." + ext;
                    File file = new File(targetFile, fileName);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    try {
                        workMultipartFile.transferTo(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("uploadWorkFileError", "上传失败,请及时联系管理员");
                        return "useradd";
                    }
                    workSavePath = targetFile + File.separator + fileName;
                }
            }
        }
        if (result.hasErrors()) {
            return "useradd";
        }
        user.setCreationDate(new Date());
        user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        user.setIdPicPath(savePath);
        user.setWorkPicPath(workSavePath);
        if (userService.add(user)) {
            return "redirect:/user/userList.html";
        } else {
            return "useradd";
        }
    }

    /**
     * 跳转至修改用户界面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/toUserModify.html")
    public String getUserById(@RequestParam("uid") String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "usermodify";
        } else {
            throw new RuntimeException("您访问的数据不存在");
        }
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @RequestMapping("/modifyUserSave.html")
    public String modifySave(User user, HttpSession session) {
        user.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        if (userService.modify(user)) {
            return "redirect:/user/userList.html";
        } else {
            return "usermodify";
        }
    }

    /**
     * 查看用户信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/userView/{id}")
    public String viewUser(@PathVariable String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "userview";
        } else {
            throw new RuntimeException("您访问的数据不存在");
        }
    }

    /**
     * 通过Ajax查看用户信息(也可以通过配置springmvc-servlet.xml中的消息转换器解决乱码问题)
     * @param id
     * @return
     */
    @RequestMapping(value = "/userView.json", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String view(String id) {
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            return JSON.toJSONString(user);
        } else {
            return "null";
        }
    }

    /**
     * 判断用户编码是否存在
     * @param userCode
     * @return
     */
    @RequestMapping("/userCodeExists.html")
    @ResponseBody
    public String userCodeExists(@RequestParam(value = "userCode", defaultValue = "") String userCode) {

        //判断用户账号是否可用
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        } else {
            User user = userService.selectUserCodeExist(userCode);
            if (null != user) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "notExist");
            }
        }
        //把resultMap转为json字符串
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 跳转至修改密码界面
     * @return
     */
    @RequestMapping("/passwordModify.html")
    public String passModify(){
        return "pwdmodify";
    }

    /**
     * 根据用户ID获取旧密码
     * @param oldpassword
     * @return
     */
    @RequestMapping("/getPassByUserId.json")
    @ResponseBody
    public String getPasswordByUserId(@RequestParam("oldpassword") String oldpassword, HttpServletRequest request) {
        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
        Map<String, String> resultMap = new HashMap<String, String>();

        if (null == o) {//session过期
            resultMap.put("result", "sessionerror");
            return "login";
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {//旧密码输入为空
            resultMap.put("result", "error");
        } else {
            String sessionPwd = ((User) o).getUserPassword();
            if (oldpassword.equals(sessionPwd)) {
                resultMap.put("result", "true");
            } else {//旧密码输入不正确
                resultMap.put("result", "false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 修改密码
     * @param newpassword
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/updatePassword.html")
    public String updatePassword(@RequestParam("newpassword") String newpassword, HttpSession session, Model model) {

        Object o = session.getAttribute(Constants.USER_SESSION);
        boolean flag = false;
        if (o != null && !StringUtils.isNullOrEmpty(newpassword)) {
            flag = userService.updatePwd(((User) o).getId(), newpassword);
            if (flag) {
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                session.removeAttribute(Constants.USER_SESSION);//session注销
                return "redirect:/login/toLogin.html";
            } else {
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
                return "pwdmodify";
            }
        } else {
            model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            return "pwdmodify";
        }
    }

    /**
     * 通过Ajax获取用户列表
     * @return
     */
    @RequestMapping("/getRoleList.json")
    @ResponseBody
    public String getRoleList(){
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        //把roleList转换成json对象输出
        return JSONArray.toJSONString(roleList);
    }

    /**
     * 删除用户
     * @return
     */
    @RequestMapping("/delUser.json")
    @ResponseBody
    public String delUser(@RequestParam("uid") String id){
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            // TODO: handle exception
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notExist");
        }else{
            if(userService.deleteUserById(delId)){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }
        //把resultMap转换成json对象,返回
        return JSONArray.toJSONString(resultMap);
    }
}
