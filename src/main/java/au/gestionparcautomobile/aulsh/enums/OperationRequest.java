package au.gestionparcautomobile.aulsh.enums;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;

import java.time.LocalDate;


public record OperationRequest(
        @PastOrPresent(message = "Date operation must be in the past or present")
        LocalDate dateOperation,

        LocalDate dateFinValidite,

        @NotBlank(message = "Nom de centre is required")
        String nomCentre,

        @NotBlank(message = "Détails are required")
        String details,

        @Positive(message = "Cout must be positive")
        @NotNull(message = "Cout is required")
        double cout,

        @NotNull(message = "Vehicule is required.")
        String immatriculation,

        @NotNull(message = "File is required")
        MultipartFile file,

        @NotBlank(message = "Nom fichier is required")
        String fileName,

        @NotBlank(message = "TypeOperation is required")
        String typeOperation,

        String categorieMaintenance
) {}