package com.resustainability.fincorehub.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import com.resustainability.fincorehub.config.SecurityUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "pcmaster")
public class PcMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "profit_centre",nullable = false, length = 100)
	private Long profitCentre;
	
	@Column(name = "profit_centre_name", nullable = false, length = 200)
	private String profitCentreName;
	
	@Column(name = "site_name_for_mis", nullable = false, length = 100)
	private String siteNameForMIS;
	
	@Column(name = "sbu_final", nullable = false, length = 100)
	private String sbuFinal;
	
	@Column(name = "unit", nullable = false, length = 100)
	private String unit;
	
	@Column(name = "sbu", nullable = false, length = 100)
	private String sbu;
	
	@Column(name = "bu", nullable = false, length = 100)
	private String bu;
	
	@Column(name = "plantcode", nullable = false, length = 100)
	private String plantCode;
	
	@Column(name = "created_by", updatable = false)
	private String createdBy;
    
	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	
	public PcMaster() {

	}




	



	public PcMaster(Long id, Long profitCentre, String profitCentreName, String siteNameForMIS, String sbuFinal,
			String unit, String sbu, String bu, String plantCode, String createdBy, String modifiedBy,
			LocalDateTime createdOn, LocalDateTime modifiedOn) {
		super();
		this.id = id;
		this.profitCentre = profitCentre;
		this.profitCentreName = profitCentreName;
		this.siteNameForMIS = siteNameForMIS;
		this.sbuFinal = sbuFinal;
		this.unit = unit;
		this.sbu = sbu;
		this.bu = bu;
		this.plantCode = plantCode;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
	}



	@Override
	public String toString() {
		return "PcMaster [id=" + id + ", profitCentre=" + profitCentre + ", profitCentreName=" + profitCentreName
				+ ", siteNameForMIS=" + siteNameForMIS + ", sbuFinal=" + sbuFinal + ", unit=" + unit + ", sbu=" + sbu
				+ ", bu=" + bu + ", plantCode=" + plantCode + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + "]";
	}





	@Override
	public int hashCode() {
		return Objects.hash(bu, createdBy, createdOn, id, modifiedBy, modifiedOn, plantCode, profitCentre,
				profitCentreName, sbu, sbuFinal, siteNameForMIS, unit);
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PcMaster other = (PcMaster) obj;
		return Objects.equals(bu, other.bu) && Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(createdOn, other.createdOn) && Objects.equals(id, other.id)
				&& Objects.equals(modifiedBy, other.modifiedBy) && Objects.equals(modifiedOn, other.modifiedOn)
				&& Objects.equals(plantCode, other.plantCode) && Objects.equals(profitCentre, other.profitCentre)
				&& Objects.equals(profitCentreName, other.profitCentreName) && Objects.equals(sbu, other.sbu)
				&& Objects.equals(sbuFinal, other.sbuFinal) && Objects.equals(siteNameForMIS, other.siteNameForMIS)
				&& Objects.equals(unit, other.unit);
	}




	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getProfitCentre() {
		return profitCentre;
	}


	public void setProfitCentre(Long profitCentre) {
		this.profitCentre = profitCentre;
	}


	public String getProfitCentreName() {
		return profitCentreName;
	}


	public void setProfitCentreName(String profitCentreName) {
		this.profitCentreName = profitCentreName;
	}


	public String getSiteNameForMIS() {
		return siteNameForMIS;
	}


	public void setSiteNameForMIS(String siteNameForMIS) {
		this.siteNameForMIS = siteNameForMIS;
	}


	public String getSbuFinal() {
		return sbuFinal;
	}


	public void setSbuFinal(String sbuFinal) {
		this.sbuFinal = sbuFinal;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public String getSbu() {
		return sbu;
	}


	public void setSbu(String sbu) {
		this.sbu = sbu;
	}


	public String getBu() {
		return bu;
	}


	public void setBu(String bu) {
		this.bu = bu;
	}
	
	
	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public LocalDateTime getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}


	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}


	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	

	@PrePersist
	public void onCreate() {

	    String user = SecurityUtils.getCurrentUser();
	    LocalDateTime now = LocalDateTime.now();

	    this.createdBy = user;
	    this.createdOn = now;


	    this.modifiedBy = null;
	    this.modifiedOn = null;
	}
	
	@PreUpdate
	public void onUpdate() {

	    this.modifiedBy = SecurityUtils.getCurrentUser();
	    this.modifiedOn = LocalDateTime.now();
	}
	
		

}
