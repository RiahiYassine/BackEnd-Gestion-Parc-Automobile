package au.gestionparcautomobile.aulsh.controllers.Operation;


import au.gestionparcautomobile.aulsh.entities.Operation;
import au.gestionparcautomobile.aulsh.enums.OperationRequest;
import au.gestionparcautomobile.aulsh.enums.TypeOperation;
import au.gestionparcautomobile.aulsh.exceptions.AssuranceNotFoundException;
import au.gestionparcautomobile.aulsh.records.*;
import au.gestionparcautomobile.aulsh.services.Operation.IOperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/operations")
public class OperationController {

    private final IOperationService iOperationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Operation> createOperation(@ModelAttribute @Valid OperationRequest operationRequest) throws IOException {
        Operation createdOperation = iOperationService.createOperation(operationRequest);
        return ResponseEntity.ok(createdOperation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Operation> updateOperation(@PathVariable Long id, @ModelAttribute @Valid  OperationRequest operationRequest) throws IOException {
        Operation updatedOperation = iOperationService.updateOperation(id, operationRequest);
        return ResponseEntity.ok(updatedOperation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperation(@PathVariable Long id) {
        iOperationService.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operation> getOperationById(@PathVariable Long id) {
        Operation operation = iOperationService.getOperationById(id);
        return ResponseEntity.ok(operation);
    }

    @GetMapping
    public ResponseEntity<List<Operation>> getAllOperations() {
        List<Operation> operations = iOperationService.getAllOperations();
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/typeOperation/{typeOperation}")
    public ResponseEntity<List<Operation>> getAllOperationsByTypeOperation(@PathVariable TypeOperation typeOperation) {
        List<Operation> operations = iOperationService.getAllOperationsByTypeOperation(typeOperation);
        return ResponseEntity.ok(operations);
    }


    @GetMapping("/{typeOperation}/centres")
    public ResponseEntity<List<String>> getAllCentresByTypeOperation(@PathVariable TypeOperation typeOperation) {
        List<String> centres = iOperationService.getAllCentresByTypeOperation(typeOperation);
        return ResponseEntity.ok(centres);
    }


    @GetMapping("/{typeOperation}/immatriculations")
    public ResponseEntity<List<String>> getAllImmatriculationsByTypeOperations(@PathVariable TypeOperation typeOperation) {
        List<String> immatriculations = iOperationService.getAllImmatriculationsByTypeOperations(typeOperation);
        return ResponseEntity.ok(immatriculations);
    }




    @GetMapping("/{typeOperation}/marques")
    public ResponseEntity<List<String>> getAllMarquesByTypeOperations(@PathVariable TypeOperation typeOperation) {
        List<String> marques = iOperationService.getAllMarquesByTypeOperations(typeOperation);
        return ResponseEntity.ok(marques);
    }

    @GetMapping("/{typeOperation}/modeles")
    public ResponseEntity<List<String>> getAllModelesByTypeOperations(@PathVariable TypeOperation typeOperation) {
        List<String> modeles = iOperationService.getAllModelesByTypeOperations(typeOperation);
        return ResponseEntity.ok(modeles);
    }


    @GetMapping("/{typeOperation}/carburants")
    public ResponseEntity<List<String>> getAllCarburantsByTypeOperations(@PathVariable TypeOperation typeOperation) {
        List<String> carburants = iOperationService.getAllCarburantsByTypeOperations(typeOperation);
        return ResponseEntity.ok(carburants);
    }

    @PostMapping("/assurances/expired/filter")
    public ResponseEntity<?> filterAssurancesExpired(@RequestBody AssuranceFilter filter) {
        try {
            List<Operation> assurances = iOperationService.filterAssurancesExpired(filter);
            return new ResponseEntity<>(assurances, HttpStatus.OK);
        } catch (AssuranceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/assurances/active/filter")
    public ResponseEntity<?> filterAssurancesActive(@RequestBody AssuranceFilter filter) {
        try {
            List<Operation> assurances = iOperationService.filterAssurancesActive(filter);
            return new ResponseEntity<>(assurances, HttpStatus.OK);
        } catch (AssuranceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/visitetechniques/expired/filter")
    public ResponseEntity<?> filterVisiteTechniquesExpired(@RequestBody VisiteTechniqueFilter filter) {
        try {
            List<Operation> visitetechniques = iOperationService.filterVisiteTechniqueExpired(filter);
            return new ResponseEntity<>(visitetechniques, HttpStatus.OK);
        } catch (AssuranceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/visitetechniques/active/filter")
    public ResponseEntity<?> filterVisiteTechniqueActive(@RequestBody VisiteTechniqueFilter filter) {
        try {
            List<Operation> visitetechniques = iOperationService.filterVisiteTechniqueActive(filter);
            return new ResponseEntity<>(visitetechniques, HttpStatus.OK);
        } catch (AssuranceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/assurances/reschedule/{id}")
    public void rescheduleExpiredAssuranceById(@PathVariable Long id) {
        iOperationService.rescheduleExpiredAssuranceById(id);
    }

    @GetMapping("/{typeOperation}/expired")
    public List<Operation> getExpiredOperationsByType(@PathVariable TypeOperation typeOperation) {
        return iOperationService.getExpiredOperationsByType(typeOperation);
    }


    @GetMapping("/{typeOperation}/active")
    public List<Operation> getActiveOperationsByType(@PathVariable TypeOperation typeOperation) {
        return iOperationService.getActiveOperationsByType(typeOperation);
    }


    @PutMapping("/visiteTechniques/reschedule/{id}")
    public void rescheduleExpiredVisiteTechniqueById(@PathVariable Long id) {
        iOperationService.rescheduleExpiredVisiteTechniqueById(id);
    }



    @PostMapping("/reparations/filter")
    public ResponseEntity<?> filterReparations(@RequestBody ReparationFilter filter) {
        try {
            List<Operation> reparations = iOperationService.filterReparations(filter);
            return new ResponseEntity<>(reparations, HttpStatus.OK);
        } catch (AssuranceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/maintenances/filter")
    public ResponseEntity<?> filterMaintenances(@RequestBody MaintenanceFilter filter) {
        try {
            List<Operation> maintenances = iOperationService.filterMaintenances(filter);
            return new ResponseEntity<>(maintenances, HttpStatus.OK);
        } catch (AssuranceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Operation operation = iOperationService.getOperationById(id);
        if (operation == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(operation.getFile());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + operation.getFileName() + "\"")
                .body(resource);
    }


    @GetMapping("/costs/{year}")
    public List<Map<String, Object>> getCostsByYear(@PathVariable int year) {
        return iOperationService.getCostsByYear(year);
    }
}
