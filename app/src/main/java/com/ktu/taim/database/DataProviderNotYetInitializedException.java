package com.ktu.taim.database;

/**
 * Created by A on 20/10/2016.
 */
public class DataProviderNotYetInitializedException extends RuntimeException {

    public DataProviderNotYetInitializedException() {
        super("Data provider has not been initialized with a correct context yet.");
    }
}
