package au.gestionparcautomobile.aulsh.repositories;

import au.gestionparcautomobile.aulsh.entities.Marque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarqueRepository extends JpaRepository<Marque, Long> {
    @Query("SELECT m.nomMarque FROM Marque m")
    List<String> findAllMarques();

    Optional<Marque> findByNomMarque(String nomMarque);
    @Query("SELECT m.nomMarque FROM Marque m")
    List<String> findAllNomMarque();
}
