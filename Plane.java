/**
 * Models an airplane seating system.
 * @author DuocNguyen
 *
 */
public class Plane {
	//this is for testing purposes. 
	private Passenger[][] firstSeats;
	private Passenger[][] econSeats;
	final int NUMBER_OF_ESEATS = 140;
	final int NUMBER_OF_FSEATS = 8;
	private int fSeatCounter; 
	private int eSeatCounter; 
	private String[] fColSym; 
	private String[] eColSym;
	private String[] fSeatPref;
	private String[] eSeatPref;
	private String flightName;

	/**
	 * Construct a seating system for a flight.
	 */
	public Plane () {
		econSeats = new Passenger[20][6];
		firstSeats = new Passenger[2][4];
		fSeatCounter = 0; 
		eSeatCounter = 0;
		flightName = null;

		eColSym = new String[6];	//First class columns symbols
		eColSym[0] = "A";eColSym[1] = "B";eColSym[2] = "C";
		eColSym[3] = "D";eColSym[4] = "E";eColSym[5] = "F";
		fColSym = new String[4];	//Economy class columns symbols
		fColSym[0] = "A";fColSym[1] = "B";fColSym[2] = "C";fColSym[3] = "D";

		eSeatPref = new String[6];
		eSeatPref[0] = "W"; eSeatPref[1] = "C"; eSeatPref[2] = "A";
		eSeatPref[5] = "W"; eSeatPref[4] = "C"; eSeatPref[3] = "A";
		fSeatPref = new String[4];
		fSeatPref[0] = "W"; fSeatPref[1] = "A"; fSeatPref[2] = "A"; fSeatPref[3] = "W";
	}

	/**
	 * Add a passenger to the plane.
	 * @param info Passenger's information.
	 * precondition: info[0] is name, info[1] is service class, info[2] is seat preference.
	 * postcondition: Passenger is added to plane.
	 */
	public void addPassenger(String[] info) {
		Passenger p = new Passenger(info[0].trim(), info[1]);
		String seatPref = info[2].trim();

		//***For first class there's no such thing as [C]enter seat preference. 

		if(p.getServiceClass().equalsIgnoreCase("first")) {


			for(int row = 0; row < firstSeats.length; row++) {
				for(int col = 0; col < firstSeats[0].length ; col++) {
					if(firstSeats[row][col] == null && seatPref.equals(fSeatPref[col])) {
						String seat = (row +1) + fColSym[col];
						p.setSeat(seat);
						firstSeats[row][col] = p;
						fSeatCounter++;
						System.out.println(seat);
						return;
					}

				}
			}


		} else {


			for(int row = 0; row < econSeats.length; row++) {
				for(int col = 0; col < econSeats[0].length ; col++) {
					if(econSeats[row][col] == null && seatPref.equals(eSeatPref[col])) {
						String seat = (row +10) + eColSym[col];
						p.setSeat(seat);
						econSeats[row][col] = p;
						eSeatCounter++;
						System.out.println(seat);
						return;
					}

				}
			}

		}
	}

	/**
	 * Add a group of passenger to plane.
	 * @param info Group's information.
	 * precondition: info[0] is group name, info[1] is member names, info[2] is service class.
	 * postcondition: Group is added to plane.
	 */
	public void addGroup(String[] info) {
		String groupName = info[0];
		String[] names = info[1].split(",");
		String serviceClass = info[2];
		int numMembers  = names.length;
		String[] seatsTaken = new String[numMembers];	//print seats taken at the end.

		if(serviceClass.equalsIgnoreCase("first")) {

			int i = 0;
			while(i < numMembers) {
				int[] largestAdjacency = largestAdjacency("first");
				int r = largestAdjacency[0];	//start row
				int c = largestAdjacency[1];	//start column
				int numAdjacency = largestAdjacency[2];	//
				int counter = 0;
				for(int row = r; row < firstSeats.length; row++) {
					for(int col = c; col < firstSeats[0].length; col++) {
						if(counter < numAdjacency && i < numMembers) {
							Passenger p = new Passenger(names[i].trim(), serviceClass);	//cannot specify seat.
							p.setGroupName(groupName);
							p.setSeat((row +1) + fColSym[col]);
							firstSeats[row][col] = p;
							fSeatCounter++;
							//add seats taken here!
							seatsTaken[i] = p.getSeat();
							i++;
							counter++;
						} else {
							break;
						}
					}
					if(counter >= numAdjacency) {
						break;
					}
				}
				counter = 0;
			}		
		} else {
			int i = 0;
			while(i < numMembers) {
				int[] largestAdjacency = largestAdjacency("economy");
				int r = largestAdjacency[0];	//start row
				int c = largestAdjacency[1];	//start column
				int numAdjacency = largestAdjacency[2];	//
				int counter = 0;

				for(int row = r; row < econSeats.length; row++) {
					for(int col = c; col < econSeats[0].length; col++) {
						if(counter < numAdjacency && i < numMembers) {
							Passenger p = new Passenger(names[i].trim(), serviceClass);	//cannot specify seat.
							p.setGroupName(groupName);
							p.setSeat((row + 10) + eColSym[col]);
							econSeats[row][col] = p;
							eSeatCounter++;
							//add seats taken here!
							seatsTaken[i] = p.getSeat();
							i++;
							counter++;
						} else {
							break;
						}
					}
					if(counter >= numAdjacency) {
						break;
					}
				}
				counter = 0;
			}			
		}

		for(int k = 0; k < numMembers; k++) {
			System.out.println(seatsTaken[k] + ": " + names[k].trim());
		}

	}

