package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Review;
import learn.cosmiclearning.models.Subject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectMapper implements RowMapper<Subject> {
    @Override
    public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Subject(
                rs.getInt("subject_id"),
                rs.getString("name"),
                rs.getString("description")
        );
    }
}
