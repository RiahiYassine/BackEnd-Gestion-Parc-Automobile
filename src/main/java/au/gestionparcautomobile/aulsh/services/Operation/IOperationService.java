package au.gestionparcautomobile.aulsh.services.Operation;

import au.gestionparcautomobile.aulsh.entities.Operation;
import au.gestionparcautomobile.aulsh.enums.OperationRequest;
import au.gestionparcautomobile.aulsh.enums.TypeOperation;
import au.gestionparcautomobile.aulsh.records.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IOperationService {

    Operation createOperation(OperationRequest operationRequest) throws IOException;
    Operation updateOperation(Long id, OperationRequest operationRequest) throws IOException;
    void deleteOperation(Long id);
    Operation getOperationById(Long id);
    List<Operation> getAllOperations();

    List<Operation> getAllOperationsByTypeOperation(TypeOperation typeOperation);
    List<String> getAllCentresByTypeOperation(TypeOperation typeOperation);
    List<String> getAllImmatriculationsByTypeOperations(TypeOperation typeOperation);
    List<String> getAllMarquesByTypeOperations(TypeOperation typeOperation);
    List<String> getAllModelesByTypeOperations(TypeOperation typeOperation);
    List<String> getAllCarburantsByTypeOperations(TypeOperation typeOperation);


    List<Operation> filterAssurancesExpired(AssuranceFilter filter);
    List<Operation> filterAssurancesActive(AssuranceFilter filter);

    List<Operation> filterVisiteTechniqueExpired(VisiteTechniqueFilter filter);
    List<Operation> filterVisiteTechniqueActive(VisiteTechniqueFilter filter);

    List<Operation> filterReparations(ReparationFilter filter);


    List<Operation> filterMaintenances(MaintenanceFilter filter);

    public void rescheduleExpiredAssuranceById(Long id);


    public void rescheduleExpiredVisiteTechniqueById(Long id);

    public List<Operation> getExpiredOperationsByType(TypeOperation typeOperation);

    public List<Operation> getActiveOperationsByType(TypeOperation typeOperation);


    //new

    public List<Map<String, Object>> getCostsByYear(int year);


    List<Operation> filterCarburants(CarburantFilter filter);

}
