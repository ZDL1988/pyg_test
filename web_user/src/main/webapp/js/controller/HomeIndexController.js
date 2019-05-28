//首页控制器
app.controller('homeIndexController',function($scope,homeIndexService){
	$scope.findOrderList=function(){
        homeIndexService.findOrderList().success(
					function(response){
						$scope.orderlsit=response;
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

	$scope.findstatus=function(status){
		if (status=='1'){
			return "未支付";

		}else if (status=='2'){
			return "已支付";
		}
	}

});