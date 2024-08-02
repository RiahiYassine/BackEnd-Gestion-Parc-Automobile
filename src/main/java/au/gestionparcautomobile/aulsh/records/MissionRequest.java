package au.gestionparcautomobile.aulsh.records;

import au.gestionparcautomobile.aulsh.entities.Departement;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record MissionRequest(

        @NotBlank(message = "reference mission est requis.")
        String reference,

        @NotBlank(message = "destination mission est requis.")
        String destination,

        @NotBlank(message = "objectif mission est requis.")
        String objectif,

        @NotNull(message = "date debut mission est requis")
        @Future(message = "La date debut de mission doit être dans le futur.")
        LocalDate dateDebut,

        @NotNull(message = "date fin mission est requis")
        @Future(message = "La date fin de mission doit être dans le futur.")
        LocalDate dateFin,

        @NotNull(message = "Responsable is required.")
        Long responsableId,


        @NotNull(message = "Chauffeur is required.")
        Long chauffeurId,

        @NotNull
        Long departementId,

        List<Long> accompagnantsIds

) {
}
