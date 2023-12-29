package com.university.post.security;
import com.university.post.enums.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserSecurity userSecurity;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return new CustomAuthenticationExceptionFilter();
    }

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return new CustomAuthorizationExceptionFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userSecurity) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder()); // cung cấp password encoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS) //security stateless
                // Ngăn chặn request từ một domain khác
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/login", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/api/v1/order-status/*").permitAll()
                .antMatchers("/api/v1/admin/**").hasAuthority(String.valueOf(Permission.COMPANY_ADMIN))
                .antMatchers("/api/v1/management/staffs").hasAnyAuthority(String.valueOf(Permission.COMPANY_ADMIN),
                                                                        String.valueOf(Permission.GATHERING_ADMIN),
                                                                        String.valueOf(Permission.TRANSACTION_ADMIN))
                .antMatchers("/api/v1/delivery-points/**", "/api/v1/order-delivery/*").hasAnyAuthority(String.valueOf(Permission.GATHERING_ADMIN), String.valueOf(Permission.GATHERING_STAFF),
                                                                                                            String.valueOf(Permission.TRANSACTION_ADMIN), String.valueOf(Permission.TRANSACTION_STAFF))
                .anyRequest().authenticated() // Tất cả các request khác đều cần phải xác thực mới được truy cập
                .and()
                .exceptionHandling().authenticationEntryPoint(getAuthenticationEntryPoint())
                .and()
                .exceptionHandling().accessDeniedHandler(getAccessDeniedHandler())
                .and()
                .logout().clearAuthentication(true)
                .logoutSuccessUrl("/api/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
