package org.yuemi.git;

import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class H2PasswordProvider {

    private final File configFile;

    public H2PasswordProvider(File pluginFolder) {
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }
        this.configFile = new File(pluginFolder, "credentials.properties");
    }

    public String[] getOrGeneratePasswords() throws IOException {
        if (configFile.exists()) {
            Properties props = new Properties();
            try (FileInputStream in = new FileInputStream(configFile)) {
                props.load(in);
                return new String[] {
                    props.getProperty("filePassword"),
                    props.getProperty("dbPassword")
                };
            }
        } else {
            String filePassword = generateRandomPassword();
            String dbPassword = generateRandomPassword();

            Properties props = new Properties();
            props.setProperty("filePassword", filePassword);
            props.setProperty("dbPassword", dbPassword);

            try (FileOutputStream out = new FileOutputStream(configFile)) {
                props.store(out, "GitCraft H2 Passwords - Do not share");
            }

            return new String[] { filePassword, dbPassword };
        }
    }

    private String generateRandomPassword() {
        byte[] randomBytes = new byte[16];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }
}
