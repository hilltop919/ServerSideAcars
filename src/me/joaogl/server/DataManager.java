package me.joaogl.server;

import me.joaogl.server.aircraftmanager.AircraftManager;

public class DataManager {

	
	public static int getTotalAircraft(){
		return 4;
	}
	
	public static void setupRegList(){
		System.out.println(" - Creating aircraft ammount.");	
		AircraftManager manager = new AircraftManager();	
		System.out.println(" - Setting up the aircraft list.");
		for (int i = 0; i < AircraftManager.aircraftReg.length; i++) {
			//SQL DATA INTO THE ARRAY
			//SQL DATA OF LIFE INTO THE ARRY TOO
			if (i == 0) AircraftManager.setId(i, "A", 10, 50);
			if (i == 1) AircraftManager.setId(i, "B", 50, 10);
			if (i == 2) AircraftManager.setId(i, "C", 60, 20);
			if (i == 3) AircraftManager.setId(i, "D", 100, 30);
		}
		System.out.println("Done setting up the data planes, server up and ready.");	
	}

	public static void setIntoDB(String where, String var, String content){
		//SQL SET VAR = CONTENT WHERE where = VAR
	}
}
