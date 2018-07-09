package com.hw.action;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hw.domain.${title};
import com.hw.service.${title}Service;
import com.hw.service.PersonService;

/**
* @Title ${title}Action
* @Description ${describle}Action
* @Company ${company}
* @author ${author}
* @date ${date}
*/
@Controller
@RequestMapping("/${lower}")
public class ${title}Action extends BaseAction {
	
	@Autowired
	private ${title}Service ${lower}Service;
	@Autowired
	PersonService ps;
	
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam Map<String, Object> reqMap,
		HttpSession session) throws Exception {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Map<String, String> params = QueryRequest.buildQuery(reqMap);
		if (!"administrator".equals(session.getAttribute("username"))) {
			this.addPermission(session, params);// 这里是数据资源权限的入口，不能注掉
		}
		int start = Integer
				.parseInt(String.valueOf(reqMap.get("start") == null ? "0"
						: reqMap.get("start")));
		int rownum = Integer
				.parseInt(String.valueOf(reqMap.get("rownum") == null ? "10"
						: reqMap.get("rownum")));
		boolean rowcount = Boolean.parseBoolean(String.valueOf(reqMap
				.get("rowcount") == null ? "true" : reqMap.get("rowcount")));
		boolean excel = Boolean
				.parseBoolean(String.valueOf(reqMap.get("excel") == null ? "false"
						: reqMap.get("excel")));
		String sort = String.valueOf(reqMap.get("sort") == null ? "" : reqMap
				.get("sort"));
		if (excel) {
			String path = ${lower}Service.exportExcel(params);
			result.put("path", path);
			return result;
		}
		if (null != sort && !sort.equals("")) {
			${lower}Service.addSort(sort);
		}
		result.put("records", ${lower}Service.list(params, start, rownum));
		if (rowcount) {
			result.put("count", ${lower}Service.getCount());
		}
		return result;
	}
	public Map<String, String> addPermission(HttpSession session,
			Map<String, String> paramMap) throws Exception {
		paramMap.put("org_permission", ps.getPermission(session, paramMap));
		return paramMap;
	}
	@RequestMapping("/add")
	@ResponseBody
	public Map<String, Object> addEscape(@RequestBody ${title} ${lower}) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		boolean status = false;
		String id = null;
		try {
			id = ${lower}Service.add(${lower});
			if (StringUtils.isNotBlank(id))
				status = true;
		} catch (Exception e) {
			status = false;
		}
		result.put("status", status);
		result.put("id", id);
		return result;
	}


	@RequestMapping("/update")
	@ResponseBody
	public Map<String, Object> update(@RequestBody ${title} ${lower}) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("status", ${lower}Service.update(${lower}));
		return result;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(String id) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("status", ${lower}Service.delete(id));
		return result;
	}
}
