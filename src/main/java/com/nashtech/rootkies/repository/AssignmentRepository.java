package com.nashtech.rootkies.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.nashtech.rootkies.model.Asset;
import com.nashtech.rootkies.model.Assignment;
import com.nashtech.rootkies.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long>, JpaSpecificationExecutor<Assignment> {

    List<Assignment> findByAsset(Asset asset);

    @Query("SELECT COUNT (*) FROM Assignment a WHERE a.assignedBy.location.locationId = ?1 AND a.isDeleted = false AND (a.state = 1 OR a.state = 2 OR a.state = 3)")
    Long CountAllByLocationAndDefaultState(Long locationId);

    @Query("FROM Assignment a WHERE a.assignedBy.location.locationId = ?1 AND a.isDeleted = false AND (a.state = 1 OR a.state = 2 OR a.state = 3)")
    Page<Assignment> getAllByLocationAndDefaultState(Pageable page, Long locationId);

    @Query("FROM Assignment a WHERE a.assignedBy.location.locationId = ?1 AND a.isDeleted = false AND a.assignmentId = ?2")
    Optional<Assignment> findByAssignmentId(Long locationId, Long assignmentId);

    @Query(value = "SELECT CASE WHEN count(assignmentid) > 0 THEN true ELSE false END checkExist "
            + "FROM assignments a WHERE assignedby = ?1 or assignedto =?1 ", nativeQuery = true)

    Boolean checkAnyValidAssignment(String staffCode);

    Page<Assignment> findByAssignedToAndStateNotAndStateNotAndAssignedDateLessThanAndIsDeleted(User assignedTo, 
                    Short state1, Short state2, LocalDateTime current, Boolean isDeleted, Pageable pageable);
}
