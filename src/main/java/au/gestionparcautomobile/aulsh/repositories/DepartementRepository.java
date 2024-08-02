package au.gestionparcautomobile.aulsh.repositories;

import au.gestionparcautomobile.aulsh.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {

    @Query("SELECT d.id, d.libelle, d.description, c FROM Departement d LEFT JOIN d.chef c WHERE d.id = :id")
    Optional<Departement> findDepartementById(Long id);

}
