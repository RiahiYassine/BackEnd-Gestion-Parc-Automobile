package au.gestionparcautomobile.aulsh.services.Mission;

import au.gestionparcautomobile.aulsh.entities.*;
import au.gestionparcautomobile.aulsh.enums.Status;
import au.gestionparcautomobile.aulsh.exceptions.NoVehiculesFoundException;
import au.gestionparcautomobile.aulsh.records.CardsInfo;
import au.gestionparcautomobile.aulsh.records.MissionFilter;
import au.gestionparcautomobile.aulsh.records.MissionRequest;
import au.gestionparcautomobile.aulsh.records.VehiculeFilter;
import au.gestionparcautomobile.aulsh.repositories.AffectationRepository;
import au.gestionparcautomobile.aulsh.repositories.DepartementRepository;
import au.gestionparcautomobile.aulsh.repositories.EmployeRepository;
import au.gestionparcautomobile.aulsh.repositories.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements IMissionService{

    private final MissionRepository missionRepository;
    private final EmployeRepository employeRepository;
    private final DepartementRepository departementRepository;
    private final AffectationRepository affectationRepository;

    @Override
    @Transactional
    public Mission createMission(MissionRequest missionRequest) {

        Employe responsable = employeRepository.findById(missionRequest.responsableId())
                .orElseThrow(() -> new RuntimeException("Employe responsable not found with id " + missionRequest.responsableId()));

        Employe chauffeur = employeRepository.findById(missionRequest.chauffeurId())
                .orElseThrow(() -> new RuntimeException("Employe chauffeur not found with id " + missionRequest.chauffeurId()));

        Departement departement = departementRepository.findById(missionRequest.departementId())
                .orElseThrow(() -> new RuntimeException("Departement not found with id " + missionRequest.departementId()));

        Mission mission = Mission.builder()
                .reference(missionRequest.reference())
                .destination(missionRequest.destination())
                .objectif(missionRequest.objectif())
                .dateDebut(missionRequest.dateDebut())
                .dateFin(missionRequest.dateFin())
                .responsable(responsable)
                .chauffeur(chauffeur)
                .departement(departement)
                .dateOrder(LocalDate.now())
                .build();

        if (missionRequest.accompagnantsIds() != null && !missionRequest.accompagnantsIds().isEmpty()) {
            List<Employe> accompagnants = employeRepository.findAllById(missionRequest.accompagnantsIds());
            mission.setAccompagnants(accompagnants);
        }


        Affectation affectation = Affectation.builder()
                .mission(mission)
                .vehicule(null) // Initially null
                .status(Status.NON_TRAITE)
                .dateReaction(null) // Initially null
                .motif(null) // Initially null
                .build();

        affectation = affectationRepository.save(affectation);


        mission.setAffectation(affectation);

        mission = missionRepository.save(mission);

        return mission;
    }


    @Override
    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Mission updateMission(Long id, MissionRequest missionRequest) {

        Mission existingMission = getMissionById(id);

        Employe responsable = employeRepository.findById(missionRequest.responsableId())
                .orElseThrow(() -> new RuntimeException("Employe responsable not found with id " + missionRequest.responsableId()));


        Employe chauffeur = employeRepository.findById(missionRequest.chauffeurId())
                .orElseThrow(() -> new RuntimeException("Employe chauffeur not found with id " + missionRequest.responsableId()));


        Departement departement = departementRepository.findById(missionRequest.departementId())
                .orElseThrow(() -> new RuntimeException("Departement not found with id " + missionRequest.departementId()));


        existingMission.setReference(missionRequest.reference());
        existingMission.setDestination(missionRequest.destination());
        existingMission.setObjectif(missionRequest.objectif());
        existingMission.setDateDebut(missionRequest.dateDebut());
        existingMission.setDateFin(missionRequest.dateFin());
        existingMission.setResponsable(responsable);
        existingMission.setChauffeur(chauffeur);
        existingMission.setDepartement(departement);

        if (missionRequest.accompagnantsIds() != null) {
            List<Employe> accompagnants = employeRepository.findAllById(missionRequest.accompagnantsIds());
            existingMission.setAccompagnants(accompagnants);
        }

        return missionRepository.save(existingMission);
    }

    @Override
    @Transactional
    public Mission getMissionById(Long id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("mission not found with id " + id));

        // Initialize the lazy-loaded associations
        Hibernate.initialize(mission.getResponsable());
        Hibernate.initialize(mission.getChauffeur());
        Hibernate.initialize(mission.getDepartement());
        Hibernate.initialize(mission.getAccompagnants());

        return mission;
    }

    @Override
    @Transactional
    public List<Mission> getAllMissions() {
        List<Mission> missions = missionRepository.findByStatus(Status.NON_TRAITE);

        for (Mission mission : missions) {
            Hibernate.initialize(mission.getResponsable());
            Hibernate.initialize(mission.getChauffeur());
            Hibernate.initialize(mission.getDepartement());
            Hibernate.initialize(mission.getAccompagnants());


            mission.getResponsable().getNom();
            mission.getChauffeur().getNom();
            mission.getDepartement().getLibelle();
            mission.getAccompagnants().size();
        }

        return missions;
    }

    @Override
    public List<String> getAllReferences() {
        return missionRepository.findAllReferences();
    }

    @Override
    public List<String> getAllDestinations() {
        return missionRepository.findAllDestinations();
    }

    @Override
    public List<String> getAllMatricules() {
        return affectationRepository.findAllMatricules();
    }

    @Override
    public List<String> getAllDepartements() {
        return missionRepository.findAllDepartements();
    }

    @Override
    public List<String> getAllResponsable() {
        return missionRepository.findAllResponsable();
    }

    @Override
    public List<String> getAllChauffeur() {
        return missionRepository.findAllChauffeur();
    }

    @Override
    public List<Mission> getAllMissionsByDepartement(Long id) {
        return missionRepository.findByDepartement(id);
    }


    @Override
    public List<Mission> filterMissions(MissionFilter filter) {
        List<Mission> allMissions = missionRepository.findAll();

        List<Mission> filteredMissions = allMissions.stream().filter(mission -> {
            boolean matches = true;

            if (filter.reference() != null && !filter.reference().isEmpty()) {
                matches = filter.reference().equalsIgnoreCase(mission.getReference());
            }

            if (filter.destination() != null && !filter.destination().isEmpty()) {
                matches = filter.destination().equalsIgnoreCase(mission.getDestination());
            }

             if (filter.status() != null && !filter.status().isEmpty()) {
                matches = matches && filter.status().equalsIgnoreCase(String.valueOf(mission.getAffectation().getStatus()));
            }


           if (filter.matricule() != null && !filter.matricule().isEmpty()) {
               matches = matches &&
                       Optional.ofNullable(mission.getAffectation())
                               .map(Affectation::getVehicule)
                               .map(Vehicule::getVehiculeSpecif)
                               .map(VehiculeSpecif::getImmatriculation)
                               .map(filter.matricule()::equalsIgnoreCase)
                               .orElse(false);
            }


            if (filter.chauffeur() != null && !filter.chauffeur().isEmpty()) {
                matches = matches && filter.chauffeur().equalsIgnoreCase(mission.getChauffeur().getNom()+ ' ' +mission.getChauffeur().getPrenom());
            }

            if (filter.responsable() != null && !filter.responsable().isEmpty()) {
                matches = matches && filter.responsable().equalsIgnoreCase(mission.getResponsable().getNom()+ ' ' +mission.getResponsable().getPrenom());
            }

            if (filter.departement() != null && !filter.departement().isEmpty()) {
                matches = filter.departement().equalsIgnoreCase(mission.getDepartement().getLibelle());
            }

            return matches;
        }).collect(Collectors.toList());

        if (filteredMissions.isEmpty()) {
            throw new NoVehiculesFoundException("Aucune Mission was found with this filtering.");
        }
        return filteredMissions;
    }



    @Override
    public List<Mission> filterMissionDepartement(Long id,MissionFilter filter) {
        List<Mission> allMissions = missionRepository.findByDepartement(id);

        List<Mission> filteredMissions = allMissions.stream().filter(mission -> {
            boolean matches = true;

            if (filter.reference() != null && !filter.reference().isEmpty()) {
                matches = filter.reference().equalsIgnoreCase(mission.getReference());
            }

            if (filter.destination() != null && !filter.destination().isEmpty()) {
                matches = matches && filter.destination().equalsIgnoreCase(mission.getDestination());
            }

            if (filter.status() != null && !filter.status().isEmpty()) {
                matches = matches && filter.status().equalsIgnoreCase(String.valueOf(mission.getAffectation().getStatus()));
            }


            if (filter.matricule() != null && !filter.matricule().isEmpty()) {
                matches = matches &&
                        Optional.ofNullable(mission.getAffectation())
                                .map(Affectation::getVehicule)
                                .map(Vehicule::getVehiculeSpecif)
                                .map(VehiculeSpecif::getImmatriculation)
                                .map(filter.matricule()::equalsIgnoreCase)
                                .orElse(false);
            }


            if (filter.chauffeur() != null && !filter.chauffeur().isEmpty()) {
                matches = matches && filter.chauffeur().equalsIgnoreCase(mission.getChauffeur().getNom()+ ' ' +mission.getChauffeur().getPrenom());
            }

            if (filter.responsable() != null && !filter.responsable().isEmpty()) {
                matches = matches && filter.responsable().equalsIgnoreCase(mission.getResponsable().getNom()+ ' ' +mission.getResponsable().getPrenom());
            }

            return matches;
        }).collect(Collectors.toList());

        if (filteredMissions.isEmpty()) {
            throw new NoVehiculesFoundException("Aucune Mission was found with this filtering.");
        }
        return filteredMissions;
    }


    @Override
    @Transactional
    public List<Mission> getAllMissionsAccepter() {
        List<Mission> missions = missionRepository.findByStatus(Status.ACCEPTE);

        for (Mission mission : missions) {
            Hibernate.initialize(mission.getResponsable());
            Hibernate.initialize(mission.getChauffeur());
            Hibernate.initialize(mission.getDepartement());
            Hibernate.initialize(mission.getAccompagnants());


            mission.getResponsable().getNom();
            mission.getChauffeur().getNom();
            mission.getDepartement().getLibelle();
            mission.getAccompagnants().size();
        }

        return missions;
    }


    @Override
    @Transactional
    public List<Mission> getAllMissionsRefuser() {
        List<Mission> missions = missionRepository.findByStatus(Status.REFUSE);

        for (Mission mission : missions) {
            Hibernate.initialize(mission.getResponsable());
            Hibernate.initialize(mission.getChauffeur());
            Hibernate.initialize(mission.getDepartement());
            Hibernate.initialize(mission.getAccompagnants());


            mission.getResponsable().getNom();
            mission.getChauffeur().getNom();
            mission.getDepartement().getLibelle();
            mission.getAccompagnants().size();
        }

        return missions;
    }

    @Override
    public long countMission() {
        return missionRepository.count();
    }


    @Override
    public long countDemandes() {
        return missionRepository.countMissionsWithNonTraiteStatus();
    }


    @Override
    public long countMissonEnCour() {
        return missionRepository.countMissonEnCour();
    }

    public List<Mission> getMissionsByDateReminder() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate dayAfter = today.plusDays(2);

        // Fetch alerts for today and tomorrow with pagination
        Pageable pageable = PageRequest.of(0, 50); // Adjust the batch size as needed
        List<Mission> missionsForToday = missionRepository.findByDateDebut(today, pageable);
        List<Mission> missionsForTomorrow = missionRepository.findByDateDebut(tomorrow, pageable);
        List<Mission> missionsForDayAfter = missionRepository.findByDateDebut(dayAfter, pageable);


        // Combine the two lists
        missionsForToday.addAll(missionsForTomorrow);
        missionsForToday.addAll(missionsForDayAfter);

        return missionsForToday;
    }

    @Override
    public List<Mission> getMissionAcceptedByDepartement(Long departementId){

        List<Mission> missions = getMissionsByDateReminder();

        List<Mission> missionsAlertes = new ArrayList<Mission>();

        for(Mission mission : missions){
            if(mission.getDepartement().getId().equals(departementId)){
                if(mission.getAffectation().getStatus().equals(Status.ACCEPTE)){
                    missionsAlertes.add(mission);
                }
            }
        }
        return missionsAlertes;
    }



}
