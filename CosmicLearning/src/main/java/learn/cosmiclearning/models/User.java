package learn.cosmiclearning.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class User {

    private int userId;
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @Min(value = 18, message = "User must be at least 18 years old")
    private int age;
    private String title;
    @NotBlank(message = "University cannot be blank")
    private String university;
    private int amount;
    private int appUserId;

    public User(int userId, String firstName, String lastName, int age,  String title, String university,  int amount, int appUserId) {
        this.age = age;
        this.amount = amount;
        this.appUserId = appUserId;
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

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUser(int appUserId) {
        this.appUserId = appUserId;
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
        return getUserId() == user.getUserId() && getAge() == user.getAge() && getAmount() == user.getAmount() && getAppUserId() == user.getAppUserId() && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getTitle(), user.getTitle()) && Objects.equals(getUniversity(), user.getUniversity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFirstName(), getLastName(), getAge(), getTitle(), getUniversity(), getAmount(), getAppUserId());
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
                ", amount=" + amount +
                ", appUserId=" + appUserId +
                '}';
    }
}
