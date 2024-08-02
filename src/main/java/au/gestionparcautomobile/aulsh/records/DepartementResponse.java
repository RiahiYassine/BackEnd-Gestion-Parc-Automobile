package au.gestionparcautomobile.aulsh.records;

import au.gestionparcautomobile.aulsh.entities.Chef;

public record DepartementResponse(
        Long id,
        String libelle,
        String description,
        Chef chef
) {
}
