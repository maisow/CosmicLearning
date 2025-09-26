package learn.cosmiclearning.controllers;


import learn.cosmiclearning.domain.Result;
import learn.cosmiclearning.domain.ResultType;
import learn.cosmiclearning.domain.SubjectService;
import learn.cosmiclearning.models.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cosmic-learning/subject")
public class SubjectController {

    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @GetMapping
    public List<Subject> findAll() {
        return service.findAll();
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<Subject> findSubjectById(@PathVariable int subjectId) {
        Subject subject = service.findSubjectById(subjectId);
        if (subject == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subject, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Subject subject) {
        Result<Subject> result = service.create(subject);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<?> update(@PathVariable int subjectId, @RequestBody Subject subject) {
        if (subjectId != subject.getSubjectId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result<Subject> result = service.update(subject);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/{subjectId}")
    public ResponseEntity<?> deleteById(@PathVariable int subjectId) {
        Result<Void> result = service.deleteById(subjectId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }


}
