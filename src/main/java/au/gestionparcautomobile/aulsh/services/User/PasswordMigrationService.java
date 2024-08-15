package au.gestionparcautomobile.aulsh.services.User;

import au.gestionparcautomobile.aulsh.entities.User;
import au.gestionparcautomobile.aulsh.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordMigrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void migratePasswords() {
        // Fetch all users with plain text passwords
        Iterable<User> users = userRepository.findAll();

        for (User user : users) {
            if (!isPasswordEncoded(user.getPassword())) {
                // Encode the password
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);

                // Save the updated user
                userRepository.save(user);
            }
        }
    }

    private boolean isPasswordEncoded(String password) {
        // This is a very basic check. Modify as necessary.
        return password != null && password.startsWith("$2a$");
    }
}