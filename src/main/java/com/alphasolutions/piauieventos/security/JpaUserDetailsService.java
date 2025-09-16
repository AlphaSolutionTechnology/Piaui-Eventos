package com.alphasolutions.piauieventos.security;

import com.alphasolutions.piauieventos.model.UserModel;
import com.alphasolutions.piauieventos.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmailWithRole(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String roleName = user.getUserRole() != null ? user.getUserRole().getRoleName() : "user";
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));

        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
