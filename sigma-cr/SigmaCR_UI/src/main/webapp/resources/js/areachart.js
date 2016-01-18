/**
 * Created with IntelliJ IDEA.
 * User: DELL
 * Date: 8/14/13
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
$(function () {
    $('#containerarea').highcharts({
        chart: {
            type: 'area'
        },
        title: {
            text: 'Product feedback over time'
        },
        subtitle: {
            text: ''
        },
        xAxis: {
            categories: ['2012 Jan','', '2012 Mar','', '2012 May','', '2012 Jul','', '2012 Sep','', '2012 Nov','', '2013Jan', ''],
            tickmarkPlacement: 'on',
            title: {
                enabled: false
            }
        },
        yAxis: {
            title: {
                text: 'Num of Reviews'
            },
            labels: {
                formatter: function () {
                    return this.value;
                }
            }
        },
        tooltip: {
            shared: true,
            valueSuffix: ''
        },
        plotOptions: {
            area: {
                stacking: 'normal',
                lineColor: '#666666',
                lineWidth: 1,
                marker: {
                    lineWidth: 1,
                    lineColor: '#666666'
                }
            }
        },
        series: [
            {
                name: 'Neutral',
                color: '#CECECE',
                data: [0,96, 250, 350, 400,100, 300,250 ,200, 100, 123, 90, 10,5]

            },
            {
                name: 'Positive',
                color: '#007A29',
                data: [0,296, 454, 756, 1500,2488, 2824,2771 ,2555, 2223, 1127, 1012, 821,602]
            },
            {
                name: 'Negative',
                color: '#B82E00',
                data: [0,96, 250, 450, 550,650, 600,512 ,428, 578, 602, 428, 505,688]
            }

        ],


        credits: {
            enabled: false
        }
    });
});