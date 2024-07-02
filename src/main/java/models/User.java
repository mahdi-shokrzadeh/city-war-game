package models;

import database.Database;

import java.util.List;
import java.util.ArrayList;

public class User implements Comparable<User> {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String role;
    private String passRecoveryQuestion;
    private String passRecoveryAnswer;
    private int level;
    private int experience;
    private int hitPoints;
    private int coins;
    private Integer clanID;
    private boolean firstLogin;
    private List<Integer> userCardIDS;
    private GameCharacter character;
    private int damage = 0;

    public User(String username, String password, String nickname, String email, String role,
            String recovery_pass_question, String recovery_pass_answer) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.passRecoveryQuestion = recovery_pass_question;
        this.passRecoveryAnswer = recovery_pass_answer;
        this.level = 1;
        this.experience = 0;
        this.hitPoints = 100;
        this.coins = 0;
        this.clanID = null;
        this.firstLogin = false;
        this.userCardIDS = new ArrayList<>();
    }

    // getter and setters
    public User() {
    }

    public int getID() {
        return id;
    }

    public void setID(int _id) {
        id = _id;
    }

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

    public String getPassRecoveryQuestion() {
        return passRecoveryQuestion;
    }

    public String getPassRecoveryAnswer() {
        return passRecoveryAnswer;
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

    public int getCoins() {
        return coins;
    }

    public Integer getClanID() {
        return clanID;
    }

    public boolean getFirstLogin() {
        return firstLogin;
    }

    public List<Integer> getUserCardIDS() {
        return userCardIDS;
    }

    public GameCharacter getGameCharacter() {
        return character;
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

    public void setPassRecoveryQuestion(String recovery_pass_question) {
        this.passRecoveryQuestion = recovery_pass_question;
    }

    public void setPassRecoveryAnswer(String recovery_pass_answer) {
        this.passRecoveryAnswer = recovery_pass_answer;
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

    public void setCoins(int value) {
        coins = value;
    }

    public void changeCoin(int value) {
        coins += value;
    }

    public void setClanID(int id) {
        clanID = id;
    }

    public void firstLogin() {
        firstLogin = false;
    }

    public void addUserCardID(int id) {
        userCardIDS.add(id);
    }

    public void removeCardID(int id) {
        userCardIDS.remove(id);
    }

    @Override
    public int compareTo(User u) {
        if (username.equals(u.getUsername()) && email.equals(u.getEmail()) && nickname.equals(u.getNickname())
                && role.equals(u.getRole())) {
            return 0;
        } else {
            return -1;
        }
    }

    public void setGameCharacter(GameCharacter c) {
        character = c;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

}
