package hqr.o365.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import hqr.o365.domain.TaAppRpt;

@Repository
public interface TaAppRptRepo extends JpaRepository<TaAppRpt, Integer>{
	
	@Query(value="select seq_no,tenant_id,app_id,secret_id,rpt_dt,total_user,total_global_admin,enable_global_admin,disable_gloabl_admin "
			+ "from (select A.*, rownum ro from (select * from ta_app_rpt) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<TaAppRpt> getSysRpt(int startRow, int endRow);
}