	/**
	 * To cancel a reservation.
	 * @param info Cancel reservation information.
	 * precondition: info[0] is either individual or group, info[1] is either name or group name.
	 * postcondition: Reservation is canceled.
	 */
	public void remove(String[] info) {
		String type = info[0]; 	//type of cancellation. Individual or Group.
		if(type.equalsIgnoreCase("i")) {
			//individual
			String name = info[1];

			//first class
			for(int row = 0; row < firstSeats.length; row++) {
				for(int col = 0; col < firstSeats[0].length; col++) {
					if(firstSeats[row][col] != null && firstSeats[row][col].getName().equals(name)) {
						if(firstSeats[row][col].getGroupName() != null) {
							System.out.println("Cannot cancel a member from a group!");
							return;
						}
						firstSeats[row][col] = null;
						fSeatCounter--;
						System.out.println("Canceled Reservation!");
						return;	//exit method
					}
				}
			}

			//economy class
			for(int row = 0; row < econSeats.length; row++) {
				for(int col = 0; col < econSeats[0].length; col++) {
					if(econSeats[row][col] != null && econSeats[row][col].getName().equals(name)) {
						if(econSeats[row][col].getGroupName() != null) {
							System.out.println("Cannot cancel a member from a group!");
							return;
						}
						econSeats[row][col] = null;
						eSeatCounter--;
						System.out.println("Canceled Reservation!");
						return;	//exit method
					}
				}
			}
		} else {
			//group
			String groupName = info[1];
			boolean found = false;
			//first class
			for(int row = 0; row < firstSeats.length; row++) {
				for(int col = 0; col < firstSeats[0].length; col++) {
					if(firstSeats[row][col] != null && firstSeats[row][col].getGroupName() != null && firstSeats[row][col].getGroupName().equals(groupName)) {
						firstSeats[row][col] = null;
						fSeatCounter--;
						found = true;
					}
				}
			}
			if(found) {
				System.out.println("Reservation cancelled!");
				return;
			}

			//economy class
			for(int row = 0; row < econSeats.length; row++) {
				for(int col = 0; col < econSeats[0].length; col++) {
					if(econSeats[row][col] != null && econSeats[row][col].getGroupName() != null && econSeats[row][col].getGroupName().equals(groupName)) {
						econSeats[row][col] = null;
						eSeatCounter--;
						found = true;
					}
				}
			}
			if(found) {
				System.out.println("Reservation cancelled!");
				return;
			}
		}
		//If no such reservation exist then just ignore.
	}

