package au.gestionparcautomobile.aulsh.controllers.Departement;

import au.gestionparcautomobile.aulsh.entities.Departement;
import au.gestionparcautomobile.aulsh.records.DepartementRequest;
import au.gestionparcautomobile.aulsh.services.Departement.IDepartementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("departements")
public class DepartementController {

    private final IDepartementService iDepartementService;


    @PostMapping
    public ResponseEntity<Departement> createDepartement(@RequestBody DepartementRequest departementRequest) {
        Departement departement = iDepartementService.createDepartement(departementRequest);
        return ResponseEntity.ok(departement);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Departement> updateDepartement(@PathVariable Long id , @RequestBody DepartementRequest departementRequest) {
        Departement departement = iDepartementService.updateDepartement(id,departementRequest);
        return ResponseEntity.ok(departement);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departement> getDepartement(@PathVariable Long id) {
        Departement departement = iDepartementService.getDepartement(id);
        return ResponseEntity.ok(departement);
    }

    @GetMapping
    public ResponseEntity<List<Departement>> getDepartements() {
        List<Departement> departements = iDepartementService.getAllDepartement();
        return ResponseEntity.ok(departements);
    }




}
