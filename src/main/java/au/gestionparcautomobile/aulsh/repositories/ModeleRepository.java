package au.gestionparcautomobile.aulsh.repositories;

import au.gestionparcautomobile.aulsh.entities.Marque;
import au.gestionparcautomobile.aulsh.entities.Modele;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeleRepository extends JpaRepository<Modele, Long> {
    @Query("SELECT m.nomModele FROM Modele m")
    List<String> findAllModeles();

    long countByMarqueId(Long marqueId);

    Optional<Modele> findByNomModeleAndMarque(String nomModel, Marque marque);

    @Query("SELECT COUNT(m) <= 1 FROM Modele m WHERE m.marque.id = :marqueId")
    boolean existsByMarqueId(Long marqueId);

    @Query("SELECT m.nomModele FROM Modele m WHERE m.marque.id = :marqueId")
    List<String> findAllNomModelByMarqueId(@Param("marqueId") Long marqueId);

    List<Modele> findByMarqueId(Long marqueId);

}
