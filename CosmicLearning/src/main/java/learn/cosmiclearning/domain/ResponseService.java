package learn.cosmiclearning.domain;


import jakarta.validation.Validator;
import learn.cosmiclearning.data.AnswerOptionsRepository;
import learn.cosmiclearning.data.QuestionRepository;
import learn.cosmiclearning.data.ResponseRepository;
import learn.cosmiclearning.data.UserRepository;
import learn.cosmiclearning.models.AnswerOptions;
import learn.cosmiclearning.models.Question;
import learn.cosmiclearning.models.Response;
import learn.cosmiclearning.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    private final ResponseRepository repository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionsRepository answerOptionsRepository;
    private final Validator validator;

    public ResponseService(ResponseRepository repository, UserRepository userRepository, QuestionRepository questionRepository, AnswerOptionsRepository answerOptionsRepository, Validator validator) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.answerOptionsRepository = answerOptionsRepository;
        this.validator = validator;
    }


    public Response getAnswer(int userId, int questionId, int answerOptionId,  String username) {
        return repository.findAnswer(userId, questionId, answerOptionId, username);
    }

  /*  public Response saveResponse(Response response) {
        return repository.saveResponse(response);
    } */

    public Result<Response> add(Response response, String username) {
        Result<Response> result = new Result<>();

        if (!result.isSuccess()) {
            result.addErrorMessage("Could not save response");
            return result;
        }

        Question question = questionRepository.findById(response.getQuestion().getQuestionId(), username);
        if (question == null) {
            result.addErrorMessage("Question not found.");
            return result;
        }

        response.setQuestion(question);

        User user = userRepository.findById(response.getUser().getUserId());
        if (user == null) {
            result.addErrorMessage("User not found.");
            return result;
        }

        response.setUser(user);

        Response afterAdd = repository.saveResponse(response);
        result.setPayload(afterAdd);
        return result;
    }

    public boolean isAnswerCorrect(int responseId) {
        Boolean correct = repository.isAnswerCorrectForResponse(responseId);
        return correct != null && correct;
    }
}