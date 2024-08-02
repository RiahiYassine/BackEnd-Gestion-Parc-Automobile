package au.gestionparcautomobile.aulsh.entities;

import au.gestionparcautomobile.aulsh.enums.Grade;
import au.gestionparcautomobile.aulsh.enums.RoleUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Table(name="users")

@Inheritance(strategy = InheritanceType.JOINED)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Le nom de user est requis.")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom de user est requis.")
    @Column(nullable = false)
    private String prenom;

    @Email(message = "L'adresse email doit être valide.")
    @NotBlank(message = "L'email de user est requis.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Cin de user est requis.")
    @Column(nullable = false, unique = true)
    private String cin;

    @NotBlank(message = "Le mot de passe est requis.")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleUser role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;
}
