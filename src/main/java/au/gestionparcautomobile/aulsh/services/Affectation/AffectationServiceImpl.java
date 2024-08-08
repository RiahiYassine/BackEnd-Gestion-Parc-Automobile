package au.gestionparcautomobile.aulsh.services.Affectation;

import au.gestionparcautomobile.aulsh.entities.Affectation;
import au.gestionparcautomobile.aulsh.entities.Mission;
import au.gestionparcautomobile.aulsh.entities.Vehicule;
import au.gestionparcautomobile.aulsh.enums.Status;
import au.gestionparcautomobile.aulsh.enums.StatusVehicule;
import au.gestionparcautomobile.aulsh.repositories.AffectationRepository;
import au.gestionparcautomobile.aulsh.repositories.MissionRepository;
import au.gestionparcautomobile.aulsh.repositories.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AffectationServiceImpl implements IAffectationService{

    private final AffectationRepository affectationRepository;
    private final VehiculeRepository vehiculeRepository;
    private final MissionRepository missionRepository;

    @Override
    @Transactional
    public Affectation rejectAffectation(Long affectationId, String motif) {
        Affectation affectation = affectationRepository.findById(affectationId)
                .orElseThrow(() -> new RuntimeException("Affectation not found with id " + affectationId));

        affectation.setStatus(Status.REFUSE);
        affectation.setDateReaction(LocalDate.now());
        affectation.setMotif(motif);

        return affectationRepository.save(affectation);
    }

    @Override
    @Transactional
    public Affectation assignVehicleToAffectation(Long affectationId, String motif, Long vehiculeId) {
        Affectation affectation = affectationRepository.findById(affectationId)
                .orElseThrow(() -> new RuntimeException("Affectation not found with id " + affectationId));

        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Vehicule not found with id " + vehiculeId));

        affectation.setVehicule(vehicule);
        affectation.setMotif(motif);
        affectation.setStatus(Status.ACCEPTE);  // or any other appropriate status
        affectation.setDateReaction(LocalDate.now());
        affectation.setKilometrageInitial(vehicule.getVehiculeSpecif().getKilometrage());

        Affectation updatedAffectation = affectationRepository.save(affectation);

        return updatedAffectation;
    }

    @Override
    public Affectation updateKilometrage(Long id, Long kilometrageInitial, Long kilometrageRetour) {
        Affectation affectation = affectationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Affectation not found with id " + id));

        Vehicule vehicule = vehiculeRepository.findById(affectation.getVehicule().getId())
                .orElseThrow(() -> new RuntimeException("Vehicule not found with id " + affectation.getVehicule().getId()));


        vehicule.getVehiculeSpecif().setKilometrage(kilometrageRetour);
        vehiculeRepository.save(vehicule); // Save the updated vehicule entity
        affectation.setKilometrageRetour(kilometrageRetour);


        return affectationRepository.save(affectation);
    }


    @Scheduled(cron = "0 30 11 * * ?", zone = "Africa/Casablanca")
    public void updateVehiculeStatus() {
        LocalDate today = LocalDate.now(ZoneId.of("Africa/Casablanca"));

        List<Mission> missions = missionRepository.findByStatus(Status.ACCEPTE);

        for (Mission mission : missions) {
            Vehicule vehicule = mission.getAffectation().getVehicule();

            if (mission.getDateDebut().equals(today)) {
                vehicule.setStatusVehicule(StatusVehicule.EN_SERVICE);
                vehiculeRepository.save(vehicule);
            }

            if (mission.getDateFin().equals(today)) {
                vehicule.setStatusVehicule(StatusVehicule.EN_PARC);
                vehiculeRepository.save(vehicule);
            }
        }
    }


}
