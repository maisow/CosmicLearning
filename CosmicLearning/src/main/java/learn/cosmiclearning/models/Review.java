package learn.cosmiclearning.models;

import java.time.LocalDate;
import java.util.Objects;

public class Review {

    private int reviewId;
    private String description;
    private int stars;
    private LocalDate reviewDate;
    private User student;

    public Review(String description, LocalDate reviewDate, int reviewId, int stars, User student) {
        this.description = description;
        this.reviewDate = reviewDate;
        this.reviewId = reviewId;
        this.stars = stars;
        this.student = student;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Review review)) return false;
        return getReviewId() == review.getReviewId() && getStars() == review.getStars() && Objects.equals(getDescription(), review.getDescription()) && Objects.equals(getReviewDate(), review.getReviewDate()) && Objects.equals(getStudent(), review.getStudent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReviewId(), getDescription(), getStars(), getReviewDate(), getStudent());
    }

    @Override
    public String toString() {
        return "Review{" +
                "description='" + description + '\'' +
                ", reviewId=" + reviewId +
                ", stars=" + stars +
                ", reviewDate=" + reviewDate +
                ", student=" + student +
                '}';
    }
}
