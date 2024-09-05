package com.xbank.servicegateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xbank.servicegateway.shared.Exceptions.UpstreamServiceException;
import com.xbank.servicegateway.shared.service.UserClient;
import com.xbank.servicegateway.shared.utils.ApiException;
import com.xbank.servicegateway.shared.utils.ContextKeys;
import io.grpc.Context;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(2)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserClient userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
        final String authHeader= request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            System.out.println("Checking authHeader if null or no Bearer *********************    6***");
            filterChain.doFilter(request, response);
            return;
        }

        if( SecurityContextHolder.getContext().getAuthentication() == null){
            Context context = Context.current().withValue(ContextKeys.token, authHeader);
            context.attach();

            var res = this.userDetailsService.authenticateUser();
            Context userContext = Context.current().withValue(ContextKeys.user, res);
            userContext.attach();

            SecurityContextHolder.getContext().setAuthentication(null);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    res,
                    null,
                    List.of(new SimpleGrantedAuthority(res.getRole()))
            );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        }
        } catch (UpstreamServiceException e){
//            request.setAttribute("filter.error", e);
//            request.getRequestDispatcher("/error").forward(request, response);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(this.objectMapper.writeValueAsString(new ApiException<>(e.getMessage(), null)));
        }
    }
}