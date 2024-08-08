package au.gestionparcautomobile.aulsh.repositories;

import au.gestionparcautomobile.aulsh.entities.Alerte;
import au.gestionparcautomobile.aulsh.enums.AlerteStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Long> {

    List<Alerte> findByDateReminder(LocalDate date, Pageable pageable);

    @Query("SELECT DISTINCT a.typeAlerte.name FROM Alerte a")
    List<String> findAllTypeAlertes();

    @Query("SELECT DISTINCT a.vehicule.vehiculeSpecif.immatriculation FROM Alerte a")
    List<String> findAllAlertesMatricules();


    List<Alerte> findByStatus(AlerteStatus status);

    @Query("SELECT COUNT(a) > 0 FROM Alerte a WHERE a.typeAlerte.id = :typeId")
    boolean existsByTypeAlerteId(@Param("typeId") Long typeId);

}
