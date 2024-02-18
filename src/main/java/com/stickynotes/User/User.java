package com.stickynotes.User;

import com.stickynotes.notes.Note;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    private String email;
    private String password;

    private Boolean isActive=false;

    @ManyToMany(fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"
                    ,referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id" ,
            referencedColumnName = "id" )
    )
    private List<Role> roles;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Note> notes;


    public User(String firstName, String lastName, String email, String password, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
