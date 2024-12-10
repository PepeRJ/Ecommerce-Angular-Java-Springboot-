package com.ecommerce.ecommerce_backend.security;

// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class VerificarSesionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Durante desarrollo, permitir todas las peticiones
        filterChain.doFilter(request, response);
        
        /*
        // Código original comentado para desarrollo
        String requestURI = request.getRequestURI();
        
        if (requestURI.equals("/") || 
            requestURI.startsWith("/api/usuarios/") ||
            requestURI.startsWith("/api/productos/") ||
            requestURI.equals("/error")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Acceso no autorizado: sesión no encontrada\"}");
            return;
        }

        filterChain.doFilter(request, response);
        */
    }
}