    package au.gestionparcautomobile.aulsh.services.Vehicule;

    import au.gestionparcautomobile.aulsh.entities.Vehicule;
    import au.gestionparcautomobile.aulsh.records.VehiculeFilter;
    import au.gestionparcautomobile.aulsh.records.VehiculeRequest;

    import java.time.LocalDate;
    import java.util.List;

    public interface IVehiculeService {

        Vehicule createVehicule(VehiculeRequest vehiculeRequest);
        Vehicule getVehiculeById(Long id);
        Vehicule updateVehicule(Long id, VehiculeRequest vehiculeRequest);
        void deleteVehicule(Long id);

        List<Vehicule> getAllVehicules();

        List<Vehicule> filterVehicules(VehiculeFilter filter);

        List<Vehicule> findAvailableVehicles(LocalDate startDate, LocalDate endDate);
    }
