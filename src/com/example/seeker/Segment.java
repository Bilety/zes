package com.example.seeker;

import com.google.android.maps.GeoPoint;

public class Segment {

	private GeoPoint start;
    private String instruction;
    private int length;
    private double distance;

    public Segment() {
    }

    public void setInstruction(final String turn) {
            this.instruction = turn;
    }

    public String getInstruction() {
            return instruction;
    }

    public void setPoint(final GeoPoint point) {
            start = point;
    }

    public GeoPoint startPoint() {
            return start;
    }

    public void setLength(final int length) {
            this.length = length;
    }

    public int getLength() {
            return length;
    }

    public void setDistance(double distance) {
            this.distance = distance;
    }

    public double getDistance() {
            return distance;
    }

	
}
