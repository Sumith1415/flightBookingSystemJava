import java.util.Scanner;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Time;
import java.io.IOException;//for clearing the screen;

public class main{
	static Scanner input = new Scanner(System.in);
	static String url = "jdbc:mysql://localhost:3306/flight_booking";
	static String root = "root";
	static String pass = ""; //Enter your MySQL password;
	
	static Statement stmt = null;
	static Connection conn = null;
	static ResultSet r = null;
	
	static int n;
	
	public static void main(String args[]) {
		clear();//calling clear() to clear the terminal screen;
		try {
			conn = DriverManager.getConnection(url,root,pass);
			stmt = conn.createStatement();
			stmt.executeUpdate("create database if not exists flight_booking;");
			stmt.executeUpdate("use flight_booking;");
			content();
		}
		catch(Exception e) {
			System.out.println("Sorry, Some Error Occured\nPlease Try Again Later\nCheck U have Entered A Valid MySQL Password");
		}
	}
	
	public static void content() {
		
		while(true) {
			
			System.out.println("\n--------------Welcome to HOME PAGE-------------");
			
			System.out.println("\n\nPress <1> to Reserve Seat\nPress <2> to Check Flight Details\nPress <9> for Admin(Authorities)\nPress <0> to exit");
			System.out.println("\nEnter ur Choice: ");
			char choice = input.next().charAt(0);
			
			switch(choice) {
				case '1':
					clear();
					reservation();
					break;
					
				case '2':
					clear();
					flightDetails();
					break;
				
				case '9':
					clear();
					Admin();
					break;
					
				case '0':
					clear();
					System.out.println("\n\nThank u for visiting\n");
					System.exit(0);
					break;
					
				default:
					System.out.println("INVALID INPUT");
			}
			System.out.println("\n\n");
		}
	}
	
	public static void contentAdmin() {
		
		while(true) {
			System.out.println("\n--------------Admin Page-------------");
			
			System.out.println("\n\nPress <1> to Insert Flight Details\nPress <2> to Retrieve data\nPress <3> to Update Flight Details\nPress <0> to exit");
			System.out.println("\nEnter ur Choice: ");
			char choice = input.next().charAt(0);
			boolean c = true;
			
			switch(choice) {
				case '1':
					clear();
					insertFlightDetails();
					break;
					
				case '2':
					clear();
					retrieveData();
					break;
					
				case '3':
					clear();
					updateFlightDetails();
					break;
				
				case '0':
					clear();
					System.out.println("\nThank you ADMIN!!!!");
					c = false;
					break;
					
				default:
					System.out.println("INVALID INPUT");
			}
			System.out.println("\n\n");
			if(!c)break;
		}
	}
	
	public static void updateFlightDetails() {
		System.out.println("\n--------------Update Flight Details-------------");
		
		System.out.println("\n\nEnter Database Column Names for Updating the Values:\nf_number, f_origin, f_destination, f_departure_time, f_arrival_time, f_price");
		System.out.println("\n\nEnter the Column Name to Update the value: ");
		String column = input.next();
		String columns = column.toLowerCase();
		
		try {
			conn = DriverManager.getConnection(url,root,pass);
			
			if(conn != null) {
				stmt = conn.createStatement();
			
				if(columns.equals("f_number") || columns.equals("f_price")) {
					System.out.println("\nEnter the New Value: ");
					int value = input.nextInt();
					System.out.println("\nEnter the Flight number: ");
					int number = input.nextInt();
					
					stmt.executeUpdate("update flightDetails set "+columns+" = "+value+" where f_number = "+number+";");
				}
				else if(columns.equals("f_origin") || columns.equals("f_destination")) {
					System.out.println("\nEnter the New Value: ");
					String value = input.next();
					System.out.println("\nEnter the Flight number: ");
					int number = input.nextInt();
					
					stmt.executeUpdate("update flightDetails set "+columns+" = '"+value+"' where f_number = "+number+";");
				}
				else if(columns.equals("f_departure_time") || columns.equals("f_arrival_time")) {
					System.out.println("\nEnter the New Value: ");
					String value = input.next();
					System.out.println("\nEnter the Flight number: ");
					int number = input.nextInt();
					
					Time t1 = Time.valueOf(value);
					
					stmt.executeUpdate("update flightDetails set "+columns+" = '"+t1+"' where  f_number = "+number+";");
					System.out.println("Updated");
				}
				else {
					System.out.println("\nEnter a valid Column Name\n");
					return;
				}
			}
			else System.out.println("Connection Failed\n");
		}
		catch(Exception e) {
			System.out.println("Sorry, Some Error Occured\nPlease Try Again Later");
		}
	}
	
