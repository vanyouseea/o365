package hqr.o365.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hqr.o365.domain.TaAppRpt;

@Repository
public interface TaAppRptRepo extends JpaRepository<TaAppRpt, Integer>{
	
	@Query(value="select seq_no,tenant_id,app_id,secret_id,remarks,rpt_dt,total_user,total_global_admin,enable_global_admin,disable_gloabl_admin,spo "
			+ "from (select A.*, rownum ro from (select * from ta_app_rpt order by remarks) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<TaAppRpt> getSysRpt(int startRow, int endRow);
	
	@Transactional
    @Modifying
	@Query(value="delete from ta_app_rpt where tenant_id= :tenantId", nativeQuery = true)
	void deleteByTenantId(String tenantId);
	
}
