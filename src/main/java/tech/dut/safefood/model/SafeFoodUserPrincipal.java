package tech.dut.safefood.model;

import tech.dut.safefood.enums.UserEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;


@Getter
@Setter
public class SafeFoodUserPrincipal implements UserDetails, OAuth2User {

    private Long userId;
    private String name;
    @JsonIgnore
    private String password;

    private boolean tfaChecked;
    private List<Role> roles;
    private UserEnum.Status status;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public SafeFoodUserPrincipal(Long id, String name, String password,
                                 UserEnum.Status status, List<Role> roles, Collection<? extends GrantedAuthority> authorities) {
        this.userId = id;
        this.name = name;
        this.password = password;
        this.status = status;
        this.roles = roles;
        this.authorities = authorities;
    }

    public static SafeFoodUserPrincipal create(User user, Collection<? extends GrantedAuthority> authorities) throws UsernameNotFoundException {
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("The user is empty");
        }
        return new SafeFoodUserPrincipal(user.getId(), user.getEmail(), user.getPassword(), user.getStatus(), Collections.singletonList(user.getRole()), authorities);
    }

    public static SafeFoodUserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new SafeFoodUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus(),
                Collections.singletonList(user.getRole()),
                authorities
        );
    }

    public static SafeFoodUserPrincipal create(User user, Map<String, Object> attributes) {
        SafeFoodUserPrincipal userPrincipal = SafeFoodUserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        return UserEnum.Status.ACTIVED.equals(status);
    }
}