	public static void insertFlightDetails() {
		
		System.out.println("\n--------------Insert Flight Details-------------");
		
		System.out.println("Flight number: ");
		int f_number = input.nextInt();
		System.out.println("\nOrigin: ");
		String origin = input.next();
		String f_origin = origin.toLowerCase();
		System.out.println("\nDestination: ");
		String destination = input.next();
		String f_destination = destination.toLowerCase();
		System.out.println("\nDeparture Time(hh:mm:ss format): ");
		String f_dt = input.next();
		System.out.println("\nArrival Time(hh:mm:ss format): ");
		String f_at = input.next();
		System.out.println("\nPrice of per seat: ");
		int f_price = input.nextInt();
		
		Time t1 = Time.valueOf(f_dt);
		Time t2 = Time.valueOf(f_at);
		
		try {
			conn = DriverManager.getConnection(url,root,pass);
			
			if(conn != null) {
				stmt = conn.createStatement();
				
				stmt.executeUpdate("create table if not exists flightDetails(f_id int auto_increment primary key, f_number int unique key not null, f_origin varchar(30) not null, f_destination varchar(30) not null, f_departure_time time not null, f_arrival_time time not null, f_price int not null);");
				
				stmt.executeUpdate("insert into flightDetails(f_number,f_origin,f_destination,f_departure_time,f_arrival_time,f_price) values ("+f_number+",'"+f_origin+"','"+f_destination+"','"+t1+"','"+t2+"',"+f_price+");");
			}
			else System.out.println("Connection Failed");
		}
		catch(Exception e) {
			System.out.println("Sorry, Some Error Occured\nPlease Try Again Later\n");
		}
	}
	
	public static void retrieveData() {
		while(true) {
			System.out.println("\n--------------Retrieving Data From Database-------------");
			
			System.out.println("\n\nPress <1> to Retrieve Flight Details\nPress <2> to Retrieve Customer Details\nPress <3> to Retrieve Seats Reserved Details\nPress <0> to Exit\nEnter ur Choice:");
			 char choice = input.next().charAt(0);
			 boolean c = true;
		 
			switch(choice) {
				case '1':
					clear();
					flightDetails();
					break;
					
				case '2':
					clear();
					customerDetailsAdmin();
					break;
					
				case '3':
					clear();
					seatsReserved();
					break;
					
				case '0':
					clear();
					System.out.println("\nBack To ADMIN Main Page!!!");
					c = false;
					break;
					
				default:
					System.out.println("INVALID INPUT");
			}
			System.out.println("\n\n");
			if(!c)break;
		}
	}
	
	public static void Admin() {
		System.out.println("\n--------------Admin Authentication Page-------------");
		
		String pwd = "pass";
		System.out.println("\n\nEnter a password: ");
		String cnf = input.next();
		
		if(pwd.equals(cnf)) {
			clear();
			contentAdmin();
		}
		else {
			clear();
			System.out.println("Enter a valid Password");
		}
	}
	
	public static int[] insertCustomerDetails() {
		
		System.out.println("\n--------------Customer Details-------------");
		
		System.out.println("\n\nEnter the no.of person: ");
		n = input.nextInt();
		
		String p_name[] = new String[n];
		String gender[] = new String[n];
		for(int i=0; i<n; i++) {
			System.out.println("\n------------------------------------------");
			System.out.println("\nEnter the passanger["+(i+1)+"] name: ");
			p_name[i] = input.next();
			System.out.println("Enter ur gender: ");
			gender[i] = input.next();
		}
		
		System.out.println("\nEnter ur Mobile Number: ");
		String c_mobile = input.next();
		System.out.println("\nEnter ur Address: ");
		String address = input.next();
		String c_address = address.toLowerCase();
		int c_id[] = new int[n];
		
		try {
			conn = DriverManager.getConnection(url,root,pass);
			
			if(conn != null) {
				stmt = conn.createStatement();
				
				stmt.executeUpdate("create table if not exists customerDetails (c_id int auto_increment primary key, c_name varchar(50) not null, c_gender varchar(15) not null, c_mobile varchar(10) not null, c_address varchar(50) not null);");
				
				for(int i=0; i<n; i++) {
					stmt.executeUpdate("insert into customerDetails(c_name,c_gender,c_mobile,c_address) values('"+p_name[i]+"','"+gender[i]+"','"+c_mobile+"','"+c_address+"');");
					
					r = stmt.executeQuery("select max(c_id) as id from customerDetails;");
				
					if(r.next()) {
						c_id[i] = r.getInt("id");
					}
					else  System.out.println("No customerDetails row is found");
				}
				return c_id;
			}
			else {
				 System.out.println("Connection Failed");
				 return new int[0];
			}
		}
		catch(Exception e) {
			System.out.println("Sorry, Some Error Occured\nPlease Try Again Later\n");
			return new int[0];
		}
	}

