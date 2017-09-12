<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="java.util.Random" %>
<%@ page import="java.lang.Math" %>
<%@ page import="java.io.*" %>

<%
    /* 响应时间设置 */
    float max = 110;     //单位：毫秒
    float min = 90;

    String maxStr = request.getParameter("max");
    String minStr = request.getParameter("min");

    if (null != maxStr && !"".equalsIgnoreCase(maxStr)) {
        max = Float.valueOf(maxStr);
    }

    if (null != minStr && !"".equalsIgnoreCase(minStr)) {
        min = Float.valueOf(minStr);
    }

    Random random = new Random();
    float s = random.nextFloat() * (max - min) + min;
    Thread.sleep(Math.round(s));


    String respSize = request.getParameter("size");

    boolean sizeFlag = (null == respSize || "".equalsIgnoreCase(respSize));

    byte[] bytes = null;
    int len;

    if (!sizeFlag) {
        len = Integer.valueOf(respSize);
        bytes = new byte[len];
    } else {
        InputStream inputStream = request.getInputStream();
        bytes = new byte[4 * 1024];
        len = inputStream.read(bytes);

        /* 避免不传size并且请求体为空时,len赋值为-1 */
        if (-1 == len) {
            len = 0;
        }
    }

    OutputStream os = response.getOutputStream();
    os.write(bytes, 0, len);
    os.flush();

    response.flushBuffer();
    out.clear();
    out = pageContext.pushBody();
%>