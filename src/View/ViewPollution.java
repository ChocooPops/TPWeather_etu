/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author apeyt
 */
public class ViewPollution extends JPanel {

    ChartPanel chartPanel;
Timer timer;
    int currentIndex = 0;
    
    public ViewPollution() {
        initChart();
        initMouseListener(); 
        validate();
        repaint();
    }
    
    private void initMouseListener() {
    chartPanel.addChartMouseListener(new ChartMouseListener() {
        @Override
        public void chartMouseClicked(ChartMouseEvent event) {
            ChartEntity entity = event.getEntity();

            if (entity instanceof XYItemEntity) {
                XYItemEntity xyEntity = (XYItemEntity) entity;
                int seriesIndex = xyEntity.getSeriesIndex();
                int itemIndex = xyEntity.getItem();

                // Récupérer les valeurs au survol
                Number xValue = xyEntity.getDataset().getX(seriesIndex, itemIndex);
                Number yValue = xyEntity.getDataset().getY(seriesIndex, itemIndex);

                // Afficher les valeurs (vous pouvez ajuster ceci selon vos besoins)
                System.out.println("Series: " + seriesIndex + ", X Value: " + xValue + ", Y Value: " + yValue);
            }
        }

        @Override
        public void chartMouseMoved(ChartMouseEvent event) {
            ChartEntity entity = event.getEntity();

            if (entity instanceof XYItemEntity) {
                XYItemEntity xyEntity = (XYItemEntity) entity;
                int seriesIndex = xyEntity.getSeriesIndex();
                int itemIndex = xyEntity.getItem();

                // Récupérer les valeurs au survol
                Number xValue = xyEntity.getDataset().getX(seriesIndex, itemIndex);
                Number yValue = xyEntity.getDataset().getY(seriesIndex, itemIndex);

                // Afficher les valeurs (vous pouvez ajuster ceci selon vos besoins)
                System.out.println("Series: " + seriesIndex + ", X Value: " + xValue + ", Y Value: " + yValue);
            }
        }
    });
}
    
    
    
    
    private void initChart() {
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Pollution chart",
                "Date",
                "Air Quality Indice",
                createDataset(),
                PlotOrientation.VERTICAL,
                false, true, false);

        chartPanel = new ChartPanel(lineChart);

        this.add(chartPanel);
    }

    @Override
    public Dimension getPreferredSize() {

        Dimension dim = this.size();
        chartPanel.setPreferredSize(new Dimension(800, 600));
        return new Dimension(800, 600);
    }

    private CategoryDataset createDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(0, "pollution", "1970");
        dataset.addValue(2, "pollution", "1980");
        dataset.addValue(1.5, "pollution", "1990");
        dataset.addValue(2, "pollution", "2000");
        dataset.addValue(1, "pollution", "2010");
        dataset.addValue(3, "pollution", "2014");

        return dataset;
    }

    public void updateDataset(JSONArray pollution) throws JSONException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < pollution.length(); i++) {
            JSONObject result = pollution.getJSONObject(i);

            long dt = result.getLong("dt");
            int aqi = result.getInt("aqi");
            int co = result.getInt("co");
            int no = result.getInt("no");
            int no2 = result.getInt("no2");
            int o3 = result.getInt("o3");
            int so2 = result.getInt("so2");

            java.util.Date date = new Date(dt * 1000);
            String pattern = "HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String dateString = simpleDateFormat.format(date);

            dataset.addValue(co, "co", dateString);
            dataset.addValue(no, "no", dateString);
            dataset.addValue(no2, "no2", dateString);
            dataset.addValue(o3, "o3", dateString);
            dataset.addValue(so2, "so2", dateString);
            dataset.addValue(aqi, "pollution", dateString);
            
            
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Pollution chart",
                "Date",
                "Air Quality Indice",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setTickLabelPaint(Color.BLACK);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
    // Ajuster l'épaisseur de la ligne pour chaque série
    for (int i = 0; i < dataset.getRowCount(); i++) {
        renderer.setSeriesStroke(i, new BasicStroke(5.0f)); // Ajustez l'épaisseur de la ligne ici
    }

    chartPanel.setChart(lineChart);
    }

    
}
