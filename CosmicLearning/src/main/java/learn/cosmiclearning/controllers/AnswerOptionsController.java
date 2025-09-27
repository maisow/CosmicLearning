package learn.cosmiclearning.controllers;


import learn.cosmiclearning.domain.AnswerOptionsService;
import learn.cosmiclearning.domain.QuestionService;
import learn.cosmiclearning.domain.Result;
import learn.cosmiclearning.domain.ResultType;
import learn.cosmiclearning.models.AnswerOptions;
import learn.cosmiclearning.models.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cosmic-learning/answer-options")
public class AnswerOptionsController {

    private final AnswerOptionsService service;

    public AnswerOptionsController(AnswerOptionsService service) {
        this.service = service;
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<List<AnswerOptions>> findAnswerOptionsByQuestion(@PathVariable int questionId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<AnswerOptions> answerOptions = service.getAnswerOptionsByQuestion(questionId, username);

        if (answerOptions == null || answerOptions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(answerOptions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AnswerOptions answerOptions) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Result<AnswerOptions> result = service.add(answerOptions, username);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{answerOptionId}")
    public ResponseEntity<?> update(@PathVariable int answerOptionId, @RequestBody AnswerOptions answerOptions) {
        if (answerOptionId != answerOptions.getAnswerOptionId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Result<AnswerOptions> result = service.update(answerOptions, username);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{answerOptionId}")
    public ResponseEntity<?> deleteById(@PathVariable int answerOptionId) {
        Result<Void> result = service.deleteById(answerOptionId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }
}
