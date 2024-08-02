package au.gestionparcautomobile.aulsh.services.Operation;

import au.gestionparcautomobile.aulsh.entities.Operation;
import au.gestionparcautomobile.aulsh.entities.Vehicule;
import au.gestionparcautomobile.aulsh.enums.CategorieMaintenance;
import au.gestionparcautomobile.aulsh.enums.OperationRequest;
import au.gestionparcautomobile.aulsh.enums.TypeOperation;
import au.gestionparcautomobile.aulsh.exceptions.AssuranceNotFoundException;
import au.gestionparcautomobile.aulsh.records.*;
import au.gestionparcautomobile.aulsh.repositories.OperationRepository;
import au.gestionparcautomobile.aulsh.repositories.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements IOperationService{

    private final VehiculeRepository vehiculeRepository;
    private final OperationRepository operationRepository;

    @Override
    public Operation createOperation(OperationRequest operationRequest) throws IOException {
        Vehicule vehicule = vehiculeRepository.findByImmatriculation(operationRequest.immatriculation())
                .orElseThrow(() -> new RuntimeException("Vehicule not found"));

        MultipartFile file = operationRequest.file();
        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        Operation.OperationBuilder operationBuilder = Operation.builder()
                .dateOperation(operationRequest.dateOperation())
                .dateFinValidite(operationRequest.dateFinValidite())
                .nomCentre(operationRequest.nomCentre())
                .details(operationRequest.details())
                .cout(operationRequest.cout())
                .vehicule(vehicule)
                .file(operationRequest.file().getBytes())
                .fileName(operationRequest.fileName())
                .typeOperation(TypeOperation.valueOf(operationRequest.typeOperation()));

        if (TypeOperation.valueOf(operationRequest.typeOperation()).equals(TypeOperation.MAINTENANCE)) {
            operationBuilder.categorieMaintenance(CategorieMaintenance.valueOf(operationRequest.categorieMaintenance()));
        }

        Operation operation = operationBuilder.build();

        return operationRepository.save(operation);
    }

    @Override
    @Transactional
    public Operation updateOperation(Long id, OperationRequest operationRequest) throws IOException {
        Operation existingOperation = operationRepository.findByIdWithEagerLoading(id).orElseThrow(() -> new RuntimeException("Operation not found"));
        MultipartFile file = operationRequest.file();

        Vehicule vehicule = vehiculeRepository.findByImmatriculation(operationRequest.immatriculation())
                .orElseThrow(() -> new RuntimeException("Vehicule not found"));

        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        existingOperation.setDateOperation(operationRequest.dateOperation());
        if((existingOperation.getTypeOperation().equals(TypeOperation.ASSURANCE) ) || (existingOperation.getTypeOperation().equals(TypeOperation.VISITE_TECHNIQUE))){
            existingOperation.setDateFinValidite(operationRequest.dateFinValidite());
        }
        existingOperation.setNomCentre(operationRequest.nomCentre());
        existingOperation.setDetails(operationRequest.details());
        existingOperation.setCout(operationRequest.cout());
        existingOperation.setVehicule(vehicule);
        existingOperation.setFile(operationRequest.file().getBytes());
        existingOperation.setFileName(operationRequest.fileName());
        if(existingOperation.getTypeOperation().equals(TypeOperation.MAINTENANCE)) {
            existingOperation.setCategorieMaintenance(CategorieMaintenance.valueOf(operationRequest.categorieMaintenance()));
        }

        Hibernate.initialize(existingOperation.getVehicule());
        Hibernate.initialize(existingOperation.getVehicule().getVehiculeSpecif());
        Hibernate.initialize(existingOperation.getVehicule().getVehiculeSpecif().getModele());
        Hibernate.initialize(existingOperation.getVehicule().getVehiculeSpecif().getModele().getMarque());


        return operationRepository.save(existingOperation);
    }

    @Override
    public void deleteOperation(Long id) {
        operationRepository.deleteById(id);
    }

    @Override
    public Operation getOperationById(Long id) {
        return operationRepository.findByIdWithEagerLoading(id).orElseThrow(() -> new RuntimeException("Operation not found"));
    }

    @Override
    public List<Operation> getAllOperations() {
        return operationRepository.findAllWithEagerLoading();
    }

    @Override
    public List<Operation> getAllOperationsByTypeOperation(TypeOperation typeOperation) {
        return operationRepository.findAllByTypeOperationWithEagerLoading(typeOperation);
    }

    @Override
    public List<String> getAllCentresByTypeOperation(TypeOperation typeOperation) {
        return operationRepository.findAllNomCentreByTypeOperation(typeOperation);
    }

    @Override
    public List<String> getAllImmatriculationsByTypeOperations(TypeOperation typeOperation) {
        return operationRepository.findAllImmatriculationsByTypeOperations(typeOperation);
    }


    @Override
    public List<String> getAllMarquesByTypeOperations(TypeOperation typeOperation) {
        return operationRepository.getAllMarquesByTypeOperations(typeOperation);
    }

    @Override
    public List<String> getAllModelesByTypeOperations(TypeOperation typeOperation) {
        return operationRepository.getAllModelesByTypeOperations(typeOperation);
    }

    @Override
    public List<String> getAllCarburantsByTypeOperations(TypeOperation typeOperation) {
        return operationRepository.getAllCarburantsByTypeOperations(typeOperation);
    }


    @Override
    public List<Operation> filterAssurancesExpired(AssuranceFilter filter) {
        List<Operation> allOperations = operationRepository.findExpiredOperationsByTypeWithEagerLoading(TypeOperation.ASSURANCE,LocalDate.now());

        List<Operation> filteredOperations = allOperations.stream().filter(operation -> {
            boolean matches = true;

            //immatriculation
            if (filter.immatriculation() != null && !filter.immatriculation().isEmpty()) {
                matches = filter.immatriculation().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getImmatriculation());
            }

            //typeImmatriculation
            if (filter.typeImmatriculation() != null && !filter.typeImmatriculation().isEmpty()) {
                matches = matches && filter.typeImmatriculation().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeImmatriculation()));
            }

            //marque
            if (filter.marque() != null && !filter.marque().isEmpty()) {
                matches = matches && filter.marque().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getMarque().getNomMarque());
            }

            //modele
            if (filter.modele() != null && !filter.modele().isEmpty()) {
                matches = matches && filter.modele().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getNomModele());
            }

            //typeCarburant
            if (filter.typeCarburant() != null && !filter.typeCarburant().isEmpty()) {
                matches = matches && filter.typeCarburant().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeCarburant()));
            }

            //centre
            if (filter.centre() != null && !filter.centre().isEmpty()) {
                matches = matches && filter.centre().equalsIgnoreCase(String.valueOf(operation.getNomCentre()));
            }

            return matches;
        }).collect(Collectors.toList());

        if (filteredOperations.isEmpty()) {
            throw new AssuranceNotFoundException("Aucune assurance expired was found with this filtering.");
        }
        return filteredOperations;
    }


    @Override
    public List<Operation> filterAssurancesActive(AssuranceFilter filter) {
        List<Operation> allOperations = operationRepository.findActiveOperationsByTypeWithEagerLoading(TypeOperation.ASSURANCE,LocalDate.now());

        List<Operation> filteredOperations = allOperations.stream().filter(operation -> {
            boolean matches = true;

            //immatriculation
            if (filter.immatriculation() != null && !filter.immatriculation().isEmpty()) {
                matches = filter.immatriculation().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getImmatriculation());
            }

            //typeImmatriculation
            if (filter.typeImmatriculation() != null && !filter.typeImmatriculation().isEmpty()) {
                matches = matches && filter.typeImmatriculation().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeImmatriculation()));
            }

            //marque
            if (filter.marque() != null && !filter.marque().isEmpty()) {
                matches = matches && filter.marque().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getMarque().getNomMarque());
            }

            //modele
            if (filter.modele() != null && !filter.modele().isEmpty()) {
                matches = matches && filter.modele().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getNomModele());
            }

            //typeCarburant
            if (filter.typeCarburant() != null && !filter.typeCarburant().isEmpty()) {
                matches = matches && filter.typeCarburant().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeCarburant()));
            }

            //centre
            if (filter.centre() != null && !filter.centre().isEmpty()) {
                matches = matches && filter.centre().equalsIgnoreCase(String.valueOf(operation.getNomCentre()));
            }

            return matches;
        }).collect(Collectors.toList());

        if (filteredOperations.isEmpty()) {
            throw new AssuranceNotFoundException("Aucune assurance active was found with this filtering.");
        }
        return filteredOperations;
    }




    @Override
    public List<Operation> filterVisiteTechniqueExpired(VisiteTechniqueFilter filter) {
        List<Operation> allOperations = operationRepository.findExpiredOperationsByTypeWithEagerLoading(TypeOperation.VISITE_TECHNIQUE,LocalDate.now());

        List<Operation> filteredOperations = allOperations.stream().filter(operation -> {
            boolean matches = true;

            //immatriculation
            if (filter.immatriculation() != null && !filter.immatriculation().isEmpty()) {
                matches = filter.immatriculation().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getImmatriculation());
            }

            //typeImmatriculation
            if (filter.typeImmatriculation() != null && !filter.typeImmatriculation().isEmpty()) {
                matches = matches && filter.typeImmatriculation().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeImmatriculation()));
            }

            //marque
            if (filter.marque() != null && !filter.marque().isEmpty()) {
                matches = matches && filter.marque().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getMarque().getNomMarque());
            }

            //modele
            if (filter.modele() != null && !filter.modele().isEmpty()) {
                matches = matches && filter.modele().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getNomModele());
            }

            //typeCarburant
            if (filter.typeCarburant() != null && !filter.typeCarburant().isEmpty()) {
                matches = matches && filter.typeCarburant().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeCarburant()));
            }

            //centre
            if (filter.centre() != null && !filter.centre().isEmpty()) {
                matches = matches && filter.centre().equalsIgnoreCase(String.valueOf(operation.getNomCentre()));
            }

            return matches;
        }).collect(Collectors.toList());

        if (filteredOperations.isEmpty()) {
            throw new AssuranceNotFoundException("Aucune visite technique expired was found with this filtering.");
        }
        return filteredOperations;
    }


    @Override
    public List<Operation> filterVisiteTechniqueActive(VisiteTechniqueFilter filter) {
        List<Operation> allOperations = operationRepository.findActiveOperationsByTypeWithEagerLoading(TypeOperation.VISITE_TECHNIQUE,LocalDate.now());

        List<Operation> filteredOperations = allOperations.stream().filter(operation -> {
            boolean matches = true;

            //immatriculation
            if (filter.immatriculation() != null && !filter.immatriculation().isEmpty()) {
                matches = filter.immatriculation().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getImmatriculation());
            }

            //typeImmatriculation
            if (filter.typeImmatriculation() != null && !filter.typeImmatriculation().isEmpty()) {
                matches = matches && filter.typeImmatriculation().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeImmatriculation()));
            }

            //marque
            if (filter.marque() != null && !filter.marque().isEmpty()) {
                matches = matches && filter.marque().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getMarque().getNomMarque());
            }

            //modele
            if (filter.modele() != null && !filter.modele().isEmpty()) {
                matches = matches && filter.modele().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getNomModele());
            }

            //typeCarburant
            if (filter.typeCarburant() != null && !filter.typeCarburant().isEmpty()) {
                matches = matches && filter.typeCarburant().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeCarburant()));
            }

            //centre
            if (filter.centre() != null && !filter.centre().isEmpty()) {
                matches = matches && filter.centre().equalsIgnoreCase(String.valueOf(operation.getNomCentre()));
            }

            return matches;
        }).collect(Collectors.toList());

        if (filteredOperations.isEmpty()) {
            throw new AssuranceNotFoundException("Aucune assurance active was found with this filtering.");
        }
        return filteredOperations;
    }



    @Override
    @Transactional
    public void rescheduleExpiredAssuranceById(Long id) {
        Optional<Operation> optionalAssurance = operationRepository.findByIdWithEagerLoading(id);
        if (optionalAssurance.isPresent()) {
            Operation assurance = optionalAssurance.get();
            LocalDate dateDebut = assurance.getDateOperation();
            LocalDate dateFinValidite = assurance.getDateFinValidite();
            LocalDate currentDate = LocalDate.now();

            // Calculate the duration between dateDebut and dateFinValidite
            long duration = ChronoUnit.DAYS.between(dateDebut, dateFinValidite);
            System.out.println("hey9"+duration);
            // Update dateDebut and dateFinValidite
            assurance.setDateOperation(currentDate);
            assurance.setDateFinValidite(currentDate.plusDays(duration));
            // Save the updated assurance
            operationRepository.save(assurance);
        } else {
            throw new RuntimeException("Assurance not found with id " + id);
        }
    }

    @Override
    public List<Operation> getExpiredOperationsByType(TypeOperation typeOperation) {
        return operationRepository.findExpiredOperationsByTypeWithEagerLoading(typeOperation, LocalDate.now());
    }

    public List<Operation> getActiveOperationsByType(TypeOperation typeOperation) {
        return operationRepository.findActiveOperationsByTypeWithEagerLoading(typeOperation, LocalDate.now());
    }



    @Override
    @Transactional
    public void rescheduleExpiredVisiteTechniqueById(Long id) {
        Optional<Operation> optionalVisiteTechnique = operationRepository.findByIdWithEagerLoading(id);
        if (optionalVisiteTechnique.isPresent()) {
            Operation visiteTechnique = optionalVisiteTechnique.get();
            LocalDate dateDebut = visiteTechnique.getDateOperation();
            LocalDate dateFinValidite = visiteTechnique.getDateFinValidite();
            LocalDate currentDate = LocalDate.now();

            // Calculate the duration between dateDebut and dateFinValidite
            long duration = ChronoUnit.DAYS.between(dateDebut, dateFinValidite);

            // Update dateDebut and dateFinValidite
            visiteTechnique.setDateOperation(currentDate);
            visiteTechnique.setDateFinValidite(currentDate.plusDays(duration));

            // Save the updated assurance
            operationRepository.save(visiteTechnique);
        } else {
            throw new RuntimeException("Visite technique not found with id " + id);
        }
    }




    @Override
    public List<Operation> filterReparations(ReparationFilter filter) {
        List<Operation> allOperations = operationRepository.findAllByTypeOperationWithEagerLoading(TypeOperation.REPARATION);

        List<Operation> filteredOperations = allOperations.stream().filter(operation -> {
            boolean matches = true;

            //immatriculation
            if (filter.immatriculation() != null && !filter.immatriculation().isEmpty()) {
                matches = filter.immatriculation().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getImmatriculation());
            }

            //typeImmatriculation
            if (filter.typeImmatriculation() != null && !filter.typeImmatriculation().isEmpty()) {
                matches = matches && filter.typeImmatriculation().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeImmatriculation()));
            }

            //marque
            if (filter.marque() != null && !filter.marque().isEmpty()) {
                matches = matches && filter.marque().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getMarque().getNomMarque());
            }

            //modele
            if (filter.modele() != null && !filter.modele().isEmpty()) {
                matches = matches && filter.modele().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getNomModele());
            }

            //typeCarburant
            if (filter.typeCarburant() != null && !filter.typeCarburant().isEmpty()) {
                matches = matches && filter.typeCarburant().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeCarburant()));
            }

            //centre
            if (filter.centre() != null && !filter.centre().isEmpty()) {
                matches = matches && filter.centre().equalsIgnoreCase(String.valueOf(operation.getNomCentre()));
            }

            return matches;
        }).collect(Collectors.toList());

        if (filteredOperations.isEmpty()) {
            throw new AssuranceNotFoundException("Aucune reparation was found with this filtering.");
        }
        return filteredOperations;
    }



    @Override
    public List<Operation> filterMaintenances(MaintenanceFilter filter) {
        List<Operation> allOperations = operationRepository.findAllByTypeOperationWithEagerLoading(TypeOperation.MAINTENANCE);

        List<Operation> filteredOperations = allOperations.stream().filter(operation -> {
            boolean matches = true;

            //immatriculation
            if (filter.immatriculation() != null && !filter.immatriculation().isEmpty()) {
                matches = filter.immatriculation().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getImmatriculation());
            }

            //typeImmatriculation
            if (filter.typeImmatriculation() != null && !filter.typeImmatriculation().isEmpty()) {
                matches = matches && filter.typeImmatriculation().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeImmatriculation()));
            }

            //marque
            if (filter.marque() != null && !filter.marque().isEmpty()) {
                matches = matches && filter.marque().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getMarque().getNomMarque());
            }

            //modele
            if (filter.modele() != null && !filter.modele().isEmpty()) {
                matches = matches && filter.modele().equalsIgnoreCase(operation.getVehicule().getVehiculeSpecif().getModele().getNomModele());
            }

            //typeCarburant
            if (filter.typeCarburant() != null && !filter.typeCarburant().isEmpty()) {
                matches = matches && filter.typeCarburant().equalsIgnoreCase(String.valueOf(operation.getVehicule().getVehiculeSpecif().getTypeCarburant()));
            }

            //centre
            if (filter.centre() != null && !filter.centre().isEmpty()) {
                matches = matches && filter.centre().equalsIgnoreCase(String.valueOf(operation.getNomCentre()));
            }

            if (filter.categorieMaintenance() != null && !filter.categorieMaintenance().isEmpty()) {
                matches = matches && filter.categorieMaintenance().equalsIgnoreCase(String.valueOf(operation.getCategorieMaintenance()));
            }

            return matches;
        }).collect(Collectors.toList());

        if (filteredOperations.isEmpty()) {
            throw new AssuranceNotFoundException("Aucune maintenance was found with this filtering.");
        }
        return filteredOperations;
    }



