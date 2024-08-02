package au.gestionparcautomobile.aulsh.services.VehiculeSpecif;

import au.gestionparcautomobile.aulsh.entities.Marque;
import au.gestionparcautomobile.aulsh.entities.Modele;
import au.gestionparcautomobile.aulsh.entities.VehiculeSpecif;
import au.gestionparcautomobile.aulsh.repositories.MarqueRepository;
import au.gestionparcautomobile.aulsh.repositories.ModeleRepository;
import au.gestionparcautomobile.aulsh.repositories.VehiculeSpecifRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehiculeSpecifServiceImpl implements IVehiculeSpecifService{
    private static final Logger logger = LoggerFactory.getLogger(VehiculeSpecifServiceImpl.class);

    private final VehiculeSpecifRepository vehiculeSpecifRepository;
    private final MarqueRepository marqueRepository;
    private final ModeleRepository modeleRepository;

    @Override
    public VehiculeSpecif createVehiculeSpecif(VehiculeSpecif vehiculeSpecif) {
        return vehiculeSpecifRepository.save(vehiculeSpecif);
    }

    @Override
    public VehiculeSpecif updateVehiculeSpecif(Long id, VehiculeSpecif vehiculeSpecif) {
        VehiculeSpecif existingVehiculeSpecif = vehiculeSpecifRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Véhicule spécifié introuvable avec l'id: " + id));

        Modele modele = getOrCreateModele(vehiculeSpecif.getModele());

        existingVehiculeSpecif.setNumeroChassis(vehiculeSpecif.getNumeroChassis());
        existingVehiculeSpecif.setTypeImmatriculation(vehiculeSpecif.getTypeImmatriculation());
        existingVehiculeSpecif.setImmatriculation(vehiculeSpecif.getImmatriculation());
        existingVehiculeSpecif.setNombreDePlaces(vehiculeSpecif.getNombreDePlaces());
        existingVehiculeSpecif.setPuissance(vehiculeSpecif.getPuissance());
        existingVehiculeSpecif.setPoids(vehiculeSpecif.getPoids());
        existingVehiculeSpecif.setKilometrage(vehiculeSpecif.getKilometrage());
        existingVehiculeSpecif.setModele(modele);
        existingVehiculeSpecif.setTypeCarburant(vehiculeSpecif.getTypeCarburant());
        VehiculeSpecif savedVehiculeSpecif = vehiculeSpecifRepository.save(existingVehiculeSpecif);
        return savedVehiculeSpecif;
    }

    private Modele getOrCreateModele(Modele modele) {
        Marque marque = getOrCreateMarque(modele.getMarque());
        return modeleRepository.findByNomModeleAndMarque(modele.getNomModele(), marque)
                .orElseGet(() -> {
                    Modele newModele = new Modele();
                    newModele.setNomModele(modele.getNomModele());
                    newModele.setMarque(marque);
                    return modeleRepository.save(newModele);
                });
    }

    private Marque getOrCreateMarque(Marque marqueRequest) {
        return marqueRepository.findByNomMarque(marqueRequest.getNomMarque())
                .orElseGet(() -> {
                    Marque newMarque = new Marque();
                    newMarque.setNomMarque(marqueRequest.getNomMarque());
                    return marqueRepository.save(newMarque);
                });
    }

    @Override
    @Transactional
    public void deleteVehiculeSpecifById(Long id) {

        VehiculeSpecif vehiculeSpecif = vehiculeSpecifRepository.findByIdWithEagerLoading(id)
                .orElseThrow(() -> new RuntimeException("Véhicule spécifié introuvable avec l'id: " + id));

        Long modeleId = vehiculeSpecif.getModele().getId();

        Long marqueId = vehiculeSpecif.getModele().getMarque().getId();

        vehiculeSpecifRepository.deleteById(id);

        if(!vehiculeSpecifRepository.existsByModeleId(modeleId)){
            modeleRepository.deleteById(modeleId);
        }

        if (!modeleRepository.existsByMarqueId(marqueId)) {
            marqueRepository.deleteById(marqueId);
        }

    }


    @Override
    public VehiculeSpecif getVehiculeSpecifById(Long id) {
        return vehiculeSpecifRepository.findByIdWithEagerLoading(id)
                .orElseThrow(() -> new RuntimeException("VehiculeSpecif not found with id " + id));
    }

    @Override
    public List<VehiculeSpecif> getAllVehiculeSpecifs() {
        return vehiculeSpecifRepository.findAllWithEagerLoading();
    }

    @Override
    public List<String> getAllImmatriculation() {
        return vehiculeSpecifRepository.findAllImmatriculations();
    }

    @Override
    public List<String> getAllMarques() {
        return marqueRepository.findAllMarques();
    }

    @Override
    public List<String> getAllModeles() {
        return modeleRepository.findAllModeles();
    }

}
