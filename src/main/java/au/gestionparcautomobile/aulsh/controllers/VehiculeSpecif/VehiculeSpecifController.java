package au.gestionparcautomobile.aulsh.controllers.VehiculeSpecif;


import au.gestionparcautomobile.aulsh.entities.VehiculeSpecif;
import au.gestionparcautomobile.aulsh.services.VehiculeSpecif.IVehiculeSpecifService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehiculespecifications")
public class VehiculeSpecifController {

    private final IVehiculeSpecifService iVehiculeSpecifService;


    @PostMapping
    public ResponseEntity<VehiculeSpecif> createVehiculeSpecif(@RequestBody VehiculeSpecif vehiculeSpecif) {
        VehiculeSpecif createdVehiculeSpecif = iVehiculeSpecifService.createVehiculeSpecif(vehiculeSpecif);
        return ResponseEntity.ok(createdVehiculeSpecif);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculeSpecif> getVehiculeSpecifById(@PathVariable Long id) {
        VehiculeSpecif vehiculeSpecif = iVehiculeSpecifService.getVehiculeSpecifById(id);
        return ResponseEntity.ok(vehiculeSpecif);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculeSpecif> updateVehiculeSpecif(@PathVariable Long id, @RequestBody VehiculeSpecif vehiculeSpecif) {
        VehiculeSpecif updatedVehiculeSpecif = iVehiculeSpecifService.updateVehiculeSpecif(id, vehiculeSpecif);
        return ResponseEntity.ok(updatedVehiculeSpecif);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehiculeSpecif(@PathVariable Long id) {
        iVehiculeSpecifService.deleteVehiculeSpecifById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<VehiculeSpecif>> getAllVehiculeSpecifs() {
        List<VehiculeSpecif> vehiculeSpecifs = iVehiculeSpecifService.getAllVehiculeSpecifs();
        return ResponseEntity.ok(vehiculeSpecifs);
    }

    @GetMapping("/immatriculations")
    public List<String> getAllImmatriculations() {
        return iVehiculeSpecifService.getAllImmatriculation();
    }

    @GetMapping("/marques")
    public List<String> getAllMarques() {
        return iVehiculeSpecifService.getAllMarques();
    }

    @GetMapping("/modeles")
    public List<String> getAllModeles() {
        return iVehiculeSpecifService.getAllModeles();
    }
}
