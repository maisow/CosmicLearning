package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Subject;

import java.util.List;

public interface SubjectRepository {

    Subject createSubject(Subject subject);

    boolean updateSubject(Subject subject);

    boolean deleteSubject(int subjectId);

}
