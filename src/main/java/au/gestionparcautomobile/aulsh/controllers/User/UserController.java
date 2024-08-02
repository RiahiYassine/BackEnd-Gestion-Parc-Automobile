package au.gestionparcautomobile.aulsh.controllers.User;

import au.gestionparcautomobile.aulsh.entities.Chef;
import au.gestionparcautomobile.aulsh.entities.Employe;
import au.gestionparcautomobile.aulsh.entities.Mission;
import au.gestionparcautomobile.aulsh.entities.User;
import au.gestionparcautomobile.aulsh.exceptions.NoVehiculesFoundException;
import au.gestionparcautomobile.aulsh.records.*;
import au.gestionparcautomobile.aulsh.services.User.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final IUserService iUserService;


    @PostMapping
    public ResponseEntity<Employe> createEmploye(@RequestBody EmployeRequest EmployeRequest) {
        Employe employe = iUserService.createEmploye(EmployeRequest);
        return ResponseEntity.ok(employe);
    }

    @GetMapping("/employes/{id}")
    public ResponseEntity<Employe> getEmployeById(@PathVariable Long id) {
        Employe employe = iUserService.getEmployeById(id);
        return ResponseEntity.ok(employe);
    }

    @PutMapping("/employes/{id}")
    public ResponseEntity<Employe> updateEmploye(@PathVariable Long id, @RequestBody EmployeRequest employeRequest) {
        Employe updatedEmploye = iUserService.updateEmploye(id, employeRequest);
        return ResponseEntity.ok(updatedEmploye);
    }

    @DeleteMapping("/employes/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        iUserService.deleteEmploye(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employes")
    public ResponseEntity<List<Employe>> getAllEmployes() {
        List<Employe> employes = iUserService.getAllEmployes();
        return ResponseEntity.ok(employes);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = iUserService.authenticateUser(loginRequest.email(), loginRequest.password());
        if (user != null) {

            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getNom(),
                    user.getPrenom(),
                    user.getEmail(),
                    user.getCin(),
                    user.getRole(),
                    user.getGrade()
            );


            if (user instanceof Chef) {
                Chef chef = (Chef) user;
                userResponse.setDepartement(chef.getDepartement());
            } else if (user instanceof Employe) {
                Employe employe = (Employe) user;
                userResponse.setDepartement(employe.getDepartement());
            }
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/employes/departement/{id}")
    public ResponseEntity<List<Employe>> getAllEmployesByDepartement(@PathVariable Long id) {
        List<Employe> employes = iUserService.getAllEmployesByDepartement(id);
        return ResponseEntity.ok(employes);
    }

    @PostMapping("/employes/filter/{id}")
    public ResponseEntity<?> filterEmployes(@PathVariable Long id,@RequestBody EmployeFilter filter) {
        try {
            List<Employe> employes = iUserService.filterEmployes(id,filter);
            return new ResponseEntity<>(employes, HttpStatus.OK);
        } catch (NoVehiculesFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
