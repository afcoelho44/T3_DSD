using tcp_client;

ConnectionService.SetIP("localhost");
var connectionService = new ConnectionService();
connectionService.SendAndReceiveMessage("1; Eu estou programando com o C#! :)");