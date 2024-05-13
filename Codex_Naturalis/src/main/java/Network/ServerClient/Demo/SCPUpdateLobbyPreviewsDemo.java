package Network.ServerClient.Demo;

import Client.Model.Records.LobbyPreviewRecord;
import Network.ServerClient.Packets.ServerClientPacket;
import Network.ServerMessageExecutor;

import java.io.*;
import java.util.Set;

public class SCPUpdateLobbyPreviewsDemo implements ServerClientPacket, Serializable {

    private final Set<LobbyPreviewRecord> lobbyPreviewSet;

    public SCPUpdateLobbyPreviewsDemo(Set<LobbyPreviewRecord> lobbyPreviewSet){
        this.lobbyPreviewSet = lobbyPreviewSet;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        String lobbyPreview = "\nLobby preview:\n";

        for(LobbyPreviewRecord preview : lobbyPreviewSet){
            lobbyPreview = lobbyPreview + "Lobby name: "+preview.lobbyName()+"\n" +
                    "Participants: "+preview.currentUsers()+"/"+preview.maxUsers()+"\n"+
                    "Game started: "+preview.gameStarted()+"\n\n";

        }

        System.out.println(lobbyPreview);

    }
}
