package learn.cosmiclearning.controllers;


import learn.cosmiclearning.domain.QuestionService;
import learn.cosmiclearning.domain.Result;
import learn.cosmiclearning.domain.ResultType;
import learn.cosmiclearning.domain.ReviewService;
import learn.cosmiclearning.models.Question;
import learn.cosmiclearning.models.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cosmic-learning/question")
public class QuestionController {


    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @GetMapping("/by-subject/{subjectId}")
    public ResponseEntity<List<Question>> findQuestionsBySubject(@PathVariable int subjectId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Question> questions = service.findQuestionsBySubject(subjectId, username);

        if (questions == null || questions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/by-professor/{professorId}")
    public ResponseEntity<List<Question>> findQuestionsByProfessor(@PathVariable int professorId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Question> questions = service.findQuestionsByProfessor(professorId, username);

        if (questions == null || questions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Question question) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Result<Question> result = service.add(question, username);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<?> update(@PathVariable int questionId, @RequestBody Question question) {
        if (questionId != question.getQuestionId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Result<Question> result = service.update(question, username);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteById(@PathVariable int questionId) {
        Result<Void> result = service.deleteById(questionId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }
}
