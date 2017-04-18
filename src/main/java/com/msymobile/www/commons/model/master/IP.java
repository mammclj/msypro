package com.msymobile.www.commons.model.master;

public class IP {
    private String id;

    private String countryId;

    private String regionId;

    private String cityId;

    private Integer isp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId == null ? null : countryId.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId == null ? null : cityId.trim();
    }

    public Integer getIsp() {
        return isp;
    }

    public void setIsp(Integer isp) {
        this.isp = isp;
    }

	public IP() {
		super();
	}

	public IP(String id, String countryId, String regionId, String cityId, Integer isp) {
		super();
		this.id = id;
		this.countryId = countryId;
		this.regionId = regionId;
		this.cityId = cityId;
		this.isp = isp;
	}

	@Override
	public String toString() {
		return "IP [id=" + id + ", countryId=" + countryId + ", regionId=" + regionId + ", cityId=" + cityId + ", isp="
				+ isp + "]";
	}
    
}