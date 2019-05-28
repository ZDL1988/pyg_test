//服务层
app.service('HomeaddressService',function($http){
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../address/findAll.do');
	}
	this.updatestatus=function (status) {
        return $http.get('../address/updatestatus.do?id='+status);
    }
    this.findprovinces=function () {
		return $http.get('../address/findprovinces.do');
    }

    this.findcity=function (pid) {

        return $http.get('../address/findcity.do?id='+pid);
    }

    this.findarea=function (cid) {

        return $http.get('../address/findareas.do?id='+cid);
    }
    this.add=function (pojo) {
        return $http.post('../address/add.do',pojo);
    }
    this.del=function (id) {
        return $http.post('../address/del.do?id='+id);
    }
    this.updateedit=function (id) {
        return $http.post('../address/updateedit.do?id='+id);
    }
	this.up=function (pojo) {
        return $http.post('../address/up.do',pojo);
    }
});