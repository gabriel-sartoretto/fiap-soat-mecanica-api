package br.com.fiap.soat.mecanica.adapters.in.web.security;

import br.com.fiap.soat.mecanica.application.usuario.BuscarUsuarioPorEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;

    @Override
    public UserDetails loadUserByUsername(String email) {
        var usuario = buscarUsuarioPorEmailUseCase.executar(email);

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getCargoEnum().name()))
        );
    }
}