	public static void reservation() {
		System.out.println("\n--------------Reservation Details-------------");
		
		System.out.println("\n\nEnter the origin: ");
		String origin = input.next();
		String f_o = origin.toLowerCase();
		System.out.println("\nEnter the destination.: ");
		String destination = input.next();
		String f_d = destination.toLowerCase();
		
		try {
			conn = DriverManager.getConnection(url,root,pass);
			
			if(conn != null) {
				stmt = conn.createStatement();
				r = stmt.executeQuery("select * from flightDetails where f_destination = '"+f_d+"' && f_origin = '"+f_o+"';");
				
				System.out.println("\n-------------------Flight Details-------------");
				
				if(!r.next()) {
					System.out.println("\n\nNo flights found for the specified origin and destination");
					return;
				}
				System.out.println("\n\nFlightNumber  Origin    Destination   DepartureTime    ArrivalTime   Price\n");
				do {
					int f_number = r.getInt("f_number");
					String f_origin = r.getString("f_origin");
					String f_destination = r.getString("f_destination");
					Time dt = r.getTime("f_departure_time");
					Time at = r.getTime("f_arrival_time");
					int f_price = r.getInt("f_price");
					
					System.out.println("  "+f_number+" \t "+f_origin+"    "+f_destination+"    "+dt.toString()+"  "+at.toString()+"  "+f_price+"\n");
				}
				while(r.next());
				
				System.out.println("\n\nEnter the Flight Number for booking ticket: ");
				int f = input.nextInt();
				
				clear();
				
				r = stmt.executeQuery("select f_number from flightDetails where f_number = "+f+";");
				if(!r.next()) {
					System.out.println("\n\nEnter a valid Flight Number");
					return;
				}
				
				int c_id[] = insertCustomerDetails();
				insertSeatDetails(f, c_id);
			}
			else System.out.println("Connection Failed");
		}
		catch(Exception e) {
			System.out.println("Sorry, Some Error Occured\nPlease Try Again Later\n");
		}
	}
	
	public static void flightDetails() {
		try {
			conn = DriverManager.getConnection(url,root,pass);
			
			if(conn != null) {
				stmt = conn.createStatement();
				r = stmt.executeQuery("select * from flightDetails;");
				
				
				System.out.println("\n-------------------Flight Details-------------");
				if(!r.next()) {
					System.out.println("\n\nNo flights found");
					return;
				}
				
				System.out.println("\n\nFlightNumber  Origin    Destination   DepartureTime    ArrivalTime  Price\n");
				do {
					int f_number = r.getInt("f_number");
					String f_origin = r.getString("f_origin");
					String f_destination = r.getString("f_destination");
					Time dt = r.getTime("f_departure_time");
					Time at = r.getTime("f_arrival_time");
					int f_price = r.getInt("f_price");
					
					System.out.println("  "+f_number+" \t "+f_origin+"    "+f_destination+"    "+dt.toString()+"  "+at.toString()+"  "+f_price+"\n");
				}
				while(r.next());
			}
			else System.out.println("Connection Failed");
		}
		catch(Exception e) {
			System.out.println("Sorry, Some Error Occured\nPlease Try Again Later\n");
		}
	}
	
	public static void customerDetailsAdmin() {
		
		try {
			conn = DriverManager.getConnection(url,root,pass);
			
			if(conn != null) {
				stmt = conn.createStatement();
				r = stmt.executeQuery("select * from customerDetails;");
				
				System.out.println("\n-------------------Customer Details-------------");
				
				System.out.println("\n\nc_id   c_name   c_gender   c_mobile   c_address\n");
				while(r.next()) {
					int c_id = r.getInt("c_id");
					String c_name = r.getString("c_name");
					String c_gender = r.getString("c_gender");
					String c_mobile = r.getString("c_mobile");
					String c_address = r.getString("c_address");
					
					System.out.println(" "+c_id+"\t  "+c_name+"    "+c_gender+"    "+c_mobile+"  "+c_address+"\n");
				}
			}
			else System.out.println("Connection failed\n");
		}
		catch(Exception e) {
			System.out.println("Sorry Some Error Occured\nPlease Try Again Later\n");
		}
	}
	
