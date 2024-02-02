$(document).ready(function () {
    $(".one_level_menubar").click(function () {
        $(this).next().slideToggle();
        $(this).parent().siblings().children("ul").slideUp();
    })
})
function showPopup(){
    var overlay = document.getElementById("overlay");
    overlay.style.display = "block";
}
function hidePopup(){
    var overlay = document.getElementById("overlay");
    overlay.style.display = "none";
}
function showPopup2(){
    var overlay = document.getElementById("overlay2");
    overlay.style.display = "block";
}
function hidePopup2(){
    var overlay = document.getElementById("overlay2");
    overlay.style.display = "none";
    $('.updateTips').children("span").text("");
}
function showPopup3(){
    var overlay = document.getElementById("overlay3");
    overlay.style.display = "block";
}
function hidePopup3(){
    var overlay = document.getElementById("overlay3");
    overlay.style.display = "none";
    $('.updateTips').children("span").text("");
}
function showPopup4(){
    var overlay = document.getElementById("overlay4");
    overlay.style.display = "block";
}
function hidePopup4(){
    var overlay = document.getElementById("overlay4");
    overlay.style.display = "none";
    $('.updateTips').children("span").text("");
}
function showPopup5(){
    var overlay = document.getElementById("overlay5");
    overlay.style.display = "block";
}
function hidePopup5(){
    var overlay = document.getElementById("overlay5");
    overlay.style.display = "none";
    $('.updateTips').children("span").text("");
}