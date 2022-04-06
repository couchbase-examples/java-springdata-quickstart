package trycb.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import trycb.model.Profile;

@Repository
public interface ProfileRepository extends PagingAndSortingRepository<Profile, UUID> {
  @Query("#{#n1ql.selectEntity} WHERE firstName LIKE '%' || $1 || '%' OR lastName LIKE '%' || $1 || '%' OR address LIKE '%' || $1 || '%' OFFSET $2 * $3 LIMIT $3")
  List<Profile> findByText(String query, int pageNum, int pageSize);

  Page<Profile> findByAge(byte age, Pageable pageable);
}
