package learn.cosmiclearning.domain;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import learn.cosmiclearning.data.QuestionRepository;
import learn.cosmiclearning.data.ReviewRepository;
import learn.cosmiclearning.data.UserRepository;
import learn.cosmiclearning.models.Question;
import learn.cosmiclearning.models.Review;
import learn.cosmiclearning.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class QuestionService {

    private final QuestionRepository repository;
    private final UserRepository userRepository;
    private final Validator validator;

    public QuestionService(QuestionRepository repository, UserRepository userRepository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
        this.userRepository = userRepository;
    }

    public List<Question> findQuestionsBySubject(int subjectId, String username) {
        return repository.findQuestionsBySubject(subjectId, username);
    }

    public List<Question> findQuestionsByProfessor(int professorId, String username) {
        return repository.findQuestionsBySubject(professorId, username);
    }

    public Result<Question> add(Question question, String username) {
        Result<Question> result = validate(question);

        if (!result.isSuccess()) {
            result.addErrorMessage("Could not create question");
            return result;
        }

        List<String> roles = userRepository.getRolesByUsername(username);
        if (!roles.contains("PROFESSOR")) {
            result.addErrorMessage("Only professors can add questions.");
            return result;
        }

        User professor = userRepository.findById(question.getProfessor().getUserId());
        if (professor == null) {
            result.addErrorMessage("Professor not found.");
            return result;
        }

        question.setProfessor(professor);

        Question afterAdd = repository.addQuestion(question);
        result.setPayload(afterAdd);
        return result;
    }

    public Result<Question> update(Question question, String username) {
        Result<Question> result = validate(question);

        if (question.getQuestionId() <= 0) {
            result.addErrorMessage("Question id is required");
            return result;
        }

        List<String> roles = userRepository.getRolesByUsername(username);
        if (!roles.contains("PROFESSOR")) {
            result.addErrorMessage("Only professors can update questions.");
            return result;
        }

        User professor = userRepository.findByUsername(username);
        if (professor == null) {
            result.addErrorMessage("professor user not found.");
            return result;
        }

        Question existing = repository.findById(question.getQuestionId(), username);
        if (existing == null) {
            result.addErrorMessage(ResultType.NOT_FOUND, "Question id %s not found", question.getQuestionId());
            return result;
        }

        if (existing.getProfessor().getUserId() != professor.getUserId()) {
            result.addErrorMessage(ResultType.INVALID, "You do not have permission to update this question.");
            return result;
        }

        question.setProfessor(professor);

        if (repository.updateQuestion(question)) {
            result.setPayload(question);
        } else {
            result.addErrorMessage(
                    ResultType.NOT_FOUND,
                    "Question id %s not found",
                    question.getQuestionId());
        }

        return result;
    }

    public Result<Void> deleteById(int id) {
        Result<Void> result  = new Result<>();
        boolean didDelete = repository.deleteQuestion(id);

        if (!didDelete) {
            result.addErrorMessage(
                    ResultType.NOT_FOUND,
                    "Question id %s not found",
                    id);
        }
        return result;
    }

    private Result<Question> validate(Question question) {
        Result<Question> result = new Result<>();

        if (question == null) {
            result.addErrorMessage("Question cannot be null");
            return result;
        }

        Set<ConstraintViolation<Question>> violations = validator.validate(question);

        for (var violation : violations) {
            result.addErrorMessage(violation.getMessage());
        }

        return result;
    }
}
