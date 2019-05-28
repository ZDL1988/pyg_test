 //控制层 
app.controller('goods2Controller' ,function($scope,$location,$controller,itemCatService   ,goods2Service){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		 goods2Service.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		 goods2Service.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体
	$scope.findOne=function(id){
		 goods2Service.findOne(id).success(
			function(response){
				$scope.entity= response;
			}
		);
	}



	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		 goods2Service.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
	// 显示状态
	$scope.status = ["未审核","审核通过","审核未通过","关闭"];
	
	$scope.itemCatList = [];
	// 显示分类:
	$scope.findItemCatList = function(){
		
		itemCatService.findAll().success(function(response){
			for(var i=0;i<response.length;i++){
				$scope.itemCatList[response[i].id] = response[i].name;
			}
		});
	}
	

	$scope.EP=function () {

		$scope.reloadList();
		//获取协议
		var http = $location.protocol()
		//获取主机地址
		var host = $location.host();
		//获取端口号
		var port = $location.port();
        window.open(http+"://"+host+":"+port+"/goods/Epgood.do?ids="+$scope.selectIds,"_blank");
        $scope.selectIds = [];
	}
});	
