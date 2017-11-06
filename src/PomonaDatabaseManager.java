import java.sql.*;
import java.util.*;

public class PomonaDatabaseManager 
{
	public static void main(String[] args)
	{
		Connection myConnection;
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			myConnection = DriverManager.getConnection("jdbc:mysql://localhost/CS435Lab4?user=root&password=root");
			Scanner keyboard = new Scanner(System.in);
			int selection = 0;
			System.out.print("Enter number for the desired action:"
					+ "\n1. Display the schedule of all trips for a given date, start and destination"
					+ "\n2. Edit a schedule"
					+ "\n3. Display the stops of a given trip"
					+ "\n4. Display the weekly schedule of a given driver and date"
					+ "\n5. Add a driver."
					+ "\n6. Add a bus."
					+ "\n7. Delete a bus."
					+ "\n8. Insert data of a trip offering.\n");
			
			System.out.print("Enter selecion: ");
			selection = keyboard.nextInt();
			if (selection == 1)
			{
				keyboard.nextLine();
				String startLocationName;
				String destinationName;
				String date;
				System.out.print("Enter the starting location name: ");
				startLocationName = keyboard.nextLine();
				System.out.print("Enter the destination name: ");
				destinationName = keyboard.nextLine();
				System.out.print("Enter the date: ");
				date = keyboard.nextLine();
				Statement stmt = myConnection.createStatement();
				String varSql = "Select * ";
				String varWhere = "From Trip T,TripOffering O "
						          + "Where T.TripNumber=O.TripNumber AND T.StartLocationName=\""
						          + startLocationName + "\" AND T.DestinationName=\"" + destinationName
						          + "\" AND O.Date=\"" + date + "\"";
				varSql = varSql + varWhere;
				
				ResultSet rs = stmt.executeQuery(varSql);
				ResultSetMetaData rsMeta = rs.getMetaData();
				String varColNames = " ";
				int varColCount = rsMeta.getColumnCount();
				for (int col=1;col<=varColCount;col++)
				{
					varColNames = varColNames + rsMeta.getColumnName(col) + " ";
				}
				System.out.println(varColNames);
				while(rs.next())
				{
					for (int col=1;col<=varColCount;col++)
					{
						System.out.print(rs.getString(col) + " ");
					}
					System.out.println();
				}
			}
			if (selection == 2)
			{
				int tripNumber;
				System.out.print("Enter the trip number: ");
				tripNumber = keyboard.nextInt();
				String date;
				System.out.print("Enter the date of trip: ");
				date = keyboard.nextLine();
				String startTime;
				System.out.print("Enter the scheduled start time for the trip: ");
				startTime = keyboard.nextLine();
				keyboard.nextLine();
				int editSelection;
				System.out.print("Enter number for how you would like to edit the schedule: "
						         +"\n1. Delete trip offering."
						         +"\n2. Add a trip offering."
						         +"\n3. Change the driver for the trip."
						         +"\n4. Change the bus for the trip." );
				editSelection = keyboard.nextInt();
				if (editSelection == 1)
				{
					Statement stmt = myConnection.createStatement();
					String sql = "Delete FROM TripOffering"
							     +"Where TripNumber=" + tripNumber + " AND Date=" + date + "AND ScheduledStartTime=" + startTime;
					ResultSet rs = stmt.executeQuery(sql);
				}
				if (editSelection == 2)
				{
					String scheduledArrivalTime;
					System.out.print("Enter ScheduledArrivalTime: ");
					scheduledArrivalTime = keyboard.nextLine();
					String driverName;
					System.out.print("Enter driver's name: ");
					driverName = keyboard.nextLine();
					keyboard.nextLine();
					int busID;
					System.out.print("Enter the bus number: ");
					busID = keyboard.nextInt();
					Statement stmt = myConnection.createStatement();
					String sql = "Insert into TripOffering " +
					             "Values(" + tripNumber +"," + date + ", '" + startTime + "', '" + scheduledArrivalTime + "', '"
					             + driverName + "', " + busID + ")";
					stmt.executeUpdate(sql);
				}
				if (editSelection == 3)
				{
					keyboard.nextLine();
					String driverName;
					System.out.print("Enter new driver name: ");
					driverName = keyboard.nextLine();
					Statement stmt = myConnection.createStatement();
					String sql = "Update TripOffering " +
					             "Set DriverName=\"" + driverName + "\"where TripNumber=\"" + tripNumber + "\" AND Date=\""
					             + date + "\"AND ScheduledStartTime=\"" + startTime + "\"";
					stmt.executeUpdate(sql);
				}
				if (editSelection == 4)
				{
					keyboard.nextLine();
					int busID;
					System.out.println("Enter the new Bus ID: ");
					busID = keyboard.nextInt();
					PreparedStatement ps = myConnection.prepareStatement("Update TripOffering SET BusID =? Where TripNumber=? AND Date=? AND ScheduledStart=?");
					ps.setInt(1, busID);
					ps.setInt(2, tripNumber);
					ps.setString(3, date);
					ps.setString(4, startTime);
					ps.executeUpdate();
					
				}
				
			}
			if (selection == 3)
			{
				keyboard.nextLine();
				int tripNumber;
				System.out.print("Enter the trip number: ");
				tripNumber = keyboard.nextInt();
				Statement stmt = myConnection.createStatement();
				String varSql = "Select * From TripStopInfo Where TripNumber=" + tripNumber;
				ResultSet rs = stmt.executeQuery(varSql);
				ResultSetMetaData rsMeta = rs.getMetaData();
				String varColNames = " ";
				int varColCount = rsMeta.getColumnCount();
				for (int col=1;col<=varColCount;col++)
				{
					varColNames = varColNames + rsMeta.getColumnName(col) + " ";
				}
				System.out.println(varColNames);
				while(rs.next())
				{
					for (int col=1;col<=varColCount;col++)
					{
						System.out.print(rs.getString(col) + " ");
					}
					System.out.println();
				}				
			}
			if (selection == 4)
			{
				Statement stmt = myConnection.createStatement();
				keyboard.nextLine();
				String driverName;
				System.out.print("Enter the name of a driver: ");
				driverName = keyboard.nextLine();
				String date;
				System.out.print("Enter the date: ");
				date = keyboard.nextLine();
				String varSql = "Select * From Driver D,TripOffering T Where T.DriverName=D.DriverName AND D.DriverName=\"" 
				                + driverName + "\"AND T.Date=\"" + date + "\"";
				ResultSet rs = stmt.executeQuery(varSql);
				ResultSetMetaData rsMeta = rs.getMetaData();
				String varColNames = " ";
				int varColCount = rsMeta.getColumnCount();
				for (int col=1;col<=varColCount;col++)
				{
					varColNames = varColNames + rsMeta.getColumnName(col) + " ";
				}
				System.out.println(varColNames);
				while(rs.next())
				{
					for (int col=1;col<=varColCount;col++)
					{
						System.out.print(rs.getString(col) + " ");
					}
					System.out.println();
				}	
				
			}
			if (selection == 5)
			{
				keyboard.nextLine();
				String driverName;
				String driverPhone;
				System.out.print("Enter the driver's name: ");
				driverName = keyboard.nextLine();
				System.out.print("Enter the driver's phone number: ");
				driverPhone = keyboard.nextLine();
				PreparedStatement ps = myConnection.prepareStatement("Insert Into Driver(DriverName,DriverTelephoneNumber) Values(?,?)");
				ps.setString(1, driverName);
				ps.setString(2, driverPhone);
				ps.executeUpdate();
			}
			if (selection == 6)
			{
				int busID;
				System.out.print("Enter the bus number: ");
				busID = keyboard.nextInt();
				int year;
				System.out.print("Enter the year: ");
				year = keyboard.nextInt();
				keyboard.nextLine();
				String model;
				System.out.print("Enter the model of the bus: ");
				model = keyboard.nextLine();
				PreparedStatement ps = myConnection.prepareStatement("Insert Into Bus(BusID,Model,Year) Values (?,?,?)");
				ps.setInt(1, busID);
				ps.setString(2, model);
				ps.setInt(3, year);
				ps.executeUpdate();
			}
			if (selection == 7)
			{
				int busID;
				System.out.print("Enter bus id to be deleted: ");
				busID = keyboard.nextInt();
				String sql = "Delete from Bus where BusID=?";
				PreparedStatement ps = myConnection.prepareStatement(sql);
				ps.setInt(1, busID);
				ps.executeUpdate();
			}
			if (selection == 8)
			{
				int tripNumber;
				String date;
				String startTime;
				String stopNumber;
				System.out.print("Enter the tripNumber: ");
				tripNumber = keyboard.nextInt();
				keyboard.nextLine();
				System.out.print("Enter the date: ");
				date = keyboard.nextLine();
				System.out.print("Enter the scheduled start time: ");
				startTime = keyboard.nextLine();
				System.out.print("Enter the stop number: ");
				stopNumber = keyboard.nextLine();
				String sql = "Insert into ActualTripStopInfo(TripNumber,Date,ScheduledStartTime,StopNumber) Values(?,?,?,?)";
				PreparedStatement ps = myConnection.prepareStatement(sql);
				ps.setInt(1, tripNumber);
				ps.setString(2, date);
				ps.setString(3, startTime);
				ps.setString(4, stopNumber);
				ps.executeUpdate();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
