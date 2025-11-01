package com.example.demo.config;

import com.example.demo.helpers.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = jwtTokenProvider.getTokenFromHeader(request.getHeader("Authorization"));

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Long tenantId = jwtTokenProvider.getTenantFromToken(token);
                TenantContext.setTenantId(tenantId);
            }

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }

}