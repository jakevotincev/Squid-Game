package ru.jakev.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jakev.backend.entities.Role;

/**
 * @author evotintsev
 * @since 25.06.2023
 */
@Data
@NoArgsConstructor
//todo: make immutable
public class AccountDTO {
    private Integer id;
    private String name;
    private Role role;
}
