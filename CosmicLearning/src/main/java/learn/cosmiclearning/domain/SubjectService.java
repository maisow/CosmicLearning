package learn.cosmiclearning.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import learn.cosmiclearning.data.SubjectRepository;
import learn.cosmiclearning.models.Subject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SubjectService {


    private final SubjectRepository repository;
    private final Validator validator;

    public SubjectService(SubjectRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<Subject> findAll() {
        return repository.findAllSubjects();
    }

    public Subject findSubjectById(int id) {
        return repository.findSubjectById(id);
    }

    public Result<Subject> create(Subject subject) {
        Result<Subject> result = validate(subject);

        if (!result.isSuccess()) {
            return result;
        }

        Subject afterAdd = repository.createSubject(subject);
        result.setPayload(afterAdd);
        return result;
    }

    public Result<Void> deleteById(int id) {
        Result<Void> result  = new Result<>();
        boolean didDelete = repository.deleteSubject(id);

        if (!didDelete) {
            result.addErrorMessage(
                    ResultType.NOT_FOUND,
                    "Subject id %s not found",
                    id);
        }

        return result;
    }

    public Result<Subject> update(Subject subject) {
        Result<Subject> result = validate(subject);

        if (subject.getSubjectId() <= 0) {
            result.addErrorMessage("Subject id is required");
        }

        if (result.isSuccess()) {
            if (repository.updateSubject(subject)) {
                result.setPayload(subject);
            } else {
                result.addErrorMessage(
                        ResultType.NOT_FOUND,
                        "Subject id %s not found",
                        subject.getSubjectId());
            }
        }

        return result;
    }

    private Result<Subject> validate(Subject subject) {
        Result<Subject> result = new Result<>();

        if (subject == null) {
            result.addErrorMessage("Subject cannot be null");
            return result;
        }

        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        for (var violation : violations) {
            result.addErrorMessage(violation.getMessage());
        }

        boolean duplicateName = repository.findAllSubjects().stream()
                .anyMatch(g -> g.getSubjectId() != subject.getSubjectId() && g.getName().equalsIgnoreCase(subject.getName()));

        if (duplicateName) {
            result.addErrorMessage("Duplicate subject name: %s", subject.getName());
        }

        return result;
    }
}
