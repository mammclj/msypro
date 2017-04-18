package msypro;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.msymobile.www.commons.model.master.TaoBaoArea;
import com.msymobile.www.commons.service.TaoBaoAreaService;
import com.msymobile.www.commons.utils.IPUtil;
import com.msymobile.www.commons.vo.Json;
import com.msymobile.www.commons.vo.TaoBaoAreaData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml","classpath:spring-datasources.xml",
		"classpath:spring-mybatis.xml","classpath:spring-mybatis-mapper-master.xml",
		"classpath:spring-mybatis-mapper-slave.xml","classpath:spring-transaction.xml"})
public class TestSpring {

	private static final Logger logger = LoggerFactory.getLogger(TestSpring.class);
	
	@Autowired
	private TaoBaoAreaService taoBaoAreaService;
	
	@Test
	public void TestInsert(){
		Json json = new Json(true,"",null);
		boolean success = true;
		Map<String,Object> map = new HashMap<String,Object>();
		String message = "查询失败！";
		TaoBaoAreaData taoBaoAreaData = new TaoBaoAreaData();
		String ip = "101.254.183.41";
		String result = IPUtil.getAddressByIP(ip);
		if(result!=null && !"".equals(result.trim())){
			result = result.replaceAll("_id", "Id").replace("data", "taoBaoArea");
			//json转对象
			taoBaoAreaData = JSON.toJavaObject(JSON.parseObject(result), TaoBaoAreaData.class);
			if(taoBaoAreaData.getCode() == 0){
				TaoBaoArea taoBaoArea = taoBaoAreaData.getTaoBaoArea();
				if(taoBaoArea!=null){
					taoBaoArea.setId(UUID.randomUUID().toString().replaceAll("-", ""));
					if(this.taoBaoAreaService.registerTaoBaoArea(taoBaoArea)==1){
						map.put("regionId", taoBaoArea.getRegionId());
						map.put("cityId", taoBaoArea.getCityId());
						message = "查询成功！";
					}
				}
			}
		}
		logger.info(JSON.toJSONString(json));
	}
}
