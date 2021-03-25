connect();
url = window.location.pathname;
var stuff = url.split('/');
var id_cvt = stuff[stuff.length-3];
var id_user = $("#id-login").text();
var img = $("#img").attr('src');
var user_active = stuff[stuff.length-1];

console.log(user_active);


    $("#"+user_active).addClass("active");
scrollToBottom(document.getElementById('content'));

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
//        console.log('Connected to: ' + frame);
        stompClient.subscribe('/topic/conversation/' + id_cvt, function (response) {
            var data = JSON.parse(response.body);
            var d = new Date(data.createdAt);

            if(data.id_user_send == id_user){
                addMsg(data.content); // thèn gửi tin nhắn
            }else{
                showMsgReceive(data.content, d.getHours()+':'+d.getMinutes()); // thèn nhận tin nhắn
            }
            scrollToBottom(document.getElementById('content'));
        });
    });
}

function addMsg(text) {
    $('#chat-conversation').append("<div class='message me'>"+
                                   "<div class='text-main'>"+
                                       "<div class='text-group me'>"+
                                          "<div class='text me'>"+
                                              "<p>" + text + "</p>"+
                                          "</div>"+
                                      "</div>"+
                                   "</div>"+
                               "</div>");
}

function showMsgReceive(text, time) {
    $('#chat-conversation').append("<div class='message'>"+
                                   "<img class='avatar-img' alt='' src='"+img+"'>"+
                                   "<div class='text-main'>"+
                                       "<div class='text-group '>"+
                                          "<div class='text'>"+
                                              "<p>" + text + "</p>"+
                                          "</div>"+
                                      "</div>"+
                                      "<span>"+time+"</span>"+
                                   "</div>"+
                               "</div>");
}

function sendMessage() {
    var content = $("#content-chat").val();
    if( content == ":)"){
        var content = "&#127773";
        stompClient.send("/app/conversations/"+id_cvt, {}, JSON.stringify({id_user_send:id_user,content: content}));
        $("#content-chat").val("");
        $(".emoji-wysiwyg-editor").text("");
        scrollToBottom($('#content'));
    }else if(content != ""){
        stompClient.send("/app/conversations/"+id_cvt, {}, JSON.stringify({id_user_send:id_user,content: content}));
        $("#content-chat").val("");
        $(".emoji-wysiwyg-editor").text("");
        scrollToBottom($('#content'));
    }
}

function scrollToBottom(el) {
    el.scrollTop = el.scrollHeight;
}


function disconnect() {
   if (stompClient != null) {
        stompClient.disconnect();
        console.log("Disconnected");
    }
}

function onError(error) {

}

