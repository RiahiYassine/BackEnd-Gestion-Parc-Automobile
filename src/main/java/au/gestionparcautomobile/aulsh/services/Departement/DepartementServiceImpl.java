package au.gestionparcautomobile.aulsh.services.Departement;

import au.gestionparcautomobile.aulsh.entities.Chef;
import au.gestionparcautomobile.aulsh.entities.Departement;
import au.gestionparcautomobile.aulsh.records.DepartementRequest;
import au.gestionparcautomobile.aulsh.repositories.DepartementRepository;
import au.gestionparcautomobile.aulsh.services.User.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartementServiceImpl implements IDepartementService{

    private final DepartementRepository departementRepository;
    private final IUserService iUserService;

    @Override
    public Departement createDepartement(DepartementRequest departementRequest) {

        Chef chef = departementRequest.chef();

        Departement departement = departementRequest.departement();

        departement.setChef(chef);

        return departementRepository.save(departement);

    }

    @Override
    public Departement updateDepartement(Long id,DepartementRequest departementRequest) {

        Departement existingDepartement = departementRepository.findById(id).orElseThrow(() -> new RuntimeException("departement not found with id " + id));

        iUserService.updateChef(existingDepartement.getChef().getId(),departementRequest.chef());

        existingDepartement.setLibelle(departementRequest.departement().getLibelle());
        existingDepartement.setDescription(departementRequest.departement().getDescription());

        Departement updatedDepartement = departementRepository.save(existingDepartement);

        return updatedDepartement;
    }

    @Override
    public Departement getDepartement(Long id) {
        Departement departement = departementRepository.findById(id).orElseThrow(() -> new RuntimeException("departement not found with id " + id));
        return departement;
    }

    @Override
    public List<Departement> getAllDepartement() {
        return departementRepository.findAll();
    }


    @Override
    public void deleteDepartement(Long id){
        departementRepository.deleteById(id);
    }
}
