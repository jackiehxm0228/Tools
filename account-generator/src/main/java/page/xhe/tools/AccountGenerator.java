package page.xhe.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class AccountGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountGenerator.class);

    public static void main(String[] args) {
        @SuppressWarnings("resource")
		Scanner consoleInput = new Scanner(System.in);

        System.out.println("Enter username: ");
        String userName = consoleInput.nextLine();
        System.out.println("Enter password: ");
        String password = consoleInput.nextLine();

        URL resource = AccountGenerator.class.getClassLoader().getResource("accountGenerator.properties");
        Connection conn = null;
        Statement stmt = null;
        try {
            assert resource != null;
            File file = new File(resource.toURI());
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));

            String url = (String) properties.get("datasource.url");
            String driver = (String) properties.get("datasource.driver-class-name");
            String dbUser = (String) properties.get("datasource.username");
            String dbPwd = (String) properties.get("datasource.password");
            Integer strength = (Integer) properties.get("password.encoder.strength");

            Class.forName(driver);
            conn = DriverManager.getConnection(url, dbUser, dbPwd);

            stmt = conn.createStatement();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(strength == null ? 10 : strength);
            String accountSql = "insert into account (username,password,isAccountNonExpired,isAccountNonLocked,isCredentialsNonExpired,isEnabled) values ('"
                    + userName + "','" + passwordEncoder.encode(password) + "',true,true,true,true);";
            String authoritySql = "insert into authority (username,authority) values ('" + userName
                    + "','ROLE_ADMIN');";

            stmt.executeUpdate(accountSql);
            stmt.executeUpdate(authoritySql);
        } catch (IOException | URISyntaxException | ClassNotFoundException | SQLException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        LOGGER.info("New account: " + userName + " has been created!");
    }
}
