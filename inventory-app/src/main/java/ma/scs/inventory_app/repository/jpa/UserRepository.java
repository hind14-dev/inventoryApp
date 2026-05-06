package ma.scs.inventory_app.repository.jpa;



import ma.scs.inventory_app.entities.User;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User>  findByUserId(Long id);
    Optional<User> findByUserIdAndArea_AreaId(Long userId, Long areaId);
    Optional<User> findByUserIdAndRole(Long userId, String role);
}

