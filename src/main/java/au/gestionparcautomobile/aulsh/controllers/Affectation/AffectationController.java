package au.gestionparcautomobile.aulsh.controllers.Affectation;


import au.gestionparcautomobile.aulsh.entities.Affectation;
import au.gestionparcautomobile.aulsh.entities.Employe;
import au.gestionparcautomobile.aulsh.services.Affectation.IAffectationService;
import au.gestionparcautomobile.aulsh.services.pdfGenerator.IPdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("affectations")
public class AffectationController {

    private final IAffectationService iAffectationService;
    private final IPdfGeneratorService iPdfGeneratorService;


    @PutMapping("/{id}/reject")
    public ResponseEntity<Affectation> rejectAffectation(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String motif = requestBody.get("motif");
        Affectation updatedAffectation = iAffectationService.rejectAffectation(id, motif);
        return ResponseEntity.ok(updatedAffectation);
    }


    @PutMapping("/{id}/accept")
    public ResponseEntity<Map<String, Object>> assignVehicleToAffectation(
            @PathVariable Long id, @RequestBody Map<String, Object> requestBody) {

        String motif = (String) requestBody.get("motif");
        Long vehiculeId = ((Number) requestBody.get("vehiculeId")).longValue();
        Affectation updatedAffectation = iAffectationService.assignVehicleToAffectation(id, motif, vehiculeId);

        // Prepare the response map
        Map<String, Object> response = new HashMap<>();
        response.put("affectation", updatedAffectation);
        response.put("mission", updatedAffectation.getMission());
        response.put("vehicule", updatedAffectation.getVehicule());
        response.put("listAccompagnant", updatedAffectation.getMission().getAccompagnants());

        return ResponseEntity.ok(response);
    }



    @PutMapping("/kilometrage/{id}")
    public ResponseEntity<Affectation> updateKilometrage(@PathVariable Long id, @RequestBody Map<String, Object> requestBody) {
        Long kilometrageInitial = Long.parseLong(requestBody.get("kilometrageInitial").toString());
        Long kilometrageRetour = Long.parseLong(requestBody.get("kilometrageRetour").toString());
        Affectation updatedAffectation = iAffectationService.updateKilometrage(id, kilometrageInitial, kilometrageRetour);
        return ResponseEntity.ok(updatedAffectation);
    }




}
