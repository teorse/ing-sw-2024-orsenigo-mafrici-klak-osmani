package Network.ServerClientPacket.Demo;

import Client.Model.Records.LobbyPreviewRecord;
import Network.ServerClientPacket.ServerClientPacket;
import Network.ServerMessageExecutor;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
