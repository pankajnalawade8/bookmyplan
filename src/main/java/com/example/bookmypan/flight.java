package com.example.bookmypan;

public class flight {
    private String flightNumber;
    private String destination;
    private double price;

    public flight(String flightNumber, String destination, double price) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.price = price;
    }

    // Getters are required for Spring to convert this to JSON
    public String getFlightNumber() { return flightNumber; }
    public String getDestination() { return destination; }
    public double getPrice() { return price; }
}