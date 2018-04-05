var chetbox
var funbox
var ws
var token

function showtoken() {
  console.log(token);
}

function connect() {
  ws = new WebSocket("ws://localhost:5999");
  ws.onmessage = function (event) {
    resptext = event.data
    var ojbk = JSON.parse(resptext)
    msgType = ojbk.msgType
    console.log(ojbk)
    if (msgType === 2) {
      code = ojbk.resultCode;
      if (code === 'success') {
        addmsg(null, '登陆成功!', 'sys', 'green')
        var user = document.getElementById('fromid')
        user.disabled = 'true'
        var send = document.getElementById('sendButton')
        send.disabled = null
        token = ojbk.token
      } else {
        addmsg(null, '登陆失败:' + ojbk.token, 'sys', 'red')
      }
    }
    if (msgType === 4) {
      if (ojbk.confirmResult === true) {
        addmsg(null, '群组[' + ojbk.groupId + ']的管理员同意用户[' + ojbk.toUserId + ']入群!', 'sys', 'p')
      } else {
        addmsg(null, '群组[' + ojbk.groupId + ']的管理员拒绝用户[' + ojbk.toUserId + ']入群!', 'sys', 'p')
      }
    }
    if (msgType === 6) {
      addmsg(ojbk.fromUserId, ojbk.msg)
    }
    if (msgType === 8) {
      addmsg('群[' + ojbk.toGroup + ']的用户[' + ojbk.fromUserId + ']的消息', ojbk.msg, null, 'blue')
    }
    if (msgType === 12) {
      addmsg(null, '用户[' + ojbk.fromUserId + ']退出您所在的群组[' + ojbk.groupId + ']!', 'sys', 'p')
    }
    if (msgType === 14) {
      if (ojbk.confirmResult === true) {
        addmsg(null, '用户[' + ojbk.fromUserId + ']同意和你建立好友关系!', 'sys', 'p')
      } else {
        addmsg(null, '用户[' + ojbk.fromUserId + ']拒绝和你建立好友关系!', 'sys', 'p')
      }
    }
    if (msgType === 16) {
      addmsg(null, '用户[' + ojbk.fromUserId + ']已经移除和你的好友关系!', 'sys', 'p')
    }
    if (msgType === 18) {
      addmsg(null, '用户[' + ojbk.toUserId + ']已经被管理员从[' + ojbk.fromGroup + ']', 'sys', 'p')
    }
    if (msgType === 20) {
      addmsg(null, '您已经被授予所在群[' + ojbk.groupId + ']的管理权限!', 'sys', 'p')
    }
    if (msgType === 22) {
      addmsg(null, '您已经被撤销所在群[' + ojbk.groupId + ']的管理权限!', 'sys', 'p')
    }
    if (msgType === 100 || msgType === 101 || msgType === 102) {
      addmsg(null, ojbk.msg, 'sys', 'p')
    }
    if (msgType === 103) {
      addmsg(null, '用户[' + ojbk.fromUserId + ']请求添加你为好友,请处理!', 'sys', 'p')
    }
    if (msgType === 105) {
      addmsg(null, '用户[' + ojbk.fromUserId + ']请求进入您管理的群组[' + ojbk.groupId + '],请处理!', 'sys', 'p')
    }
    if (msgType === 107) {
      addmsg(null, '好友[' + ojbk.friendId + ']上线了', 'sys', 'o')
    }
    if (msgType === 108) {
      addmsg(null, '好友[' + ojbk.friendId + ']下线了', 'sys', 'o')
    }
    if (msgType === 109) {
      addmsg('群[' + ojbk.groupId + ']的消息', '用户[' + ojbk.userId + ']上线了', null, 'o')
    }
    if (msgType === 110) {
      addmsg('群[' + ojbk.groupId + ']的消息', '用户[' + ojbk.userId + ']下线了', null, 'o')
    }
    if (msgType === 112) {
      addmsg('群[' + ojbk.groupId + ']解散了', null, 'o')
    }
  }
  ws.onclose = function (event) {}
  ws.onopen = function (event) {
    ws.send("hello tio server");
    addmsg(null, '连接服务器成功!', 'local', 'green')
  }
  ws.onerror = function (event) {
    if (event.type) {

    }
  }
}

function disconnect() {
  if (ws !== undefined) {
    ws.close()
    ws = undefined
    addmsg(null, '断开服务器!', 'local', 'p')
  }
}

