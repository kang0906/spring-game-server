package com.example.game.config.log.access;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccessLogger {

    Logger log = LoggerFactory.getLogger("ACCESS");

    private String getURL(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRequestURL());

        String queryString = request.getQueryString();
        if (queryString != null) {
            sb.append("?").append(queryString);
        }
        return sb.toString();
    }

    public void log(HttpServletRequest request, HttpServletResponse response, long elapsed) {
        String accessLog = buildAccessLog(request, response, elapsed);
        log.info(accessLog);
//        log.info("\"host\": {}, method: {}, url: {}, status: {}, elapsed: {}", remoteHost, method, url, status, elapsed);
    }

    private String buildAccessLog(HttpServletRequest request, HttpServletResponse response, long elapsed) {

//        String remoteHost = request.getRemoteHost(); // ip 주소 조회 방식 수정 https://m.blog.naver.com/bb_/222844419943
        String remoteHost = getClientIP(request);
        String method = request.getMethod();
        String url = getURL(request);
        Long userId = (Long)request.getAttribute("userId");
        int status = response.getStatus();

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (remoteHost != null) {
            sb
                    .append("\"").append("remoteHost").append("\"")
                    .append(":")
                    .append("\"").append(remoteHost).append("\"");
        }
        if (method != null) {
            sb
                    .append(",")
                    .append("\"").append("method").append("\"")
                    .append(":")
                    .append("\"").append(method).append("\"");
        }
        if (url != null) {
            sb
                    .append("\"").append("url").append("\"")
                    .append(":")
                    .append("\"").append(url).append("\"");
        }
        if (status != 0) {
            sb
                    .append(",")
                    .append("\"").append("status").append("\"")
                    .append(":")
                    .append("\"").append(status).append("\"");
        }
        if (userId != null) {
            sb
                    .append(",")
                    .append("\"").append("userId").append("\"")
                    .append(":")
                    .append("\"").append(userId).append("\"");
        }
        if (elapsed != 0) {
            sb
                    .append(",")
                    .append("\"").append("elapsed").append("\"")
                    .append(":")
                    .append(elapsed);
        }
        sb.append("}");
        return sb.toString();
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
