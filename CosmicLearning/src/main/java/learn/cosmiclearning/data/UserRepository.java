package learn.cosmiclearning.data;

import learn.cosmiclearning.models.User;

public interface UserRepository {

    User createProfile(User user);

    User findProfileById(String id);

}
