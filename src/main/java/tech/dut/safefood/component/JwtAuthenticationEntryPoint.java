package tech.dut.safefood.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Exception exception = (Exception) request.getAttribute("exception");

        String message;
        if (exception != null) {
            message = exception.getMessage();
        } else if (authException.getCause() != null) {
            message = authException.getMessage();
        } else {
            message = authException.getMessage();
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("code", HttpStatus.UNAUTHORIZED.value());
        responseData.put("status", "error");
        responseData.put("message", "Unauthorized");
        responseData.put("responseData", message);
        byte[] body = new ObjectMapper().writeValueAsBytes(responseData);
        response.getOutputStream().write(body);
    }
}
