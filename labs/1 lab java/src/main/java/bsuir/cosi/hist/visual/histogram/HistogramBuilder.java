package bsuir.cosi.hist.visual.histogram;

import bsuir.cosi.hist.image.ImageDataCollector;
import bsuir.cosi.hist.visual.histogram.util.VisibleAction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HistogramBuilder {

    private static final Integer BINS = 256;

    public static void makeBrightnessHistogram(BufferedImage image, String title) {
        HistogramDataset dataset = new HistogramDataset();

        List<Double> valueList = ImageDataCollector.getImageBrightnessList(image);
        double[] array = new double[valueList.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = valueList.get(i);
        }

        dataset.addSeries("Brightness", array, BINS);

        JFreeChart chart = ChartFactory.createHistogram(title, "Value",
                "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());

        Paint[] paintArray = {
                new Color(0xFF5EFF8D, true)
        };
        plot.setDrawingSupplier(new DefaultDrawingSupplier(
                paintArray,
                DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        JFrame f = new JFrame("Histogram");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(panel);
        f.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void makeRGBHistogramFromImage(BufferedImage image) {
        HistogramDataset dataset = new HistogramDataset();
        Raster raster = image.getRaster();

        int height = image.getHeight();
        int width = image.getWidth();

        double[] r = new double[width * height];
        r = raster.getSamples(0, 0, width, height, 0, r);
        dataset.addSeries("Red", r, BINS);
        r = raster.getSamples(0, 0, width, height, 1, r);
        dataset.addSeries("Green", r, BINS);
        r = raster.getSamples(0, 0, width, height, 2, r);
        dataset.addSeries("Blue", r, BINS);


        JFreeChart chart = ChartFactory.createHistogram("Histogram", "Value",
                "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());

        Paint[] paintArray = {
                new Color(0x80ff0000, true),
                new Color(0x8000ff00, true),
                new Color(0x800000ff, true)
        };
        plot.setDrawingSupplier(new DefaultDrawingSupplier(
                paintArray,
                DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        JPanel jPanel = new JPanel();
        jPanel.add(new JCheckBox(new VisibleAction(0, dataset, renderer)));
        jPanel.add(new JCheckBox(new VisibleAction(1, dataset, renderer)));
        jPanel.add(new JCheckBox(new VisibleAction(2, dataset, renderer)));

        JFrame f = new JFrame("Histogram");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(panel);
        f.add(jPanel, BorderLayout.SOUTH);
        f.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }


}
