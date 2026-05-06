package ma.scs.inventory_app.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ma.scs.inventory_app.dto.AreaRecordsDTO;
import ma.scs.inventory_app.dto.TopAgentDTO;
import ma.scs.inventory_app.entities.DataEntry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LiveDashboardDataRepository extends JpaRepository<DataEntry, Long> {

    // ✅ Total number of records
    @Query("SELECT COUNT(d) FROM DataEntry d")
    Optional<Long> getTotalRecords();

    // ✅ Average records per day (portable JPQL, no CAST)
    // -> Count all records divided by number of distinct days
    @Query(value = """
    	       SELECT COUNT(*) * 1.0 / COUNT(DISTINCT CAST(d.date_entry AS DATE))
    	       FROM inventorydata d 
    	       """,
    	       nativeQuery = true)
    Optional<Double> getAvgRecordsPerDay();

    // ✅ Top 10 agents with record counts
    @Query("SELECT new ma.scs.inventory_app.dto.TopAgentDTO(u.fullName, COUNT(d), a.area) " +
           "FROM DataEntry d " +
           "JOIN d.user u " +
           "LEFT JOIN d.area a " +
           "GROUP BY u.id, u.fullName, a.area " +
           "ORDER BY COUNT(d) DESC")
    List<TopAgentDTO> getTopAgents(); // Use Pageable to limit TOP 10

    // ✅ Records per area
    @Query("SELECT new ma.scs.inventory_app.dto.AreaRecordsDTO(a.area, COUNT(d)) " +
           "FROM DataEntry d " +
           "JOIN d.area a " +
           "GROUP BY a.id, a.area " +
           "ORDER BY COUNT(d) DESC")
    List<AreaRecordsDTO> getRecordsPerArea();

    // ✅ Count of new records in the last day
    @Query("SELECT COUNT(d) FROM DataEntry d WHERE d.dateEntry >= :oneDayAgo")
    Optional<Long> getRecordsNumInLastDay(@Param("oneDayAgo") LocalDateTime oneDayAgo);

}
