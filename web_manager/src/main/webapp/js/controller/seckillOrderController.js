//控制层
app.controller('seckillOrderController' ,function($scope,$controller,$location,seckillOrderService){

    $controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中
    $scope.findAll=function(){
        seckillOrderService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }


    $scope.searchEntity={};//定义搜索对象

    //搜索
    $scope.search=function(){
        seckillOrderService.search($scope.searchEntity).success(
            function(response){
                $scope.list=response;

            }
        );
    }
});

