package learn.cosmiclearning.models;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Objects;

public class Review {

    private int reviewId;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @Min(value = 1, message = "Minimum number of stars is 1")
    @Max(value = 5, message = "Maximum number of stars is 5")
    private int stars;
    private LocalDate reviewDate = LocalDate.now();
    private User student;
    private User professor;

    public Review( int reviewId, String description,  int stars, LocalDate reviewDate, User student, User professor) {
        this.description = description;
        this.reviewDate = reviewDate;
        this.reviewId = reviewId;
        this.stars = stars;
        this.student = student;
        this.professor = professor;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getProfessor() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor = professor;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Review review)) return false;
        return getReviewId() == review.getReviewId() && getStars() == review.getStars() && Objects.equals(getDescription(), review.getDescription()) && Objects.equals(reviewDate, review.reviewDate) && Objects.equals(getStudent(), review.getStudent()) && Objects.equals(getProfessor(), review.getProfessor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReviewId(), getDescription(), getStars(), reviewDate, getStudent(), getProfessor());
    }

    @Override
    public String toString() {
        return "Review{" +
                "description='" + description + '\'' +
                ", reviewId=" + reviewId +
                ", stars=" + stars +
                ", reviewDate=" + reviewDate +
                ", student=" + student +
                ", professor=" + professor +
                '}';
    }
}
