package au.gestionparcautomobile.aulsh.repositories;

import au.gestionparcautomobile.aulsh.entities.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {


    @Query("SELECT v FROM Vehicule v " +
            "JOIN FETCH v.vehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque " +
            "WHERE v.vehiculeSpecif.immatriculation = :immatriculation")
    Optional<Vehicule> findByImmatriculation(@Param("immatriculation") String immatriculation);
    @Query("SELECT v FROM Vehicule v " +
            "JOIN FETCH v.vehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque " +
            "WHERE v.id NOT IN (:ids)")
    List<Vehicule> findByIdNotInWithEagerLoading(@Param("ids") List<Long> ids);

    @Query("SELECT v FROM Vehicule v " +
            "JOIN FETCH v.vehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque")
    List<Vehicule> findAllWithEagerLoading();


    @Query("SELECT v FROM Vehicule v " +
            "JOIN FETCH v.vehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque " +
            "WHERE v.id = :id")
    Optional<Vehicule> findByIdWithEagerLoading(@Param("id") Long id);
}
