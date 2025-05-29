package model;

public class User {
    private String username;
    private String password;
    private int bestScore;

    public User(String username, String password, int bestScore) {
        this.username = username;
        this.password = password;
        this.bestScore = bestScore;
    }

    // Getter metotları
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getBestScore() {
        return bestScore;
    }

    // Setter metotları
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", bestScore=" + bestScore +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username != null ? username.equals(user.username) : user.username == null;
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}