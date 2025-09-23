package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Review;

import java.util.List;

public interface ReviewRepository {

    List<Review> findReviewsByProfessor(int professorId, String username);

    Review addReview(Review review);

    boolean updateReview(Review review);

    boolean deleteReview(int reviewId);
}
