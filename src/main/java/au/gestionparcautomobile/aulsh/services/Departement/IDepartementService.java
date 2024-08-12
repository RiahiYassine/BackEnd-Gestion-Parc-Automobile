package au.gestionparcautomobile.aulsh.services.Departement;

import au.gestionparcautomobile.aulsh.entities.Departement;
import au.gestionparcautomobile.aulsh.records.DepartementRequest;

import java.util.List;

public interface IDepartementService {

    Departement createDepartement(DepartementRequest departementRequest);
    Departement updateDepartement(Long id,DepartementRequest departementRequest);
    Departement getDepartement(Long id);
    List<Departement> getAllDepartement();
    void deleteDepartement(Long id);
}
