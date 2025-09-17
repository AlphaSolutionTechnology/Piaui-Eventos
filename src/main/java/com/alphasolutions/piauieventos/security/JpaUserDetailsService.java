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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String key = username == null ? "" : username.trim();

        UserModel user = isEmail(key)
                ? userRepository.findByEmailWithRole(key).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"))
                : findByBrazilPhone(key).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String roleName = user.getUserRole() != null ? user.getUserRole().getRoleName() : "user";
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));

        return new User(user.getEmail(), user.getPassword(), authorities);
    }

    private boolean isEmail(String value) {
        return value.contains("@") && value.contains(".");
    }

    private Optional<UserModel> findByBrazilPhone(String value) {
        String digits = value.replaceAll("\\D", "");
        // tenta exatamente como veio (após normalizar)
        Optional<UserModel> found = userRepository.findByPhoneWithRole(digits);
        if (found.isPresent()) return found;
        // se vier com DDI 55, tenta sem
        if (digits.startsWith("55") && digits.length() > 2) {
            String noDdi = digits.substring(2);
            found = userRepository.findByPhoneWithRole(noDdi);
            if (found.isPresent()) return found;
        } else {
            String withDdi = "55" + digits;
            found = userRepository.findByPhoneWithRole(withDdi);
            if (found.isPresent()) return found;
        }
        return Optional.empty();
    }
}
