package au.gestionparcautomobile.aulsh.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "marques")
public class Marque {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Le nom de la marque est requis.")
    @Column(nullable = false, unique = true)
    private String nomMarque;

    @OneToMany(mappedBy = "marque", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Modele> modelesAssocies;

}
