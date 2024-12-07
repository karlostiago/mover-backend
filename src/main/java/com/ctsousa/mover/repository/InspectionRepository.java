package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface InspectionRepository extends JpaRepository<InspectionEntity, Long> {

    @Query("SELECT COUNT(photo) > 0 FROM InspectionPhotoEntity photo " +
            "WHERE photo = :photo " +
            "AND photo.photoEntity.image IS NOT NULL " +
            "AND photo.photoEntity.image <> '' " +
            "AND photo.inspectionStatus = 'UNDER_REVIEW'")
    boolean existsPhotoInAnalysis(@Param("photo") InspectionPhotoEntity photo);

    @Query("SELECT COUNT(photo) > 0 FROM InspectionPhotoEntity photo " +
            "WHERE photo = :photo " +
            "AND photo.photoEntity.image IS NOT NULL " +
            "AND photo.photoEntity.image <> '' " +
            "AND photo.inspectionStatus = 'REJECTED'")
    boolean existsPhotoRejected(@Param("photo") InspectionPhotoEntity photo);

    @Query("SELECT COUNT(photo) > 0 FROM InspectionPhotoEntity photo " +
            "WHERE photo = :photo " +
            "AND photo.photoEntity.image IS NOT NULL " +
            "AND photo.photoEntity.image <> '' " +
            "AND photo.inspectionStatus = 'APPROVED'")
    boolean existsPhotoApproved(@Param("photo") InspectionPhotoEntity photo);

    @Query("SELECT i FROM InspectionEntity i " +
            "LEFT JOIN FETCH i.contract c " +
            "LEFT JOIN FETCH c.vehicle v " +
            "LEFT JOIN FETCH v.brand b " +
            "LEFT JOIN FETCH v.model m " +
            "LEFT JOIN FETCH i.photos p " +
            "LEFT JOIN FETCH i.questions q " +
            "WHERE c.id = :contractId")
    List<InspectionEntity> findUnderReviewInspectionsWithQuestionsByContractId(@Param("contractId") Long contractId);

    @Query("SELECT i FROM InspectionEntity i WHERE i.contract.id = :contractId")
    List<InspectionEntity> findByContractId(@Param("contractId") Long contractId);

    @Query("SELECT i FROM InspectionEntity i " +
            "JOIN FETCH i.photos " +
            "JOIN FETCH i.questions " +
            "WHERE i.contract.id = :contractId")
    List<InspectionEntity> findByContractIdJoinFech(@Param("contractId") Long contractId);

}
