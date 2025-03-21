package com.newtonduarte.simple_twitter.repositories;

import com.newtonduarte.simple_twitter.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
