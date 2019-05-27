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
<div id="main" style="width: 600px;height:400px;"></div>
<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    option = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            x: 'left',
            data:[]
            // data:['手机','电视','电脑','酒','衣服','家具','汽车']
        },
        series: [
            {
                name:'访问来源',
                type:'pie',
                radius: ['50%', '70%'],
                avoidLabelOverlap: false,
                label: {
                    normal: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        show: true,
                        textStyle: {
                            fontSize: '30',
                            fontWeight: 'bold'
                        }
                    }
                },
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                data:[]
                // data:[ {value:15, name:'手机'},
                //     {value:20, name:'电视'},
                //     {value:13, name:'电脑'},
                //     {value:18, name:'酒'},
                //     {value:35, name:'衣服'},
                //     {value:1, name:'家具'},
                //     {value:35, name:'汽车'}]

            }
        ]
    };

    // // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    $.ajax({
        url:"../rest/userChart.do",
        async:false,
        cache:false,
        success:function (data) {
    console.log(data);
            myChart.setOption({
                legend: {data:data.name},
                    // data:['手机','电视','电脑','酒','衣服','家具','汽车']
                // xAxis:{data:data.categories},
                series:[{name:'销量',data:data.category}]
            });

        },
        error:function (error) {
            alert(error);
        }
    })
</script>
</body>
</html>
