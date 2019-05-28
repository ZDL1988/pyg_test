// 定义控制器:
app.controller("brandController",function($scope,$location,$controller,$http,brandService){
	// AngularJS中的继承:伪继承
	$controller('baseController',{$scope:$scope});
	
	// 查询所有的品牌列表的方法:
	$scope.findAll = function(){
		// 向后台发送请求:
		brandService.findAll().success(function(response){
			$scope.list = response;
		});
	}

	// 分页查询
	$scope.findByPage = function(page,rows){
		// 向后台发送请求获取数据:
		brandService.findByPage(page,rows).success(function(response){
			$scope.paginationConf.totalItems = response.total;
			$scope.list = response.rows;
		});
	}
	
	// 保存品牌的方法:
	$scope.save = function(){
		// 区分是保存还是修改
		var object;
		if($scope.entity.id != null){
			// 更新
			object = brandService.update($scope.entity);
		}else{
			// 保存
			object = brandService.save($scope.entity);
		}
		object.success(function(response){
			// {success:true,message:xxx}
			// 判断保存是否成功:
			if(response.success==true){
				// 保存成功
				alert(response.message);
				$scope.reloadList();
			}else{
				// 保存失败
				alert(response.message);
			}
		});
	}
	
	// 查询一个:
	$scope.findById = function(id){
		brandService.findById(id).success(function(response){
			// {id:xx,name:yy,firstChar:zz}
			$scope.entity = response;
		});
	}
	
	// 删除品牌:
	$scope.dele = function(){
		brandService.dele($scope.selectIds).success(function(response){
			// 判断保存是否成功:
			if(response.success==true){
				// 保存成功
				// alert(response.message);
				$scope.reloadList();
				$scope.selectIds = [];
			}else{
				// 保存失败
				alert(response.message);
			}
		});
	}
	
	$scope.searchEntity={};
	
	// 假设定义一个查询的实体：searchEntity
	$scope.search = function(page,rows){
		// 向后台发送请求获取数据:
		brandService.search(page,rows,$scope.searchEntity).success(function(response){
			$scope.paginationConf.totalItems = response.total;
			$scope.list = response.rows;
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
        window.open(http+"://"+host+":"+port+"/brand/Epmb.do");

    }

    $scope.uploadFile = function () {
        var url = "localhost:8084/brand/importListExcel.do";
        var formData = new FormData();//使用FormData进行文件上传
        formData.append("file", file.files[0]);//拿到当前文件
        $http.post(url, formData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).success(function (data, status) {

        }).error(function (data, status) {

        });
    };

});
