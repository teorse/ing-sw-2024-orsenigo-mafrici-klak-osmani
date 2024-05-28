package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPLogInSuccess implements ServerClientPacket{

    private final String username;

    public SCPLogInSuccess(String username) {
        this.username = username;
    }


    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.loginSuccess(username);
    }
}