	/**
	 * Print the availability of plane.
	 * @param serviceClass Desired service class to be printed.
	 */
	public void availability(String serviceClass) {
		if(serviceClass.equalsIgnoreCase("first")) {
			System.out.println("First");
			for(int i = 0; i < firstSeats.length; i++) {
				boolean found = false;
				for(int j = 0; j < firstSeats[0].length; j++) {
					if(firstSeats[i][j] == null) {
						if(!found) {
							System.out.print((i + 1) +  ": ");
						}
						if(j == firstSeats[0].length -1) {
							System.out.print(fColSym[j]);
						} else {
							System.out.print(fColSym[j] + ",");
						}
						found = true;
					}
				}
				System.out.println();
			}
		} else {
			System.out.println("Economy");
			for(int i = 0; i < econSeats.length; i++) {
				boolean found = false;
				for(int j = 0; j < econSeats[0].length; j++) {
					if(econSeats[i][j] == null) {
						if(!found) {
							System.out.print((i + 1) +  ": ");
						}
						if(j == econSeats[0].length-1) {
							System.out.print(eColSym[j]);
						} else {
							System.out.print(eColSym[j] + ",");
						}
						found = true;
					}
				}
				System.out.println();
			}
		}

	}

	/**
	 * To print existing passengers on plane.
	 * @param serviceClass Desired service class to be printed.
	 * precondition: Service class must be either first or economy.
	 * postcondition: Manifest chart is printed.
	 */
	public void manifest(String serviceClass) {
		//i is row
		//j is column
		if(serviceClass.equalsIgnoreCase("first")) {
			for(int i = 0; i < firstSeats.length; i++) {
				for(int j = 0; j < firstSeats[0].length; j++) {
					if(firstSeats[i][j] != null) {
						System.out.println((i+1) + fColSym[j] + ": " + firstSeats[i][j].getName());
					}
				}
			}
		} else {
			for(int i = 0; i < econSeats.length; i++) {
				for(int j = 0; j < econSeats[0].length; j++) {
					if(econSeats[i][j] != null) {
						System.out.println((i+10) + eColSym[j] + ": " + econSeats[i][j].getName());
					}
				}
			}
		}

	}
	
	/**
	 * Finds the largest adjacency seats available.
	 * @param serviceClass Desired service class. 
	 * @return An array with number of adjacency and starting seat.
	 */
	public int[] largestAdjacency(String serviceClass) {
		int[] result = new int[3];
		int longest = 0;
		boolean start = false;
		int counter = 0;
		int[] head = {0,0};

		if(serviceClass.equalsIgnoreCase("first")) {
			//first class

			for(int row = 0; row < firstSeats.length; row++) {
				for(int col = 0; col < firstSeats[0].length; col++) {
					if(firstSeats[row][col] == null) {
						if(!start) {
							//start a new adjacency seats
							head[0] = row;
							head[1] = col;
							counter++;
							start = true;
						} else {
							//just counting the adjacency seats
							counter++;
						}
						if(row == firstSeats.length-1 && col == firstSeats[0].length-1) {
							if(counter > longest) {
								longest = counter;
								result[0] = head[0];
								result[1] = head[1];
								result[2] = longest;
							}
						}
					} else {
						//out of the adjacency. Do comparison. 
						if(counter > longest) {
							longest = counter;
							result[0] = head[0];
							result[1] = head[1];
							result[2] = longest;
						}
						counter = 0;
						start = false;
					}
				}
			}
			return result;
		} else {
			//economy class
			for(int row = 0; row < econSeats.length; row++) {
				for(int col = 0; col < econSeats[0].length; col++) {
					if(econSeats[row][col] == null) {
						if(!start) {
							//start a new adjacency seats
							head[0] = row;
							head[1] = col;
							counter++;
							start = true;
						} else {
							//just counting the adjacency seats
							counter++;
						}
						if(row == econSeats.length-1 && col == econSeats[0].length-1) {
							if(counter > longest) {
								longest = counter;
								result[0] = head[0];
								result[1] = head[1];
								result[2] = longest;
							}
						}
					} else {
						//out of the adjacency. Do comparison. 
						if(counter > longest) {
							longest = counter;
							result[0] = head[0];
							result[1] = head[1];
							result[2] = longest;
						}
						counter = 0;
						start = false;
					}
				}
			}
			return result; //***** test this case.
		}
	}

	/**
	 * Checks for availability.
	 * @param serviceClass Desired service class to be checked.
	 * @param passengers Number of passengers to check.
	 * @return	True if available, otherwise false.
	 */
	public boolean checkAvailability(String serviceClass, int passengers) {
		if(serviceClass.equalsIgnoreCase("first")) {
			return ((NUMBER_OF_FSEATS - fSeatCounter - passengers) >= 0) ? true : false;
		} else {
			return ((NUMBER_OF_ESEATS - eSeatCounter - passengers) >= 0) ? true: false;
		}
	}

