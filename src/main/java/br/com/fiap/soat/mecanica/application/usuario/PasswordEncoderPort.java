package br.com.fiap.soat.mecanica.application.usuario;

public interface PasswordEncoderPort {

    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
