package cz.enehano.training.demoapp.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

//ToDo validations
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String surname;
    @Email
    private String email;
    private String phoneNumber;
    @NotNull
    private String password;
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    public User(String firstName, String surname, @Email String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Long getCreatedById() {
        return creator == null ? null : creator.getId();
    }

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
    }
}
