package cn.chm.o2o.interceptor.shopadmin;

import cn.chm.o2o.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 店家管理系统登录验证拦截器
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 事前拦截，即用户操作发生前，改写preHandle里面的逻辑，来拦截事情的发生
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 从session中取出用户信息
        Object userObject = request.getSession().getAttribute("user");
        if (userObject != null) {
            // 如果用户信息不为空，则将用户信息转换成PersonInfo类型
            PersonInfo user = (PersonInfo) userObject;
            // 空值判断和权限判断
            if (user != null && user.getUserId() != null && user.getUserId() > 0
                    && user.getEnableStatus() == 1 && user.getUserType() > 1) {
                // 通过验证返回true，拦截器返回true，用户接下来的操作才可以执行
                return true;
            }
        }
        // 若不满足条件，跳转到账号登录页面
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open ('" + request.getContextPath()
                + "/frontend/index','_self')");
        out.println("</script>");
        out.println("</html>");
        // return false就会停止执行shopadmin下的controller里面的代码
        return false;
    }
}
