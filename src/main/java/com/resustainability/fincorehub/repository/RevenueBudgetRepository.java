package com.resustainability.fincorehub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resustainability.fincorehub.entity.RevenueBudgetMaster;
import com.resustainability.fincorehub.response.IRevenueBudgetResponse;

import jakarta.transaction.Transactional;

@Repository
public interface RevenueBudgetRepository
        extends JpaRepository<RevenueBudgetMaster, Long> {

    @Query("""
        SELECT
            b.id AS id,
            b.bu AS bu,
            b.sbu AS sbu,
            b.site AS site,
            b.financialYear AS financialYear,
            b.apr AS apr,
            b.may AS may,
            b.jun AS jun,
            b.jul AS jul,
            b.aug AS aug,
            b.sep AS sep,
            b.oct AS oct,
            b.nov AS nov,
            b.dec AS dec,
            b.jan AS jan,
            b.feb AS feb,
            b.mar AS mar,
            b.total AS total
        FROM RevenueBudgetMaster b
        WHERE
            (:bu IS NULL OR :bu = ''
                OR LOWER(b.bu) = LOWER(:bu))
        AND
            (:site IS NULL OR :site = ''
                OR LOWER(b.site) = LOWER(:site))
        AND
            (:financialYear IS NULL OR :financialYear = ''
                OR b.financialYear = :financialYear)
    """)
    Page<IRevenueBudgetResponse> findAllBudget(
            @Param("bu") String bu,
            @Param("site") String site,
            @Param("financialYear") String financialYear,
            Pageable pageable
    );

    @Query("""
        SELECT
            b.id AS id,
            b.bu AS bu,
            b.sbu AS sbu,
            b.site AS site,
            b.financialYear AS financialYear,
            b.apr AS apr,
            b.may AS may,
            b.jun AS jun,
            b.jul AS jul,
            b.aug AS aug,
            b.sep AS sep,
            b.oct AS oct,
            b.nov AS nov,
            b.dec AS dec,
            b.jan AS jan,
            b.feb AS feb,
            b.mar AS mar,
            b.total AS total
        FROM RevenueBudgetMaster b
        WHERE b.id = :id
    """)
    Optional<IRevenueBudgetResponse> findBudgetById(
            @Param("id") Long id
    );

    @Modifying
    @Transactional
    @Query("""
        UPDATE RevenueBudgetMaster b SET
            b.bu = :bu,
            b.sbu = :sbu,
            b.site = :site,
            b.financialYear = :financialYear,
            b.apr = :apr,
            b.may = :may,
            b.jun = :jun,
            b.jul = :jul,
            b.aug = :aug,
            b.sep = :sep,
            b.oct = :oct,
            b.nov = :nov,
            b.dec = :dec,
            b.jan = :jan,
            b.feb = :feb,
            b.mar = :mar,
            b.total = :total
        WHERE b.id = :id
    """)
    int updateBudget(
            @Param("id") Long id,
            @Param("bu") String bu,
            @Param("sbu") String sbu,
            @Param("site") String site,
            @Param("financialYear") String financialYear,
            @Param("apr") Double apr,
            @Param("may") Double may,
            @Param("jun") Double jun,
            @Param("jul") Double jul,
            @Param("aug") Double aug,
            @Param("sep") Double sep,
            @Param("oct") Double oct,
            @Param("nov") Double nov,
            @Param("dec") Double dec,
            @Param("jan") Double jan,
            @Param("feb") Double feb,
            @Param("mar") Double mar,
            @Param("total") Double total
    );
    
    boolean existsByBuIgnoreCaseAndSbuIgnoreCaseAndSiteIgnoreCaseAndFinancialYear(
            String bu,
            String sbu,
            String site,
            String financialYear
    );
    
    @Query("""
    	    SELECT b
    	    FROM RevenueBudgetMaster b
    	    WHERE
    	        (:bu IS NULL OR :bu = ''
    	            OR LOWER(b.bu) = LOWER(:bu))
    	    AND
    	        (:site IS NULL OR :site = ''
    	            OR LOWER(b.site) = LOWER(:site))
    	    AND
    	        (:financialYear IS NULL OR :financialYear = ''
    	            OR b.financialYear = :financialYear)
    	""")
    	List<RevenueBudgetMaster> exportBudgetData(
    	        @Param("bu") String bu,
    	        @Param("site") String site,
    	        @Param("financialYear") String financialYear
    	);
    
}