package com.example.game.config.jwt;

import com.example.game.config.UserDetailsImpl;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.example.game.config.jwt.JwtProperties.*;


@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public JwtAuthorizationFilter(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtTokenUtils jwtTokenUtils) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        log.info("인증이나 권한이 필요한 주소 요청이 됨");
        String jwtHeader = getHeader(request, response, chain);
        if (jwtHeader == null) {
            return;
        }

        String jwtToken = getJwtToken(jwtHeader);
        String email = jwtTokenUtils.extractUserEmail(jwtToken);
        checkUser(request, email);
        chain.doFilter(request, response);
    }

    public static String getHeader(HttpServletRequest request, HttpServletResponse response,
                                   FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader(HEADER_STRING);
        if (jwtHeader == null || !jwtHeader.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return null;
        }
        return jwtHeader;
    }

    public static String getJwtToken(String jwtHeader) {
        return jwtHeader.replace(TOKEN_PREFIX, "");
    }

    public void checkUser(HttpServletRequest request, String email) {
        if (email != null) {
            User user = userRepository.findByEmail(email).orElse(null);
            request.setAttribute("userId", user.getUserId());
            user.setLastAccessDate();
            userRepository.save(user);
            UserDetails userDetails = new UserDetailsImpl(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
