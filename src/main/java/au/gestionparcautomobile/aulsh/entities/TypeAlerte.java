package au.gestionparcautomobile.aulsh.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="typeAlertes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TypeAlerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    @NotBlank(message = "message est requis.")
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "typeAlerte", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Alerte> alertes;
}
