connect();
url = window.location.pathname;
var stuff = url.split('/');
var group_id = stuff[stuff.length-2];
var id_user = $("#id-login").text();
const img = $("#img").attr('src');

$("#"+group_id).addClass("active");

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected to: ' + frame);
        stompClient.subscribe('/topic/group/' + group_id, function (response) {
            var data = JSON.parse(response.body);
            var d = new Date(data.createdAt);

            if(data.id_user_send == id_user){
                addMsg(data.content); // thèn gửi tin nhắn
            }else{
                showMsgReceive(data.content, d.getHours()+':'+d.getMinutes(), data.img); // thèn nhận tin nhắn
            }
            scrollToBottom(document.getElementById('content'));
        });
    });
}

function addMsg(text) {
     $('#chatGroup-conversation').append("<div class='message me'>"+
                                       "<div class='text-main'>"+
                                           "<div class='text-group me'>"+
                                              "<div class='text me'>"+
                                                  "<p>" + text + "</p>"+
                                              "</div>"+
                                          "</div>"+
                                       "</div>"+
                                   "</div>");
 }

 function showMsgReceive(text, time, img) {
     $('#chatGroup-conversation').append("<div class='message'>"+
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
        stompClient.send("/app/groups/"+group_id, {}, JSON.stringify({id_user_send:id_user,img:img, content: content}));
        scrollToBottom(document.getElementById('content'));
        $(".emoji-wysiwyg-editor").text("");
        $("#content-chat").val("");
    }else if(content != ""){
        stompClient.send("/app/groups/"+group_id, {}, JSON.stringify({id_user_send:id_user,img:img, content: content}));
        scrollToBottom(document.getElementById('content'));
        $(".emoji-wysiwyg-editor").text("");
        $("#content-chat").val("");
    }
}

function scrollToBottom(el) {
    el.scrollTop = el.scrollHeight;
}


function disconnect() {
    stompClient.disconnect();
    console.log("Disconnected");
}