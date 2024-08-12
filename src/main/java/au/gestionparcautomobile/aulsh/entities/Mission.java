package au.gestionparcautomobile.aulsh.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder

@Entity
@Table(name="missions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    @NotBlank(message = "reference mission est requis.")
    @Column(nullable = false)
    private String reference;

    @NotNull(message = "Responsable is required.")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "responsable_id")
    private Employe responsable;


    @NotNull(message = "Chauffeur is required.")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "chauffeur_id")
    private Employe chauffeur;

    @NotBlank(message = "destination mission est requis.")
    @Column(nullable = false)
    private String destination;

    @NotBlank(message = "objectif mission est requis.")
    @Column(nullable = false)
    private String objectif;

    @Future(message = "La date debut de mission doit être dans le futur.")
    @Column(nullable = false)
    private LocalDate dateDebut;

    @Future(message = "La date fin de mission doit être dans le futur.")
    @Column(nullable = false)
    private LocalDate dateFin;

    private LocalDate dateOrder;

    @NotNull
    @ManyToOne
    @JoinColumn(name="departement_id",nullable = false)
    private Departement departement;

    @OneToOne(mappedBy = "mission", fetch = FetchType.LAZY ,  orphanRemoval = true)
    private Affectation affectation;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "mission_accompagnants",
            joinColumns = { @JoinColumn(name = "mission_id") },
            inverseJoinColumns = { @JoinColumn(name = "employe_id") })
    private List<Employe> accompagnants;
}
