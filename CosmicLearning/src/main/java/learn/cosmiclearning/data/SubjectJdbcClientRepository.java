package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Subject;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubjectJdbcClientRepository implements SubjectRepository {

    private final JdbcClient jdbcClient;

    public SubjectJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Subject createSubject(Subject subject) {

        final String sql = "insert into subject (name, description) " +
                "values (:name, :description);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("name", subject.getName())
                .param("description", subject.getDescription())
                .update(keyHolder, "subject_id");

        if (rowsAffected <= 0) {
            return null;
        }

        subject.setSubjectId(keyHolder.getKey().intValue());

        return subject;
    }

    @Override
    public boolean updateSubject(Subject subject) {

        final String sql = "update subject set "
                + "name = :name, "
                + "description = :description "
                + "where subject_id = :subject_id;";

        return jdbcClient.sql(sql)
                .param("name", subject.getName())
                .param("description", subject.getDescription())
                .param("subject_id", subject.getSubjectId())
                .update() > 0;
    }

    @Override
    public boolean deleteSubject(int subjectId) {
        jdbcClient.sql("delete from question where subject_id = ?;").param(subjectId).update();
        return jdbcClient.sql("delete from subject where subject_id = ?;").param(subjectId).update() > 0;
    }

    @Override
    public List<Subject> findAllSubjects() {
        String sql = """
                select
                subject_id,
                name,
                description
                from subject
                """;

        return jdbcClient
                .sql(sql)
                .query(new SubjectMapper())
                .list();
    }

    @Override
    public Subject findSubjectById(int subjectId) {

        final String sql = "select subject_id, name, description from subject "
                + "where subject_id = ?;";

        return jdbcClient.sql(sql)
                .param(subjectId)
                .query(new SubjectMapper())
                .optional()
                .orElse(null);
    }
}
