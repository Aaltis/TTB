package fi.breakwaterworks.DAO;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.model.UserRole;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<UserRole, Long> {

	UserRole findByName(String name);

    @Override
    void delete(UserRole role);

}

