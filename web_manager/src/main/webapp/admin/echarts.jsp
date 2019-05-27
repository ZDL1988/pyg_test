<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>ECharts</title>
    <!-- 引入 echarts.js -->
    <script src="../js/echarts2.0/echarts.min.js"></script>
    <script src="../js/jquery/jquery-3.2.1.js"></script>
</head>
<body>
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div class="row">
    <div id="main" style="width: 800px;height:300px;">kjkgucdfglghjknlmasdasdasd</div>
</div>
<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    option = {
        xAxis: {
            type: 'category',
            data: []
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: [],
            type: 'line'
        }]
    };

    // // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    $.ajax({
        url:"../rest/echartslist.do",
        async:false,
        cache:false,
        success:function (data) {
            console.log(data)
            myChart.setOption({
                xAxis:{data:data.categories},
                series:[{name:'销量',data:data.data}]
            });
        },
        error:function (error) {
            alert(error);
        }
    })
</script>
</body>
</html>
