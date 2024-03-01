package ru.jakev.backend.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author evotintsev
 * @since 25.06.2023
 */
@Entity
public class Account implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    @Nonnull
    private String username;
    @Nonnull
    private String password;
    @Nonnull
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "account")
    private Form form;

    @Nonnull
    private Boolean participatesInGame;

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Account() {
    }

    public Integer getId() {
        return id;
    }

    @Nonnull
    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    public String getPassword() {
        return password;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(@Nonnull Role role) {
        this.role = role;
    }

    @Nonnull
    public Boolean getParticipatesInGame() {
        return participatesInGame;
    }

    public void setParticipatesInGame(@Nonnull Boolean participatesInGame) {
        this.participatesInGame = participatesInGame;
    }
}
