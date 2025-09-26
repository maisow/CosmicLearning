package learn.cosmiclearning.data;

import learn.cosmiclearning.models.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestionMapper implements RowMapper<Question> {

    private final List<String> roles;
    public QuestionMapper(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public Question mapRow(ResultSet rs, int rowNum) throws SQLException {

        User user = new User(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("age"),
                rs.getString("title"),
                rs.getString("university"),
                rs.getInt("amount"),
                rs.getInt("app_user_id"));

        Subject subject = new Subject(
                rs.getInt("subject_id"),
                rs.getString("name"),
                rs.getString("description")
        );

        return new Question(
                rs.getInt("question_id"),
                rs.getString("question"),
                rs.getString("hint"),
                subject,
                user);

    }
}
