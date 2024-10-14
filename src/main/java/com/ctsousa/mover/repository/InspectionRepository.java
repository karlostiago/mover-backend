package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
