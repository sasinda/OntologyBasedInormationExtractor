jQuery('#productReviews td > div').each(function () {
    var a = jQuery(this).clone().children().remove().end();
    console.log(a.text());
});