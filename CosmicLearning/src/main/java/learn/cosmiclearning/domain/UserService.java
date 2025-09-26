package learn.cosmiclearning.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import learn.cosmiclearning.data.UserRepository;
import learn.cosmiclearning.models.Subject;
import learn.cosmiclearning.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository repository;
    private final Validator validator;

    public UserService(UserRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public User findProfileById(int id, String username) {
        return repository.findProfileById(id, username);
    }

    public Result<User> create(User user) {
        Result<User> result = validate(user);

        if (!result.isSuccess()) {
            return result;
        }

        User afterAdd = repository.createProfile(user);
        result.setPayload(afterAdd);
        return result;
    }

    public List<User> findAll(String username) {
        return repository.findAllProfiles(username);
    }

    public Result<User> update(User user) {
        Result<User> result = validate(user);

        if (user.getUserId() <= 0) {
            result.addErrorMessage("User id is required");
        }

        if (result.isSuccess()) {
            if (repository.updateProfile(user)) {
                result.setPayload(user);
            } else {
                result.addErrorMessage(
                        ResultType.NOT_FOUND,
                        "User id %s not found",
                        user.getUserId());
            }
        }

        return result;
    }

    private Result<User> validate(User user) {
        Result<User> result = new Result<>();

        if (user == null) {
            result.addErrorMessage("User cannot be null");
            return result;
        }

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        for (var violation : violations) {
            result.addErrorMessage(violation.getMessage());
        }

        return result;
    }

}
