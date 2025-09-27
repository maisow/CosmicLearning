package learn.cosmiclearning.controllers;


import learn.cosmiclearning.domain.Result;
import learn.cosmiclearning.domain.ResultType;
import learn.cosmiclearning.domain.ReviewService;
import learn.cosmiclearning.domain.SubjectService;
import learn.cosmiclearning.models.Review;
import learn.cosmiclearning.models.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cosmic-learning/review")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping("/{professorId}")
    public ResponseEntity<List<Review>> findReviewsByProfessor(@PathVariable int professorId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Review> reviews = service.findReviewByProfessor(professorId, username);

        if (reviews == null || reviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Review review) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Result<Review> result = service.add(review, username);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(@PathVariable int reviewId, @RequestBody Review review) {
        if (reviewId != review.getReviewId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Result<Review> result = service.update(review, username);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteById(@PathVariable int reviewId) {
        Result<Void> result = service.deleteById(reviewId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }
}
