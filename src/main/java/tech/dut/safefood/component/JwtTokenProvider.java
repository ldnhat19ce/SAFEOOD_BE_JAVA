package tech.dut.safefood.component;


import tech.dut.safefood.dto.response.AdminAuthenticationResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.SafeFoodUserPrincipal;
import tech.dut.safefood.util.constants.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider implements Serializable {
    private final String AUTHORIZATION_HEADER = "Authorization";

    private final String TOKEN_PREFIX = "Bearer";

    @Value("${application.security.password-secret}")
    private String SECRET;
    @Value("${application.security.duration-accessToken}")
    private Long DURATION_ACCESSTOKEN;

    @Value("${application.security.duration-refreshToken}")
    private Long DURATION_REFRESHTOKEN;

    public Object getUsername(String token) {
        String encodeSecret = TextCodec.BASE64.encode(SECRET);

        Claims claims = Jwts.parser().
                setSigningKey(encodeSecret).
                parseClaimsJws(token).
                getBody();
        if (claims.get("email") == null) {
            if (!Constants.JWT_CLAIM_KEY_IS_EMAIL_API.equals("email")) {
                throw new SafeFoodException(SafeFoodException.ERROR_INVALID_TOKEN);
            } else {
                return null;
            }
        }
        return claims.get("email");
    }


    public boolean validateToken(String token) throws BadCredentialsException {
        try {
            String encodeSecret = TextCodec.BASE64.encode(SECRET);
            Jwts.parser().setSigningKey(encodeSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException exception) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", exception);

        } catch (ExpiredJwtException expiredJwtException) {
            throw expiredJwtException;
        }
    }

    public String createAuthTokenForgotPassword(String email) {
        String encodeSecret = TextCodec.BASE64.encode(SECRET);
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("email",email);
        return Jwts.builder().setSubject(email).setClaims(claims)
                .setExpiration(generateExpiredTime())
                .signWith(SignatureAlgorithm.HS512, encodeSecret)
                .compact();
    }

    public String getToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX + " ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


    public AdminAuthenticationResponseDto buildTheAuthenticatedResponse(Authentication authentication, Boolean isAdminApi) {
        SafeFoodUserPrincipal user = (SafeFoodUserPrincipal) authentication.getPrincipal();
        AdminAuthenticationResponseDto adminAuthenticationResponseDto = new AdminAuthenticationResponseDto();

        adminAuthenticationResponseDto.setToken(this.generateAccessToken(authentication,isAdminApi));
        adminAuthenticationResponseDto.setRefreshToken(this.generateRefreshToken(authentication));
        adminAuthenticationResponseDto.setExpired(java.sql.Date.from(Instant.now().plusMillis(DURATION_ACCESSTOKEN)));
        adminAuthenticationResponseDto.setUserId(user.getUserId());
        adminAuthenticationResponseDto.setTokenType(TOKEN_PREFIX);
        return adminAuthenticationResponseDto;
    }

    private String generateRefreshToken(Authentication authentication) {
        Date date = new Date();
        String encodeSecret = TextCodec.BASE64.encode(SECRET);
        return Jwts.builder().setSubject(authentication.getName()).setIssuedAt(date).setExpiration(new Date(date.getTime() + DURATION_REFRESHTOKEN)).signWith(SignatureAlgorithm.HS512, encodeSecret).compact();
    }

    public Date generateExpiredTime(){
        return java.sql.Date.from(Instant.now().plusMillis(DURATION_ACCESSTOKEN));
    }

    public String generateAccessToken(Authentication authentication, Boolean isAdminApi) {
        Date date = new Date();
        SafeFoodUserPrincipal user = (SafeFoodUserPrincipal) authentication.getPrincipal();
        String encodeSecret = TextCodec.BASE64.encode(SECRET);
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("userId", user.getUserId());
        claims.put("email", user.getName());
        claims.put("tfaChecked", user.isTfaChecked());
        claims.put(Constants.JWT_CLAIM_KEY_IS_ADMIN_API, isAdminApi);
        return Jwts.builder().setSubject(user.getName()).setClaims(claims).setExpiration(new Date(date.getTime() + DURATION_ACCESSTOKEN)).signWith(SignatureAlgorithm.HS512, encodeSecret).compact();
    }

    public Object getClaimInfo(String token, String claimKey) throws SafeFoodException {
        String encodeSecret = TextCodec.BASE64.encode(SECRET);
        Claims claims = Jwts.parser().
                setSigningKey(encodeSecret).
                parseClaimsJws(token).
                getBody();
        if (claims.get(claimKey) == null) {
            if (!Constants.JWT_CLAIM_KEY_IS_ADMIN_API.equals(claimKey)) {
                throw new SafeFoodException(SafeFoodException.ERROR_INVALID_TOKEN);
            } else {
                return null;
            }
        }
        return claims.get(claimKey);
    }
}
