package au.gestionparcautomobile.aulsh.repositories;

import au.gestionparcautomobile.aulsh.entities.Alerte;
import au.gestionparcautomobile.aulsh.entities.Mission;
import au.gestionparcautomobile.aulsh.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    @Query("SELECT m.affectation.vehicule.id FROM Mission m WHERE m.dateDebut <= :endDate AND m.dateFin >= :startDate AND m.affectation.vehicule.id IS NOT NULL")
    List<Long> findVehiculeIdsInMissionPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT m.reference FROM Mission m")
    List<String> findAllReferences();

    @Query("SELECT m.destination FROM Mission m")
    List<String> findAllDestinations();


    @Query("SELECT m.departement.libelle FROM Mission m")
    List<String> findAllDepartements();

    List<Mission> findByDateDebut(LocalDate date, Pageable pageable);


    @Query("SELECT CONCAT(m.responsable.nom, ' ', m.responsable.prenom) FROM Mission m")
    List<String> findAllResponsable();

    @Query("SELECT CONCAT(m.chauffeur.nom, ' ', m.chauffeur.prenom) FROM Mission m")
    List<String> findAllChauffeur();

    @Query("SELECT m FROM Mission m WHERE m.departement.id = :departementId")
    List<Mission> findByDepartement(@Param("departementId") Long id);

    @Query("SELECT m FROM Mission m WHERE m.affectation.status =:status")
    List<Mission> findByStatus(@Param("status") Status status);


    @Query("SELECT COUNT(m) FROM Mission m WHERE m.affectation.status = 'NON_TRAITE' AND m.dateDebut>=CURRENT_DATE")
    long countMissionsWithNonTraiteStatus();

    @Query("SELECT COUNT(m) FROM Mission m WHERE m.affectation.status = 'ACCEPTE' AND m.dateDebut <= CURRENT_DATE AND m.dateFin >= CURRENT_DATE")
    long countMissonEnCour();


    long count(); // This method is provided by JpaRepository
}
