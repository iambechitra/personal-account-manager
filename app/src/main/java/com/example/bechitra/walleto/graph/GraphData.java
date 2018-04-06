package com.example.bechitra.walleto.graph;

public class GraphData {
    private String title, data;

    public GraphData(String title, String data) {

        this.title = title;
        this.data = data;
    }

    public String getData() {
        return this.data;
    }

    public String getTitle() {
        return this.title;
    }
}
