package com.msymobile.www.commons.service;

import java.util.List;

import com.msymobile.www.commons.model.master.IP;
import com.msymobile.www.commons.model.master.TaoBaoArea;

public interface TaoBaoAreaService {

	int insertTaoBaoArea(TaoBaoArea taoBaoArea);

	IP selectByPrimaryKey(String ip);

	int insertTaoBaoAreaAndChildTable(TaoBaoArea masterTaoBaoArea);

	List<TaoBaoArea> showAreaRedis(String country);
	
}
