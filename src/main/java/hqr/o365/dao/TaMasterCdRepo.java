package hqr.o365.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hqr.o365.domain.TaMasterCd;

@Repository
public interface TaMasterCdRepo extends JpaRepository<TaMasterCd, String> {
	
	@Query(value="select * from ta_master_cd where key_ty like 'SEARCH_ROLE_%'" , nativeQuery = true)
	List<TaMasterCd> getSearchRoles();
	
}
