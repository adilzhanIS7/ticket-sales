package com.example.demo.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AuthenticationService {
    private static final String SECRET_KEY = "23jfkd73hjkds83";

    public static String generateToken(int userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static int getUserIdFromToken(String token) {
        return Integer.parseInt(Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject());
    }

    public static boolean saveTokenToFile(String token) {
        try {
            FileWriter writer = new FileWriter("token.txt", false);
            writer.write(token);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getTokenFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("token.txt"))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validateTokenFile() {
        Path path = Paths.get("token.txt");
        return Files.exists(path);
    }
}