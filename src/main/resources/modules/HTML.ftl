<!DOCTYPE html>
<html>
<head lang="en">
<meta charset="UTF-8">
<title>${describle}</title>
</head>
<body>
	<!-- 定义js和处理类 -->
	<div data-control="Script" src="./${title}.js" />
	<div data-control="Style" src="/public.common/css/template.css" />
	<div data-control="HandleClass" className="${title}" />
	<div class="container-fluid">
		<div class="business-title">
			<div class="title-line" />
			<span class="title">${describle}</span>
			<div class="business-btn">
				<div data-control="Button" class="btn-sm btn-success" text="新增登记"
					click="{add}" />
			</div>
		</div>
		<!--登记-->
		<div data-control="Panel" id="add-form"
			class="business-add-form form-horizontal"
			dataSource="/${lower}/add.do" style="display: none;">

			<div class="row">
			<#list zds as model>
				<#if model.en!="id" && model.en?index_of("dm")<0 >
                        <label class="col-sm-1 control-label" style="margin-bottom: 10px;"> ${model.zh}： </label>
                        <div data-control="TextInput" class="col-sm-2" path="${model.en}" style="margin-bottom: 10px;"/>
				</#if>
			</#list>
			</div>

			<div class="row" id="btnlist" style="text-align: center;">
				<div data-control="Button" class="btn-primary btn-sm"
					style="width: 80px;" text="保存" click="{save}" />
				<div data-control="Button" class="btn-default btn-sm" id="cancel"
					style="width: 80px;" text="取消" click="{addCancel}" />
			</div>
		</div>


		<div data-control="Group" class="business-query-form" id="queryform">
			<div data-control="Group" id="query-input"
				class="business-query-input form-horizontal" dataSource="">
				<div class="business-query-input-row">
					<#--已经忽略带dm字段-->
					<#list zds as model>
						<#if model.num?eval <= 5 && model.en != "id" && model.en?index_of("dm")<0>
							   <label class="col-sm-1 control-label"> ${model.zh}： </label>
								<div data-control="TextInput" class="col-sm-1" path="${model.en}" />
						</#if>
					</#list>

               <div data-control="Button" class="btn-sm btn-primary" text="查询"
                        click="{executeQuery}" />
                    <div data-control="Button" id="cancel" class="btn-sm btn-default"
                        text="清空" click="{claerQuery}" />
			</div>
		</div>


            <div data-control="DataGrid" id="table"
			dataSource="/${lower}/query.do" rownum="10"
			class="form-horizontal" subGrid="true" onExpandRow="{expand}"
			onExpandPanel="modifyForm">
			<#list zds as model>
				<#if model.en = "id">
                	<div data-control="Col" title="id" path="id" width="" hidden="true" />
				</#if>
				<#if model.en != "id">
					<div data-control="Col" title="${model.zh}" path="${model.en}" width="200" align="center"/>
				</#if>
			</#list>
			<div data-control="Button" title="修改" formatter="button" text="修改"
				width="95" sortable="false" click="{modifyRow}" align="center" />
			<div data-control="Button" title="删除" formatter="button" text="删除"
				width="95" sortable="false" click="{remove}" align="center" />
		</div>
		<!--修改表单-->
		<div data-control="Group" id="modifyForm" class="business-add-form form-horizontal"
			dataSource="/${lower}/update.do" style="display: none;">
			<div class="row">
				<div data-control="Label" class="" path="id" hidden="true"/>
				<#list zds as model>
					<#if model.en!="id" && model.en?index_of("dm")<0 >
                        <label class="col-sm-1 control-label" style="margin-bottom: 10px;"> ${model.zh}： </label>
                        <div data-control="TextInput" class="col-sm-2" path="${model.en}" style="margin-bottom: 10px;"/>
					</#if>
				</#list>
            </div>
            <div class="row" style="text-align: center;">
                <div data-control="Button"
                     style="width: 60px; font-size: 12px; margin-bottom: 10px;"
                     class="btn btn-sm btn-primary" text="保存" click="{updateEntity}" />
                <div data-control="Button"
                     style="width: 60px; font-size: 12px; margin-bottom: 10px;"
                     class="btn btn-sm btn-default" text="取消" click="{cancel}" />
            </div>
            </div>
	</div>
</body>
</html>