package cr.ac.una.chats.util;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.*;

import java.io.IOException;

public class AudioRecorder {
    private TargetDataLine line;
    private ByteArrayOutputStream out;

    public void startRecording() {
        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];

            // Capture audio data in a separate thread
            new Thread(() -> {
                try {
                    while (line.isOpen()) {
                        int bytesRead = line.read(buffer, 0, buffer.length);
                        out.write(buffer, 0, bytesRead);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] stopRecording() {
        if (line != null) {
            line.stop();
            line.close();
        }

        // Return the recorded audio data as a byte array
        try {
            out.flush();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
