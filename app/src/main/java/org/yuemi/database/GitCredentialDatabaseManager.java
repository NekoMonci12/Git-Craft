package org.yuemi.git;

import java.io.File;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class GitCredentialDatabaseManager {

    private final String dbPath;
    private final String filePassword;
    private final String dbPassword;

    public GitCredentialDatabaseManager(File pluginFolder, String filePassword, String dbPassword) {
        this.dbPath = new File(pluginFolder, "gitcreds").getAbsolutePath();
        this.filePassword = filePassword;
        this.dbPassword = dbPassword;
        try {
            Class.forName("org.h2.Driver"); // Ensure the H2 JDBC driver is registered
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 Driver not found. Make sure it's shaded correctly.", e);
        }
        initialize();
    }

    private Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:h2:file:" + dbPath + ";CIPHER=AES";
        String user = "sa";
        String password = filePassword + " " + dbPassword;
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    private void initialize() {
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS credentials (
                        id IDENTITY PRIMARY KEY,
                        username VARCHAR NOT NULL,
                        token VARCHAR NOT NULL,
                        salt VARCHAR
                    );
                """);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean saveCredentials(String username, String token, boolean withSalt) throws SQLException {
        String salt = withSalt ? generateSalt() : null;
        String tokenToStore = withSalt ? encodeWithSalt(token, salt) : token;
        boolean replaced = false;

        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM credentials");
                if (rs.next() && rs.getInt(1) > 0) {
                    replaced = true;
                    stmt.executeUpdate("DELETE FROM credentials");
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("""
                    INSERT INTO credentials (username, token, salt)
                    VALUES (?, ?, ?)
                """)) {
                ps.setString(1, username);
                ps.setString(2, tokenToStore);
                ps.setString(3, salt);
                ps.executeUpdate();
            }
        }

        return replaced;
    }

    public String[] getCredentials() throws SQLException {
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT username, token, salt FROM credentials LIMIT 1")) {
                    if (rs.next()) {
                        String username = rs.getString("username");
                        String token = rs.getString("token");
                        String salt = rs.getString("salt");
                        if (salt != null) {
                            token = decodeWithSalt(token, salt);
                        }
                        return new String[]{username, token};
                    }
                }
            }
        }
        return null;
    }

    public boolean deleteCredentials() throws SQLException {
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                return stmt.executeUpdate("DELETE FROM credentials") > 0;
            }
        }
    }

    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String encodeWithSalt(String token, String salt) {
        byte[] tokenBytes = token.getBytes();
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        byte[] result = new byte[tokenBytes.length];
        for (int i = 0; i < tokenBytes.length; i++) {
            result[i] = (byte) (tokenBytes[i] ^ saltBytes[i % saltBytes.length]);
        }
        return Base64.getEncoder().encodeToString(result);
    }

    private String decodeWithSalt(String encoded, String salt) {
        byte[] tokenBytes = Base64.getDecoder().decode(encoded);
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        byte[] result = new byte[tokenBytes.length];
        for (int i = 0; i < tokenBytes.length; i++) {
            result[i] = (byte) (tokenBytes[i] ^ saltBytes[i % saltBytes.length]);
        }
        return new String(result);
    }
}