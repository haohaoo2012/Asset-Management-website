package com.nashtech.rootkies.repository;

import java.util.Optional;

import com.nashtech.rootkies.model.Request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {
    @Query("FROM Request r WHERE r.requestedBy.location.locationId = ?1 AND r.isDeleted = false AND r.requestId = ?2")
    Optional<Request> findByRequestId(Long locationId, Long requestId);

    @Query("SELECT COUNT (*) FROM Request r WHERE r.requestedBy.location.locationId = ?1 AND r.isDeleted = false")
    Long CountAllByLocationAndDefaultState(Long locationId);

    @Query("FROM Request r WHERE r.requestedBy.location.locationId = ?1 AND r.isDeleted = false")
    Page<Request> getAllByLocationAndDefaultState(Pageable page, Long locationId);
}
