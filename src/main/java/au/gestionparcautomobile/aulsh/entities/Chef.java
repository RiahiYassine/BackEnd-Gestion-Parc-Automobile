package au.gestionparcautomobile.aulsh.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Table(name = "chefs")

@PrimaryKeyJoinColumn(name = "id")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Chef extends User{

    @OneToOne(mappedBy = "chef", fetch = FetchType.LAZY , orphanRemoval = true)
    @JsonIgnore
    private Departement departement;
}
