package br.com.fiap.foodhub.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "admin@123";
        String hash = encoder.encode(password);

        System.out.println("Senha original: " + password);
        System.out.println("Hash BCrypt: " + hash);
    }
}
