package tech.dut.safefood.configurator;


import tech.dut.safefood.component.JwtAuthenticationEntryPoint;
import tech.dut.safefood.component.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurator extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    public JwtAuthenticationFilter authenticationJwtTokenFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource).and().csrf().disable()
                .authorizeRequests()
                .antMatchers(URL_PERMIT_ALL).permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private static final String[] URL_PERMIT_ALL = {
            "/user/authentication/**",
            "/shop/authentication/**",
            "/auth/**",
            "/oauth2/**",
            "/**/admin/authentication/login",
            "/**/user/authentication/login",
            "/**/user/authentication/sign-up/re-send-code",
            "/**/user/authentication/sign-up",
            "/**/user/authentication/sign-up/active",
            "/**/user/authentication/forgot-password",
            "/**/user/authentication/forgot-password/verify-code",
            "/**/user/authentication/forgot-password/reset",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/**/swagger-ui.html",
            "/api/v2/docs",
            "/**/swagger-ui.html",
            "/**/user/payment/vnpay/ipn-url",
            "/**/user/payment/vnpay/return-url"

    };

}