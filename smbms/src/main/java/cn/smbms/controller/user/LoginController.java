package cn.smbms.controller.user;

import cn.smbms.pojo.User;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录界面
     * @return
     */
    @RequestMapping("/toLogin.html")
    public String toLogin() {
        return "login";
    }

    /**
     * 用户登录
     * @param userCode
     * @param userPassword
     * @param session
     * @return
     */
    @RequestMapping("/doLogin.html")
    public String doLogin(@RequestParam("userCode") String userCode,
                          @RequestParam("userPassword") String userPassword,
                          HttpSession session) {
        //调用service方法，进行用户匹配
        User user = userService.login(userCode);
        if(null == user) {
//            页面跳转（login.jsp）带出提示信息--转发
//            request.getRequestDispatcher("login.jsp").forward(request, response);
            throw new RuntimeException("用户名不存在");
        }else{//用户名存在
            if(!user.getUserPassword().equals(userPassword)){//匹配密码
                throw new RuntimeException("密码输入错误");
            }
            //放入session
            session.setAttribute(Constants.USER_SESSION, user);
            //页面跳转（frame.jsp）
            /*response.sendRedirect("jsp/frame.jsp");*/
            return "redirect:/login/main.html";
        }
    }

    /**
     * 登陆成功,跳转至主页面
     * @param session
     * @return
     */
    @RequestMapping("/main.html")
    public String main(HttpSession session) {
        if (session.getAttribute(Constants.USER_SESSION) == null) {
            return "redirect:/login/toLogin.html";
        }
        return "frame";
    }

    //局部异常处理器
    /*@ExceptionHandler(value = {RuntimeException.class})
    public String handlerException(RuntimeException runtimeException, HttpServletRequest request) {
        request.setAttribute("error", runtimeException);
        return "login";
    }*/

    /**
     * 注销/退出登录
     * @param session
     * @return
     */
    @RequestMapping("/logout.html")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login/toLogin.html";
    }
}
