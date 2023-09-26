package ru.jakev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jakev.backend.entities.Form;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public interface FormRepository extends JpaRepository<Form, Long> {
}
