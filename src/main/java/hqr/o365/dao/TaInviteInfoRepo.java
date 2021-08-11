package hqr.o365.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hqr.o365.domain.TaInviteInfo;

@Repository
public interface TaInviteInfoRepo extends JpaRepository<TaInviteInfo, String> {
	@Query(value="select invite_id,licenses,licenses_cn, start_dt, end_dt,invite_status,suffix,result,seq_no "
			+ "from (select A.*, rownum ro from (select * from ta_invite_info order by start_dt desc) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<TaInviteInfo> getInviteInfos(int startRow, int endRow);
}
