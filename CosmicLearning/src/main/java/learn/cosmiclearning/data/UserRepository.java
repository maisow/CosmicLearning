package learn.cosmiclearning.data;

import learn.cosmiclearning.models.User;

public interface UserRepository {

    User createProfile(User user);

    User findProfileById(String userId, String username);

    boolean updateProfile(User user);

}
