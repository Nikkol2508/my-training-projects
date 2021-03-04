import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        String srcFolder = "/Users/nikolajaksenov/Desktop/imag";
        String dstFolder = "/Users/nikolajaksenov/Desktop/dst";
        int newWidth = 300;

        File srcDir = new File(srcFolder);

        long start = System.currentTimeMillis();

        File[] files = srcDir.listFiles();

        int processorsNumber = Runtime.getRuntime().availableProcessors();
        System.out.println(files.length);
        System.out.println((double) files.length/processorsNumber);

        int part = (int) Math.ceil((double)files.length/processorsNumber);

        ArrayList<File[]> parts = new ArrayList<>();
        for(int s = 0; s < files.length; s += part) {
            File[] partArray = new File[part];
            if((s + part) < files.length) {
                System.arraycopy(files, s, partArray, 0, part);
                parts.add(partArray);
            }
            else {
                File[] partArrayEnd = new File[files.length - s];
                System.arraycopy(files, s, partArrayEnd, 0, files.length - s);
                parts.add(partArrayEnd);
            }
        }

        List<Thread> threadList = new ArrayList<>();
        parts.forEach(p -> {
            ImageResizer resizer = new ImageResizer(p, newWidth, dstFolder, start);
            Thread t = new Thread(resizer);
            t.start();
            threadList.add(t);
        });

        try {
            for (Thread t : threadList) {
                t.join();
            }
            } catch(InterruptedException e){
                e.printStackTrace();
            }

        System.out.println("Программа завершена");


//        ImageResizer resizer5 = new ImageResizer(files, newWidth, dstFolder, start);
//        new Thread(resizer5).start();
    }
}
