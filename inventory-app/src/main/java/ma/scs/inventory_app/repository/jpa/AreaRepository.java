package ma.scs.inventory_app.repository.jpa;

import ma.scs.inventory_app.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByAreaId(Long areaId);

    // Check if an Area exists with this Range id but with a different Area id
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Area a WHERE a.range.id = :rangeId AND (:excludeAreaId IS NULL OR a.id <> :excludeAreaId)")
    boolean existsByRangeIdAndIdNot(@Param("rangeId") Long rangeId, @Param("excludeAreaId") Long excludeAreaId);

    Optional<Area> findAreaById(Long areaId);

    // 🔹 New method to get area by its name
    Optional<Area> findByArea(String areaName);
}
