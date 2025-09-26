package learn.cosmiclearning.data;

import learn.cosmiclearning.models.AppUser;
import learn.cosmiclearning.models.Review;
import learn.cosmiclearning.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReviewMapper implements RowMapper<Review> {

    private final List<String> roles;

    public ReviewMapper(List<String> roles) {
        this.roles = roles;
    }
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        User student = new User(
                rs.getInt("student_user_id"),
                rs.getString("student_first_name"),
                rs.getString("student_last_name"),
                rs.getInt("student_age"),
                rs.getString("student_title"),
                rs.getString("student_university"),
                rs.getInt("student_amount"),
                rs.getInt("student_app_user_id")
        );

        User professor = new User(
                rs.getInt("professor_user_id"),
                rs.getString("professor_first_name"),
                rs.getString("professor_last_name"),
                rs.getInt("professor_age"),
                rs.getString("professor_title"),
                rs.getString("professor_university"),
                rs.getInt("professor_amount"),
                rs.getInt("professor_app_user_id")
        );

        return new Review(
                rs.getInt("review_id"),
                rs.getString("description"),
                rs.getInt("stars"),
                LocalDate.now(),
                student,
                professor
        );
    }
}
