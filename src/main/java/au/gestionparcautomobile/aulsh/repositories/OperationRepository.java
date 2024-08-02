package au.gestionparcautomobile.aulsh.repositories;

import au.gestionparcautomobile.aulsh.entities.Operation;
import au.gestionparcautomobile.aulsh.enums.TypeOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query("SELECT o FROM Operation o " +
            "JOIN FETCH o.vehicule v " +
            "JOIN FETCH v.vehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque " +
            "WHERE o.typeOperation = :typeOperation")
    List<Operation> findAllByTypeOperationWithEagerLoading(@Param("typeOperation") TypeOperation typeOperation);


    @Query("SELECT DISTINCT o.nomCentre FROM Operation o WHERE o.typeOperation = :typeOperation")
    List<String> findAllNomCentreByTypeOperation(@Param("typeOperation") TypeOperation typeOperation);

    @Query("SELECT DISTINCT o.vehicule.vehiculeSpecif.immatriculation FROM Operation o WHERE o.typeOperation = :typeOperation")
    List<String> findAllImmatriculationsByTypeOperations(@Param("typeOperation") TypeOperation typeOperation);

    @Query("SELECT DISTINCT o.vehicule.vehiculeSpecif.modele.nomModele FROM Operation o WHERE o.typeOperation = :typeOperation")
    List<String> getAllModelesByTypeOperations(@Param("typeOperation") TypeOperation typeOperation);

    @Query("SELECT DISTINCT o.vehicule.vehiculeSpecif.modele.marque.nomMarque FROM Operation o WHERE o.typeOperation = :typeOperation")
    List<String> getAllMarquesByTypeOperations(@Param("typeOperation") TypeOperation typeOperation);

    @Query("SELECT DISTINCT o.vehicule.vehiculeSpecif.typeCarburant FROM Operation o WHERE o.typeOperation = :typeOperation")
    List<String> getAllCarburantsByTypeOperations(@Param("typeOperation") TypeOperation typeOperation);


    @Query("SELECT o FROM Operation o " +
            "JOIN FETCH o.vehicule v " +
            "JOIN FETCH v.vehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque " +
            "WHERE o.typeOperation = :typeOperation AND o.dateFinValidite < :currentDate")
    List<Operation> findExpiredOperationsByTypeWithEagerLoading(@Param("typeOperation") TypeOperation typeOperation, @Param("currentDate") LocalDate currentDate);


    @Query("SELECT o FROM Operation o " +
            "JOIN FETCH o.vehicule v " +
            "JOIN FETCH v.vehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque " +
            "WHERE o.typeOperation = :typeOperation AND o.dateFinValidite >= :currentDate")
    List<Operation> findActiveOperationsByTypeWithEagerLoading(@Param("typeOperation") TypeOperation typeOperation, @Param("currentDate") LocalDate currentDate);


    @Query("SELECT o FROM Operation o " +
            "JOIN FETCH o.vehicule v " +
            "JOIN FETCH v.vehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque")
    List<Operation> findAllWithEagerLoading();


    @Query("SELECT o FROM Operation o " +
            "JOIN FETCH o.vehicule v " +
            "JOIN FETCH v.vehiculeSpecif vs " +
            "JOIN FETCH vs.modele m " +
            "JOIN FETCH m.marque " +
            "WHERE o.id = :id")
    Optional<Operation> findByIdWithEagerLoading(@Param("id") Long id);




//new


    @Query("SELECT o.typeOperation, " +
            "YEAR(o.dateOperation) as annee, " +
            "MONTH(o.dateOperation) as mois, " +
            "SUM(o.cout) as totalCout " +
            "FROM Operation o " +
            "WHERE YEAR(o.dateOperation) = :year " +
            "GROUP BY o.typeOperation, YEAR(o.dateOperation), MONTH(o.dateOperation)")
    List<Object[]> findCostsByTypeAndMonth(int year);





}
