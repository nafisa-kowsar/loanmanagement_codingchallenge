package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class DBPropertyUtil {
	
	public static String getPropertyString() {
        Properties properties = new Properties();
        String propertyFileName = "database.properties";

        try (InputStream input = DBPropertyUtil.class.getClassLoader().getResourceAsStream(propertyFileName)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + propertyFileName);
                return null;
            }

            properties.load(input);

            String hostname = properties.getProperty("hostname");
            String dbname = properties.getProperty("dbname");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String port = properties.getProperty("port");

            return String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s", hostname, port, dbname, username, password);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
