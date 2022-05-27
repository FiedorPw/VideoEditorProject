package com.example.demo1;

import net.bramp.ffmpeg.*;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VideoEffectsTemp {
    //--------Static Defines------------------
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

    static FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

    //---------Utility-------------------------
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

        executor.createJob(builder1).run();
        executor.createJob(builder2).run();
        executor.createJob(builder3).run();
    }

    public static void concatenateFin(String filename, String output){      //skleja segmenty po cutPass
        String input1 = filename.substring(0, filename.lastIndexOf('.')) + "1" + ".mp4";
        String input2 = filename.substring(0, filename.lastIndexOf('.')) + "2" + ".mp4";
        String input3 = filename.substring(0, filename.lastIndexOf('.')) + "3" + ".mp4";
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(input1)
                .addInput(input2)
                .addInput(input3)
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(output)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .done().setComplexFilter("concat=n=3:v=1:a=1");
        executor.createJob(builder).run();
        File inputFile1 = new File(input1);
        File inputFile2 = new File(input2);
        File inputFile3 = new File(input3);
        inputFile1.delete();
        inputFile2.delete();
        inputFile3.delete();
    }


    public static void compress(String filename, String output, boolean replace){  //bluruje calosc
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(filename)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(output)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setVideoCodec("libx265")
                .setConstantRateFactor(28)
                .done();
        executor.createJob(builder).run();
        if(replace)
            replace(filename,output);
    }

    public static void replace(String replaced, String replacer){
        File deleteInput = new File(replaced);
        deleteInput.delete();
        File replacedInput = new File(replaced);
        File replacingInput = new File(replacer);
        replacingInput.renameTo(replacedInput);
    }

    //-----------------Raw Filters--------------------------
    public static void blur(String filename, String output, int blurStrength){  //bluruje calosc
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(filename)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(output)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setVideoFilter("boxblur="+blurStrength)
                .done();
        executor.createJob(builder).run();
        File inputFile = new File(filename);
        inputFile.delete();
    }
    public static void colorBalance(String filename, String output, long time1, long time2, double intensity, String color, String level){  //bluruje srodkowy segment po cutPass
        if(level.equals("low"))
            level="s";
        String colorBalance=String.valueOf(Character.toLowerCase(color.charAt(0)))  +String.valueOf(Character.toLowerCase(level.charAt(0)));
        String input = filename.substring(0, filename.lastIndexOf('.')) + "2" + ".mp4";
        File inputFile = new File(input);
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(input)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(filename.substring(0, filename.lastIndexOf('.'))+"2edit" + ".mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .done().setComplexFilter("colorbalance="+colorBalance+"="+intensity);
        executor.createJob(builder).run();
        inputFile.delete();
    }

    //------------Filter Callers-----------------------------

    public static void callBlurSegment(String filename, String output, Long time1, Long time2, int blurStrength, boolean replace){
        String intermediateInput = filename.substring(0, filename.lastIndexOf('.')) + "2" + ".mp4";
        String intermediateOutput = filename.substring(0, filename.lastIndexOf('.'))+"2edit" + ".mp4";
        cutPass(filename,time1,time2);
        blur(intermediateInput,intermediateOutput,blurStrength);
        replace(intermediateInput,intermediateOutput);
        concatenateFin(filename,output);
        if(replace)
            replace(filename,output);

    }

    public static void callColorBalanceSegment(String filename, String output, Long time1, Long time2, double intensity, String color, String level, boolean replace){
        String intermediateInput = filename.substring(0, filename.lastIndexOf('.')) + "2" + ".mp4";
        String intermediateOutput = filename.substring(0, filename.lastIndexOf('.'))+"2edit" + ".mp4";
        cutPass(filename,time1,time2);
        colorBalance(filename,output,time1,time2,intensity,color,level);
        replace(intermediateInput,intermediateOutput);
        concatenateFin(filename,output);
        if(replace)
            replace(filename,output);
    }
    public static void main(String[] args){

        compress("filmik.mp4","output.mp4",false);          // kompresuje filmik, tworzy nowa kopie
        callBlurSegment("output.mp4","a",(long) 10,(long) 30,30,true);  //bluruje od 0:10 do 0:30, zamienia plik
        compress("output.mp4","a",true);    //kompresuje plik, zamienia plik
        callColorBalanceSegment("output.mp4","blueless.mp4",(long) 1, (long) 20,-1,"blue","medium",false); //usuwa srednie niebieskie kolory od 0:00 do 0:20, tworzy nowa kopie
    }
}
