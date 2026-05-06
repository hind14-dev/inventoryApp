package ma.scs.inventory_app.repository.jpa;



import ma.scs.inventory_app.entities.DataEntry;
import ma.scs.inventory_app.dto.DataEntryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DataEntryRepository extends JpaRepository<DataEntry, Long> {
	/* @Query("""
		        SELECT new com.example.dto.DataEntryDTO(
		            e.id,
		            e.sn,
		            e.pn,
		            p.description,
		            p.unit,
		            u.fullName,
		            e.bn,
		            e.quantity,
		            e.comment,
		            e.dateEntry,
		            a.id,
		            a.name,
		            u.id,
		            u.username,
		            e.pcName,
		            e.lastChange,
		            e.changedBy
		        )
		        FROM DataEntry e
		        JOIN e.area a
		        JOIN e.user u
		        JOIN PartNumber p ON e.pn = p.pn
		        """)
	 Page<DataEntry> findAllEntries(Pageable pageable);*/
    Optional<DataEntry> findBySn(String sn);
    
    List<DataEntry> findByUser_id(Long userId);
    
    
}

