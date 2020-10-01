package bsuir.cosi.hist;

import bsuir.cosi.hist.image.ImageCorrector;
import bsuir.cosi.hist.visual.histogram.HistogramBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        File file = new File("src\\main\\resources\\dwarf.jpg");
//        File file = new File("src\\main\\resources\\flower.jpg");
        File file = new File("src\\main\\resources\\goat.jpg");

        try {
            BufferedImage image = ImageIO.read(file);

            //1-2. Show image and build init brightness histogram
            HistogramBuilder.makeBrightnessHistogram(image, "Start brightness");

            //3. Linear contrast
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter gmax border:");
            Double gMax = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Enter gmin border:");
            Double gMin = Double.parseDouble(scanner.nextLine().trim());

            if (gMin.compareTo(gMax) > 0) {
                Double temp = gMax;
                gMax = gMin;
                gMin = temp;
            }

            BufferedImage newImage = ImageCorrector.doLinearContrast(image, gMin, gMax);
            HistogramBuilder.makeBrightnessHistogram(newImage, "Linear contrast image");

            //3. Low frequencies filter
            BufferedImage oneMoreNewImage = ImageCorrector.filterHighFrequency(image);
            HistogramBuilder.makeBrightnessHistogram(oneMoreNewImage, "Low frequencies filter image");

        } catch (IOException e) {
            System.out.println("Image problem: " + e.getLocalizedMessage());
        }
    }
}
