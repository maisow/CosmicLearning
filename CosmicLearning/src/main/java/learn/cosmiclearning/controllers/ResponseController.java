package learn.cosmiclearning.controllers;

import learn.cosmiclearning.domain.AnswerOptionsService;
import learn.cosmiclearning.domain.ResponseService;
import learn.cosmiclearning.domain.Result;
import learn.cosmiclearning.models.AnswerOptions;
import learn.cosmiclearning.models.Response;
import learn.cosmiclearning.models.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cosmic-learning/response")
public class ResponseController {

    private final ResponseService service;

    public ResponseController(ResponseService service) {
        this.service = service;
    }

    @GetMapping("/{userId}/{questionId}/{answerOptionId}")
    public ResponseEntity<Response> findAnswer(@PathVariable int userId,  @PathVariable int questionId, @PathVariable int answerOptionId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Response response = service.getAnswer(userId, questionId, answerOptionId, username);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveResponse(@RequestBody Response response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Result<Response> result = service.add(response, username);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/is-correct/{answerOptionId}")
    public ResponseEntity<Boolean> isAnswerCorrect(@PathVariable int answerOptionId) {
        boolean correct = service.isAnswerCorrect(answerOptionId);
        return ResponseEntity.ok(correct);
    }




}
