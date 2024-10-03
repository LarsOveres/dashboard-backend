package com.dashboard.backend.util;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Mp3DurationUtil {

    public static long getMp3Duration(File mp3File) throws IOException {
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(mp3File);
            Map<String, Object> properties = fileFormat.properties();
            Long duration = (Long) properties.get("duration");

            return duration / 1_000_000;
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new IOException("Kan de MP3 duur niet bepalen: " + e.getMessage());
        }
    }
}