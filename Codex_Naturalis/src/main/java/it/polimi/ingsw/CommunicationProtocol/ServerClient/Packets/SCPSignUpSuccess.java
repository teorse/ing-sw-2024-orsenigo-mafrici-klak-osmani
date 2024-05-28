package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPSignUpSuccess implements ServerClientPacket{

    private final String username;

    public SCPSignUpSuccess(String username) {
        this.username = username;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.signUpSuccess(username);
    }
}
