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
@Table(name = "glmaster")
public class GlMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "account_number",nullable = false, length = 100)
	private Long accountNumber;
	
	@Column(name = "description",nullable = false, length = 200 )
	private String description;
	
	@Column(name = "type", nullable = false, length = 100)
	private String type;
	
	@Column(name = "consoleGroup", nullable = false, length = 100)
	private String consoleGroup;
	
	@Column(name = "bu", nullable = false, length = 100 )
	private String bu;
	
	@Column(name = "categoryGroup", nullable = false, length = 100)
	private String categoryGroup;
	
	@Column(name = "created_by", updatable = false)
	private String createdBy;
    
	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;
	
	@Column(name = "p_l_headers")
	private String plHeaders;
	



	@Override
	public String toString() {
		return "GlMaster [id=" + id + ", accountNumber=" + accountNumber + ", description=" + description + ", type="
				+ type + ", consoleGroup=" + consoleGroup + ", bu=" + bu + ", categoryGroup=" + categoryGroup
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdOn=" + createdOn
				+ ", modifiedOn=" + modifiedOn + ", plheader=" + plHeaders + "]";
	}


	public GlMaster() {
		
	}
	


	public GlMaster(Long id, Long accountNumber, String description, String type, String consoleGroup, String bu,
			String categoryGroup, String createdBy, String modifiedBy, LocalDateTime createdOn,
			LocalDateTime modifiedOn, String plHeaders) {
		super();
		this.id = id;
		this.accountNumber = accountNumber;
		this.description = description;
		this.type = type;
		this.consoleGroup = consoleGroup;
		this.bu = bu;
		this.categoryGroup = categoryGroup;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.plHeaders = plHeaders;
	}



	@Override
	public int hashCode() {
		return Objects.hash(accountNumber, bu, categoryGroup, consoleGroup, createdBy, createdOn, description, id,
				modifiedBy, modifiedOn, plHeaders, type);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GlMaster other = (GlMaster) obj;
		return Objects.equals(accountNumber, other.accountNumber) && Objects.equals(bu, other.bu)
				&& Objects.equals(categoryGroup, other.categoryGroup)
				&& Objects.equals(consoleGroup, other.consoleGroup) && Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(createdOn, other.createdOn) && Objects.equals(description, other.description)
				&& Objects.equals(id, other.id) && Objects.equals(modifiedBy, other.modifiedBy)
				&& Objects.equals(modifiedOn, other.modifiedOn) && Objects.equals(plHeaders, other.plHeaders)
				&& Objects.equals(type, other.type);
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConsoleGroup() {
		return consoleGroup;
	}

	public void setConsoleGroup(String consoleGroup) {
		this.consoleGroup = consoleGroup;
	}

	public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getCategoryGroup() {
		return categoryGroup;
	}

	public void setCategoryGroup(String categoryGroup) {
		this.categoryGroup = categoryGroup;
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
	
	
	public String getPlHeaders() {
		return plHeaders;
	}


	public void setPlHeaders(String plHeaders) {
		this.plHeaders = plHeaders;
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
