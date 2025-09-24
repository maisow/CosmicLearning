package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Review;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sun.tools.attach.VirtualMachine.list;

@Repository
public class ReviewJdbcClientRepository implements ReviewRepository{


    private final JdbcClient jdbcClient;

    public ReviewJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Review> findReviewsByProfessor(int professorId, String username) {
        List<String> roles = getRolesByUsername(username);

        final String sql = """
            select
            r.review_id, r.description, r.stars, r.review_date,
            u.user_id, u.first_name, u.last_name, u.age, u.title, u.university, u.email, u.amount,
            ap.app_user_id, ap.username, ap.password_hash, ap.enabled
            from review r
            join user u on r.user_id = u.user_id
            join app_user ap on u.app_user_id = ap.app_user_id
            join app_user_role aur on ap.app_user_id = aur.app_user_id
            join app_role ar on aur.app_role_id = ar.app_role_id
            where ar.name = 'student'
          and r.professor_user_id = ?
        """;

        return jdbcClient.sql(sql)
                .param(professorId)
                .query(new ReviewMapper(roles)) // Or use List.of("ROLE_STUDENT")
                .list();
    }

    @Override
    public Review addReview(Review review) {
        final String sql = "insert into review (description, stars, review_date, user_id) "
                + "values (:description, :stars, :review_date, :user_id);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("description", review.getDescription())
                .param("stars", review.getStars())
                .param("review_date", review.getReviewDate())
                .param("user_id", review.getStudent().getUserId())
                .update(keyHolder, "review_id");

        if (rowsAffected <= 0) {
            return null;
        }

        review.setReviewId(keyHolder.getKey().intValue());

        return review;

    }

    @Override
    public boolean updateReview(Review review) {
        final String sql = "update review set "
                + "description = :description, "
                + "stars = :stars, "
                + "review_date = :review_date, "
                + "user_id = :user_id "
                + "where review_id = :review_id;";

        return jdbcClient.sql(sql)
                .param("description", review.getDescription())
                .param("stars", review.getStars())
                .param("review_date", review.getReviewDate())
                .param("user_id", review.getStudent().getUserId())
                .param("review_id", review.getReviewId())
                .update() > 0;
    }

    @Override
    public boolean deleteReview(int reviewId) {
        final String sql = "delete from review where review_id = ?";
        return jdbcClient.sql(sql).param(reviewId).update() > 0;
    }

    private List<String> getRolesByUsername(String username) {
        final String sql = "select r.name "
                + "from app_user_role ur "
                + "inner join app_role r on ur.app_role_id = r.app_role_id "
                + "inner join app_user au on ur.app_user_id = au.app_user_id "
                + "where au.username = ?";
        return jdbcClient.sql(sql)
                .param(username)
                .query((rs, rowId) -> rs.getString("name"))
                .list();
    }
}
