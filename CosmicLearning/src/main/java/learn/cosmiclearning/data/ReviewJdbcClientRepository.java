package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Review;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
            r.review_id,
            r.description,
            r.stars,
            s.user_id as student_user_id,
            s.first_name as student_first_name,
            s.last_name as student_last_name,
            s.age as student_age,
            s.title as student_title,
            s.university as student_university,
            s.amount as student_amount,
            s.app_user_id as student_app_user_id,
            p.user_id as professor_user_id,
            p.first_name as professor_first_name,
            p.last_name as professor_last_name,
            p.age as professor_age,
            p.title as professor_title,
            p.university as professor_university,
            p.amount as professor_amount,
            p.app_user_id as professor_app_user_id
            from review r
            join user s on r.student_id = s.user_id
            join user p on r.professor_id = p.user_id
            where p.user_id = ?
        """;

        return jdbcClient.sql(sql)
                .param(professorId)
                .query(new ReviewMapper(roles))
                .list();
    }

    @Override
    public Review addReview(Review review) {

        if (review.getReviewDate() == null) {
            review.setReviewDate(LocalDate.now());
        }

        final String sql = "insert into review (description, stars, student_id, professor_id) "
                + "values (:description, :stars, :user_id, :professor_id);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("description", review.getDescription())
                .param("stars", review.getStars())
                .param("user_id", review.getStudent().getUserId())
                .param("professor_id", review.getProfessor().getUserId())
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
                + "student_id = :student_id "
                + "professor_id = :professor_id"
                + "where review_id = :review_id;";

        return jdbcClient.sql(sql)
                .param("description", review.getDescription())
                .param("stars", review.getStars())
                .param("user_id", review.getStudent().getUserId())
                .param("professor_id", review.getProfessor().getUserId())
                .param("review_id", review.getReviewId())
                .update() > 0;
    }

    @Override
    public boolean deleteReview(int reviewId) {
        final String sql = "delete from review where review_id = ?";
        return jdbcClient.sql(sql).param(reviewId).update() > 0;
    }

    @Override
    public Review findById(int reviewId) {
        final String sql = """
        select
            r.review_id,
            r.description,
            r.stars,
            r.review_date,
            s.user_id as student_user_id,
            s.first_name as student_first_name,
            s.last_name as student_last_name,
            s.age as student_age,
            s.title as student_title,
            s.university as student_university,
            s.amount as student_amount,
            s.app_user_id as student_app_user_id,
            p.user_id as professor_user_id,
            p.first_name as professor_first_name,
            p.last_name as professor_last_name,
            p.age as professor_age,
            p.title as professor_title,
            p.university as professor_university,
            p.amount as professor_amount,
            p.app_user_id as professor_app_user_id
        from review r
        join user s on r.student_id = s.user_id
        join user p on r.professor_id = p.user_id
        where r.review_id = ?
    """;

        List<String> roles = List.of("STUDENT", "PROFESSOR");

        return jdbcClient.sql(sql)
                .param(reviewId)
                .query(new ReviewMapper(roles))
                .optional()
                .orElse(null);
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
