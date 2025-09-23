package learn.cosmiclearning.data;

import learn.cosmiclearning.models.AppUser;
import learn.cosmiclearning.models.Review;
import learn.cosmiclearning.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReviewMapper implements RowMapper<Review> {

    private final List<String> roles;

    public ReviewMapper(List<String> roles) {
        this.roles = roles;
    }
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        AppUser appUser =  new AppUser(
                rs.getInt("app_user_id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getBoolean("enabled"),
                roles);

        User user = new User(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("age"),
                rs.getString("title"),
                rs.getString("university"),
                rs.getString("email"),
                rs.getInt("amount"),
                appUser);

        return new Review(
                rs.getInt("review_id"),
                rs.getString("description"),
                rs.getInt("stars"),
                rs.getDate("review_date").toLocalDate(),
                user);
    }
}
