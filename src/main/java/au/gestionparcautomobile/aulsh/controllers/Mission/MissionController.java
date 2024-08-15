package au.gestionparcautomobile.aulsh.controllers.Mission;

import au.gestionparcautomobile.aulsh.entities.Mission;
import au.gestionparcautomobile.aulsh.entities.Vehicule;
import au.gestionparcautomobile.aulsh.exceptions.NoVehiculesFoundException;
import au.gestionparcautomobile.aulsh.records.CardsInfo;
import au.gestionparcautomobile.aulsh.records.MissionFilter;
import au.gestionparcautomobile.aulsh.records.MissionRequest;
import au.gestionparcautomobile.aulsh.records.VehiculeFilter;
import au.gestionparcautomobile.aulsh.services.Alerte.IAlerteService;
import au.gestionparcautomobile.aulsh.services.Departement.IDepartementService;
import au.gestionparcautomobile.aulsh.services.Mission.IMissionService;
import au.gestionparcautomobile.aulsh.services.User.IUserService;
import au.gestionparcautomobile.aulsh.services.Vehicule.IVehiculeService;
import au.gestionparcautomobile.aulsh.services.pdfGenerator.IPdfGeneratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("missions")
public class MissionController {

    private final IMissionService iMissionService;
    private final IVehiculeService iVehiculeService;
    private final IDepartementService iDepartementService;
    private final IUserService iUserService;
    private final IPdfGeneratorService iPdfGeneratorService;
    private final IAlerteService iAlerteService;


    @PostMapping
    public ResponseEntity<Mission> createMission(@RequestBody @Valid MissionRequest missionRequest) {
        Mission mission = iMissionService.createMission(missionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(mission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mission> updateMission(@PathVariable Long id, @RequestBody @Valid MissionRequest missionRequest) {
        Mission mission = iMissionService.updateMission(id, missionRequest);
        return ResponseEntity.ok(mission);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mission> getMission(@PathVariable Long id) {
        Mission mission = iMissionService.getMissionById(id);
        return ResponseEntity.ok(mission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        iMissionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Mission>> getAllMissions() {
        List<Mission> missions = iMissionService.getAllMissions();
        return ResponseEntity.ok(missions);
    }

    @GetMapping("/references")
    public ResponseEntity<List<String>> getAllReferences() {
        List<String> list = iMissionService.getAllReferences();
        return ResponseEntity.ok(list);
    }


    @GetMapping("/destinations")
    public ResponseEntity<List<String>> getAllDestinations() {
        List<String> list = iMissionService.getAllDestinations();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/matricules")
    public ResponseEntity<List<String>> getAllMatricules() {
        List<String> list = iMissionService.getAllMatricules();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/departements")
    public ResponseEntity<List<String>> getAllDepartements() {
        List<String> list = iMissionService.getAllDepartements();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/responsables")
    public ResponseEntity<List<String>> getAllResponsable() {
        List<String> list = iMissionService.getAllResponsable();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/chauffeurs")
    public ResponseEntity<List<String>> getAllChauffeur() {
        List<String> list = iMissionService.getAllChauffeur();
        return ResponseEntity.ok(list);
    }


    @PostMapping("/filter")
    public ResponseEntity<?> filterVehicules(@RequestBody MissionFilter filter) {
        try {
            List<Mission> missions = iMissionService.filterMissions(filter);
            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (NoVehiculesFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/departement/{id}")
    public ResponseEntity<List<Mission>> getAllMissionsByDepartementId(@PathVariable Long id) {
        List<Mission> missions = iMissionService.getAllMissionsByDepartement(id);
        return ResponseEntity.ok(missions);
    }


    @PostMapping("/departement/filter/{id}")
    public ResponseEntity<?> filterMissionDepartement(@PathVariable Long id,@RequestBody MissionFilter filter) {
        try {
            List<Mission> missions = iMissionService.filterMissionDepartement(id,filter);
            return new ResponseEntity<>(missions, HttpStatus.OK);
        } catch (NoVehiculesFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/accepter")
    public ResponseEntity<List<Mission>> getAllMissionsAccepter() {
        List<Mission> missions = iMissionService.getAllMissionsAccepter();
        return ResponseEntity.ok(missions);
    }


    @GetMapping("/refuser")
    public ResponseEntity<List<Mission>> getAllMissionsRefuser() {
        List<Mission> missions = iMissionService.getAllMissionsRefuser();
        return ResponseEntity.ok(missions);
    }


    @GetMapping("/test/{id}")
    public ResponseEntity<Map<String, Object>> testPdf(@PathVariable Long id){


        Mission mission = iMissionService.getMissionById(id);

        byte[] pdfContent = iPdfGeneratorService.generateMissionPdf(mission);


        Map<String, Object> response = new HashMap<>();
        response.put("pdf", Base64.getEncoder().encodeToString(pdfContent));  // Encode PDF as Base64 string

        return ResponseEntity.ok(response);
    }




    @GetMapping("/cards")
    public ResponseEntity<CardsInfo> getAllCardsInfo() {
        CardsInfo cardInfo = new CardsInfo(iVehiculeService.countVehicules(),iDepartementService.countDepartements(),iUserService.countEmployes(),iAlerteService.countAlertes(),iMissionService.countMissonEnCour(),iMissionService.countDemandes());
        return ResponseEntity.ok(cardInfo);
    }

}