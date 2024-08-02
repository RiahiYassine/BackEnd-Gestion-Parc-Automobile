package au.gestionparcautomobile.aulsh.services.Affectation;

import au.gestionparcautomobile.aulsh.entities.Affectation;

public interface IAffectationService {


    Affectation rejectAffectation(Long affectationId, String motif);
    public Affectation assignVehicleToAffectation(Long affectationId, String motif, Long vehiculeId);

}
