package au.gestionparcautomobile.aulsh.entities;


import au.gestionparcautomobile.aulsh.enums.AlerteStatus;
import au.gestionparcautomobile.aulsh.enums.SeverityLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="alertes", indexes = {
        @Index(name = "idx_date_reminder", columnList = "dateReminder")})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Alerte {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;


    private LocalDate dateReminder;

    @NotBlank(message = "message est requis.")
    @Column(nullable = false)
    private String message;

    @NotNull(message = "Severity est requis.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeverityLevel severity;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="type_id",nullable = false)
    private TypeAlerte typeAlerte;

    private LocalDate createdAt;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="vehicule_id",nullable = false)
    private Vehicule vehicule;


    private Long kilometrage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlerteStatus status;

}
