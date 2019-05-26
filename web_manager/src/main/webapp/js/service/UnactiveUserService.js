//服务层
app.service('UnactiveUserService',function($http){
	    	


    //读取列表数据绑定到表单中
    this.selectUnActiveUser=function(page,rows){
        return $http.get('../activeUser/selectUnActiveUser.do?page='+page+'&rows='+rows);
    }

});
