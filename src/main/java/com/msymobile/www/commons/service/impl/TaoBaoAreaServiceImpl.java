package com.msymobile.www.commons.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msymobile.www.commons.controller.TaoBaoAreaController;
import com.msymobile.www.commons.dao.master.CityMapper;
import com.msymobile.www.commons.dao.master.CountryMapper;
import com.msymobile.www.commons.dao.master.IPMapper;
import com.msymobile.www.commons.dao.master.RegionMapper;
import com.msymobile.www.commons.dao.master.SPMapper;
import com.msymobile.www.commons.dao.master.TaoBaoAreaMapper;
import com.msymobile.www.commons.dao.slave.TaoBaoAreaInfoMapper;
import com.msymobile.www.commons.model.master.City;
import com.msymobile.www.commons.model.master.Country;
import com.msymobile.www.commons.model.master.IP;
import com.msymobile.www.commons.model.master.Region;
import com.msymobile.www.commons.model.master.SP;
import com.msymobile.www.commons.model.master.TaoBaoArea;
import com.msymobile.www.commons.model.slave.TaoBaoAreaInfo;
import com.msymobile.www.commons.service.TaoBaoAreaService;
@Service("taoBaoAreaService")
public class TaoBaoAreaServiceImpl implements TaoBaoAreaService {

	private static Logger logger = Logger.getLogger(TaoBaoAreaController.class);
	@Autowired
	private TaoBaoAreaMapper taoBaoAreaMapper;
	
	@Autowired
	private TaoBaoAreaInfoMapper taoBaoAreaInfoMapper;
	
	@Autowired
	private IPMapper ipMapper;
	@Autowired
	private CountryMapper countryMapper;
	@Autowired
	private RegionMapper regionMapper;
	@Autowired
	private CityMapper cityMapper;
	@Autowired
	private SPMapper spMapper;
	
	@Override
	public int registerTaoBaoArea(TaoBaoArea taoBaoArea) {
		if(this.taoBaoAreaMapper.insert(taoBaoArea)!=1){
			throw new RuntimeException("master insert -------------失败");
		}
		taoBaoArea.setId("2");
		TaoBaoAreaInfo taoBaoAreaInfo = new TaoBaoAreaInfo();
		BeanUtils.copyProperties(taoBaoArea, taoBaoAreaInfo);
		if(this.taoBaoAreaInfoMapper.insert(taoBaoAreaInfo)!=1){
			throw new RuntimeException("slave insert -------------失败");
		}
		logger.info("master slave insert -------------成功");
		return 1;
	}

	@Override
	public IP selectByPrimaryKey(String ip) {
		return this.ipMapper.selectByPrimaryKey(ip);
	}

	@Override
	public int insertTaoBaoAreaAndChildTable(TaoBaoArea taoBaoArea) {
		this.taoBaoAreaMapper.insert(taoBaoArea);
		int result = 0;
		String countryId = taoBaoArea.getCountryId();
		String regionId = taoBaoArea.getRegionId();
		String cityId = taoBaoArea.getCityId();
		int ispId = Integer.parseInt(taoBaoArea.getIspId());
		String ip = taoBaoArea.getIp();
		Country country = this.countryMapper.selectByPrimaryKey(countryId);
		if(country==null){
			country = new Country(countryId,taoBaoArea.getCountry());
			result = this.countryMapper.insert(country);
		}
		
		Region region = this.regionMapper.selectByPrimaryKey(regionId);
		if(region==null){
			region = new Region(regionId,taoBaoArea.getRegion());
			result = this.regionMapper.insert(region);
		}
		City city = this.cityMapper.selectByPrimaryKey(cityId);
		if(city==null){
			city = new City(cityId,taoBaoArea.getCity());
			result = this.cityMapper.insert(city);
		}
		
		SP sp = this.spMapper.selectByPrimaryKey(ispId);
		if(sp==null){
			sp = new SP(ispId,taoBaoArea.getIsp());
			result = this.spMapper.insert(sp);
		}
		
		IP ipModel = this.ipMapper.selectByPrimaryKey(taoBaoArea.getIp());
		if(ipModel==null){
			ipModel = new IP(ip,countryId,regionId,cityId,ispId);
			result = this.ipMapper.insert(ipModel);
		}
		return result;
	}

}
