package com.hw.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hw.dao.BaseDAO;
import com.hw.domain.${title};
import com.hw.domain.Type;
import com.hw.domain.BaseDomain;
import com.hw.domain.HanderClass;
import com.hw.domain.HanderMethod;

/**
* @Title ${title}Service
* @Description ${describle}Service
* @Company ${company}
* @author ${author}
* @date ${date}
*/
@Service
@HanderClass(name = "${describle}", code = "")
public class ${title}Service {

	@Autowired
	private BaseDAO ${lower}Dao;

	@HanderMethod(name = "添加${describle}录入", type = Type.LOG_ADD,getMethod = "getById")
	public String add(${title} ${lower}) {
		String id = ${lower}Dao.insert(${lower});
		return id;
	}

	@HanderMethod(name = "修改${describle}信息", type = Type.LOG_UPDATE,getMethod = "getById")
	public boolean update(${title} ${lower}) {
		return ${lower}Dao.update(${lower});
	}

	public String exportExcel(Map<String, String> params) {
		return ${lower}Dao.exportExcel(params);
	}
	public BaseDomain getById(String id) {
		return ${lower}Dao.queryDataById(id);
	}
	public void addSort(String sort) {
		${lower}Dao.addSort(sort);
	}

	public List<BaseDomain> list(Map<String, String> params, int start,
			int rownum) {
		return ${lower}Dao.queryData(params, start, rownum);
	}
	public BaseDomain queryDataOne(Map<String, String> params) {
		return ${lower}Dao.queryDataOne(params);
	}

	public Object getCount() {
		return ${lower}Dao.getCount();
	}
	@HanderMethod(name = "删除${describle}信息", type = Type.LOG_DELETE,getMethod = "getById")
	public boolean delete(String id) {
		${title} ${lower} = new ${title}();
		${lower}.setId(id);
		return ${lower}Dao.delete(${lower});
	}
}
