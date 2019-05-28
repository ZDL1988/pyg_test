//首页控制器
app.controller('HomeaddressController',function($scope,$controller,$sce,HomeaddressService){


	$scope.findAll=function(){
        HomeaddressService.findAll().success(
					function(response){
						$scope.list=response;
					}
			);
	}
	$scope.findspe=function(sp){
		var b = JSON.parse(sp);
		var d ='';
      for (var i in b){
      	d+=b[i];

	  }

	  return d;
	}

	$scope.status=function(status){
		if (status=='1'){

            return "默认地址";

		}else{
			return "设为默认地址";
		}
	}

	$scope.updatestatus=function (status) {
        HomeaddressService.updatestatus(status).success(function (data) {
            $scope.findAll();
			alert(data);
        })
    }

    $scope.findprovinces=function () {
        HomeaddressService.findprovinces().success(function (data) {
			$scope.provinceslist = data;
        })
    }

    $scope.findcity = function () {
		var provinceid = $scope.pojo.provinceId;
        HomeaddressService.findcity(provinceid).success(function (data) {
			$scope.citylist = data;
        })
    }

    $scope.findarea = function () {
		var cityid = $scope.pojo.cityId;
        HomeaddressService.findarea(cityid).success(function (data) {
			$scope.arealist = data;
        })
    }

    $scope.add=function (id) {
		if(id !=null){
            HomeaddressService.up($scope.pojo).success(function (data) {
                $scope.pojo={};
                alert(data)
            })
		}else {
            HomeaddressService.add($scope.pojo).success(function (data) {
                alert(data)
                $scope.pojo={};
                $scope.findAll();

            })
		}

    }

    $scope.del=function (id) {
        HomeaddressService.del(id).success(function (data) {

			alert(data)
			$scope.findAll();
        })
    }

    $scope.updateedit=function (id) {
        HomeaddressService.updateedit(id).success(function (data) {
        	$scope.pojo=data;
        })
    }
    $scope.cq=function () {
      $scope.pojo={};

    }



});