//new

    public List<Map<String, Object>> getCostsByYear(int year) {
        List<Object[]> results = operationRepository.findCostsByTypeAndMonth(year);

        Map<TypeOperation, Map<Integer, Double>> data = new HashMap<>();

        // Initialize map with operation types and months
        for (TypeOperation type : TypeOperation.values()) {
            Map<Integer, Double> monthlyData = new HashMap<>();
            for (int i = 1; i <= 12; i++) {
                monthlyData.put(i, 0.0);
            }
            data.put(type, monthlyData);
        }

        // Populate the data map with the results from the query
        for (Object[] result : results) {
            TypeOperation typeOperation = (TypeOperation) result[0];
            int month = (int) result[2];
            double cost = (double) result[3];

            data.get(typeOperation).put(month, cost);
        }

        // Build the final result
        List<Map<String, Object>> finalResult = new ArrayList<>();

        for (Map.Entry<TypeOperation, Map<Integer, Double>> entry : data.entrySet()) {
            Map<String, Object> row = new LinkedHashMap<>();
            TypeOperation typeOperation = entry.getKey();
            Map<Integer, Double> monthlyData = entry.getValue();

            double total = 0.0;
            row.put("type", typeOperation);
            row.put("annee", year);
            for (int i = 1; i <= 12; i++) {
                double monthCost = monthlyData.get(i);
                row.put(String.valueOf(i), monthCost);
                total += monthCost;
            }
            row.put("total", total);
            finalResult.add(row);
        }

        return finalResult;
    }


}
