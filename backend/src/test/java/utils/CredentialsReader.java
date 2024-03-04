package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class CredentialsReader {

    public static Map<String, String> getCredentials(String csvFile) {
        return parseCSV(csvFile).getLoginPasswordMap();
    }

    public static List<String> getLogins(String csvFile) {
        return parseCSV(csvFile).getLogins();
    }

    private static Credentials parseCSV(String csvFile) {
        List<String> loginsList = new ArrayList<>();
        Map<String, String> loginPasswordMap = new HashMap<>();
        String line;
        String csvSplitBy = ",\\s*";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] entry = line.split(csvSplitBy);
                if (entry.length == 2) {
                    loginsList.add(entry[0]);
                    loginPasswordMap.put(entry[0], entry[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Credentials credentials = new Credentials();
        credentials.setLoginPasswordMap(loginPasswordMap);
        credentials.setLogins(loginsList);

        return credentials;
    }

    public static class Credentials {
        private Map<String, String> loginPasswordMap;
        private List<String> logins;

        public Map<String, String> getLoginPasswordMap() {
            return loginPasswordMap;
        }

        public void setLoginPasswordMap(Map<String, String> loginPasswordMap) {
            this.loginPasswordMap = loginPasswordMap;
        }

        public List<String> getLogins() {
            return logins;
        }

        public void setLogins(List<String> logins) {
            this.logins = logins;
        }
    }

}

