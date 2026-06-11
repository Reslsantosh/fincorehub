package com.resustainability.fincorehub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table( name = "ebitda_budget_master",uniqueConstraints = {
	        @UniqueConstraint(columnNames = { "bu","sbu","site","financial_year"})
	    })
public class EbitdaBudgetMaster {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bu;
    private String sbu;
    private String site;
    private String financialYear; // Store as "2026-27"

    
    private Double apr = 0.0;
    private Double may = 0.0;
    private Double jun = 0.0;
    private Double jul = 0.0;
    private Double aug = 0.0;
    private Double sep = 0.0;
    private Double oct = 0.0;
    private Double nov = 0.0;
    private Double dec = 0.0;
    private Double jan = 0.0;
    private Double feb = 0.0;
    private Double mar = 0.0;

    private Double total;

	public EbitdaBudgetMaster() {

	}

	public EbitdaBudgetMaster(Long id, String bu, String sbu, String site, String financialYear, Double apr, Double may,
			Double jun, Double jul, Double aug, Double sep, Double oct, Double nov, Double dec, Double jan, Double feb,
			Double mar, Double total) {
		super();
		this.id = id;
		this.bu = bu;
		this.sbu = sbu;
		this.site = site;
		this.financialYear = financialYear;
		this.apr = apr;
		this.may = may;
		this.jun = jun;
		this.jul = jul;
		this.aug = aug;
		this.sep = sep;
		this.oct = oct;
		this.nov = nov;
		this.dec = dec;
		this.jan = jan;
		this.feb = feb;
		this.mar = mar;
		this.total = total;
	}

	@Override
	public String toString() {
		return "EbitdaBudgetMaster [id=" + id + ", bu=" + bu + ", sbu=" + sbu + ", site=" + site + ", financialYear="
				+ financialYear + ", apr=" + apr + ", may=" + may + ", jun=" + jun + ", jul=" + jul + ", aug=" + aug
				+ ", sep=" + sep + ", oct=" + oct + ", nov=" + nov + ", dec=" + dec + ", jan=" + jan + ", feb=" + feb
				+ ", mar=" + mar + ", total=" + total + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getSbu() {
		return sbu;
	}

	public void setSbu(String sbu) {
		this.sbu = sbu;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public Double getApr() {
		return apr;
	}

	public void setApr(Double apr) {
		this.apr = apr;
	}

	public Double getMay() {
		return may;
	}

	public void setMay(Double may) {
		this.may = may;
	}

	public Double getJun() {
		return jun;
	}

	public void setJun(Double jun) {
		this.jun = jun;
	}

	public Double getJul() {
		return jul;
	}

	public void setJul(Double jul) {
		this.jul = jul;
	}

	public Double getAug() {
		return aug;
	}

	public void setAug(Double aug) {
		this.aug = aug;
	}

	public Double getSep() {
		return sep;
	}

	public void setSep(Double sep) {
		this.sep = sep;
	}

	public Double getOct() {
		return oct;
	}

	public void setOct(Double oct) {
		this.oct = oct;
	}

	public Double getNov() {
		return nov;
	}

	public void setNov(Double nov) {
		this.nov = nov;
	}

	public Double getDec() {
		return dec;
	}

	public void setDec(Double dec) {
		this.dec = dec;
	}

	public Double getJan() {
		return jan;
	}

	public void setJan(Double jan) {
		this.jan = jan;
	}

	public Double getFeb() {
		return feb;
	}

	public void setFeb(Double feb) {
		this.feb = feb;
	}

	public Double getMar() {
		return mar;
	}

	public void setMar(Double mar) {
		this.mar = mar;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
    
	
    

}
