package com.example.demo1;

import net.bramp.ffmpeg.*;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VideoEffectsTemp {

    static FFmpeg ffmpeg;
    static {
        try {
            ffmpeg = new FFmpeg("ffmpegdir/bin/ffmpeg.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static FFprobe ffprobe;
    static {
        try {
            ffprobe = new FFprobe("ffmpegdir/bin/ffprobe.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void concatenateFin(String filename, String output){
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(filename.substring(0, filename.lastIndexOf('.'))+"1" + ".mp4")
                .addInput(filename.substring(0, filename.lastIndexOf('.'))+"2edit" + ".mp4")
                .addInput(filename.substring(0, filename.lastIndexOf('.'))+"3" + ".mp4")
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(output)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .done().setComplexFilter("concat=n=3:v=1:a=1");
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

    }
    public static void blur(String filename, String output, long time1, long time2){
        cutPass(filename,output,time1, time2);
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(filename.substring(0, filename.lastIndexOf('.'))+"2" + ".mp4")     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(filename.substring(0, filename.lastIndexOf('.'))+"2edit" + ".mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setVideoFilter("boxblur=10")
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        concatenateFin(filename,output);
    }
    public static void cutPass(String filename, String output, long time1, long time2){
        FFmpegBuilder builder1 = new FFmpegBuilder()
                .setInput(filename)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(filename.substring(0, filename.lastIndexOf('.'))+"1" + ".mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setStartOffset(0,TimeUnit.SECONDS)
                .setDuration(time1, TimeUnit.SECONDS)
                .done();
        FFmpegBuilder builder2 = new FFmpegBuilder()
                .setInput(filename)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(filename.substring(0, filename.lastIndexOf('.'))+"2" + ".mp4")  // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setStartOffset(time1,TimeUnit.SECONDS)
                .setDuration(time2-time1, TimeUnit.SECONDS)
                .done();
        FFmpegBuilder builder3 = new FFmpegBuilder()
                .setInput(filename)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(filename.substring(0, filename.lastIndexOf('.'))+"3" + ".mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setStartOffset(time2,TimeUnit.SECONDS)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder1).run();
        executor.createJob(builder2).run();
        executor.createJob(builder3).run();
    }


    public static void main(String[] args){

    blur("filmik.mp4","output.mp4",10,20);

    }
}
