package com.msymobile.www.commons.model.master;

public class SP {
    private Integer spId;

    private String spName;

    public Integer getSpId() {
        return spId;
    }

    public void setSpId(Integer spId) {
        this.spId = spId;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName == null ? null : spName.trim();
    }

	public SP() {
		super();
	}

	public SP(Integer spId, String spName) {
		super();
		this.spId = spId;
		this.spName = spName;
	}

	@Override
	public String toString() {
		return "SP [spId=" + spId + ", spName=" + spName + "]";
	}
    
}