	public static void seatsReserved() {
		try {
			conn = DriverManager.getConnection(url,root,pass);
			
			if(conn != null) {
				stmt = conn.createStatement();
				r = stmt.executeQuery("select customerDetails.c_id, seats.s_id, customerDetails.c_name, seats.s_number, seats.f_number from customerDetails join seats on customerDetails.c_id = seats.c_id;");
				
				System.out.println("\n-------------------Customer Details-------------");
				
				System.out.println("\n\nc_id   s_id   c_name   s_number   f_number\n");
				while(r.next()) {
					int c_id = r.getInt("c_id");
					int s_id = r.getInt("s_id");
					String c_name = r.getString("c_name");
					int s_number = r.getInt("s_number");
					int f_number = r.getInt("f_number");
					
					System.out.println(" "+c_id+"\t  "+s_id+"	"+c_name+"	"+s_number+"	"+f_number+"\n");
				}
			}
			else System.out.println("Connection failed\n");
		}
		catch(Exception e) {
			System.out.println("Sorry, Some Error Occured\n\nPlease Try Again Later\n\n");
		}
	}
	
	public static void insertSeatDetails(int f_number, int c_id[]) {
		try {
			conn = DriverManager.getConnection(url, root, pass);
			
			if(conn != null) {
				stmt = conn.createStatement();
				
				System.out.println("\n-------------------Seat Detail Page-------------");
				
				stmt.executeUpdate("create table if not exists seats (s_id int auto_increment primary key, s_number int not null, f_number int not null, c_id int not null, foreign key(c_id) references customerDetails(c_id), foreign key(f_number) references flightDetails(f_number));");

				r = stmt.executeQuery("select s_number from seats where f_number = "+f_number+";");

				boolean[] seatAvailability = new boolean[41];
				while (r.next()) {
					seatAvailability[r.getInt("s_number")] = true;
				}

				System.out.println("The Available Seats for Flight Number (" + f_number + "):");
				for (int i = 1; i <= 40; i++) {
					if (seatAvailability[i]) {
						System.out.println("Seat " + i + ": reserved");
					}
					else {
						System.out.println("Seat " + i + ": available");
					}
				}
				
				System.out.println("\nNOTE: Seats 1-10 and 30-40 are window seats\n");
				System.out.println("Enter your seat number between 1-40 (available seats only): ");
				int s_number[] = new int[n];
			
				for(int i=0; i<n; i++) {
					System.out.println("\nEnter seat number of passenger "+(i+1)+": ");
					s_number[i] = input.nextInt();

					if (s_number[i] < 1 || s_number[i] > 40 || seatAvailability[s_number[i]]) {
						System.out.println(s_number[i] + " seat is already reserved or invalid.");
						
						//Deleting all customer detail if there is an any error;
						
						for(int j=n-1; j>=0; j--) {
							stmt.executeUpdate("delete from customerDetails where c_id = "+c_id[j]+";");
						}
						return;
					}
				}
				clear();
				System.out.println("\n-------------------Payment Page-------------");

				r = stmt.executeQuery("select f_price from flightDetails where f_number = "+f_number+";");
				int ticketPrice = 0;
				if (r.next()) {
					ticketPrice = r.getInt("f_price");
					System.out.println("\n\nYour ticket price is " + ticketPrice);
				}
				else {
					for(int j=n-1; j>=0; j--) {
							stmt.executeUpdate("delete from customerDetails where c_id = "+c_id[j]+";");
						}
					return; // Exit if the flight data cannot be fetched
				}
				
				int totalPrice = ticketPrice * n;
				System.out.println("\nTotal Price for "+n+" tickets: "+totalPrice);

				System.out.print("Do you want to confirm ur payment(y/n): ");
				char c = input.next().charAt(0);

				if (c == 'y' || c == 'Y') {
					
					for(int i=0; i<n; i++) {
						stmt.executeUpdate("insert into seats(s_number, f_number, c_id) values("+s_number[i]+","+f_number+","+c_id[i]+");");
					}
					
					System.out.println("\nYour Ticket has been Booked\n\nThank u");
					System.out.println("\n-------------------Ticket Details-------------");

					for(int i=0; i<n; i++) {
						r = stmt.executeQuery("select * from seats where s_number = "+s_number[i]+" and f_number = "+f_number+";");
						if (r.next()) {
							System.out.println("Details are: customer_id: "+r.getInt("c_id")+", seat number: "+ r.getInt("s_number")+", flight number: "+r.getInt("f_number"));
						}
					}
				}
				else {
					System.out.println("\nCome back later");
					for(int j=n-1; j>=0; j--) {
							stmt.executeUpdate("delete from customerDetails where c_id = "+c_id[j]+";");
						}
					return;
				}
			}
			else System.out.println("Connection Failed");
		}
		catch (Exception e) {
			System.out.println("Sorry, Some Error Occured\nPlease Try Again Later\n");
		}
	}

	public static void clear() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		}
		catch(Exception e) {
			System.out.println("Sorry, Some Error Occured in Clearing the Screen\n");
		}
	}
	
}