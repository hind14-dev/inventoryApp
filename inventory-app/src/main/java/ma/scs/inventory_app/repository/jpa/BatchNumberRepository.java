package ma.scs.inventory_app.repository.jpa;

import ma.scs.inventory_app.entities.BatchNumber;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BatchNumberRepository extends JpaRepository<BatchNumber, Long> {
    
    @Override
    Optional<BatchNumber> findById(Long aLong);
    
    // Override findAll to return only records where is_default_bn = 0
    @Query("SELECT b FROM BatchNumber b WHERE b.isDefaultBn = false")
    @Override
    Page<BatchNumber> findAll(Pageable pageable);
    
    // Also override the non-paginated findAll if needed
    @Query("SELECT b FROM BatchNumber b WHERE b.isDefaultBn = false")
    @Override
    List<BatchNumber> findAll();
    
    Optional<BatchNumber> findByBn(String bn);
    
    // Additional method to find all including default batch numbers if needed
    @Query("SELECT b FROM BatchNumber b")
    Page<BatchNumber> findAllIncludingDefaults(Pageable pageable);
    
    @Query("SELECT b FROM BatchNumber b")
    List<BatchNumber> findAllIncludingDefaults();
    
    Optional<BatchNumber> findFirstByIsDefaultBnTrue();
    
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BatchNumber b WHERE UPPER(b.bn) = UPPER(:bn)")
    boolean existsByBnIgnoreCase(@Param("bn") String bn);
}