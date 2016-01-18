/**
 * Created with IntelliJ IDEA.
 * User: DELL
 * Date: 8/14/13
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
$(function () {
    $('#containerstack').highcharts({
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Description'
        },
        xAxis: {
            categories: [' ', ' ', ' ', ' ', ' ',' ']

        },
        yAxis: {
            min: 0,
            max: 100,
            title: {
                text: 'Opinion percentages'
            }
        },
        legend: {
            backgroundColor: '#FFFFFF',
            reversed: true
        },
        plotOptions: {
            series: {
                stacking: 'normal'
            }
        },   credits: {
            enabled: false
        },
        series: [{
            showInLegend:true,
            name: 'Neutral',
            data: [50, 30, 40, 70, 25,60] ,
            pointWidth: 20
        },
            {
                showInLegend:true,
            name: 'Negative',
            data: [20, 20, 30, 20, 70,30] ,
                pointWidth: 20
        }, {
                showInLegend:true,
            name: 'Positive',
            data: [30, 50, 30, 20, 5,10],
                pointWidth: 20
        }]
    });
});
