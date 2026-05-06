package ma.scs.inventory_app.repository.jpa;

import ma.scs.inventory_app.entities.Range;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRangeRepository extends JpaRepository<Range, Long> {
    List<Range> findByPrefix(String prefix);
}
