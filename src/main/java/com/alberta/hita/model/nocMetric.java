package com.alberta.hita.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Description.
 * <p/>
 * Date: 3/19/14
 *
 * @author <a href="mailto:brian.m.alberta@jpmchase.com">v535083</a>
 * @version 1.0
 */
public class nocMetric {

  /*  private String name;
    private ArrayList<DataPoint<Integer>> data;

    public nocMetric(String name, ArrayList<DataPoint<Integer>> data) {
        this.name = name;
        this.data = data;
    }

    public nocMetric() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<DataPoint<Integer>> getData() {
        return data;
    }

    public void setData(ArrayList<DataPoint<Integer>> data) {
        this.data = data;
    } */

    private List<DataPoint<Integer>> nocMet;
    private List<DataPoint<Integer>> newMet;
    private List<DataPoint<Integer>> openMet;
    private List<DataPoint<Integer>> closedMet;

    public nocMetric() {
        this.nocMet = new ArrayList<>();
        this.newMet = new ArrayList<>();
        this.openMet = new ArrayList<>();
        this.closedMet = new ArrayList<>();
    }

    public List<DataPoint<Integer>> getNocMet() {
        return nocMet;
    }

    public void setNocMet(List<DataPoint<Integer>> nocMet) {
        this.nocMet = nocMet;
    }

    public List<DataPoint<Integer>> getNewMet() {
        return newMet;
    }

    public void setNewMet(List<DataPoint<Integer>> newMet) {
        this.newMet = newMet;
    }

    public List<DataPoint<Integer>> getOpenMet() {
        return openMet;
    }

    public void setOpenMet(List<DataPoint<Integer>> openMet) {
        this.openMet = openMet;
    }

    public List<DataPoint<Integer>> getClosedMet() {
        return closedMet;
    }

    public void setClosedMet(List<DataPoint<Integer>> closedMet) {
        this.closedMet = closedMet;
    }
}
