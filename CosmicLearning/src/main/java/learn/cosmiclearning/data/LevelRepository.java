package learn.cosmiclearning.data;

import learn.cosmiclearning.models.Level;

public interface LevelRepository {

    Level createLevel(Level level);

    boolean updateLevel(Level level);

}
