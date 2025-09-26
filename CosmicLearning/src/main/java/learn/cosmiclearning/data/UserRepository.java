package learn.cosmiclearning.data;

import learn.cosmiclearning.models.User;

import java.util.List;

public interface UserRepository {

    User createProfile(User user);

    User findProfileById(int userId, String username);

    boolean updateProfile(User user);

    List<User> findAllProfiles(String username);

    User findByUsername(String username);

    User findById(int userId);

    List<String> getRolesByUsername(String username);
}
