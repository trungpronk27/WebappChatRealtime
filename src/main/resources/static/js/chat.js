$(document).ready(function() {
    getId();
    $(".nav #chat").addClass("active");
    loadMessageIsRead();
    countNewMessage();
});

function getId() {
    $('.member a').click(function() {
        var id = $(this).attr("id");

        $.ajax({
            type : "GET",
            url : "/conversation/" + id,
            dataType : 'json',
            contentType : "application/json; charset=utf-8",

            success : function(data) {
                var getIdCvt = data.id;
                window.location = "/conversation/" + getIdCvt + "/mess/" + id;
            },
            error : function(e) {
                console.log("errors")
                console.log(e)
            }
        });
    });
}



function loadMessageIsRead() {
    var id_login = $("#id-login").text();
    setInterval(function() {
        var isClass = $(".hid_id");
        if (isClass.length > 0) {
            for (var i = 0; i < isClass.length; i++) {
                var id_user = isClass[i].value;
                $.ajax({
                    type : "GET",
                    url : "http://localhost:8080/chat/loadMessageIsRead",
                    dataType : 'json',
                    async : false,
                    contentType : "application/json",
                    data : {
                        user1 : id_login,
                        user2 : id_user
                    },
                    success : function(result) {
                        if (result.id_user_send != id_login) {
                            if(result.isRead == 0)    {
                                var isRead = $('.messIsRead');
                                isRead[i].innerHTML = "<p class='short-chat read'>"+ result.content+ "</p>";
                            } else {
                                var isRead = $('.messIsRead');
                                isRead[i].innerHTML = "<p class='short-chat'>"+ result.content+ "</p>";
                            }
                        } else if (result.id_user_send == id_login){
                            var isRead = $('.messIsRead');
                            isRead[i].innerHTML = "<p class='short-chat'>"+"You: "+ result.content+ "</p>";
                        }
                    }
                })
            }
        }
    }, 1000);
}



function countNewMessage() {
    var id_login = $("#id-login").text();
    setInterval(function() {
        var isClass = $(".hid_id");
        if (isClass.length > 0) {
            for (var i = 0; i < isClass.length; i++) {
                var id_user = isClass[i].value;
                $.ajax({
                    type : "GET",
                    url : "http://localhost:8080/chat/countNewMessage",
                    dataType : 'json',
                    async : false,
                    contentType : "application/json",
                    data : {
                        user1 : id_login,
                        user2 : id_user
                    },
                    success : function(result) {
                        if(result > 0)    {
                            var newMessage = $('.countNewMessage');
                            newMessage[i].innerHTML = "<p style='font-weight:bold; color:red;'> +"+ result+ "</p>";
                        }
                    }
                })
            }
        }
    }, 1000);
}

