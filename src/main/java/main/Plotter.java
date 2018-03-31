package main;


import com.alibaba.fastjson.JSONObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

public class Plotter {

    //Compare dates to see if the current date is later than the target date
    public int CompareDate(String d1, String d2){
        String[] date1= d1.split("-");
        String[] date2= d2.split("-");
        for (int i = 0; i < date1.length; i++) {
            if (Integer.parseInt(date1[i])>Integer.parseInt(date2[i]))
                return 0;
            else if(Integer.parseInt(date1[i])<Integer.parseInt(date2[i]))
                return 1;
        }
        return 2;
    }

    //The return list cutline stores the dates in the specified range
    public List<String> ParseDate(LinkedHashMap<String,JSONObject> inputdata, String keywords[], String StartDate, String EndDate){
        List<String> cutline = new ArrayList<String>();
        List<String> category = new ArrayList<String>();
        int count = 0;
        boolean record_start = false;
        boolean record_end = false;
        //Select desired dates from our key-value map
        System.out.println("Parsing the dates...");
        for (String key : inputdata.keySet()) {
            if (!record_start&&!record_end){
                if (CompareDate(key,StartDate)!=1){
                    cutline.add(key);
                    record_start = true;
                }
            }else if (record_start&&!record_end){
                if(CompareDate(key,EndDate)!=0){
                    cutline.add(key);
                } else{
                    record_end = true;
                }
            }
            count++;

        }
        return cutline;
    }

    //The first dimension of "data" is keywords("open","low"...), the second dimension is the number corresponding to each field
    public Double[][] ParseNumber(LinkedHashMap<String,JSONObject> inputdata , List<String> cutline,String keywords[]) {
        Double data[][] = new Double[keywords.length][cutline.size()];
        System.out.println("Parsing stock data...");
        for (int i = 0; i < keywords.length; i++) {
            for (int j = 0; j < cutline.size(); j++) {
                data[i][j] = Double.parseDouble(inputdata.get(cutline.get(j).toString()).get((i+1)+". "+keywords[i]).toString());
            }
        }
        return data;
    }

    public void PlotData( List<String> cutline,String keywords[], Double data[][]){
        //Using prepared array and list to create the graph
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int m = 0; m < keywords.length; m++) {
            for (int n = 0; n < cutline.size(); n++) {
                dataset.addValue(data[m][n], keywords[m],cutline.get(n));
            }
        }
        String chartTitle = "Candle Data from "+ cutline.get(0) + " to " +cutline.get(cutline.size()-1);
        String xTitle = "Date";
        String yTitle = "Value";
        JFreeChart chart = ChartFactory.createLineChart(chartTitle, // Title
                xTitle,
                yTitle,
                dataset,
                PlotOrientation.VERTICAL, // Orientation
                true, //
                true, //
                false //
        );

        //Setting up the axis
        Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 8);
        CategoryPlot plot = chart.getCategoryPlot();
        CategoryAxis axis = plot.getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        axis.setLabelFont(labelFont);
        axis.setLowerMargin(0.01);
        axis.setUpperMargin(0.06);
        axis.setMaximumCategoryLabelLines(10);

        ChartFrame chartFrame=new ChartFrame("chart",chart);
        chartFrame.pack();
        chartFrame.setVisible(true);


    }

}
