$(function () {
    $('#containerMuliBar').highcharts({
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Feature scores comparisons'
        },
        subtitle: {
            text: ''
        },
        xAxis: {
            categories: ['Picture Quality', 'Sound', 'Design', 'Ease of use', 'Screen','Price','Size'],
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,
            max: 10,
            title: {
                text: 'Sigma Score',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            valueSuffix: ''
        },
        plotOptions: {
            series: {
                pointWidth: 15
            },
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 100,
            floating: true,
            borderWidth: 1,
            backgroundColor: '#FFFFFF',
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: [ {
            name: 'Samsung UN40EH5300 40-Inch',
            data: [9.22, 8.50, 6.60, 7.65, 9.00,6.32,7.43]


        }, {
            name: 'Coby LEDTV2326 23-Inch',
            data: [7.03, 5.66, 3.46, 6.89, 7.55,8.66,5.99]

        }]
    });
});
