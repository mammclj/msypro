package com.msymobile.www.commons.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.msymobile.www.commons.model.master.IP;
import com.msymobile.www.commons.model.master.TaoBaoArea;
import com.msymobile.www.commons.service.TaoBaoAreaService;
import com.msymobile.www.commons.utils.IPUtil;
import com.msymobile.www.commons.utils.RedisUtil;
import com.msymobile.www.commons.vo.Json;
import com.msymobile.www.commons.vo.TaoBaoAreaData;

@Controller
@RequestMapping("/taoBaoAreaController")
public class TaoBaoAreaController {
	private static Logger logger = Logger.getLogger(TaoBaoAreaController.class);
	
	@Autowired
	private TaoBaoAreaService taoBaoAreaService;
	
	@RequestMapping("/showAreaInfoByIp")
	public String showAreaInfoByIp(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		boolean success = false;
		Map<String,Object> map = new HashMap<String,Object>();
		String regionId = "";
		String cityId = "";
		String message = "查询失败！";
		TaoBaoAreaData taoBaoAreaData = new TaoBaoAreaData();
		try {
			out = response.getWriter();
			String ip = IPUtil.getRealIp(request);
			ip = "101.254.183.41"; 
			logger.info("请求地址真实ip是---------------------------------> "+ip);
//			ip = ip.substring(0, ip.lastIndexOf("."))+".0";
//			logger.info("只要获取ip的前三段就可以确定所属地信息了，此参数ip为--------> ip:"+ip);
			
			//先查看redis中有没有数据
			String values = RedisUtil.getJedis().get(ip);
			if(values != null && !"".equals(values.trim())){
				String[] data = values.trim().split(";");
				regionId = data[0];
				cityId = data[1];
				message = "查询成功！";
			}else{
				//查看数据库中是否有此ip
				IP ipModel = this.taoBaoAreaService.selectByPrimaryKey(ip);
				if(ipModel!=null){
					regionId = ipModel.getRegionId();
					cityId = ipModel.getCityId();
					//信息存入redis
					RedisUtil.getJedis().set(ip, regionId+";"+cityId);
					message = "查询成功！";
				}else{
					String result = IPUtil.getAddressByIP(ip);
					if(result!=null && !"".equals(result.trim())){
						result = result.replaceAll("_id", "Id").replace("data", "taoBaoArea");
						//json转对象
						taoBaoAreaData = JSON.toJavaObject(JSON.parseObject(result), TaoBaoAreaData.class);
						if(taoBaoAreaData.getCode() == 0){
							TaoBaoArea masterTaoBaoArea = taoBaoAreaData.getTaoBaoArea();
							if(masterTaoBaoArea!=null){
								masterTaoBaoArea.setId(UUID.randomUUID().toString().replaceAll("-", ""));
								if(this.taoBaoAreaService.insertTaoBaoAreaAndChildTable(masterTaoBaoArea)==1){
									regionId = masterTaoBaoArea.getRegionId();
									cityId = masterTaoBaoArea.getCityId();
									//信息存入redis
									RedisUtil.getJedis().set(ip, regionId+";"+cityId);
									message = "查询成功！";
								}
							}
						}
					}
				}
			}
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out !=null){
				map.put("regionId", regionId);
				map.put("cityId", cityId);
				out.print(JSON.toJSONString(new Json(success,message,map)));
				out.close();
			}
		}
		return null;
	}
}
