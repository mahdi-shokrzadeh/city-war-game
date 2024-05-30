package models;

public class User {

    private String username;
    private String password;
    private String nickname;
    private String email;
    private String role;
    private String recovery_pass_question;
    private String recovery_pass_answer;
    private int level;
    private int experience;
    private int hitPoints;

    public User(String username, String password, String nickname, String email, String role,
            String recovery_pass_question, String recovery_pass_answer, int level, int experience, int hitPoints) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.recovery_pass_question = recovery_pass_question;
        this.recovery_pass_answer = recovery_pass_answer;
        this.level = level;
        this.experience = experience;
        this.hitPoints = hitPoints;
    }

    // getter and setters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getrecovery_pass_question() {
        return recovery_pass_question;
    }

    public String getrecovery_pass_answer() {
        return recovery_pass_answer;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setrecovery_pass_question(String recovery_pass_question) {
        this.recovery_pass_question = recovery_pass_question;
    }

    public void setrecovery_pass_answer(String recovery_pass_answer) {
        this.recovery_pass_answer = recovery_pass_answer;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
