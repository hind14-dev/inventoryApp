package ma.scs.inventory_app.repository.jpa;

import ma.scs.inventory_app.entities.PartNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PartNumberRepository extends JpaRepository<PartNumber, Long> {
    
    // Your existing method
    Optional<PartNumber> findByPn(String pn);
    
    // ============ BATCH OPERATIONS FOR FILE UPLOAD ============
    
    /**
     * Find multiple part numbers by their PN values
     * Useful for checking which parts already exist before bulk insert
     */
    @Query("SELECT p FROM PartNumber p WHERE p.pn IN :pnList")
    List<PartNumber> findByPnIn(@Param("pnList") List<String> pnList);
    
    /**
     * Check if part numbers exist - returns only the PN strings that exist
     * More efficient when you only need to know which ones exist
     */
    @Query("SELECT p.pn FROM PartNumber p WHERE p.pn IN :pnList")
    Set<String> findExistingPns(@Param("pnList") List<String> pnList);
    
    /**
     * Batch update - useful for updating existing records from Excel
     * Based on your actual database structure
     */
    @Modifying
    @Transactional
    @Query("UPDATE PartNumber p SET p.pnDesc = :pnDesc, p.price = :price, " +
           "p.totalStock = :totalStock, p.unit = :unit WHERE p.pn = :pn")
    int updatePartNumber(@Param("pn") String pn, 
                        @Param("pnDesc") String pnDesc,
                        @Param("price") Float price, 
                        @Param("totalStock") Float totalStock,
                        @Param("unit") String unit);
    
    /**
     * Delete part numbers by PN list - useful for cleanup operations
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM PartNumber p WHERE p.pn IN :pnList")
    int deleteByPnIn(@Param("pnList") List<String> pnList);
    
    /**
     * Count existing part numbers from a list
     * Useful for validation before processing
     */
    @Query("SELECT COUNT(p) FROM PartNumber p WHERE p.pn IN :pnList")
    long countByPnIn(@Param("pnList") List<String> pnList);
    
    /**
     * Find duplicates within the database
     * Useful for data integrity checks
     */
    @Query("SELECT p.pn, COUNT(p) FROM PartNumber p GROUP BY p.pn HAVING COUNT(p) > 1")
    List<Object[]> findDuplicatePns();
    
    /**
     * Custom method for paginated search - useful for large datasets
     * Based on your actual database fields
     */
    @Query("SELECT p FROM PartNumber p WHERE " +
           "(:pn IS NULL OR p.pn LIKE %:pn%) AND " +
           "(:pnDesc IS NULL OR p.pnDesc LIKE %:pnDesc%) AND " +
           "(:unit IS NULL OR p.unit LIKE %:unit%)")
    List<PartNumber> findWithFilters(@Param("pn") String pn, 
                                   @Param("pnDesc") String pnDesc,
                                   @Param("unit") String unit);
    
    // ============ UTILITY METHODS FOR FILE PROCESSING ============
    
    /**
     * Check if PN already exists - simple boolean check
     */
    boolean existsByPn(String pn);
    
    /**
     * Find all PNs that match a pattern - useful for validation
     */
    @Query("SELECT p FROM PartNumber p WHERE p.pn LIKE :pattern")
    List<PartNumber> findByPnPattern(@Param("pattern") String pattern);
}