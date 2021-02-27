package fi.breakwaterworks.DAO;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByName(String name);

    @Override
    void delete(User user);
}
