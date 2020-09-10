package cn.smbms.Interceptor;

import cn.smbms.pojo.User;
import cn.smbms.tools.Constants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SysInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/error.jsp");
            return false;   //拦截请求,重定向到error页面
        }
        return true;    //放行请求,进入控制器的处理方法
    }
}
