//购物车控制层
app.controller('homeOrderPayController', function ($scope, homeOrderPayService) {
    //查询购物车列表
    $scope.findOrderListByPage = function () {
        homeOrderPayService.findOrderListByPage().success(
            function (response) {
                console.log(response);
                $scope.orderPayList = response.rows;

                $scope.totalValue = response.total;
            }
        );
    }



});