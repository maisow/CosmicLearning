package learn.cosmiclearning.data;

import learn.cosmiclearning.models.AppUser;

public interface AppUserRepository {

    AppUser findByUsername(String username);

    AppUser create(AppUser appUser);

    boolean update(AppUser appUser);
}
