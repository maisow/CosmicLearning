package learn.cosmiclearning.domain;


import jakarta.validation.Validator;
import learn.cosmiclearning.data.QuestionRepository;
import learn.cosmiclearning.data.ReviewRepository;
import learn.cosmiclearning.data.UserRepository;
import learn.cosmiclearning.models.Question;
import learn.cosmiclearning.models.Review;
import org.springframework.stereotype.Service;

import java.util.List;

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



}
