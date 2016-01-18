var readline = require('readline');
var searchAmazonSearchUrl = "http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=";
var productLink;
var jsdom = require("jsdom");
var successProductVisits = 0;
var retryEffortNum = 0
var constants = require('./Constants');
var nom = require('nom');
var constants = require("./Constants");
var textNum = 0;
var textNumLogger=1


var productUrl = process.argv.slice(2, 3).toString();
var fileLocation = process.argv.slice(3, 4).toString();


jsdom.env(productUrl,
    ["http://code.jquery.com/jquery.js"],
    function (errors, window) {
        getAllReviewsLink(window)
    }

);


function findMoreReviews(productLink) {

    var dome = require("jsdom");
    jsdom.env(
        productLink,
        ["http://code.jquery.com/jquery.js"],
        function (errors, window) {
            if (errors == null) {
                successProductVisits++;
                retryEffortNum = 0;
                console.log("visited procuct page " + successProductVisits + " successfully");
                getAllReviewsLink(window);
            } else {
                retryEffortNum++;
                console.log("error while visiting page :" + productLink);
                if (retryEffortNum <= 2) {
                    console.log("retrying...");
                    //findMoreReviews(productLink);
                } else {
                    console.log("page aborted");
                    retryEffortNum = 0;
                }
            }
        }
    );
}

function getAllReviewsLink(window) {
    if (window != null) {
        var seeAllReviewsUrl = window.$('#seeAllReviewsUrl').attr("href");
        if (seeAllReviewsUrl != undefined) {
            console.log(seeAllReviewsUrl);
            findRecursiveLink(seeAllReviewsUrl);
            return seeAllReviewsUrl;
        } else {
            return null;
        }

    } else {
        return null;
    }

}


function findRecursiveLink(reviewPage) {


    jsdom.env(
        reviewPage,
        ["http://code.jquery.com/jquery.js"],
        function (errors, window) {

            if (errors == null) {

                var href = window.$("span.paging").find("a").attr('href');
                if (href != undefined) {

                    var baseLInk = href.split('pageNumber=');
                    var frontPartofLink = baseLInk[0];
                    var lastPartofLink = baseLInk[1].split('&')[1];

                    var noOfPages = window.$("span.paging").find("a").eq(window.$("span.paging").find("a").length - 2).text();

                    for (var i = 1; i <= noOfPages; i++) {
                        var url = frontPartofLink + 'pageNumber=' + i + '&' + lastPartofLink;


                        readReviews(url);

                    }
                } else {
                    readReviews(reviewPage);
                }


            }

        }
    );

}


function readReviews(reviewPageLink) {

    nom(reviewPageLink, function (err, $) {




        if (err == null) {
            var text = '';

            $('#productReviews td > div').each(function () {
                var star_rating = "" ;
                var review_writer="";
                var parent = $(this).clone();

                try{
                    // review_writer =$(this).clone().children()[2].children[1].children[1].children[0].children[0].children[0].data;
                } catch(e){
                    try{
                        review_writer =$(this).clone().children()[1].children[1].children[1].children[0].children[0].children[0].data;
                    }catch (e){
                        console.log("writer not logged");
                        writer="NULL";
                    }
                    // review_writer =$(this).clone().children()[1].children[1].children[1].children[0].children[0].children[0].data;
                }

                try {
                    star_rating = $(this).clone().children()[1].children[0].next.children[0].children[0].children[0].data.substring(0,3);

                } catch (e) {
                    try{
                        star_rating = $(this).clone().children()[0].children[0].next.children[0].children[0].children[0].data.substring(0,3);
                    }catch (e){
                        console.log("star rate not logged");
                        star_rating="NULL";
                    }


                }


                parent.children().each(function () {
                    $(this).remove();

                }); // Remove all child elements we just need the body text...
                text ="\<link = "+reviewPageLink+"\>"+'\n' +"\<author="+review_writer+'\>'+'\n' +"\<SR="+ star_rating +">"+ '\n' + parent.text().trim();
                writeFile(text)
            });

            // writeFile(text);

        } else {

            //readReviews(reviewPageLink);
        }
    });


}
function writeFile(text) {

    textNum++;
    var fs = require('fs');
    fs.writeFile(fileLocation + "/page-" + textNum + ".txt", text, function (err) {
        if (err) {
            console.log(err);
        } else {
            console.log("The file page-" + textNumLogger + ".txt was saved!");
            textNumLogger++;
        }
    });
}