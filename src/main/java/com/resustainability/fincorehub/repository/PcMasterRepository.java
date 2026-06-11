package com.resustainability.fincorehub.repository;


import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resustainability.fincorehub.entity.PcMaster;
import com.resustainability.fincorehub.response.IPcMasterResponse;

import jakarta.transaction.Transactional;

@Repository
public interface PcMasterRepository extends JpaRepository<PcMaster, Long> {
	
	    boolean existsByProfitCentre(Long profitCentre);

	    List<PcMaster> findByProfitCentreIn(List<Long> profitCentres);

	    @Query("""
	    	    SELECT 
	    	        p.id AS id,
	    	        p.profitCentre AS profitCentre,
	    	        p.profitCentreName AS profitCentreName,
	    	        p.siteNameForMIS AS siteNameForMIS,
	    	        p.sbuFinal AS sbuFinal,
	    	        p.unit AS unit,
	    	        p.sbu AS sbu,
	    	        p.bu AS bu,
	    	        p.plantCode AS plantCode
	    	    FROM PcMaster p
	    	    WHERE
	    	        (:pcCode IS NULL OR :pcCode = '' OR
	    	            CAST(p.profitCentre AS string) LIKE CONCAT('%', :pcCode, '%')
	    	        )
	    	    AND
				        (:bu IS NULL OR :bu = '' OR
				         
				            LOWER(p.bu) = LOWER(:bu)
				        )
				        
	    	""")
	    	Page<IPcMasterResponse> findAllPc(
	    	        @Param("pcCode") String pcCode,
	    	        @Param("bu") String bu,
	    	        Pageable pageable
	    	);

    @Query("""
        SELECT 
            p.id AS id,
            p.profitCentre AS profitCentre,
            p.profitCentreName AS profitCentreName,
            p.siteNameForMIS AS siteNameForMIS,
            p.sbuFinal AS sbuFinal,
            p.unit AS unit,
            p.sbu AS sbu,
            p.bu AS bu,
            p.plantCode AS plantCode
        FROM PcMaster p
        WHERE p.id = :id
    """)
    Optional<IPcMasterResponse> findByPcId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("""
        UPDATE PcMaster p SET
            p.profitCentre = :profitCentre,
            p.profitCentreName = :profitCentreName,
            p.siteNameForMIS = :siteNameForMIS,
            p.sbuFinal = :sbuFinal,
            p.unit = :unit,
            p.sbu = :sbu,
            p.bu = :bu,
            p.plantCode = :plantCode,
            p.modifiedBy = :modifiedBy,
	        p.modifiedOn = :modifiedOn
        WHERE p.id = :id
    """)
    int updatePc(
            @Param("id") Long id,
            @Param("profitCentre") Long profitCentre,
            @Param("profitCentreName") String profitCentreName,
            @Param("siteNameForMIS") String siteNameForMIS,
            @Param("sbuFinal") String sbuFinal,
            @Param("unit") String unit,
            @Param("sbu") String sbu,
            @Param("bu") String bu,
            @Param("plantCode") String plantCode,
            @Param("modifiedBy") String modifiedBy,
	        @Param("modifiedOn") LocalDateTime modifiedOn
    );
    
    @Modifying
    @Transactional
    @Query("""
        UPDATE PcMaster p SET
            p.sbu = :sbu,
            p.bu = :bu,
            p.modifiedBy = :modifiedBy,
            p.modifiedOn = :modifiedOn
        WHERE p.id IN :ids
    """)
    int bulkUpdateSbuBu(
            @Param("ids") List<Long> ids,
            @Param("sbu") String sbu,
            @Param("bu") String bu,
            @Param("modifiedBy") String modifiedBy,
            @Param("modifiedOn") LocalDateTime modifiedOn
    );
    
    @Query("""
            SELECT p
            FROM PcMaster p
            WHERE
                (
                    :pcCode IS NULL OR :pcCode = ''
                    OR CAST(p.profitCentre AS string)
                    LIKE CONCAT('%', :pcCode, '%')
                )
            AND
                (
                    :bu IS NULL OR :bu = ''
                    OR LOWER(p.bu) = LOWER(:bu)
                )
    """)
    List<PcMaster> exportPcData(
            @Param("pcCode") String pcCode,
            @Param("bu") String bu
    );
}
