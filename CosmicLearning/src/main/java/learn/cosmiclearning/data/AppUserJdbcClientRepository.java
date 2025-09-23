package learn.cosmiclearning.data;

import learn.cosmiclearning.models.AppUser;

public class AppUserJdbcClientRepository implements AppUserRepository {
    @Override
    public AppUser findByUsername(String username) {
        return null;
    }

    @Override
    public AppUser create(AppUser appUser) {
        return null;
    }

    @Override
    public boolean update(AppUser appUser) {
        return false;
    }
}
