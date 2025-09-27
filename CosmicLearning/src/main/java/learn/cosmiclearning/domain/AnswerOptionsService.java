package learn.cosmiclearning.domain;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import learn.cosmiclearning.data.AnswerOptionsRepository;
import learn.cosmiclearning.data.QuestionRepository;
import learn.cosmiclearning.data.SubjectRepository;
import learn.cosmiclearning.data.UserRepository;
import learn.cosmiclearning.models.AnswerOptions;
import learn.cosmiclearning.models.Question;
import learn.cosmiclearning.models.Subject;
import learn.cosmiclearning.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AnswerOptionsService {


    private final AnswerOptionsRepository repository;
    private final UserRepository userRepository;
    private final Validator validator;
    private final QuestionRepository questionRepository;

    public AnswerOptionsService(AnswerOptionsRepository repository, UserRepository userRepository, QuestionRepository questionRepositor, Validator validator) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepositor;
        this.validator = validator;
    }

    public List<AnswerOptions> getAnswerOptionsByQuestion(int questionId, String username) {
        return repository.getAnswerOptionsByQuestion(questionId, username);
    }

    public Result<AnswerOptions> add(AnswerOptions answerOptions, String username) {
        Result<AnswerOptions> result = validate(answerOptions);

        if (!result.isSuccess()) {
            result.addErrorMessage("Could not create answer option");
            return result;
        }

        List<String> roles = userRepository.getRolesByUsername(username);
        if (!roles.contains("PROFESSOR")) {
            result.addErrorMessage(ResultType.INVALID, "Only professors can add answer options");
            return result;
        }

        Question question = questionRepository.findById(answerOptions.getQuestion().getQuestionId(), username);
        if (question == null) {
            result.addErrorMessage("Question not found.");
            return result;
        }

        answerOptions.setQuestion(question);

        AnswerOptions afterAdd = repository.createAnswerOptions(answerOptions);
        result.setPayload(afterAdd);
        return result;
    }

    public Result<AnswerOptions> update(AnswerOptions answerOptions, String username) {
        Result<AnswerOptions> result = validate(answerOptions);

        if (answerOptions.getAnswerOptionId() <= 0) {
            result.addErrorMessage("Answer Option id is required");
            return result;
        }

        List<String> roles = userRepository.getRolesByUsername(username);
        if (!roles.contains("PROFESSOR")) {
            result.addErrorMessage("Only professors can update answer options");
            return result;
        }

        if (result.isSuccess()) {
            if (repository.updateAnswerOptions(answerOptions)) {
                    result.setPayload(answerOptions);
                } else {
                    result.addErrorMessage(
                            ResultType.NOT_FOUND,
                            "Answer Option id %s not found",
                            answerOptions.getAnswerOptionId());
                }
            }

        return result;
    }

    public Result<Void> deleteById(int id) {
        Result<Void> result  = new Result<>();
        boolean didDelete = repository.deleteAnswerOptions(id);

        if (!didDelete) {
            result.addErrorMessage(
                    ResultType.NOT_FOUND,
                    "Answer Option id %s not found",
                    id);
        }
        return result;
    }

    private Result<AnswerOptions> validate(AnswerOptions answerOptions) {
        Result<AnswerOptions> result = new Result<>();

        if (answerOptions == null) {
            result.addErrorMessage("Answer Options cannot be null");
            return result;
        }

        Set<ConstraintViolation<AnswerOptions>> violations = validator.validate(answerOptions);

        for (var violation : violations) {
            result.addErrorMessage(violation.getMessage());
        }

        return result;
    }



}
