package com.hw.domain;
import com.hw.domain.*;
/**
* @Title ${title}
* @Description ${describle}
* @Company ${company}
* @author ${author}
* @date ${date}
*/
@DomainTitle(desc = "${chineseTablename}")
public class ${title} extends BaseDomain{
	private static final long serialVersionUID = 1L;

    <#list zds as model>
    @Title(title = "${model.zh}")
    private String ${model.en};
    </#list>

    <#list zds as model>
    public String get${model.upper}() {
        return this.${model.en};
    }
    public void set${model.upper}(String ${model.en}) {
        this.${model.en} = ${model.en};
    }
    </#list>
}