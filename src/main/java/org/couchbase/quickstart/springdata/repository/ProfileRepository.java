package org.couchbase.quickstart.springdata.repository;

import java.util.List;
import java.util.UUID;

import org.couchbase.quickstart.springdata.models.Profile;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfileRepository extends PagingAndSortingRepository<Profile, UUID> {
  // Repository method that executes a custom SQL++ query 
  @Query("#{#n1ql.selectEntity} WHERE firstName LIKE '%' || $1 || '%' OR lastName LIKE '%' || $1 || '%' OR address LIKE '%' || $1 || '%' OFFSET $2 * $3 LIMIT $3")
  List<Profile> findByText(String query, int pageNum, int pageSize);

  Page<Profile> findByAge(byte age, Pageable pageable);
}
