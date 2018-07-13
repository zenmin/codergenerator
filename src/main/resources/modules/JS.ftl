/**
 * ${describle}
 * create by ${author}
 * date ${date}
 */
function ${title}() {
};

${title}.inherit(Portlet);

${title}.prototype.init = function(oComponent) {
    ${title}.base.init.call(this);
    this._component = oComponent;
}

/**
 * 页面加载完成调用方法
 * @param {} oComponent
 */
${title}.prototype.complete = function(oComponent) {
	this._userinfo = new UserInfo();
    this._table = this._component.getElementById("table");
    this._addform = this._component.getElementById("add-form");
    this._modifyForm = this._component.getElementById("modifyForm");
    this._modifyForm.hide();
    this._queryinput = this._component.getElementById("query-input");
    this._table.queryAndDisplay(); 
    this.params = new Object();
}
/**
 * 点击查询按钮
 */
${title}.prototype.executeQuery = function(oControl) {
    var params = this._queryinput.getValue();
    this._table.queryAndDisplay(params);
}
/**
 * 点击新增按钮
 */
${title}.prototype.add = function(oControl) {
    this._addform.clearForm();
    this._addform.show();
}
// 删除数据
${title}.prototype.remove= function(oTable, rowid, iCol, cellcontent, e) {
	if (!confirm("是否确定删除"))
		return;
	var soap = new HWSOAP("/${lower}/delete.do");
	soap.send({
				id : rowid
			});
	var result = soap.getResult();
	if (result.status) {
		oTable.reshow();
	}
}

/**
 * 点击保存按钮
 */
${title}.prototype.save = function(oControl) {

    var oForm = oControl.getParent();
	if (!oForm.checkValidate()) {
		return false;
	}
	var data = oForm.getData();
	
	var title = "保存失败!";
	var url = "/${lower}/add.do";
	var soap = new HWSOAP(url);
	soap.sendb(data);
	var result = soap.getResult();
	if (result.status) {
		title = "保存成功!";
		this._addform.clearForm();
		this._addform.hide();
		this._adding = false;
	}
	this._table.queryAndDisplay();
	WinUtil.showTip(oControl.getHTML(), {
				title : title,
				placement : "bottom"
			});
}
/**
 * 在新增界面点击取消
 */
${title}.prototype.addCancel = function(oControl) {
    this._addform.clearForm();
    this._addform.hide();
}
// 点击修改按钮展开的页面
${title}.prototype.modifyRow = function(oTable, rowid, iCol, cellcontent, e) {
	oTable.onRowExpand(rowid);
}

// 页面中table列表展开执行的函数
${title}.prototype.expand = function(oTable, oPanel, tableid, rowid) {
	oPanel.queryForm("/${lower}/query.do", {
				id : rowid
			});
}
// 修改数据
${title}.prototype.updateEntity = function(oControl) {
	var title = "修改失败!";
	var data = this._modifyForm.getData();
	if (!this._modifyForm.checkValidate()) {
		return false;
	}
	var url = "/${lower}/update.do";
	var soap = new HWSOAP(url);
	soap.sendb(data);
	var result = soap.getResult();
	if (result.status) {
		title = "修改成功!";
		this._table.reshow();
	}
	WinUtil.showTip(oControl.getHTML(), {
				title : title,
				placement : "bottom"
			});
}
/**
 * 在新增界面点击取消
 */
${title}.prototype.cancel = function(oControl) {
    this._addform.clearForm();
    this._addform.hide();
}
/**
 * 
 * 更新界面点击取消
 */
${title}.prototype.cancel = function(oControl) {
    this._table.onCurrentRowCollapse();
}
/**
 * 点击清空按钮
 */
${title}.prototype.claerQuery = function() {
    this._queryinput.clearForm();
}
/**
 * 注册方法
 */
${title}.HANDLER = {
}

${title}.E_HANDLER = {
    "MSG_EXPORT_ALL" : null
};