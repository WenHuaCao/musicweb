package buaa.cs.caowenhua.musicweb.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author CaoWenHua
 * @create 2021-06-25  10:10
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        HttpSession session = request.getSession();
        //判断当前session中是否有用户的id
        Object User = session.getAttribute("userid");
        if(User != null){
            return true;
        }
        /**
         * 转发到主页，并提示错误
         */
        System.out.println(request.getRequestURI() );
        request.setAttribute("ErrorMessage","当前未登录,请先登录!");
        request.getRequestDispatcher("/login").forward(request,response);
        return false;
    }
}