function send() {
  var sb = document.getElementById('sendButton')
  if (sb.innerHTML === 'Send') {
    addmsg(null, '请选择P2P或者P2G后再进行发送', 'local', 'p')
  } else {
    var msg = document.getElementById("textId").value
    var userId = document.getElementById('fromid').value
    if (msg === "") {
      addmsg(null, '请不要发送消息!', 'local', 'p')
    } else if (sb.innerHTML === 'Send - P2P') {
      var toid = document.getElementById('toid').value
      if (toid === '') {
        addmsg(null, '请指定需要发送的用户ID!', 'local', 'p')
      } else {
        var json = '{"msgType" : "5", "msgBody":{"token" : "'+token+'", toUserId" : "' + toid + '","msg" : "' + msg + '","fromUserId" : "' + userId + '"}}'
        console.log(json);
        ws.send(json)
      }
    } else if (sb.innerHTML === 'Send - P2G') {
      var gid = document.getElementById('gid').value
      if (gid === '') {
        addmsg(null, '请指定需要发送的群组ID!', 'local', 'p')
      } else {
        var json = '{"msgType" : "7", "msgBody":{"token" : "'+token+'", "toGroup" : "' + gid + '","msg" : "' + msg + '","fromUserId" : "' + userId + '"}}'
        console.log(json);
        ws.send(json)
      }
    }
  }
}

function addmsg(title, msg, type, color) {
  var content = document.getElementById('contentId')
  var line = document.createElement('div')
  line.className = 'line'
  line.contentedtable = 'true'
  if (color === 'blue') {
    line.style.backgroundColor = 'rgba(2, 77, 128, 0.37)'
    line.style.color = 'white'
  } else if (color === 'red') {
    line.style.backgroundColor = 'rgba(162, 4, 18, 0.56)'
    line.style.color = 'white'
  } else if (color === 'green') {
    line.style.backgroundColor = 'rgba(59, 208, 7, 0.59)'
    line.style.color = 'white'
  } else if (color === 'p') {
    line.style.backgroundColor = 'rgba(230, 241, 22, 0.59)'
    line.style.color = 'gray'
  } else if (color === 'o') {
    line.style.backgroundColor = 'rgba(255, 107, 0, 0.6)'
    line.style.color = 'white'
  } else if (color === 'e') {
    line.style.backgroundColor = '#294d50'
    line.style.color = 'white'
  }

  var myDate = new Date()
  if (type === 'sys') {
    line.innerHTML = myDate.toLocaleDateString() + ' ' + myDate.toLocaleTimeString() + '  来自  ' + '服务器' + '  :  <br>' + msg
  } else if (type === 'local') {
    line.innerHTML = myDate.toLocaleDateString() + ' ' + myDate.toLocaleTimeString() + '  来自  ' + '本地客户端' + '  :  <br>' + msg
  } else {
    line.innerHTML = myDate.toLocaleDateString() + ' ' + myDate.toLocaleTimeString() + '  来自  ' + title + '  :  <br>' + msg
  }
  content.appendChild(line);
  content.scrollTop = content.scrollHeight;
}

function load() {
  var w = document.documentElement.clientWidth
  var h = document.documentElement.clientHeight
  chetbox = document.getElementById('chetbox')
  funbox = document.getElementById('funbox')
  chetbox.style.height = h * 0.95 + 'px'
  chetbox.style.width = w * 0.7 + 'px'
  funbox.style.height = h * 0.95 + 'px'
  funbox.style.width = w * 0.27 + 'px'
}

function clean() {
  var cont = document.getElementById('contentId')
  cont.innerHTML = ''
}

function login() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var json = '{"msgType" : "1", "msgBody":{"loginUserId" : "' + userId + '"}}'
    ws.send(json)
  }
}

function joingroup() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var groupId = document.getElementById('gid').value
    var json = '{"msgType" : "3", "msgBody":{"token" : "'+token+'", "fromUserId" : "' + userId + '","groupId" : "' + groupId + '"}}'
    ws.send(json)
  }
}

// 5
function p2p() {
  var sb = document.getElementById('sendButton')
  sb.innerHTML = 'Send - P2P'
}

// 7
function p2g() {
  var sb = document.getElementById('sendButton')
  sb.innerHTML = 'Send - P2G'
}

function logout() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var json = '{"msgType" : "9", "msgBody":{"token" : "'+token+'", "logoutUserId" : "' + userId + '"}}'
    ws.send(json)
    addmsg(null, '登出成功!', 'local', 'e')
    var user = document.getElementById('fromid')
    user.disabled = null
    var send = document.getElementById('sendButton')
    send.disabled = 'false'
  }
}

