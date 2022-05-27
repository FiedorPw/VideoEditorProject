package com.example.demo1;

import net.bramp.ffmpeg.*;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.File;
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
    public static void concatenateFin(String filename, String output){      //skleja segmenty po cutPass
        String input1 = filename.substring(0, filename.lastIndexOf('.')) + "1" + ".mp4";
        String input2 = filename.substring(0, filename.lastIndexOf('.')) + "2edit" + ".mp4";
        String input3 = filename.substring(0, filename.lastIndexOf('.')) + "3" + ".mp4";
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(input1)
                .addInput(input2)
                .addInput(input3)
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(output)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .done().setComplexFilter("concat=n=3:v=1:a=1");
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        File inputFile1 = new File(input1);
        File inputFile2 = new File(input2);
        File inputFile3 = new File(input3);
        inputFile1.delete();
        inputFile2.delete();
        inputFile3.delete();
    }

    public static void blur(String filename, String output, int blurStrength){  //bluruje calosc
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(filename)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(output)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setVideoFilter("boxblur="+blurStrength)
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }
    public static void colorBalance(String filename, String output, long time1, long time2, double intensity, String color, String level ){  //bluruje srodkowy segment po cutPass
        String colorBalance=String.valueOf(color.charAt(0))  +String.valueOf(level.charAt(0));
        cutPass(filename,time1, time2);
        String input = filename.substring(0, filename.lastIndexOf('.')) + "2" + ".mp4";
        File inputFile = new File(input);
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(input)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(filename.substring(0, filename.lastIndexOf('.'))+"2edit" + ".mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .done().setComplexFilter("colorbalance="+colorBalance+"="+intensity);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        inputFile.delete();
        concatenateFin(filename,output);
    }
    public static void blurSegment(String filename, String output, long time1, long time2, int blurStrength){  //bluruje srodkowy segment po cutPass
        cutPass(filename,time1, time2);
        String input = filename.substring(0, filename.lastIndexOf('.')) + "2" + ".mp4";
        File inputFile = new File(input);
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(input)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(filename.substring(0, filename.lastIndexOf('.'))+"2edit" + ".mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setVideoFilter("boxblur="+blurStrength)
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        inputFile.delete();
        concatenateFin(filename,output);
    }
    public static void cutPass(String filename, long time1, long time2){     //Dzieli filmik na trzy
        String input1 = filename.substring(0, filename.lastIndexOf('.')) + "1" + ".mp4";
        String input2 = filename.substring(0, filename.lastIndexOf('.')) + "2" + ".mp4";
        String input3 = filename.substring(0, filename.lastIndexOf('.')) + "3" + ".mp4";
        FFmpegBuilder builder1 = new FFmpegBuilder()
                .setInput(filename)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(input1)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setStartOffset(0,TimeUnit.SECONDS)
                .setDuration(time1, TimeUnit.SECONDS)
                .done();
        FFmpegBuilder builder2 = new FFmpegBuilder()
                .setInput(filename)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(input2)  // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setStartOffset(time1,TimeUnit.SECONDS)
                .setDuration(time2-time1, TimeUnit.SECONDS)
                .done();
        FFmpegBuilder builder3 = new FFmpegBuilder()
                .setInput(filename)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(input3)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setStartOffset(time2,TimeUnit.SECONDS)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder1).run();
        executor.createJob(builder2).run();
        executor.createJob(builder3).run();
    }


    public static void main(String[] args){

    //blurSegment("filmik.mp4","output.mp4",10,20,20);
    colorBalance("filmik.mp4","output.mp4",10,20,(double) -1,"red","medium");

    }
}
