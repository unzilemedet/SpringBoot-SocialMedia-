package com.bilgeadam.model.entity;

import com.bilgeadam.model.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Auth extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String activationCode;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "auth_roles",
            joinColumns = @JoinColumn(
                    name = "auth_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    //@JsonIgnore
    private List<Role> roles;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EStatus status = EStatus.PENDING;
}
