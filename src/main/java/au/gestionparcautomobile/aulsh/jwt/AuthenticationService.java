package au.gestionparcautomobile.aulsh.jwt;

import au.gestionparcautomobile.aulsh.controllers.User.CustomUserDetails;
import au.gestionparcautomobile.aulsh.entities.Chef;
import au.gestionparcautomobile.aulsh.entities.User;
import au.gestionparcautomobile.aulsh.enums.RoleUser;
import au.gestionparcautomobile.aulsh.repositories.ChefRepository;
import au.gestionparcautomobile.aulsh.repositories.UserRepository;
import au.gestionparcautomobile.aulsh.records.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ChefRepository chefRepository;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil, ChefRepository chefRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.chefRepository = chefRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new CustomUserDetails(user);
    }

    public JwtResponse login(String email, String password, AuthenticationManager authenticationManager) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(email);
            UserResponse userResponse = new UserResponse(user.getId(), user.getNom(), user.getPrenom(), user.getEmail(), user.getCin(), user.getRole(), user.getGrade());

            if(userResponse.getRole().equals(RoleUser.CHEF_DEPARTMENT)){
                Chef chef = chefRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("chef not found with id "));
                userResponse.setDepartement(chef.getDepartement());
            }

            String token = jwtUtil.generateToken(userDetails);
            return new JwtResponse(token, userResponse);
        } catch (Exception e) {
            throw new RuntimeException("Invalid login credentials", e);
        }
    }
}
