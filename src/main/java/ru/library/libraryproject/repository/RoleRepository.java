package ru.library.libraryproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.library.libraryproject.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}