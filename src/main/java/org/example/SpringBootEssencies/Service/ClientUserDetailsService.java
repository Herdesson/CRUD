package org.example.SpringBootEssencies.Service;

import lombok.RequiredArgsConstructor;
import org.example.SpringBootEssencies.Repository.ClienteUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientUserDetailsService implements UserDetailsService {
    private final ClienteUserRepository clienteUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(clienteUserRepository.findByUserName(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
