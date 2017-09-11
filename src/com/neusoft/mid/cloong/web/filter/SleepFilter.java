package com.neusoft.mid.cloong.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * 测试模拟延时filter
 *
 * @author <a href="mailto:li-zr@neusoft.com">li-zr</a>
 *         2017/9/10 15:26
 */
public class SleepFilter implements Filter {

    Map<String, String> propMap;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        System.out.println("延时filter初始化开始...");

        Enumeration<String> enumeration = filterConfig.getInitParameterNames();

        propMap = new HashMap<String, String>();

        while (enumeration.hasMoreElements()) {
            String url = enumeration.nextElement();
            propMap.put(url, filterConfig.getInitParameter(url));
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        /* 过滤url */
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();

        System.out.println("拦截到请求uri: " + uri);

        boolean shouldHoldUp = false;
        String key = "";

        for (String iterator : propMap.keySet()) {
            if (uri.contains(iterator + ".action")) {
                shouldHoldUp = true;
                key = iterator;
                break;
            }
        }

        /* 所有过滤的uri里不包含当前请求的uri，则直接跳过 */
        if (!shouldHoldUp) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            System.out.println("请求uri: " + uri + "被过滤.");
            /* 计算随机时间 */
            String timeStr = propMap.get(key);

            float min = Float.valueOf(timeStr.split(",")[0]);
            float max = Float.valueOf(timeStr.split(",")[1]);

            Random random = new Random();
            float s = random.nextFloat() * (max - min) + min;

            System.out.println("睡眠时间： " + s + "毫秒.");

            /* 执行等待 */
            try {
                Thread.sleep(Math.round(s));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* 继续执行请求 */
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {
        /* do nothing */
    }
}
