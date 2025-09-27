package learn.cosmiclearning.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import learn.cosmiclearning.data.ReviewRepository;
import learn.cosmiclearning.data.UserRepository;
import learn.cosmiclearning.models.Review;
import learn.cosmiclearning.models.Subject;
import learn.cosmiclearning.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ReviewService {

    private final ReviewRepository repository;
    private final UserRepository userRepository;
    private final Validator validator;

    public ReviewService(ReviewRepository repository, UserRepository userRepository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
        this.userRepository = userRepository;
    }

    public List<Review> findReviewByProfessor(int professorId, String username) {
        return repository.findReviewsByProfessor(professorId, username);
    }

    public Result<Review> add(Review review, String username) {
        Result<Review> result = validate(review);

        if (!result.isSuccess()) {
            result.addErrorMessage("Could not create review");
            return result;
        }

        List<String> roles = userRepository.getRolesByUsername(username);
        if (!roles.contains("STUDENT")) {
            result.addErrorMessage("Only students can add reviews.");
            return result;
        }

        User student = userRepository.findByUsername(username);
        if (student == null) {
            result.addErrorMessage("Student user not found.");
            return result;
        }

        User professor = userRepository.findById(review.getProfessor().getUserId());
        if (professor == null) {
            result.addErrorMessage("Professor not found.");
            return result;
        }

        review.setStudent(student);
        review.setProfessor(professor);

        Review afterAdd = repository.addReview(review);
        result.setPayload(afterAdd);
        return result;
    }

    public Result<Void> deleteById(int id) {
        Result<Void> result  = new Result<>();
        boolean didDelete = repository.deleteReview(id);

        if (!didDelete) {
            result.addErrorMessage(
                    ResultType.NOT_FOUND,
                    "Review id %s not found",
                    id);
        }

        return result;
    }

    public Result<Review> update(Review review, String username) {
        Result<Review> result = validate(review);

        if (review.getReviewId() <= 0) {
            result.addErrorMessage("Review id is required");
            return result;
        }

        List<String> roles = userRepository.getRolesByUsername(username);
        if (!roles.contains("STUDENT")) {
            result.addErrorMessage("Only students can update reviews.");
            return result;
        }

        User student = userRepository.findByUsername(username);
        if (student == null) {
            result.addErrorMessage("Student user not found.");
            return result;
        }

        Review existing = repository.findById(review.getReviewId());
        if (existing == null) {
            result.addErrorMessage(ResultType.NOT_FOUND, "Review id %s not found", review.getReviewId());
            return result;
        }

        if (existing.getStudent().getUserId() != student.getUserId()) {
            result.addErrorMessage(ResultType.INVALID, "You do not have permission to update this review.");
            return result;
        }

        User professor = userRepository.findById(review.getProfessor().getUserId());
        if (professor == null) {
            result.addErrorMessage("Professor not found.");
            return result;
        }

        review.setStudent(student);
        review.setProfessor(professor);

        if (repository.updateReview(review)) {
            result.setPayload(review);
        } else {
            result.addErrorMessage(
                    ResultType.NOT_FOUND,
                    "Review id %s not found",
                    review.getReviewId());
        }

        return result;
    }

    private Result<Review> validate(Review review) {
        Result<Review> result = new Result<>();

        if (review == null) {
            result.addErrorMessage("Review cannot be null");
            return result;
        }

        Set<ConstraintViolation<Review>> violations = validator.validate(review);

        for (var violation : violations) {
            result.addErrorMessage(violation.getMessage());
        }

        return result;
    }

}
