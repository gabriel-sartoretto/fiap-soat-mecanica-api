package br.com.fiap.soat.mecanica.application.usuario.usecase;

public interface PasswordEncoderPort {

    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
