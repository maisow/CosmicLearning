package learn.cosmiclearning.models;

import java.util.Objects;

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private int age;
    private String title;
    private String university;
    private String email;
    private int amount;
    private AppUser appUser;

    public User(int userId, String firstName, String lastName, int age,  String title, String university, String email, int amount, AppUser appUser) {
        this.age = age;
        this.amount = amount;
        this.appUser = appUser;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.university = university;
        this.userId = userId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return getUserId() == user.getUserId() && getAge() == user.getAge() && getAmount() == user.getAmount() && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getTitle(), user.getTitle()) && Objects.equals(getUniversity(), user.getUniversity()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getAppUser(), user.getAppUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFirstName(), getLastName(), getAge(), getTitle(), getUniversity(), getEmail(), getAmount(), getAppUser());
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", title='" + title + '\'' +
                ", university='" + university + '\'' +
                ", email='" + email + '\'' +
                ", amount=" + amount +
                ", appUser=" + appUser +
                '}';
    }
}
