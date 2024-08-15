package au.gestionparcautomobile.aulsh.jwt;

import au.gestionparcautomobile.aulsh.records.UserResponse;

public class JwtResponse {
    private String jwt;
    private UserResponse user;

    public JwtResponse(String jwt, UserResponse user) {
        this.jwt = jwt;
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
