package au.gestionparcautomobile.aulsh.controllers.Vehicule;

import au.gestionparcautomobile.aulsh.entities.Vehicule;
import au.gestionparcautomobile.aulsh.exceptions.NoVehiculesFoundException;
import au.gestionparcautomobile.aulsh.records.VehiculeFilter;
import au.gestionparcautomobile.aulsh.records.VehiculeRequest;
import au.gestionparcautomobile.aulsh.services.Vehicule.IVehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicules")
public class VehiculeController {

    private final IVehiculeService iVehiculeService;

    @PostMapping
    public ResponseEntity<Vehicule> createVehicule(@RequestBody VehiculeRequest vehiculeRequest) {
        Vehicule createdVehicule = iVehiculeService.createVehicule(vehiculeRequest);
        return ResponseEntity.ok(createdVehicule);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicule> getVehiculeById(@PathVariable Long id) {
        Vehicule vehicule = iVehiculeService.getVehiculeById(id);
        return ResponseEntity.ok(vehicule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicule> updateVehicule(@PathVariable Long id, @RequestBody VehiculeRequest vehiculeRequest) {
        Vehicule updatedVehicule = iVehiculeService.updateVehicule(id, vehiculeRequest);
        return ResponseEntity.ok(updatedVehicule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        iVehiculeService.deleteVehicule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Vehicule>> getAllVehicules() {
        List<Vehicule> vehicules = iVehiculeService.getAllVehicules();
        return ResponseEntity.ok(vehicules);
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterVehicules(@RequestBody VehiculeFilter filter) {
        try {
            List<Vehicule> vehicules = iVehiculeService.filterVehicules(filter);
            return new ResponseEntity<>(vehicules, HttpStatus.OK);
        } catch (NoVehiculesFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Vehicule>> getAvailableVehicles(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Vehicule> availableVehicles = iVehiculeService.findAvailableVehicles(startDate, endDate);
        return ResponseEntity.ok(availableVehicles);
    }

}
