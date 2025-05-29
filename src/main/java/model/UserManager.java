package model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class UserManager {
    private Map<String, User> users;
    private String currentUser;

    // Singleton Pattern
    private static UserManager instance;

    private UserManager() {
        users = new HashMap<>();
        // Demo kullanıcıları ekle
        users.put("demo", new User("demo", "1234", 25));
        users.put("test", new User("test", "test", 18));
        users.put("player", new User("player", "pass", 0));
        System.out.println("🔧 UserManager instance oluşturuldu");
    }

    // Singleton getInstance metodu
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("❌ Login: Boş alan tespit edildi");
            return false;
        }

        username = username.trim();
        password = password.trim(); // ÖNEMLİ: Şifreyi de trim et

        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = username;
            System.out.println("✅ Login başarılı: " + username);
            return true;
        }

        // Debug bilgileri
        System.out.println("❌ Login başarısız!");
        System.out.println("Aranan kullanıcı: '" + username + "'");
        System.out.println("Girilen şifre: '" + password + "'");
        if (user != null) {
            System.out.println("Kayıtlı şifre: '" + user.getPassword() + "'");
        } else {
            System.out.println("Kullanıcı bulunamadı!");
        }
        debugPrintUsers();

        return false;
    }

    public String register(String username, String password, String confirmPassword) {
        if (username == null || password == null || confirmPassword == null) {
            return "Tüm alanları doldurun!";
        }

        username = username.trim();
        password = password.trim(); // ÖNEMLİ: Şifreyi de trim et
        confirmPassword = confirmPassword.trim(); // ÖNEMLİ: Onay şifresini de trim et

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "Tüm alanları doldurun!";
        }

        if (username.length() < 3) {
            return "Kullanıcı adı en az 3 karakter olmalı!";
        }

        if (password.length() < 4) {
            return "Şifre en az 4 karakter olmalı!";
        }

        if (!password.equals(confirmPassword)) {
            return "Şifreler eşleşmiyor!";
        }

        if (users.containsKey(username)) {
            return "Bu kullanıcı adı zaten mevcut!";
        }

        users.put(username, new User(username, password, 0));
        System.out.println("✅ Kayıt başarılı: " + username + " / " + password);
        debugPrintUsers();

        return null; // Başarılı kayıt
    }

    public void logout() {
        currentUser = null;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public int getUserBestScore(String username) {
        User user = users.get(username);
        return user != null ? user.getBestScore() : 0;
    }

    public boolean updateUserScore(String username, int newScore) {
        User user = users.get(username);
        if (user != null && newScore > user.getBestScore()) {
            user.setBestScore(newScore);
            return true; // Yeni rekor
        }
        return false;
    }

    public List<User> getLeaderboard() {
        List<User> leaderboard = new ArrayList<>();
        for (User user : users.values()) {
            if (user.getBestScore() > 0) {
                leaderboard.add(user);
            }
        }
        leaderboard.sort((a, b) -> Integer.compare(b.getBestScore(), a.getBestScore()));
        return leaderboard.subList(0, Math.min(5, leaderboard.size()));
    }

    // Debug metodu - kayıtlı kullanıcıları göster
    public void debugPrintUsers() {
        System.out.println("📋 Kayıtlı kullanıcılar (Instance: " + this.hashCode() + "):");
        for (Map.Entry<String, User> entry : users.entrySet()) {
            System.out.println("- Kullanıcı: '" + entry.getKey() + "' | Şifre: '" + entry.getValue().getPassword() + "'");
        }
        System.out.println("Toplam kullanıcı sayısı: " + users.size());
    }
}