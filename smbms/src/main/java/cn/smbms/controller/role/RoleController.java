package cn.smbms.controller.role;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @RequestMapping("/roleList.html")
    public String getRoleList(@RequestParam(value = "queryRoleCode",defaultValue = "") String queryRoleCode,
                              @RequestParam(value = "queryRoleName",defaultValue = "") String queryRoleName,Model model){

        List<Role> roleList = roleService.queryRoleList(queryRoleCode,queryRoleName);
        model.addAttribute("roleList",roleList);
        model.addAttribute("queryRoleCode",queryRoleCode);
        model.addAttribute("queryRoleName",queryRoleName);
        return "rolelist";
    }

    @RequestMapping("/addRole.html")
    public String addRole(@ModelAttribute("role") Role role){
        return "roleadd";
    }

    @RequestMapping("/addRoleSave.html")
    public String addRoleSave(@ModelAttribute("role") Role role, HttpSession session){

        role.setCreationDate(new Date());
        role.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        if(roleService.addRole(role) == 1){
            return "redirect:/role/roleList.html";
        }
        return "roleadd";
    }

    @RequestMapping(value = "/roleView.json",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String view(String id){
        if(!StringUtils.isNullOrEmpty(id)){
            Role role = roleService.getRoleById(id);
            return JSON.toJSONString(role);
        }else{
            return "null";
        }
    }

    @RequestMapping("/toRoleModify.html")
    public String modify(@RequestParam("roleid") String id,Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            Role role = roleService.getRoleById(id);
            model.addAttribute("role",role);
            return "rolemodify";
        }else{
            throw new RuntimeException("您访问的数据不存在");
        }
    }

    @RequestMapping("/roleModifySave.html")
    public String modifySave(Role role,HttpSession session){
        role.setModifyBy(((Role)session.getAttribute(Constants.USER_SESSION)).getId());
        role.setModifyDate(new Date());

        if(roleService.modify(role) >= 1){
            return "redirect:/role/roleList.html";
        }else{
            return "rolemodify";
        }
    }

    @RequestMapping("/delRole.json")
    public String delRole(@RequestParam("roleid") String id){
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e){
            delId = 0;
        }
        HashMap<String ,String> hashMap = new HashMap<>();
        if(delId < 0){
            hashMap.put("delResult","notExist");
        }else{
            if(roleService.deleteRole(id) >= 1){
                hashMap.put("delResult","true");
            }else{
                hashMap.put("delResult","false");
            }
        }
        return JSON.toJSONString(hashMap);
    }
}
