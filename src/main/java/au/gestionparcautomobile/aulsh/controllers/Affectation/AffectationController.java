package au.gestionparcautomobile.aulsh.controllers.Affectation;


import au.gestionparcautomobile.aulsh.entities.Affectation;
import au.gestionparcautomobile.aulsh.services.Affectation.IAffectationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("affectations")
public class AffectationController {

    private final IAffectationService iAffectationService;


    @PutMapping("/{id}/reject")
    public ResponseEntity<Affectation> rejectAffectation(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String motif = requestBody.get("motif");
        Affectation updatedAffectation = iAffectationService.rejectAffectation(id, motif);
        return ResponseEntity.ok(updatedAffectation);
    }


    @PutMapping("/{id}/accept")
    public ResponseEntity<Affectation> assignVehicleToAffectation(@PathVariable Long id, @RequestBody Map<String, Object> requestBody) {
        String motif = (String) requestBody.get("motif");
        Long vehiculeId = ((Number) requestBody.get("vehiculeId")).longValue();
        Affectation updatedAffectation = iAffectationService.assignVehicleToAffectation(id, motif, vehiculeId);
        return ResponseEntity.ok(updatedAffectation);
    }

}
