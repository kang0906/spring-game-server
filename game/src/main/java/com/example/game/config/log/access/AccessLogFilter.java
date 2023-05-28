package com.example.game.config.log.access;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@WebFilter
//@WebFilter(urlPatterns = {"/api/*"}, asyncSupported = true)
@RequiredArgsConstructor
public class AccessLogFilter extends OncePerRequestFilter {

    private final AccessLogger accessLogger;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        long stime = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long etime = System.currentTimeMillis();
            long elapsed = etime - stime;
            accessLogger.log(request, response, elapsed);
        }
    }
}
