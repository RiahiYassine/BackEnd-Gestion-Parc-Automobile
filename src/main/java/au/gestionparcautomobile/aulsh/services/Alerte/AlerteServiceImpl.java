package au.gestionparcautomobile.aulsh.services.Alerte;

import au.gestionparcautomobile.aulsh.entities.*;
import au.gestionparcautomobile.aulsh.enums.AlerteStatus;
import au.gestionparcautomobile.aulsh.exceptions.NoVehiculesFoundException;
import au.gestionparcautomobile.aulsh.records.AlerteFilter;
import au.gestionparcautomobile.aulsh.records.VehiculeFilter;
import au.gestionparcautomobile.aulsh.repositories.AlerteRepository;
import au.gestionparcautomobile.aulsh.repositories.TypeAlerteRepository;
import au.gestionparcautomobile.aulsh.services.Email.IEmailService;
import au.gestionparcautomobile.aulsh.services.Notification.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlerteServiceImpl implements IAlerteService {

    private final AlerteRepository alerteRepository;
    private final TypeAlerteRepository typeAlerteRepository;
    private final INotificationService iNotificationService;
    private final IEmailService iEmailService;

    private TypeAlerte getOrCreateTypeAlerte(TypeAlerte typeAlerte) {
        return typeAlerteRepository.findByName(typeAlerte.getName())
                .orElseGet(() -> {
                    TypeAlerte newType = new TypeAlerte();
                    newType.setName(typeAlerte.getName());
                    return typeAlerteRepository.save(newType);
                });
    }

    @Override
    public Alerte createAlerte(Alerte alerte) {
        TypeAlerte typeAlerte = getOrCreateTypeAlerte(alerte.getTypeAlerte());
        alerte.setCreatedAt(LocalDate.now());
        alerte.setStatus(AlerteStatus.NOT_DONE);
        alerte.setTypeAlerte(typeAlerte);
        return alerteRepository.save(alerte);
    }

    @Override
    public Alerte updateAlerte(Long id, Alerte alerte) {
        Alerte existingAlerte = alerteRepository.findById(id).orElseThrow(() -> new RuntimeException("Alerte not found with id " + id));

        existingAlerte.setMessage(alerte.getMessage());
        existingAlerte.setKilometrage(alerte.getKilometrage());
        existingAlerte.setDateReminder(alerte.getDateReminder());
        existingAlerte.setSeverity(alerte.getSeverity());
        TypeAlerte typeAlerte = getOrCreateTypeAlerte(alerte.getTypeAlerte());
        existingAlerte.setTypeAlerte(typeAlerte);
        existingAlerte.setVehicule(alerte.getVehicule());

        return alerteRepository.save(existingAlerte);
    }

    @Override
    public Alerte getAlerte(Long id) {
        return alerteRepository.findById(id).orElseThrow(() -> new RuntimeException("Alerte not found with id " + id));
    }

    @Override
    public void deleteAlerte(Long id) {

        Alerte alerte = getAlerte(id);

        Long typeId = alerte.getTypeAlerte().getId();

        alerteRepository.deleteById(id);

        if(!alerteRepository.existsByTypeAlerteId(typeId)){
            typeAlerteRepository.deleteById(typeId);
        }

    }

    @Override
    public List<Alerte> getAllAlertes() {
        return alerteRepository.findAll();
    }

    @Override
    public List<Alerte> getAlertesByDateReminder() {
        LocalDate today = LocalDate.now();

        LocalDate tomorrow = today.plusDays(1);

        // Fetch alerts for today and tomorrow with pagination
        Pageable pageable = PageRequest.of(0, 50); // Adjust the batch size as needed
        List<Alerte> alertsForToday = alerteRepository.findByDateReminder(today, pageable);
        List<Alerte> alertsForTomorrow = alerteRepository.findByDateReminder(tomorrow, pageable);

        // Combine the two lists
        alertsForToday.addAll(alertsForTomorrow);

        // Fetch all alerts
        List<Alerte> allAlerts = alerteRepository.findAll(); // Adjust this method according to your repository's capabilities

        // Use a Map to store alerts by their ID to ensure uniqueness
        Map<Long, Alerte> uniqueAlertsMap = new LinkedHashMap<>();

        // Add alerts from the allAlerts list if they meet the criteria
        for (Alerte alerte : allAlerts) {
            if (alerte.getKilometrage() != null) {
                Long alerteKilometrage = alerte.getKilometrage();
                Long vehiculeKilometrage = alerte.getVehicule().getVehiculeSpecif().getKilometrage();

                if (alerteKilometrage >= vehiculeKilometrage) {
                    uniqueAlertsMap.put(alerte.getId(), alerte); // Use ID as the key
                }
            }
        }

        // Add today's alerts to the map (duplicates will be overwritten)
        for (Alerte alerte : alertsForToday) {
            uniqueAlertsMap.put(alerte.getId(), alerte);
        }

        // Convert the Map values back to a List
        return new ArrayList<>(uniqueAlertsMap.values());
    }

    @Override
    public long countAlertes(){
        return getAlertesByDateReminder().size();
    }




    @Override
    @Scheduled(cron = "0 03 8 * * *", zone = "Africa/Casablanca")  // Runs at 11:53 AM Casablanca time
    public void checkAndSendNotifications() {
        List<Alerte> alertes = getAlertesByDateReminder();

        if (alertes.isEmpty()) {
            return;  // No alerts to process
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Create Thymeleaf context
        Context context = new Context();
        context.setVariable("alertes", alertes);

        // Send the email asynchronously
        String subject = "Alerts Gestion Parc Automobile";
        iEmailService.sendEmaill("yassineriahi1704@gmail.com", subject, context);
    }


    @Override
    public List<Alerte> filterAlertesEnCour(AlerteFilter filter) {
        List<Alerte> alertes = alerteRepository.findByStatus(AlerteStatus.NOT_DONE);

        List<Alerte> filteredAlertes = alertes.stream().filter(alerte -> {
            boolean matches = true;

            if (filter.typeAlerte() != null && !filter.typeAlerte().isEmpty()) {
                matches = filter.typeAlerte().equalsIgnoreCase(String.valueOf(alerte.getTypeAlerte().getName()));
            }

            if (filter.matricule() != null && !filter.matricule().isEmpty()) {
                matches = matches && filter.matricule().equalsIgnoreCase(alerte.getVehicule().getVehiculeSpecif().getImmatriculation());
            }

            if (filter.severity() != null && !filter.severity().isEmpty()) {
                matches = matches && filter.severity().equalsIgnoreCase(String.valueOf(alerte.getSeverity()));
            }


            return matches;
        }).collect(Collectors.toList());

        if (filteredAlertes.isEmpty()) {
            throw new NoVehiculesFoundException("Aucune alerte was found with this filtering.");
        }
        return filteredAlertes;
    }


    @Override
    public List<Alerte> filterAlertesDone(AlerteFilter filter) {
        List<Alerte> alertes = alerteRepository.findByStatus(AlerteStatus.DONE);

        List<Alerte> filteredAlertes = alertes.stream().filter(alerte -> {
            boolean matches = true;

            if (filter.typeAlerte() != null && !filter.typeAlerte().isEmpty()) {
                matches = filter.typeAlerte().equalsIgnoreCase(String.valueOf(alerte.getTypeAlerte().getName()));
            }

            if (filter.matricule() != null && !filter.matricule().isEmpty()) {
                matches = matches && filter.matricule().equalsIgnoreCase(alerte.getVehicule().getVehiculeSpecif().getImmatriculation());
            }

            if (filter.severity() != null && !filter.severity().isEmpty()) {
                matches = matches && filter.severity().equalsIgnoreCase(String.valueOf(alerte.getSeverity()));
            }


            return matches;
        }).collect(Collectors.toList());

        if (filteredAlertes.isEmpty()) {
            throw new NoVehiculesFoundException("Aucune alerte was found with this filtering.");
        }
        return filteredAlertes;
    }

    @Override
    public List<String> getAllTypeAlertes() {
        return alerteRepository.findAllTypeAlertes();
    }

    @Override
    public List<String> getAllAlertesMatricules() {
        return alerteRepository.findAllAlertesMatricules();
    }

    @Override
    public Alerte finishAlerte(Long id) {
        Alerte alerte = getAlerte(id);
        alerte.setStatus(AlerteStatus.DONE);
        return alerteRepository.save(alerte);
    }

    @Override
    public List<Alerte> getAllAlerteByStatus(AlerteStatus status) {
        List<Alerte> allAlertes = getAlertesByDateReminder();
        List<Alerte> alertes = new ArrayList<>();
        for (Alerte alerte : allAlertes) {
            if (alerte.getStatus().equals(status)) {
                alertes.add(alerte);
            }
        }
        return alertes;
    }
}