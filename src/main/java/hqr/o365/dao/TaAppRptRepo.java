package hqr.o365.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hqr.o365.domain.TaAppRpt;

@Repository
public interface TaAppRptRepo extends JpaRepository<TaAppRpt, Integer>{
	
	@Transactional
	long deleteByTenantId(String tenantId);
	
}
