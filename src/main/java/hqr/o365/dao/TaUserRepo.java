package hqr.o365.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hqr.o365.domain.TaUser;

@Repository
public interface TaUserRepo extends JpaRepository<TaUser, Integer> {
	@Query(value="select count(0) from ta_user where user_id= :userid ", nativeQuery = true)
	int chkUserId(String userid);
	
	@Query(value="select count(0) from ta_user where user_id= :userid and :pwd ", nativeQuery = true)
	int checkCredential(String userid, String pwd);
}
