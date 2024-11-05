package cr.ac.una.chats.util;

import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.ByteArrayInputStream;

public class AudioPlayer {

    public void playMp3(byte[] mp3Data) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(mp3Data);
            AdvancedPlayer player = new AdvancedPlayer(bais);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}