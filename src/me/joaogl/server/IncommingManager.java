package me.joaogl.server;

import me.joaogl.server.aircraftmanager.AircraftManager;

public class IncommingManager {
	int stage = 0;

	public String processInput(String in) {
		String out = "Unknown command - " + in;
		if (in != null) {
			if (in.equalsIgnoreCase("start") && stage == 0) {
				stage = 1;
				return "Starting a new flight, tell me the aircraft registration.";
			} else if (in.equalsIgnoreCase("start")) { return "You've already started a flight."; }
			if (stage == 1) {
				if (check(in)) return cancel();
				else {
					stage = 2;
					return "Thank you, fuel available is XXX and aircraft life is XXX.";
				}
			}
			if (stage == 2) if (check(in)) return cancel();
			if (in.equalsIgnoreCase("Disconnecting")) return "disc";
			if (in == "new") return "Connected, Welcome.";
			if (in.equalsIgnoreCase("list")) {
				String result = "Aircraft List sent to the server console.";
				for (int i = 0; i < AircraftManager.aircraftReg.length; i++) {
					System.out.println(AircraftManager.getAllPlanes(i));
				}
				return result;
			}
		}
		return out;
	}

	private boolean check(String in) {
		if (in.equalsIgnoreCase("abort") || in.equalsIgnoreCase("cancel") || in.equalsIgnoreCase("end") || in.equalsIgnoreCase("finish") || in.equalsIgnoreCase("stop")) { return true; }
		return false;
	}

	private String cancel() {
		stage = 0;
		return "Flight cancelled.";
	}

}
