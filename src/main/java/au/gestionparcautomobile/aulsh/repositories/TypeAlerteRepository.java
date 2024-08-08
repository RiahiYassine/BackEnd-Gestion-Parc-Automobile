package au.gestionparcautomobile.aulsh.repositories;

import au.gestionparcautomobile.aulsh.entities.Alerte;
import au.gestionparcautomobile.aulsh.entities.TypeAlerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeAlerteRepository extends JpaRepository<TypeAlerte, Long> {

    Optional<TypeAlerte> findByName(String name);



}
