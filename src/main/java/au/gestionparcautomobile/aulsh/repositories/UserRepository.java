package au.gestionparcautomobile.aulsh.repositories;

import au.gestionparcautomobile.aulsh.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);
}
