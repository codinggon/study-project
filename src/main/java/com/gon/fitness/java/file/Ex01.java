package com.gon.fitness.java.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Ex01 {

    public static final String CUR_PATH = "src/main/java/com/gon/fitness/java/file/";

    public static void main(String[] args) {


        Path path1 = Paths.get("my_file1.txt");
        Path path = path1.toAbsolutePath();

        Path path2 = Paths.get(CUR_PATH, "sub1", "sub2", "sub3");
        Path path3 = path2.resolve(path1);


//        measureTime(() -> fileInputStream(path3));
//        measureTime(() -> fileBufferStream(path3));

//        writeStr();



    }

    public static void writeStr() throws IOException {

        Path path2 = Paths.get(CUR_PATH, "sub1", "sub2", "sub3");
        List<String> lines = Arrays.asList(
                "반짝반짝 작은 별,",
                "아름답게 비치네.",
                "동쪽 하늘에서도",
                "서쪽 하늘에서도",
                "반짝반짝 작은 별,",
                "아름답게 비치네."
        );

        Path path4 = Paths.get("little.txt");
        Path path5 = path2.resolve(path4);
        Files.copy(path4, path5);
        try (
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path5.toString()));
            )
        {
            for (String line : lines) {
                //string -> bytes
                byte[] buffer = (line + "\n").getBytes(StandardCharsets.UTF_8);
                bos.write(buffer,0,buffer.length);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void fileBufferStream(Path path3) {
        byte[] buffer = new byte[1024];



        try (
//                FileInputStream fis = new FileInputStream(path3.toString());
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path3.toString()));
//                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(3path5.toString()));


        ) {


            int readByte;
            int count = 0;
            //1024용량만큼 방아옴
            while ((readByte = bis.read(buffer)) != -1) {
                String readStr = new String(buffer, 0, readByte, StandardCharsets.UTF_8);


                System.out.printf(" \n ----------%d : %d -------------\n ", ++count, readByte);

                System.out.println("readStr = " + readStr);

            }

            //resultTime = 18
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void fileInputStream(Path path3) {
        byte[] buffer = new byte[1024];

        try (
                FileInputStream fis = new FileInputStream(path3.toString());
//                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path3.toString()));
                //byte stream -> string stream 으로
                //encoding 적용
//                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        ) {


            int readByte;
            int count = 0;
            //1024용량만큼 방아옴
            while ((readByte = fis.read(buffer)) != -1) {
                String readStr = new String(buffer, 0, readByte, StandardCharsets.UTF_8);


                System.out.printf(" \n ----------%d : %d -------------\n ", ++count, readByte);

                System.out.println("readStr = " + readStr);

            }

            //resultTime = 18
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    public static String measureTime(Runnable runnable) {
        long startTime = System.nanoTime();
        runnable.run();
        long endTime = System.nanoTime();
        long resultTime = endTime - startTime;
        System.out.println("resultTime = " + resultTime);

        return String.format("%d 나노초",resultTime);
    }


}
