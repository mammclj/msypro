package com.msymobile.www.commons.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

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
import com.msymobile.www.commons.utils.RedisUtil;
@Service("taoBaoAreaService")
public class TaoBaoAreaServiceImpl implements TaoBaoAreaService {

	private static Logger logger = Logger.getLogger(TaoBaoAreaController.class);
	@Autowired
	private TaoBaoAreaMapper taoBaoAreaMapper;
	
	@Autowired
	private TaoBaoAreaInfoMapper taoBaoAreaInfoMapper;
	
	@Autowired
	private IPMapper iPMapper;
	@Autowired
	private CountryMapper countryMapper;
	@Autowired
	private RegionMapper regionMapper;
	@Autowired
	private CityMapper cityMapper;
	@Autowired
	private SPMapper sPMapper;
	
	public int insertTaoBaoArea(TaoBaoArea taoBaoArea) {
		if(this.taoBaoAreaMapper.insert(taoBaoArea)!=1){
			throw new RuntimeException("master insert -------------失败");
		}
		TaoBaoAreaInfo taoBaoAreaInfo = new TaoBaoAreaInfo();
		BeanUtils.copyProperties(taoBaoArea, taoBaoAreaInfo);
		if(this.taoBaoAreaInfoMapper.insert(taoBaoAreaInfo)!=1){
			throw new RuntimeException("slave insert -------------失败");
		}
		logger.info("master slave insert -------------成功");
		return 1;
	}

	public IP selectByPrimaryKey(String ip) {
		return this.iPMapper.selectByPrimaryKey(ip);
	}

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
		
		SP sp = this.sPMapper.selectByPrimaryKey(ispId);
		if(sp==null){
			sp = new SP(ispId,taoBaoArea.getIsp());
			result = this.sPMapper.insert(sp);
		}
		
		IP ipModel = this.iPMapper.selectByPrimaryKey(taoBaoArea.getIp());
		if(ipModel==null){
			ipModel = new IP(ip,countryId,regionId,cityId,ispId);
			result = this.iPMapper.insert(ipModel);
		}
		return result;
	}

	@Override
	public List<TaoBaoArea> showAreaRedis(String country) {
		List<TaoBaoArea> list = new ArrayList<TaoBaoArea>();
		String rs = RedisUtil.getJedis().get(country);
		if(rs ==null){
			logger.info("查询mysql中的数据，并保存到redis中...");
			selectTest(list, country);
		}else{
			logger.info("查询缓存数据..."+RedisUtil.getJedis().ttl(country));
			if(RedisUtil.getJedis().ttl(country)<10){
				new Thread() {  
                    @Override  
                    public void run() {  
                        //保证5秒内，一条数据只更新一次  
                        Long incr = RedisUtil.getJedis().incr("incr-flag-"+country);  
                        RedisUtil.getJedis().expire("incr-flag-"+country, 5);  
                          
                        if(1 == incr){  
                        	selectTest(list, country);
                        	logger.info("异步更新数据："+country);  
                        }  
                    }  
                }.start(); 
			}
//			logger.info(country+":-----"+RedisUtil.getJedis().get(country));
		}
		return list;
	}
	
	public void selectTest(List<TaoBaoArea> list,String country){
		list = this.taoBaoAreaMapper.selectTaoBaoAreaByCountry(country);
		byte[] results = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		
		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			for (TaoBaoArea taoBaoArea : list) {
				os.writeObject(taoBaoArea);
			}
			
			os.close();
			bos.close();
			results = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} finally {
			if(os!=null){
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(bos!=null){
				try {
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		RedisUtil.getJedis().setex(country.getBytes(), 20,results);
	}

}
