$(function () {

        var graphDisplay = true
        Date.prototype.Format = function (fmt) { //author: meizz
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }

        function dateFmt(now) {
            return now.Format("yyyy/MM/dd hh:mm:ss")
        }

        ///定义一个输入数据
        var inputdata = [];
        var chart = echarts.init(document.getElementById("chart"));
        var option = {

            title: {
                text: '实时温度',
            },
            xAxis: {
                type: 'time',
                splitNumber: 24,
                axisLabel: {
                    interval: 2,
                    rotate: 40
                },
            },

            toolbox: {
                show: true,
                feature: {
                    saveAsImage: {},//导出为图片
                    myClose: { //自定义方法 配置
                        show: true,
                        title: '关闭图表',
                        icon: 'path://M710.624 665.376l-45.248 45.248L512 557.248l-153.376 153.376-45.248-45.248L466.752 512l-153.376-153.376 45.248-45.248L512 466.752l153.376-153.376 45.248 45.248L557.248 512l153.376 153.376zM512 64C264.576 64 64 264.576 64 512s200.576 448 448 448 448-200.576 448-448S759.424 64 512 64z',
                        onclick: function () {
                            if (graphDisplay==true){
                                // clearInterval(clk1);//关闭实时请求数据
                                // $("#chart")[0].style.display = 'none';//关闭div
                                graphDisplay=false;
                            }
                            else{
                                graphDisplay=true;
                            }
                        }
                    },
                },
            },
            visualMap: {//区间内控制显示颜色
                show: false,
                min: -30,//数据最小值
                max: 50,//数据最大值
                range: [-18, 30],//符合的变换温度区间
                inRange: {//在温度区间内的变换情况
                    color: ['blue', 'orange', 'red'],
                },
                outOfRange: {//超出温度变换区间之后进行超区报警展示
                    symbol: 'pie',
                    symbolSize: 15,
                    color: ['blue', 'red'],
                }
            },
            yAxis: {
                type: 'value',
                axisLabel: {
                    formatter: '{value} '       //给Y轴上的刻度加上单位
                },
            },

            visualMap: {//区间内控制显示颜色
                show: false,
                min: -30,//数据最小值
                max: 50,//数据最大值
                range: [-18, 30],//符合的变换温度区间
                inRange: {//在温度区间内的变换情况
                    color: ['blue', 'orange', 'red'],
                },
                outOfRange: {//超出温度变换区间之后进行超区报警展示
                    symbol: 'pie',
                    symbolSize: 15,
                    color: ['blue', 'red'],
                }
            },
            dataZoom: [
                {
                    type: 'slider',//数据滑块
                    //start: 0,
                    minSpan: 8,    //5min
                    start: [],      // 左边在 动态规划 的位置。
                    end: 100,       // 右边在 100% 的位置。
                    dataBackground: {
                        lineStyle: {
                            color: '#95BC2F'
                        },
                        areaStyle: {
                            color: '#95BC2F',
                            opacity: 0.5,
                        }
                    },
                },
                {
                    type: 'inside'//使鼠标在图表中时滚轮可用
                }
            ],
            tooltip: {
                trigger: 'axis',
                formatter: function (params) {
                    var result = params[0].value[0];
                    params.forEach(function (item) {
                        result += '<br/>';
                        result += '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + item.color + '"></span>';
                        result += item.seriesName + "：";
                        result += isNaN(item.value[1]) ? 0 : (item.value[1]).toFixed(2) + "";
                    });
                    return result;
                },
            },
            series: {
                name: '温度',
                type: 'line',
                color: '#95BC2F',
                showSymbol: false,
                symbolSize: 12,
                data: inputdata,
                markPoint: {
                    data: [
                        { type: 'max', name: '最大值' },
                        { type: 'min', name: '最小值' }
                    ]
                },
                markLine: {
                    data: [
                        { type: 'average', name: '平均值' }
                    ]
                }
            }
        };

        chart.setOption(option);

        $("#timeSpan").on('click', 'li', function () {
            var timeSpan = $(this).html();
            switch (timeSpan) {
                case '5分钟':
                    chart.setOption({
                        dataZoom: [
                            {
                                start: 92,
                                end: 100
                            }
                        ],
                    });
                    break;
                case '10分钟':
                    chart.setOption({
                        dataZoom: [
                            {
                                start: 84,
                                end: 100
                            }
                        ],
                    });
                    break;
                case '15分钟':
                    chart.setOption({
                        dataZoom: [
                            {
                                start: 76,
                                end: 100
                            }
                        ],
                    });
                    break;
                case '30分钟':
                    chart.setOption({
                        dataZoom: [
                            {
                                start: 50,
                                end: 100
                            }
                        ],
                    });
                    break;
            }
        });

        function refresh() {


            $.ajax({
                url:'/production/pushData?id=1',
                type:'post',
                contentType : "application/json",
                success:function(data){
                    inputVal = data.value;
                    newtime =  data.time;
                    inputdata.push({ value: [newtime, inputVal] });

                    var start;//显示27个数据

                    if (graphDisplay == true) {
                        if (inputdata.length < 28) {
                            start = 0;
                        }
                        else {
                            start = 100 - (28 / inputdata.length) * 100;
                        }
                        chart.setOption({
                            series: {
                                data: inputdata,
                            },
                            dataZoom: [{
                                start: start,
                            }]
                        });
                    }
                },
               error:function () {
                   alert("请求数据出错")
               }
            });

        }
        clk1 = setInterval(function () {
            refresh();
        }, 1000);

        window.onresize = function () {
            chart.resize();
        };

    }
);