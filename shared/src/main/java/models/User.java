package models;

public class User extends Entity{
    private int id;
    private String name;
    private String password;
    private int score;
    private String markType;

    /**
     * Constructors.
     */
    public User(int id, String name, String password, int score) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.score = score;
    }

    public User(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(){

    }

    /**
     * Getters and setters.
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMark() {
        return markType;
    }

    public void setMarkTypeX() {
        this.markType = "X";
    }

    public void setMarkTypeO() {
        this.markType = "O";
    }

}
