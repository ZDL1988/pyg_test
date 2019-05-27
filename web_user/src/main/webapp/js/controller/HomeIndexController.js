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


});