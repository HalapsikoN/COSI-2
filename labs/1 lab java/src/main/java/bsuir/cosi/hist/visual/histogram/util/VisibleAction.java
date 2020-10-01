package bsuir.cosi.hist.visual.histogram.util;

import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class VisibleAction extends AbstractAction {

    private final int i;
    private XYBarRenderer renderer;

    public VisibleAction(int i, HistogramDataset dataset, XYBarRenderer renderer) {
        this.i = i;
        this.renderer = renderer;
        this.putValue(NAME, (String) dataset.getSeriesKey(i));
        this.putValue(SELECTED_KEY, true);
        renderer.setSeriesVisible(i, true);
    }

    public void actionPerformed(ActionEvent e) {
        renderer.setSeriesVisible(i, !renderer.getSeriesVisible(i));
    }
}
