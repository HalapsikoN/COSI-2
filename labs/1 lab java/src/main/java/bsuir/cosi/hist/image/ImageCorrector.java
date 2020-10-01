package bsuir.cosi.hist.image;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageCorrector {

    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    private static Double calculateNewBrightnessForPixel(Double pixelBrightness, Double fMin, Double fMax, Double gMin, Double gMax) {
        return (pixelBrightness - fMin) / (fMax - fMin) * (gMax - gMin) + gMin;
    }

    private static Color makeNewColorFromOldColor(Color oldColor, double factor) {

        int newRed = (int) (oldColor.getRed() * factor);
        int newGreen = (int) (oldColor.getGreen() * factor);
        int newBlue = (int) (oldColor.getBlue() * factor);

        newRed = Math.min(newRed, 255);
        newGreen = Math.min(newGreen, 255);
        newBlue = Math.min(newBlue, 255);

        newRed = Math.max(newRed, 0);
        newGreen = Math.max(newGreen, 0);
        newBlue = Math.max(newBlue, 0);

        return new Color(newRed, newGreen, newBlue);
    }

    public static BufferedImage doLinearContrast(BufferedImage image, Double gMin, Double gMax) {

        List<Double> imageBrightness = ImageDataCollector.getImageBrightnessList(image);
        Double fMax = Collections.max(imageBrightness);
        Double fMin = Collections.min(imageBrightness);

        BufferedImage result = copyImage(image);

        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {

                Double oldPixelBrightness = ImageDataCollector.getPixelBrightness(image.getRGB(i, j));
                Double newPixelBrightness = calculateNewBrightnessForPixel(oldPixelBrightness, fMin, fMax, gMin, gMax);

                double factor = newPixelBrightness / oldPixelBrightness;

                Color oldColor = new Color(image.getRGB(i, j));
                Color newColor = makeNewColorFromOldColor(oldColor, factor);

                result.setRGB(i, j, newColor.getRGB());
            }
        }

        return result;
    }

    public static BufferedImage filterHighFrequency(BufferedImage image) {

        BufferedImage result = copyImage(image);

        for (int i = 1; i < image.getWidth() - 1; ++i) {
            for (int j = 1; j < image.getHeight() - 1; ++j) {

                int[] r = new int[9];
                int[] g = new int[9];
                int[] b = new int[9];

                r[0] = new Color(image.getRGB(i - 1, j - 1)).getRed();
                r[1] = new Color(image.getRGB(i - 1, j)).getRed();
                r[2] = new Color(image.getRGB(i - 1, j + 1)).getRed();
                r[3] = new Color(image.getRGB(i, j - 1)).getRed();
                r[4] = new Color(image.getRGB(i, j)).getRed();
                r[5] = new Color(image.getRGB(i, j + 1)).getRed();
                r[6] = new Color(image.getRGB(i + 1, j - 1)).getRed();
                r[7] = new Color(image.getRGB(i + 1, j)).getRed();
                r[8] = new Color(image.getRGB(i + 1, j + 1)).getRed();

                g[0] = new Color(image.getRGB(i - 1, j - 1)).getGreen();
                g[1] = new Color(image.getRGB(i - 1, j)).getGreen();
                g[2] = new Color(image.getRGB(i - 1, j + 1)).getGreen();
                g[3] = new Color(image.getRGB(i, j - 1)).getGreen();
                g[4] = 2 * new Color(image.getRGB(i, j)).getGreen();
                g[5] = new Color(image.getRGB(i, j + 1)).getGreen();
                g[6] = new Color(image.getRGB(i + 1, j - 1)).getGreen();
                g[7] = new Color(image.getRGB(i + 1, j)).getGreen();
                g[8] = new Color(image.getRGB(i + 1, j + 1)).getGreen();

                b[0] = new Color(image.getRGB(i - 1, j - 1)).getBlue();
                b[1] = 2 * new Color(image.getRGB(i - 1, j)).getBlue();
                b[2] = new Color(image.getRGB(i - 1, j + 1)).getBlue();
                b[3] = 2 * new Color(image.getRGB(i, j - 1)).getBlue();
                b[4] = 4 * new Color(image.getRGB(i, j)).getBlue();
                b[5] = 2 * new Color(image.getRGB(i, j + 1)).getBlue();
                b[6] = new Color(image.getRGB(i + 1, j - 1)).getBlue();
                b[7] = 2 * new Color(image.getRGB(i + 1, j)).getBlue();
                b[8] = new Color(image.getRGB(i + 1, j + 1)).getBlue();

                int newRed = Math.min(Arrays.stream(r).sum() / 9, 255);
                int newGreen = Math.min(Arrays.stream(g).sum() / 10, 255);
                int newBlue = Math.min(Arrays.stream(b).sum() / 16, 255);

                Color newColor = new Color(newRed, newGreen, newBlue);
                result.setRGB(i, j, newColor.getRGB());
            }
        }

        return result;
    }
}
