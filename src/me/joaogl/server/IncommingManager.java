package me.joaogl.server;

import me.joaogl.server.aircraftmanager.AircraftManager;

public class IncommingManager {
	int stage = 0;

	public String processInput(String in) {
		String out = "Empty command";
		if (in != null) {
			out = "Unknown command - " + in;
			if (in.equalsIgnoreCase("start") && stage == 0) {
				stage = 1;
				return "Starting a new flight, tell me the aircraft registration.";
			} else if (in.equalsIgnoreCase("start")) return "You've already started a flight.";
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
				for (int i = 0; i < AircraftManager.aircraftReg.size(); i++) {
					System.out.println(AircraftManager.getAllPlanes(i));
				}
				return result;
			}
			if (in.contains("delid")) {
				String[] args = in.split(" ");
				if (args[1] != null && isInt(args[1])) {
					int id = Integer.parseInt(args[1]);
					if (id <= AircraftManager.getTotal()) {
						AircraftManager.removeId(id);
						return "Removed id " + args[1];
					} else return "Error: invalid ID. Type /list";
				} else return "Error: value typed is not an ID. Type /delid (id)";
			}
			if (in.equalsIgnoreCase("restart")) {
				DataManager.setupRegList();
				return "Restart";
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

	private boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
