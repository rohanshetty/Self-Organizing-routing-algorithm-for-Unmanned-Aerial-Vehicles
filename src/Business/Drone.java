package Business;


import Graph.Link;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Rohan
 */
public class Drone {
    
    private static int number=0;
    
    private int droneNumber;
    private String droneName;
    private Link initialCordinate;
    private Link finalCordinate;
    private double initialspeed;
    private double initialAltitude;
    private double currentBattery;
    private double currentSpeed;
    private double currentAltitude;
    private Link currentCordinate;
    private Link[] tempCordinateList; // to store the o/p of the algorithm
    private Link[] finalCordinateList; // to store the final cordinate list (final path) 
    
    private double windSpeed;
    private double temperature;
    private double turbulence;
    private double rain;
    private double icing;

    public Drone() {
    initialCordinate = new Link(0,0);
    finalCordinate = new Link(0,0);
    droneNumber = number;
    number++;
    }
    
    
    
    public String getDroneName() {
        return droneName;
    }

    public void setDroneName(String droneName) {
        this.droneName = droneName;
    }

    
    public Link getInitialCordinate() {
        return initialCordinate;
    }

    public void setInitialCordinate(Link initialCordinate) {
        this.initialCordinate = initialCordinate;
    }

    public Link getFinalCordinate() {
        return finalCordinate;
    }

    public void setFinalCordinate(Link finalCordinate) {
        this.finalCordinate = finalCordinate;
    }

    public double getInitialspeed() {
        return initialspeed;
    }

    public void setInitialspeed(double initialspeed) {
        this.initialspeed = initialspeed;
    }

    public double getInitialAltitude() {
        return initialAltitude;
    }

    public void setInitialAltitude(double initialAltitude) {
        this.initialAltitude = initialAltitude;
    }

    public double getCurrentBattery() {
        return currentBattery;
    }

    public void setCurrentBattery(double currentBattery) {
        this.currentBattery = currentBattery;
    }

    public Link getCurrentCordinate() {
        return currentCordinate;
    }

    public void setCurrentCordinate(Link currentCordinate) {
        this.currentCordinate = currentCordinate;
    }

    public Link[] getTempCordinateList() {
        return tempCordinateList;
    }

    public void setTempCordinateList(Link[] tempCordinateList) {
        this.tempCordinateList = tempCordinateList;
    }

    public Link[] getFinalCordinateList() {
        return finalCordinateList;
    }

    public void setFinalCordinateList(Link[] finalCordinateList) {
        this.finalCordinateList = finalCordinateList;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public double getCurrentAltitude() {
        return currentAltitude;
    }

    public void setCurrentAltitude(double currentAltitude) {
        this.currentAltitude = currentAltitude;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTurbulence() {
        return turbulence;
    }

    public void setTurbulence(double turbulence) {
        this.turbulence = turbulence;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getIcing() {
        return icing;
    }

    public void setIcing(double icing) {
        this.icing = icing;
    }

    public int getDroneNumber() {
        return droneNumber;
    }

    
    
    public String toString(){
        return droneName;
    }
    
}
