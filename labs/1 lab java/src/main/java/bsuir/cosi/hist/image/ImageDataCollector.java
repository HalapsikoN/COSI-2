package bsuir.cosi.hist.image;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageDataCollector {

    private static final Double R_VALUE = 0.299;
    private static final Double G_VALUE = 0.587;
    private static final Double B_VALUE = 0.114;

    public static List<Double> getImageBrightnessList(BufferedImage image) {
        List<Double> result = new ArrayList<>();

        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                result.add(getPixelBrightness(image.getRGB(i, j)));
            }
        }

        return result;
    }

    public static Double getPixelBrightness(int pixel) {
        Color color = new Color(pixel);
        return color.getRed() * R_VALUE + color.getGreen() * G_VALUE + color.getBlue() * B_VALUE;
    }
}
