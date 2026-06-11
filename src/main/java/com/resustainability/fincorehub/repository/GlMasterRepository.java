package com.resustainability.fincorehub.repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resustainability.fincorehub.entity.GlMaster;
import com.resustainability.fincorehub.response.IGlMasterResponse;

import jakarta.transaction.Transactional;


@Repository
public interface GlMasterRepository extends JpaRepository<GlMaster, Long> {
	
	@Query("""
		    SELECT 
		        g.id AS id,
		        g.accountNumber AS accountNumber,
		        g.description AS description,
		        g.type AS type,
		        g.consoleGroup AS consoleGroup,
		        g.bu AS bu,
		        g.categoryGroup AS categoryGroup,
		        g.plHeaders AS plHeaders
		    FROM GlMaster g
		    WHERE
		        (
		            :accountNumber IS NULL OR :accountNumber = ''
		            OR CAST(g.accountNumber AS string) LIKE CONCAT(:accountNumber, '%')
		        )
		    AND
		        (
		            :bu IS NULL OR :bu = ''
		            OR LOWER(g.bu) LIKE LOWER(CONCAT('%', :bu, '%'))
		        )
		""")
		Page<IGlMasterResponse> findAllGl(
		        @Param("accountNumber") String accountNumber,
		        @Param("bu") String bu,
		        Pageable pageable
		);
	
	
	@Query("""
		    SELECT 
		        g.id AS id,
		        g.accountNumber AS accountNumber,
		        g.description AS description,
		        g.type AS type,
		        g.consoleGroup AS consoleGroup,
		        g.bu AS bu,
		        g.categoryGroup AS categoryGroup,
		        g.plHeaders AS plHeaders 
		    FROM GlMaster g
		    WHERE g.id = :glId
		""")
		Optional<IGlMasterResponse> findByGlId(@Param("glId") Long glId);
	
	
	
	@Modifying
	@Query("""
	    UPDATE GlMaster g
	    SET g.accountNumber = :accountNumber,
	        g.description = :description,
	        g.type = :type,
	        g.consoleGroup = :consoleGroup,
	        g.bu = :bu,
	        g.categoryGroup = :categoryGroup,
	        g.plHeaders = :plHeaders,
	        g.modifiedBy = :modifiedBy,
	        g.modifiedOn = :modifiedOn
	    WHERE g.id = :id
	""")
	int updateGl(
	        @Param("id") Long id,
	        @Param("accountNumber") Long accountNumber,
	        @Param("description") String description,
	        @Param("type") String type,
	        @Param("consoleGroup") String consoleGroup,
	        @Param("bu") String bu,
	        @Param("categoryGroup") String categoryGroup,
	        @Param("modifiedBy") String modifiedBy,
	        @Param("modifiedOn") LocalDateTime modifiedOn,
	        @Param("plHeaders") String plHeaders
	);
	
	@Query("""
	        SELECT g
	        FROM GlMaster g
	        WHERE
	            (
	                :accountNumber IS NULL OR :accountNumber = ''
	                OR CAST(g.accountNumber AS string) LIKE CONCAT(:accountNumber, '%')
	            )
	        AND
	            (
	                :bu IS NULL OR :bu = ''
	                OR LOWER(g.bu) LIKE LOWER(CONCAT('%', :bu, '%'))
	            )
	""")
	List<GlMaster> exportGlData(
	        @Param("accountNumber") String accountNumber,
	        @Param("bu") String bu
	);

}