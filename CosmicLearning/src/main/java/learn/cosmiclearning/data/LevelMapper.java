package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Level;
import learn.cosmiclearning.models.Question;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LevelMapper implements RowMapper<Level> {
    @Override
    public Level mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new Level(
                rs.getInt("level_id"),
                rs.getInt("level"),
                rs.getInt("amount_points")
        );

    }
}
