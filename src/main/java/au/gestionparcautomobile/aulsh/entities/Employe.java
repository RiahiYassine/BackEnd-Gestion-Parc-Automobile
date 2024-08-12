package au.gestionparcautomobile.aulsh.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Table(name = "employes")

@PrimaryKeyJoinColumn(name = "id")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Employe extends User {

    @NotNull(message = "Le département est requis.")
    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;

    @OneToMany(mappedBy = "responsable", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Mission> missionsResponsable;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },

            mappedBy = "accompagnants")
    @JsonIgnore
    private List<Mission> missionsAccompagne;


    @OneToMany(mappedBy = "chauffeur", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Mission> missionsChauffeur;

}
