package au.gestionparcautomobile.aulsh.services.User;

import au.gestionparcautomobile.aulsh.entities.Chef;
import au.gestionparcautomobile.aulsh.entities.Employe;
import au.gestionparcautomobile.aulsh.entities.User;
import au.gestionparcautomobile.aulsh.records.EmployeFilter;
import au.gestionparcautomobile.aulsh.records.EmployeRequest;

import java.util.List;

public interface IUserService {

    //user

    //chef
    Chef updateChef(Long id, Chef chef);

    //chef
    Employe createEmploye(EmployeRequest employeRequest);
    void deleteEmploye(Long id);
    Employe updateEmploye(Long id,EmployeRequest employeRequest);
    Employe getEmployeById(Long id);

    List<Employe> getAllEmployes();
    List<Employe> getAllEmployesByDepartement(Long id);

    User authenticateUser(String email, String password);

    List<Employe> filterEmployes(Long id,EmployeFilter filter);

}
