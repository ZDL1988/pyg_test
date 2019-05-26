//控制层
app.controller('userController' ,function($scope,$controller,userService){

    $controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中
    $scope.findAll=function(){
        userService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }

    $scope.freeze=function(){
        //获取选中的复选框
        userService.freeze( $scope.selectIds ).success(
            function(response){
                console.log(response);
                if(response.success){
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }else{
                    alert("失败");
                }
            }
        );
    }
    $scope.unfreeze=function(){
        //获取选中的复选框
        userService.unfreeze( $scope.selectIds ).success(
            function(response){
                if(response.success){
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity={};//定义搜索对象

    //搜索
    $scope.search=function(page,rows){
        userService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

});
