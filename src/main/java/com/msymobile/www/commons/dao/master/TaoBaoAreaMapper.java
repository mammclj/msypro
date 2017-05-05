package com.msymobile.www.commons.dao.master;

import java.util.List;

import com.msymobile.www.commons.dao.BaseMapper;
import com.msymobile.www.commons.model.master.TaoBaoArea;

public interface TaoBaoAreaMapper extends BaseMapper<TaoBaoArea> {

	List<TaoBaoArea> selectTaoBaoAreaByCountry(String country);
	
}