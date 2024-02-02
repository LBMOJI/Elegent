$(document).ready(function () {
    $(".one").click(function () {
        $(this).next().slideToggle();
        $(this).parent().siblings().children("ul").slideUp();
    });

    $('#name').on("keydown", function (event) {
        let keycode = event.keyCode || event.which;
        let name = $('#name').val();
        if (keycode == "13") {
            location.href = "/admin/searchSaler/" + name;
        }
    });

    $('#aid').on("keydown", function (event) {
        let keycode = event.keyCode || event.which;
        let aid = $('#aid').val();
        if (keycode == "13") {
            location.href = "/admin/searchAcception/" + aid;
        }
    });

    $('#rid').on("keydown", function (event) {
        let keycode = event.keyCode || event.which;
        let rid = $('#rid').val();
        if (keycode == "13") {
            location.href = "/admin/searchRecord/" + rid;
        }
    });

    $('#bname').on("keydown", function (event) {
        let keycode = event.keyCode || event.which;
        let bname = $('#bname').val();
        if (keycode == "13") {
            location.href = "/admin/searchBaoxian/" + bname;
        }
    });

    $('#uid').on("keydown", function (event) {
        let keycode = event.keyCode || event.which;
        let uid = $('#uid').val();
        if (keycode == "13") {
            location.href = "/admin/searchUser/" + uid;
        }
    });

    $('#cph').on("keydown", function (event) {
        let keycode = event.keyCode || event.which;
        let cph = $('#cph').val();
        if (keycode == "13") {
            location.href = "/admin/searchCar/" + cph;
        }
    });
});

window.onload = function () {
    let my = $("#now");
    my.next().slideToggle();
    my.parent().siblings().children("ul").slideUp();
}

function showPopup1(id) {
    var overlay = document.getElementById("overlay" + id);
    overlay.style.display = "block";
}

function hidePopup1(id) {
    var overlay = document.getElementById("overlay" + id);
    overlay.style.display = "none";
}

function showPopup2(id) {
    var overlay = document.getElementById("overlay-" + id);
    overlay.style.display = "block";
}

function hidePopup2(id) {
    var overlay = document.getElementById("overlay-" + id);
    overlay.style.display = "none";
}
function showPopup() {
    var overlay = document.getElementById("overlay");
    overlay.style.display = "block";
}

function hidePopup() {
    var overlay = document.getElementById("overlay");
    overlay.style.display = "none";
    $("#addTips").text("");
}

function hidePopup3(id) {
    var overlay = document.getElementById("overlay+" + id);
    overlay.style.display = "none";
}

function showPopup3(id) {
    var overlay = document.getElementById("overlay+" + id);
    overlay.style.display = "block";
}