package au.gestionparcautomobile.aulsh.services.Affectation;

import au.gestionparcautomobile.aulsh.entities.Affectation;
import au.gestionparcautomobile.aulsh.entities.Vehicule;
import au.gestionparcautomobile.aulsh.enums.Status;
import au.gestionparcautomobile.aulsh.repositories.AffectationRepository;
import au.gestionparcautomobile.aulsh.repositories.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AffectationServiceImpl implements IAffectationService{

    private final AffectationRepository affectationRepository;
    private final VehiculeRepository vehiculeRepository;

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

        return affectationRepository.save(affectation);
    }


}
