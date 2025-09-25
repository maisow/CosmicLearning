package learn.cosmiclearning.security;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import learn.cosmiclearning.data.AppUserRepository;
import learn.cosmiclearning.domain.Result;
import learn.cosmiclearning.domain.ResultType;
import learn.cosmiclearning.models.AppUser;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository repository;
    private final PasswordEncoder encoder;
    private final Validator validator;

    public AppUserService(AppUserRepository repository,
                          PasswordEncoder encoder,
                          Validator validator) {
        this.repository = repository;
        this.encoder = encoder;
        this.validator = validator;
    }


    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return appUser;
    }

    public Result<AppUser> create(String username, String password, List<String> roles) {
        Result<AppUser> result = new Result<>();

        if (roles == null || roles.size() != 1) {
            result.addErrorMessage(ResultType.INVALID, "One role must be selected: 'STUDENT' or 'PROFESSOR'.");
            return result;
        }

        String role = roles.get(0).trim().toUpperCase();

        if (!role.equals("STUDENT") && !role.equals("PROFESSOR")) {
            result.addErrorMessage(ResultType.INVALID, "Invalid role. Must be 'STUDENT' or 'PROFESSOR'.");
            return result;
        }

        AppUser appUser = new AppUser(0, username, password, true, List.of(role));

        result = validate(appUser);
        if (!result.isSuccess()) {
            return result;
        }

        appUser.setPassword(encoder.encode(password));

        if (repository.findByUsername(username) != null) {
            result.addErrorMessage(ResultType.INVALID, "The provided username already exists");
            return result;
        }

        try {
            appUser = repository.create(appUser);
            result.setPayload(appUser);
        } catch (DuplicateKeyException ex) {
            result.addErrorMessage(ResultType.INVALID, "The provided username already exists");
        }

        return result;
    }

    private Result<AppUser> validate(AppUser appUser) {
        Result<AppUser> result = new Result<>();
        Set<ConstraintViolation<AppUser>> violations = validator.validate(appUser);

        for (var violation : violations) {
            result.addErrorMessage(violation.getMessage());
        }

        return result;
    }
}
