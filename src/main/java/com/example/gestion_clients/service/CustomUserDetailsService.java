package com.example.gestion_clients.service;

import com.example.gestion_clients.model.UserEntity;
import com.example.gestion_clients.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // Vérifier si l'utilisateur existe

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));
      //  UserEntity userEntity = userRepository.findByUsername(username);



        // Retourner un UserDetails compréhensible par Spring Security

        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                java.util.Collections.singletonList(
                        new org.springframework
                                .security
                                .core
                                .authority
                                .SimpleGrantedAuthority("ROLE_" + userEntity.getRole()))
        );
    }

}