	/**
	 * Checks if a seat preference is available.
	 * @param serviceClass Desired service class to be checked.
	 * @param pref The seat preference.
	 * @return True if available, otherwise false.
	 */
	public boolean checkSeatPref(String serviceClass, String pref) {
		if(serviceClass.equalsIgnoreCase("first")) {

			for(int row = 0; row < firstSeats.length; row++) {
				for(int col = 0; col < firstSeats[0].length; col++) {
					if(firstSeats[row][col] == null && pref.equals(fSeatPref[col])) {
						return true;
					}

				}
			}

		} else {

			for(int row = 0; row < econSeats.length; row++) {
				for(int col = 0; col < econSeats[0].length; col++) {
					if(econSeats[row][col] == null && pref.equals(eSeatPref[col])) {
						return true;
					}

				}
			}

		}

		return false;
	}

	/**
	 * Set the flight name for this plane.
	 * @param flightName The flight name.
	 * precondition: a valid name.
	 * postcondition: Flight name is saved.
	 */
	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}

	/**
	 * Gets the flight name of plane.
	 * @return Flight name.
	 */
	public String getFlightName() {
		return flightName;
	}

	/**
	 * Get all passenger information on the plane.
	 * @return All passenger's information.
	 */
	public String[] getPassengers() {
		int size = fSeatCounter + eSeatCounter;
		String[] passengers = new String[size];
		int k = 0;
		while(k < passengers.length) {
			String i = "";
			//First Class
			for(int row = 0; row < firstSeats.length; row++) {
				for(int col = 0; col < firstSeats[0].length; col++) {
					if(firstSeats[row][col] != null && k < passengers.length) {
						i = "";
						i += firstSeats[row][col].getSeat() + ", ";
						if(firstSeats[row][col].getGroupName() == null) {
							i += "I, " + firstSeats[row][col].getName();
						} else {
							i += "G, " + firstSeats[row][col].getGroupName() + ", " + firstSeats[row][col].getName();
						}
						passengers[k] = i;
						k++;
					}
				}
			}

			//Economy Class
			for(int row = 0; row < econSeats.length; row++) {
				for(int col = 0; col < econSeats[0].length; col++) {
					if(econSeats[row][col] != null && k  < passengers.length) {
						i = "";
						i += econSeats[row][col].getSeat() + ", ";			//(row + 1) + fColSym[col] + ", ";
						if(econSeats[row][col].getGroupName() == null) {
							i += "I, " + econSeats[row][col].getName();
						} else {
							i += "G, " + econSeats[row][col].getGroupName() + ", " + econSeats[row][col].getName();
						}
						passengers[k] = i;
						k++;
					}
				}
			}

		}
		return passengers;
	}

	/**
	 * Load the existing passenger to plane.
	 * @param pInfo Passenger's information.
	 * precondition: pInfo must contain passengers information.
	 * precondition: Passenger is added to plane.
	 */
	public void load(String pInfo) {
		String[] info = pInfo.split(",");
		String seat = info[0].trim();
		String type = info[1].trim(); //Individual(I) or Group(G)
		String colSym = seat.substring(seat.length() - 1, seat.length());
		int row = Integer.parseInt(seat.substring(0, seat.length()-1));
		String name; String groupName; String memberName;
		String serviceClass;
		Passenger p;
		if(row < 10) {
			serviceClass = "first";
			row -= 1;
		} else {
			serviceClass = "economy";
			row -= 10;
		}
		if(type.equalsIgnoreCase("I")) {
			name = info[2].trim();
			p = new Passenger(name, serviceClass);
			//set seatPref
		} else {
			groupName = info[2];
			memberName = info[3];
			p = new Passenger(memberName, serviceClass);
			p.setGroupName(groupName);
		}
		p.setSeat(seat);

		int col;
		if(serviceClass.equalsIgnoreCase("first")) {
			for(col = 0; col < firstSeats[0].length; col++) {
				if(fColSym[col].equalsIgnoreCase(colSym)) {
					firstSeats[row][col] = p;
					fSeatCounter++;
					break;
				}
			} 
		} else {
			for(col = 0; col < econSeats[0].length; col++) {
				if(eColSym[col].equalsIgnoreCase(colSym)) {
					econSeats[row][col] = p;
					eSeatCounter++;
					break;
				}
			} 
		}

	}
}
