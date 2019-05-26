//服务层
app.service('seckillOrderService',function($http){

    //读取列表数据绑定到表单中
    this.findAll=function(){
        return $http.get('../seckillOrderService/findAll.do');
    }

    this.search = function(searchEntity){
        return $http.post("../seckillOrderService/search.do?",searchEntity);
    }

});

