$(document).ready(function(){
    $(".nav #group").addClass("active");
    loadMessageIsRead() ;

    function loadMessageIsRead() {
        setInterval(function() {
            var isClass = $(".hid_id");
            if (isClass.length > 0) {
                for (var i = 0; i < isClass.length; i++) {
                    var id_cvt = isClass[i].value;

                    $.ajax({
                        type : "GET",
                        url : "/group/loadLastMessageInGroup",
                        dataType : 'json',
                        async : false,
                        contentType : "application/json",
                        data : {
                            cvt : id_cvt
                        },
                        success : function(result) {
//                            console.log(id_cvt);
                            var isRead = $('.lastMessage');
                            isRead[i].innerHTML = "<p>"+ result.content+ "</p>";
                        }
                    })
                }
            }
        }, 1000);
    };
});