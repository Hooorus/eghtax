package cn.calendo.filter;

import cn.calendo.common.BaseContext;
import cn.calendo.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已完成登录
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的uri
        String requestURI = request.getRequestURI();

        log.info("拦截到请求: {}", requestURI);

        //2.定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/favicon.ico",
                "front/**",
                "common/**",
                "/user/**"
        };

        //3.判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //请求合法，不需要处理
        if (check) {
            log.info("放行{}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //后台：请求不合法，需要处理，判断用户是否登录，如果登录，那就放行；如果未登录，那就通过输出流的方式向客户端页面响应数据
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，id为{}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }
        //前台：请求不合法，需要处理，判断用户是否登录，如果登录，那就放行；如果未登录，那就通过输出流的方式向客户端页面响应数据
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，id为{}", request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行，匹配的上与否
     *
     * @param urls
     * @param requestURI
     * @return boolean
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
