package hqr.o365.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hqr.o365.domain.TaMasterCd;

@Repository
public interface TaMasterCdRepo extends JpaRepository<TaMasterCd, String> {
	List<TaMasterCd> findByKeyTyStartsWith(String keyTy);
}
