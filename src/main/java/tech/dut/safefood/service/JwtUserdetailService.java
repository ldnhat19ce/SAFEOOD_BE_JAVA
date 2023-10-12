package tech.dut.safefood.service;


import tech.dut.safefood.model.Role;
import tech.dut.safefood.model.SafeFoodUserPrincipal;
import tech.dut.safefood.model.User;
import tech.dut.safefood.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class JwtUserdetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        UserDetails userDetails = null;
        try {
            userDetails = SafeFoodUserPrincipal.create(user, getAuthorities(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDetails;
    }
    public static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String userRoles = user.getRole().getName().toString();
        return AuthorityUtils.createAuthorityList(userRoles);
    }
}
