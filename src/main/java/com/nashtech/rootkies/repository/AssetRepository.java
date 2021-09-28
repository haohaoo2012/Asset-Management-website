package com.nashtech.rootkies.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.nashtech.rootkies.model.Asset;

import javax.transaction.Transactional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String>, JpaSpecificationExecutor<Asset> {

    @Query("FROM Asset a WHERE a.location.locationId = ?1 AND a.isDeleted = false AND (a.state = 1 OR a.state = 2 OR a.state = 3)")
    public Page<Asset> getAllByLocationAndDefaultState(Pageable page, Long locationId);

    @Query("SELECT COUNT (*) FROM Asset a WHERE a.location.locationId = ?1 AND a.isDeleted = false AND (a.state = 1 OR a.state = 2 OR a.state = 3)")
    public Long CountAllByLocationAndDefaultState(Long locationId);

    @Query("FROM Asset a WHERE a.location.locationId = ?1 AND a.isDeleted = false AND a.assetCode = ?2")
    Optional<Asset> findByAssetCode(Long locationId, String assetCode);

    public List<Asset> findByAssetCodeStartingWithOrderByAssetCodeDesc(String categoryCode);

    @Modifying
    @Transactional
    @Query(value = "update assets set state = 3 where assetcode =?1 " , nativeQuery = true)
    void updateStateWhenIsAssigned(String assetCode);

    @Query(value = "SELECT  c.categoryname AS category  ," +
            "(SELECT count(a2.assetcode) FROM assets a2 WHERE a2.categorycode =?1 AND a2.state =1 " +
            "AND a2.isdeleted = FALSE AND a2.locationId = ?2)AS available ," +
            "(SELECT count(a2.assetcode) FROM assets a2 WHERE a2.categorycode =?1 AND  a2.state =2 " +
            "AND a2.isdeleted = FALSE AND a2.locationId = ?2)AS notavailable ," +
            "(SELECT count(a2.assetcode) FROM assets a2 WHERE a2.categorycode =?1 AND  a2.state =3 " +
            "AND a2.isdeleted = FALSE AND a2.locationId = ?2)AS assigned," +
            "(SELECT count(a2.assetcode) FROM assets a2 WHERE a2.categorycode =?1 AND  a2.state =4 " +
            "AND a2.isdeleted = FALSE AND a2.locationId = ?2)AS waitforecyvle," +
            "(SELECT count(a2.assetcode) FROM assets a2 WHERE a2.categorycode =?1 AND  a2.state =5 " +
            "AND a2.isdeleted = FALSE AND a2.locationId = ?2)AS recycled " +
            "FROM assets a  " +
            "JOIN  categories c ON c.categorycode = a.categorycode " +
            "WHERE a.categorycode =?1  AND a.isdeleted = FALSE AND a.locationId = ?2 " +
            "GROUP BY c.categoryname LIMIT 1", nativeQuery = true)
    List<Object[]> getAssetReportByCategoryCode(String categoryCode , Long locationId);
}
