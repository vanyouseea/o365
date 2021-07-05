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
	
	@Query(value="select seq_no,app_id,mask_app_id,create_dt,last_update_dt,last_update_id,passwd,remarks,secret_id,mask_secret_id,tenant_id,user_id,selected "
			+ "from (select A.*, rownum ro from (select * from ta_office_info order by remarks) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<TaOfficeInfo> getOfficeInfos(int startRow, int endRow);
	
	@Transactional
    @Modifying
	@Query(value="update ta_office_info set selected='否'", nativeQuery = true)
	void updateAllNo();
	
	@Query(value="select * from ta_office_info where selected='是'", nativeQuery = true)
	List<TaOfficeInfo> getSelectedApp();
	
}
