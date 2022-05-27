package com.example.demo1;

import org.slf4j.*;
import net.bramp.ffmpeg.*;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.IOException;

public class VideoEffectsTemp {
    public static void main(String[] args) throws IOException {
        FFmpeg ffmpeg = new FFmpeg("ffmpegdir/bin/ffmpeg.exe");
        FFprobe ffprobe = new FFprobe("ffmpegdir/bin/ffprobe.exe");

        FFmpegBuilder builder = new FFmpegBuilder()

                .setInput("filmik.mp4")     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput("output.mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set


//                .setAudioChannels(1)         // Mono audio
//                .setAudioCodec("aac")        // using the aac codec
//                .setAudioSampleRate(48_000)  // at 48KHz
//                .setAudioBitRate(32768)      // at 32 kbit/s
//
//                .setVideoCodec("libx264")     // Video using x264
//                .setVideoFrameRate(24, 1)     // at 24 frames per second
//                .setVideoResolution(640, 480) // at 640x480 resolution

//                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .setVideoFilter("boxblur=10")

                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

// Run a one-pass encode
        executor.createJob(builder).run();

    }
}
