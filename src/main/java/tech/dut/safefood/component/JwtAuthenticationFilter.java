package tech.dut.safefood.component;

import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.SafeFoodUserPrincipal;
import tech.dut.safefood.util.constants.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.getToken(request);
        try {
            if (token != null && tokenProvider.validateToken(token)) {
                Boolean isAdminApi = (Boolean) this.tokenProvider.getClaimInfo(token, Constants.JWT_CLAIM_KEY_IS_ADMIN_API);
                if (null != isAdminApi && request.getRequestURI().contains(Constants.API_ADMIN_URL_REGEX) && !isAdminApi) {
                    throw new SafeFoodException(SafeFoodException.ERROR_INVALID_ROLE);
                }
                String username = tokenProvider.getUsername(token).toString();
                SafeFoodUserPrincipal user = (SafeFoodUserPrincipal) userDetailsService.loadUserByUsername(username);
                if (tokenProvider.validateToken(token) && user.getPassword() != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException | BadCredentialsException | SafeFoodException exception) {
            request.setAttribute("exception", exception);
        }
        filterChain.doFilter(request, response);
    }
}
