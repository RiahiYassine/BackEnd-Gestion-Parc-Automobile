package au.gestionparcautomobile.aulsh.services.Vehicule;

import au.gestionparcautomobile.aulsh.entities.Marque;
import au.gestionparcautomobile.aulsh.entities.Modele;
import au.gestionparcautomobile.aulsh.entities.Vehicule;
import au.gestionparcautomobile.aulsh.entities.VehiculeSpecif;
import au.gestionparcautomobile.aulsh.exceptions.NoVehiculesFoundException;
import au.gestionparcautomobile.aulsh.records.VehiculeFilter;
import au.gestionparcautomobile.aulsh.records.VehiculeRequest;
import au.gestionparcautomobile.aulsh.repositories.*;
import au.gestionparcautomobile.aulsh.services.VehiculeSpecif.IVehiculeSpecifService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final IVehiculeSpecifService iVehiculeSpecifService;
    private final VehiculeSpecifRepository vehiculeSpecifRepository;
    private final MarqueRepository marqueRepository;
    private final ModeleRepository modeleRepository;
    private final MissionRepository missionRepository;

    @Override
    @Transactional
    public Vehicule createVehicule(VehiculeRequest vehiculeRequest) {

        VehiculeSpecif vehiculeSpecif = vehiculeRequest.vehiculeSpecif();
        Modele modele = getOrCreateModele(vehiculeSpecif.getModele());

        // Save the VehiculeSpecif
        vehiculeSpecif.setModele(modele);
        vehiculeSpecif = vehiculeSpecifRepository.save(vehiculeSpecif);
        // Save the Vehicule
        Vehicule vehicule = vehiculeRequest.vehicule();
        vehicule.setVehiculeSpecif(vehiculeSpecif);
        return vehiculeRepository.save(vehicule);


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
    @Transactional(readOnly = true)
    public Vehicule getVehiculeById(Long id) {
        return vehiculeRepository.findByIdWithEagerLoading(id)
                .orElseThrow(() -> new RuntimeException("Vehicule not found with id " + id));
    }

    @Override
    @Transactional
    public Vehicule updateVehicule(Long id, VehiculeRequest vehiculeRequest) {

        Vehicule existingVehicule = vehiculeRepository.findByIdWithEagerLoading(id).orElseThrow(() -> new IllegalArgumentException("Vehicule not found with id: " + id));
        iVehiculeSpecifService.updateVehiculeSpecif(existingVehicule.getVehiculeSpecif().getId(),vehiculeRequest.vehiculeSpecif());
        existingVehicule.setDateEntree(vehiculeRequest.vehicule().getDateEntree());
        existingVehicule.setDisponibilite(vehiculeRequest.vehicule().isDisponibilite());


        Vehicule updatedVehicule = vehiculeRepository.save(existingVehicule);


        return updatedVehicule;
    }

    @Override
    @Transactional
    public void deleteVehicule(Long id) {

        Vehicule vehicule = vehiculeRepository.findByIdWithEagerLoading(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicule not found with id: " + id));

        iVehiculeSpecifService.deleteVehiculeSpecifById(vehicule.getVehiculeSpecif().getId());

        vehiculeRepository.deleteById(id);

    }

    @Override
    @Transactional
    public List<Vehicule> getAllVehicules() {
        List<Vehicule> vehicules = vehiculeRepository.findAllWithEagerLoading();

        // Initialize lazy-loaded properties
        vehicules.forEach(vehicule -> {
            Hibernate.initialize(vehicule.getVehiculeSpecif());
            // Initialize other lazy-loaded properties if needed
        });

        return vehicules;
    }

    @Override
    public List<Vehicule> filterVehicules(VehiculeFilter filter) {
        List<Vehicule> allVehicules = vehiculeRepository.findAllWithEagerLoading();

        List<Vehicule> filteredVehicules = allVehicules.stream().filter(vehicule -> {
            boolean matches = true;

            if (filter.immatriculation() != null && !filter.immatriculation().isEmpty()) {
                matches = filter.immatriculation().equalsIgnoreCase(vehicule.getVehiculeSpecif().getImmatriculation());
            }

            if (filter.typeImmatriculation() != null && !filter.typeImmatriculation().isEmpty()) {
                matches = matches && filter.typeImmatriculation().equalsIgnoreCase(String.valueOf(vehicule.getVehiculeSpecif().getTypeImmatriculation()));
            }

            if (filter.marque() != null && !filter.marque().isEmpty()) {
                matches = matches && filter.marque().equalsIgnoreCase(vehicule.getVehiculeSpecif().getModele().getMarque().getNomMarque());
            }

            if (filter.modele() != null && !filter.modele().isEmpty()) {
                matches = matches && filter.modele().equalsIgnoreCase(vehicule.getVehiculeSpecif().getModele().getNomModele());
            }

            if (filter.typeCarburant() != null && !filter.typeCarburant().isEmpty()) {
                matches = matches && filter.typeCarburant().equalsIgnoreCase(String.valueOf(vehicule.getVehiculeSpecif().getTypeCarburant()));
            }

            if (filter.disponibilite() != null && !filter.typeCarburant().isEmpty()) {
                matches = matches && filter.disponibilite() == vehicule.isDisponibilite();
            }

            return matches;
        }).collect(Collectors.toList());

        if (filteredVehicules.isEmpty()) {
            throw new NoVehiculesFoundException("Aucune vehicule was found with this filtering.");
        }
        return filteredVehicules;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findAvailableVehicles(LocalDate startDate, LocalDate endDate) {

        System.out.println("date debut: "+startDate);
        System.out.println("date fin: "+endDate);

        List<Long> occupiedVehiculeIds = missionRepository.findVehiculeIdsInMissionPeriod(startDate, endDate);

        System.out.println(occupiedVehiculeIds);

        if (occupiedVehiculeIds.isEmpty()) {
            return vehiculeRepository.findAll();
        } else {
            return vehiculeRepository.findByIdNotInWithEagerLoading(occupiedVehiculeIds);
        }
    }

}
