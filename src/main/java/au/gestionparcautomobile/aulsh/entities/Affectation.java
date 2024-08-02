package au.gestionparcautomobile.aulsh.entities;

import au.gestionparcautomobile.aulsh.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder

@Entity
@Table(name = "affectations")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Affectation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mission_id")
    @JsonIgnore
    private Mission mission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private LocalDate dateReaction;

    private String motif;

}
