//服务层
app.service('activeUserService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.selectActiveUser=function(page,rows){
		return $http.get('../activeUser/selectActiveUser.do?page='+page+'&rows='+rows);
	}


});
