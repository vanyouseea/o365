package hqr.o365.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hqr.o365.domain.TaOfficeInfo;

@Repository
public interface TaOfficeInfoRepo extends JpaRepository<TaOfficeInfo, Integer> {
	
	@Transactional
    @Modifying
	@Query(value="update ta_office_info set selected='否'", nativeQuery = true)
	void updateAllNo();
	
	List<TaOfficeInfo> findBySelected(String selected);
	
}
