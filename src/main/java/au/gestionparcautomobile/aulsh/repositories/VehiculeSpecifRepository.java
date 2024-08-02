package au.gestionparcautomobile.aulsh.repositories;

import au.gestionparcautomobile.aulsh.entities.VehiculeSpecif;
import au.gestionparcautomobile.aulsh.enums.TypeCarburant;
import au.gestionparcautomobile.aulsh.enums.TypeImmatriculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculeSpecifRepository extends JpaRepository<VehiculeSpecif, Long> {

    @Query("SELECT v.immatriculation FROM VehiculeSpecif v")
    List<String> findAllImmatriculations();


    @Query("SELECT vs FROM VehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque")
    List<VehiculeSpecif> findAllWithEagerLoading();

    @Query("SELECT vs FROM VehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque " +
            "WHERE vs.id = :id")
    Optional<VehiculeSpecif> findByIdWithEagerLoading(@Param("id") Long id);

    @Query("SELECT COUNT(v) > 0 FROM VehiculeSpecif v WHERE v.modele.id = :modeleId")
    boolean existsByModeleId(@Param("modeleId") Long modeleId);


}
