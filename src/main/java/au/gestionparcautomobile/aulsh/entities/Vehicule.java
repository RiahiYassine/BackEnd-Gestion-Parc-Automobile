package au.gestionparcautomobile.aulsh.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="vehicules")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    @PastOrPresent(message = "La date d'entrée doit être dans le passé ou le présent")
    @NotNull(message = "La date d'entrée est obligatoire")
    @Column(nullable = false)
    private LocalDate dateEntree;

    @NotNull(message = "La disponibilité est obligatoire")
    @Column(nullable = false)
    private boolean disponibilite;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicule_specif_id")
    private VehiculeSpecif vehiculeSpecif;

    @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Operation> operations;

    @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Affectation> affectations;


    @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Alerte> alertes;
}
