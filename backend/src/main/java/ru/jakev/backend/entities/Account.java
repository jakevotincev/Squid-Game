package ru.jakev.backend.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

/**
 * @author evotintsev
 * @since 25.06.2023
 */
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
