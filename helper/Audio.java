package helper;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Audio {
    public File file;
    public Clip clip;
    private AudioInputStream stream;
    private AudioFormat format;
    private DataLine.Info info;

    public Audio(File file) {
        this(file, false);
    }

    public Audio(File file, boolean loop) {
        try {
            stream = AudioSystem.getAudioInputStream(file);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);

            if (loop)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void play() {
        if (clip == null)
            return;

        if (clip.isRunning())
            clip.stop();

        clip.setFramePosition(0);
        clip.start();
    }

    public void close() {
        if (clip == null)
            return;

        clip.close();
    }
}