function quitgroup() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var groupId = document.getElementById('gid').value
    var json = '{"msgType" : "11", "msgBody":{"token" : "'+token+'", "fromUserId" : "' + userId + '", "groupId" : "' + groupId + '"}}'
    ws.send(json)
  }
}

function addfriend() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var toid = document.getElementById('toid').value
    var json = '{"msgType" : "13", "msgBody":{"token" : "'+token+'", "toUserId" : "' + toid + '","fromUserId":"' + userId + '"}}'
    ws.send(json)
  }
}

function removefriend() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var toid = document.getElementById('toid').value
    var json = '{"msgType" : "15", "msgBody":{"token" : "'+token+'", "fromUserId" : "' + userId + '","toUserId":"' + toid + '"}}'
    ws.send(json)
  }
}

function kickmember() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var groupId = document.getElementById('gid').value
    var toid = document.getElementById('toid').value
    var json = '{"msgType" : "17", "msgBody":{"token" : "'+token+'", "fromAdministratorId" : "' + userId + '" , "fromGroup" : "' + groupId + '", "toUSerId" : "' + toid + '"}}'
    ws.send(json)
  }
}

function addadmin() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var groupId = document.getElementById('gid').value
    var toid = document.getElementById('toid').value
    var json = '{"msgType" : "19", "msgBody":{"token" : "'+token+'", "ownerId" : "' + userId + '", "groupId" : "' + groupId + '", "toUserId" : "' + toid + '"}}'
    ws.send(json)
  }
}

function removeadmin() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var groupId = document.getElementById('gid').value
    var toid = document.getElementById('toid').value
    var json = '{"msgType" : "21", "msgBody":{"token" : "'+token+'", "ownerId" : "' + userId + '", "groupId" : "' + groupId + '", "fromUserId" : "' + toid + '"}}'
    ws.send(json)
  }
}

function heartbeat() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var json = '{"msgType" : "99"}'
    ws.send(json)
  }
}

function comfirmaddfriend() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var toid = document.getElementById('toid').value
    var confirm = document.getElementById('confirm').value
    if (confirm === 'true') {
      var json = '{"msgType" : "104", "msgBody":{"token" : "'+token+'", "fromUserId" : "' + userId + '","toUserId":"' + toid + '","confirmResult": ' + true + '}}'
      ws.send(json)
    } else {
      var json = '{"msgType" : "104", "msgBody":{"token" : "'+token+'", "fromUserId" : "' + userId + '","toUserId":"' + toid + '","confirmResult": ' + false + '}}'
      ws.send(json)
    }
  }
}

function confirmjoingroup() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var toid = document.getElementById('toid').value
    var confirm = document.getElementById('confirm').value
    var gid = document.getElementById('gid').value
    if (confirm === 'true') {
      var json = '{"msgType" : "106", "msgBody":{"token" : "'+token+'", "handleAdministratorId" : "' + userId + '", "groupId" : "' + gid + '", "toUserId" : "' + toid + '","confirmResult": ' + true + '}}'
      ws.send(json)
    } else {
      var json = '{"msgType" : "106", "msgBody":{"token" : "'+token+'", "handleAdministratorId" : "' + userId + '", "groupId" : "' + gid + '", "toUserId" : "' + toid + '","confirmResult": ' + false + '}}'
      ws.send(json)
    }
  }
}

function dissolvegroup() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var groupId = document.getElementById('gid').value
    var json = '{"msgType" : "111", "msgBody":{"token" : "'+token+'", "groupId" : "' + groupId + '", "ownerId" : "' + userId + '" }}'
    ws.send(json)
  }
}

function usersignin() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var json = '{"msgType" : "113", "msgBody":{"userId" : "' + userId + '"}}'
    ws.send(json)
  }
}

function groupsignin() {
  if (ws === undefined) {
    addmsg(null, '请先连接服务器!', 'local', 'red')
  } else if (ws.readyState === 2 || ws.readyState === 3) {
    addmsg(null, '连接已经关闭，或者打开连接失败, 请确认服务器是否启动!', 'local', 'red')
  } else {
    var userId = document.getElementById('fromid').value
    var gid = document.getElementById('gid').value
    var json = '{"msgType" : "114", "msgBody":{"token" : "'+token+'", "ownerId" : "' + userId + '", "groupId" : "' + gid + '"}}'
    ws.send(json)
  }
}