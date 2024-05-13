package Network.ServerClient.Packets;

import Network.ServerMessageExecutor;

public class SCPSignUpSuccess implements ServerClientPacket{

    private final String username;

    public SCPSignUpSuccess(String username) {
        this.username = username;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.signUpSuccess(username);
    }
}
