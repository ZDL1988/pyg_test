//服务层
app.service('homeIndexService',function($http){
	//读取列表数据绑定到表单中
	this.findOrderList=function(){
		return $http.get('../Order/findAll.do');
	}
	
});