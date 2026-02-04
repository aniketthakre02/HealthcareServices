package com.project.HealthcareService.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public final JwtUtil jwtUtil;
    private final UserDetailsServicesImpl userDetailsServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip JWT processing for public endpoints
        if (path.equals("/register") || path.equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        String header=request.getHeader("Authorization");
        String token=null;
        String email=null;

        if(header!=null && header.startsWith("Bearer ")){
            token= header.substring(7);
            email=jwtUtil.extractEmail(token);
        }
        if(email!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails=userDetailsServices.loadUserByUsername(email);
            if(jwtUtil.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken auth=new
                        UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

//                auth.setDetails(new webAuthenticationDetailsSource().buildDetiails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request,response);
    }
}
