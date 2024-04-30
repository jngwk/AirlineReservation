import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String sql = "";
        Scanner sc = new Scanner(System.in);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinedb?useSSL=false", "multicampus", "0000");
            PreparedStatement pst;
            ResultSet rs;
            // consider using hashmap for easier query
            ArrayList<Queryable> members = new ArrayList<>();
            ArrayList<Queryable> flights = new ArrayList<>();
            ArrayList<Queryable> bookings = new ArrayList<>();
            
            // get flight information
            sql = "SELECT * FROM flights";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                flights.add(new Flight(rs.getInt(1), rs.getString(2), rs.getString(3)
                    , rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7)));
            }
            // get members
            sql = "SELECT * FROM members";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                members.add(new Member(rs.getString(1), rs.getString(2), rs.getString(3)
                    , rs.getString(4), rs.getString(5), rs.getString(6)));
            }
            
            // get bookings
            sql = "SELECT * FROM bookings";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                bookings.add(new Booking(rs.getString(1), rs.getInt(2), rs.getString(3)));
            }

            // TODO seatsLeft
            
            int menu = 0; // chosen menu
            boolean jump = false; // whether or not to jump to a different menu

            // menu1)
            START_MENU:
            while(true){
                if(!jump){
                    showStartMenu();
                    do{
                        System.out.print("Please select a menu (0~4)>> ");
                        menu = sc.nextInt();
                    }while(menu < 0 || menu > 4);
                }
                
                // sign up
                if(menu == 1){
                    // add new member to database
                    if(signUp(sc, members)){
                        if(updateQuery(conn, pst, members))
                        continue;
                    }
                    jump = true;
                    menu = 2;
                    continue;
                }
                // member login
                else if(menu == 2){
                    // member menu)
                    Member currentMember = logIn(sc, members);
                    if(currentMember != null){
                        // set title (Mr / Ms)
                        String title;
                        if(currentMember.getSex().equals("M")){
                            title = "Mr. ";
                        }
                        else{
                            title = "Ms. ";
                        }
                        // member menu
                        MEMBER_MENU:
                        while(true){
                            showMemberMenu();
                            System.out.printf("Welcome, %s.\n", title + currentMember.getLastName());
                            do{
                                System.out.print("Please select a menu (0~5)>> ");
                                menu = sc.nextInt();
                            }while(menu < 0 || menu > 5);

                            // book flights
                            if(menu == 1){
                                // book menu
                                BOOK_MENU:
                                while(true){
                                    int selectId;
                                    // TODO match length of chars
                                    showBookMenu();
                                    do{
                                        System.out.print("Please select a menu (0~4)>> ");
                                        menu = sc.nextInt();
                                    }while(menu < 0 || menu > 4);
                                    
                                    // search all flights
                                    if(menu == 1){
                                        
                                        showAll(flights);
                                        System.out.print("Please select a flight by typing the corresponding ID (0.Back): ");
                                        selectId = sc.nextInt();
                                        if(selectId == 0){
                                            continue;
                                        }
                                        else{
                                            bookFlight(selectId, currentMember, bookings);
                                            updateQuery(conn, pst, bookings);
                                            updateSeatsInList(selectId, flights, -1);
                                        }
                                    }
                                    // select origin country
                                    else if(menu == 2){
                                        String selectOrigin;
                                        showOrigins(conn, pst, rs);
                                        System.out.print("Please select a country from which you are departing (e.g., KOR) (0.Back): ");
                                        selectOrigin = sc.next().toUpperCase();
                                        if(selectOrigin.equals("0")){
                                            continue;
                                        }

                                        if(showSelectedFlights(selectOrigin, flights)){
                                            System.out.print("Please select a flight by typing the corresponding ID (0.Back): ");
                                            selectId = sc.nextInt();
                                            if(selectId == 0){
                                                continue;
                                            }
                                            else{
                                                bookFlight(selectId, currentMember, bookings);
                                                updateQuery(conn, pst, bookings);
                                                updateSeatsInList(selectId, flights, -1);
                                            }
                                        }
                                    }
                                    else if(menu == 3){
                                        break BOOK_MENU;
                                    }
                                    else if(menu == 4){
                                        if(logOut(sc)){
                                            currentMember = null;
                                            break MEMBER_MENU;
                                        }
                                    }
                                    else if(menu == 0){
                                        if(endProgram(sc)){
                                            pst.close();
                                            conn.close();
                                            break START_MENU;
                                        }
                                    }
                                } // end of book menu
                            }
                            // booking details
                            else if(menu == 2){
                                showBookingDetails(currentMember, bookings, flights);
                                
                            }
                            // change booking
                            else if(menu == 3){
                               if(showBookingDetails(currentMember, bookings, flights)){
                                    System.out.println("Please type in the booking ID to proceed (0. Cancel): ");
                                    String bookingId = sc.next();
                                    if(bookingId.equals("0")){
                                        System.out.println("Your transaction has been cancelled.");
                                        continue;
                                    }
                                    else{
                                        int selectId;
                                        showAll(flights);
                                        System.out.print("Please select a flight by typing the corresponding ID (0.Back): ");
                                        selectId = sc.nextInt();
                                        if(selectId == 0){
                                            continue;
                                        }
                                        else{
                                            if(confirmationMessage(sc)){
                                                int flightId = getFlightIdFromBookings(bookingId, bookings);
                                                changeBooking(conn, pst, bookingId, selectId, bookings);
                                                updateSeatsInList(flightId, flights, 1);
                                                updateSeatsInList(selectId, flights, -1);
                                            }
                                        }
                                        
                                    }
                                }
                            }
                            // cancel booking
                            else if(menu == 4){
                                if(showBookingDetails(currentMember, bookings, flights)){
                                    System.out.println("Please type in the booking ID to proceed (0. Cancel): ");
                                    String bookingId = sc.next();
                                    if(bookingId.equals("0")){
                                        System.out.println("Your transaction has been cancelled.");
                                        continue;
                                    }
                                    else{
                                        int flightId = getFlightIdFromBookings(bookingId, bookings);
                                        updateSeatsInList(flightId, flights, +1);
                                        if(confirmationMessage(sc)){
                                            deleteBooking(conn, pst, bookingId, bookings);
                                        }
                                    }
                                }
                            }
                            // log out
                            else if(menu == 5){
                                if(logOut(sc)){
                                    currentMember = null;
                                    break MEMBER_MENU;
                                }
                            }
                            // end program
                            else if(menu == 0){
                                if(endProgram(sc)){
                                    pst.close();
                                    conn.close();
                                    break START_MENU;
                                }
                            }
                        } // end of member menu loop
                    }
                } // end of member log in
                // admin login
                else if(menu == 3){
                    if(!adminLogIn(sc)){
                        continue;
                    }
                    ADMIN_MENU:
                    while(true){
                        showAdminMenu();
                        do{
                            System.out.print("Please select a menu (0~4)>> ");
                            menu = sc.nextInt();
                        }while(menu < 0 || menu > 4);

                        // show all flights
                        if(menu == 1){
                            showAll(flights);
                        }
                        // show all bookings
                        else if(menu == 2){
                            showAll(bookings);
                        }
                        // show all members
                        else if(menu == 3){
                            showAll(members);
                        }
                        else if(menu == 4){
                            if(logOut(sc)){
                                break ADMIN_MENU;
                            }
                        }
                        else if(menu == 0){
                            if(endProgram(sc)){
                                pst.close();
                                conn.close();
                                break START_MENU;
                            }
                        }
                    } // end of admin menu
                }
                // end program
                else if (menu == 0){
                    if(endProgram(sc)){
                        pst.close();
                        conn.close();
                        break;
                    }
                }            
                
                // initialize jump
                jump = false;

            } // end of while
        }catch(Exception e){
            System.out.println(e.getMessage());
        } // end of try catch
        sc.close();
    } // end of main


    /* Methods */
    public static void showStartMenu(){
        System.out.println("================================================================");
        System.out.println("[Main Menu]");
        System.out.println("1.Sign Up 2.Sign In as Member 3.Sign In as Admin 0.End Program");
        System.out.println("================================================================");
    }

    public static void showMemberMenu(){
        System.out.println("==========================================================================================");
        System.out.println("[Member Menu]");
        System.out.println("1.Book Flight 2.Booking Details 3.Change Booking 4.Cancel Booking 5.Log Out 0.End Program");
        System.out.println("==========================================================================================");
    }

    public static void showBookMenu(){
        System.out.println("============================================================================");
        System.out.println("[Booking Menu]");
        System.out.println("1.Search All Flights 2.Select Origin Country 3.Back 4.Log Out 0.End Program");
        System.out.println("============================================================================");
    }
    
    public static void showAdminMenu(){
        System.out.println("==================================================================================");
        System.out.println("[Admin Menu]");
        System.out.println("1.View All Flights 2.View All Bookings 3.View All Members 4.Log Out 0.End Program");
        System.out.println("==================================================================================");
    }
    
    // TODO loadData
    public static void loadData(Connection conn, PreparedStatement pst, ArrayList<Queryable> queryables){

    }
    
    // updates query for any queryable list (Member)
    public static boolean updateQuery(Connection conn, PreparedStatement pst, ArrayList<Queryable> queryables){
        try {
            if(queryables.get(0) instanceof Member){
                // insert into members
                Member newMember = (Member)queryables.get(queryables.size()-1);
                String sql = "INSERT INTO members VALUES(?, ?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, newMember.getId());
                pst.setString(2, newMember.getPwd());
                pst.setString(3, newMember.getFirstName());
                pst.setString(4, newMember.getLastName());
                pst.setString(5, newMember.getPassport());
                pst.setString(6, newMember.getSex());
                pst.executeUpdate();
                System.out.println("You have successfully signed up.");
                System.out.println("Please log in to use the service.");
            }

            else if(queryables.get(0) instanceof Booking){
                // inset into bookings
                Booking newBooking = (Booking) queryables.get(queryables.size()-1);
                String sql = "INSERT INTO bookings VALUES(?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, newBooking.getbookingId());
                pst.setInt(2, newBooking.getFlightId());
                pst.setString(3, newBooking.getMemberId());
                pst.executeUpdate();
    
                
                // update seatsLeft
                sql = "UPDATE flights SET seatsLeft = seatsLeft-1 WHERE id = ?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, newBooking.getFlightId());
                pst.executeUpdate();

                System.out.println("Your flight has been successfully booked.");
            }
            return true;
        } catch(Exception e) {
            System.out.println("An error has occurred, please contact customer service.");
            return false;
        }
        
    } // end of updateQuery

    // get flight id from booking
    // to update seats
    public static int getFlightIdFromBookings(String bookingId, ArrayList<Queryable> bookings){
        int flightId = 0;
        for(Queryable queryable : bookings){
            if(queryable instanceof Booking){
                Booking booking = (Booking) queryable;
                if(booking.getbookingId().equals(bookingId)){
                    flightId = booking.getFlightId();
                }
            }
        }
        return flightId;
    } // end of getFlightId

    // update seats in list
    public static void updateSeatsInList(int selectId, ArrayList<Queryable> flights, int updateSeatsNum){
        for(Queryable queryable : flights){
            if(queryable instanceof Flight){
                Flight flight = (Flight) queryable;
                if(flight.getId() == selectId){
                    flight.updateSeatsLeft(updateSeatsNum);
                }
            }
        }
    } // end of updateSeatsInList

    // shows all flights, members, bookings
    public static void showAll(ArrayList<Queryable> queryables){
        if(queryables.get(0) instanceof Flight){
            System.out.println("[Available Flights]:");
            System.out.println("ID | Flight Number\t| Origin ---> Destination | Departure Date\t| Arrival Date\t\t| Seats Left");
            System.out.println("==============================================================================================================");
            for(Queryable queryable : queryables){
                Flight flight = (Flight) queryable;
                System.out.println(flight.toString());
            }

        }
        else if(queryables.get(0) instanceof Member){
            System.out.println("[List of Members]");
            System.out.println("ID | Full name\t| Sex");
            System.out.println("========================================================");
            for(Queryable queryable : queryables){
                Member member = (Member) queryable;
                System.out.println(member.toString());
            }
        }
        else if(queryables.get(0) instanceof Booking){
            System.out.println("[List of Bookings]");
            System.out.println("Booking ID\t| Flight ID\t| Member ID");
            for(Queryable queryable : queryables){
                Booking booking = (Booking) queryable;
                System.out.println(booking.toString());
            }
        }
    } // end of showAll

    // book flight
    public static void bookFlight(int selectId, Member member, ArrayList<Queryable> bookings){
        String memberId = member.getId();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
        String bookingId = sdf.format(now);

        bookings.add(new Booking(bookingId, selectId, memberId));
    } // end of bookFlight

    // shows origin countries as "[country code]"
    public static void showOrigins(Connection conn, PreparedStatement pst, ResultSet rs){
        try{
            String sql = "SELECT origin FROM flights WHERE seatsLeft > 0 GROUP BY origin ORDER BY origin";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1));
            }
            System.out.println("=====");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    } // end of showOrigins

    // shows origin, destination country, and other info as "[origin code] ---> [dest code] | dept date | arr date | seats"
    public static boolean showSelectedFlights(String selectOrigin, ArrayList<Queryable> flights){
        
        boolean noMatch = true;
        for(Queryable queryable : flights){
            if(queryable instanceof Flight){
                Flight flight = (Flight) queryable;
                if(flight.getOrigin().equals(selectOrigin)){
                    if(noMatch){
                        System.out.println("Here is a list of available flights with your selected origin:");
                        System.out.println("ID | Flight Number\t| Origin ---> Destination | Departure Date\t| Arrival Date\t\t| Seats Left");
                        System.out.println("==============================================================================================================");
                        noMatch = false;
                    }
                    System.out.println(flight.toString());
                }
            }
        }
        if(noMatch){
            System.out.println("There is no information on the selected origin.");
            return false;
        }
        return true;
    }// end of showSelectedFlights

    // show booking details
    public static boolean showBookingDetails(Member member, ArrayList<Queryable> bookings, ArrayList<Queryable> flights){
        boolean hasBooking = false;
        String memberId = member.getId();
        for(Queryable queryable : bookings){
            if(queryable instanceof Booking){
                Booking booking = (Booking) queryable;
                if(booking.getMemberId().equals(memberId)){
                    for(Queryable queryable2 : flights){
                        if(queryable2 instanceof Flight){
                            Flight flight = (Flight) queryable2;
                            if(booking.getFlightId() == flight.getId()){
                                if(hasBooking == false){
                                    System.out.println("Booking details:");
                                    System.out.println("Booking ID\t| Flight ID | Flight Number\t| Origin ---> Destination | Departure Date\t| Arrival Date\t\t| Seats Left");
                                    System.out.println("==============================================================================================================");
                                    hasBooking = true;
                                }
                                System.out.printf("%-17s | %s\n", booking.getbookingId(), flight.toString());
                                // System.out.println(booking.getbookingId() + " | " + flight.toString());
                            }
                        }
                    }
                }
            }
        }
        if(hasBooking == false){
            System.out.println("You do not have any bookings.");
        }
        return hasBooking;
    } // end of showBookingDetails

    // change booking
    public static void changeBooking(Connection conn, PreparedStatement pst, String bookingId, int selectId, ArrayList<Queryable> bookings){
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
        String newBookingId = sdf.format(now);

        try {
            String sql = "UPDATE bookingWithSeats SET seatsLeft = seatsLeft+1 WHERE bookingId = ?";     
            pst = conn.prepareStatement(sql);
            pst.setString(1, bookingId);
            pst.executeUpdate();

            sql = "UPDATE bookingWithSeats SET bookingId = ?, flightId = ? WHERE bookingId = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, newBookingId);
            pst.setInt(2, selectId);
            pst.setString(3, bookingId);
            pst.executeUpdate();

            sql = "UPDATE bookingWithSeats SET seatsLeft = seatsLeft-1 WHERE bookingId = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, newBookingId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Queryable queryable: bookings){
            if(queryable instanceof Booking){
                Booking booking = (Booking) queryable;
                if(booking.getbookingId().equals(bookingId)){
                    booking.setFlightId(selectId);
                    booking.setbookingId(newBookingId);
                }
            }
        }

        System.out.println("Your flight has been changed.");
    } // end of changeBooking

    // deleteBooking
    public static void deleteBooking(Connection conn, PreparedStatement pst, String bookingId, ArrayList<Queryable> bookings){
        try {
            String sql = "UPDATE bookingWithSeats SET seatsLeft = seatsLeft+1 WHERE bookingId = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, bookingId);
            pst.executeUpdate();
            
            sql = "DELETE FROM bookings WHERE bookingId = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, bookingId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(Queryable queryable : bookings){
            if(queryable instanceof Booking){
                Booking booking = (Booking) queryable;
                if(booking.getbookingId().equals(bookingId)){
                    bookings.remove(booking);
                }
            }
        }

        System.out.println("Your flight has been cancelled.");

    } // end of deleteBooking

    // adds new member info into array list
    // if sign up is successful, return true
    public static boolean signUp(Scanner sc, ArrayList<Queryable> members){
        String id;
        String pwd;
        String firstName;
        String lastName;
        String passport;
        String sex;
                        
        System.out.println("Fill in the information below to sign up.");
        // get ID and check duplicate
        do{
            System.out.print("User ID: ");
            id = sc.next();
            for(Queryable queryable: members){
                Member member = (Member)queryable;
                if(member.getId().equals(id)){
                    System.out.println("The ID already exists.");
                    System.out.print("Would you like to log in? (Y|N): ");
                    if(sc.next().toLowerCase().equals("y")){
                        return false;
                    }
                    else{
                        id = "";
                        break;
                    }
                }
            }
        }while(id.equals(""));

        // get password
        System.out.print("User Password: ");
        pwd = sc.next();

        // get name
        System.out.println("Please make sure to put the exact name on your passport.");
        System.out.print("First Name: ");
        firstName = sc.next().toUpperCase();
        System.out.print("Last Name: ");
        lastName = sc.next().toUpperCase();

        // get passport number
        System.out.print("Passport Number: ");
        passport = sc.next().toUpperCase();
        
        // get gender
        do{
            System.out.print("Sex (M|F): ");
            sex = sc.next().toUpperCase();
        }while(!sex.equals("M") && !sex.equals("F"));

        // add into array list
        members.add(new Member(id, pwd, firstName, lastName, passport, sex));

        // complete
        return true;

    } // end of signUp
    
    // returns the logged in member when successfully logged in
    public static Member logIn(Scanner sc, ArrayList<Queryable> members){
        for(int i=0; i<3; i++){
            System.out.print("User ID: ");
            String id = sc.next();
            System.out.print("User Password: ");
            String pwd = sc.next();
            
            for(Queryable queryable : members){
                if(queryable instanceof Member){
                    Member member = (Member) queryable;
                    if(member.getId().equals(id) && member.getPwd().equals(pwd)){
                        return member;
                    }
                }
            }
            System.out.println("Your ID or password is incorrect.");
        }
        System.out.println("Please sign in or try again later.");
        return null;
    }// end of log in

    public static boolean adminLogIn(Scanner sc){
        for(int i=0; i<3; i++){
            System.out.print("User ID: ");
            String id = sc.next();
            System.out.print("User Password: ");
            String pwd = sc.next();
            
            if(id.equals("admin") && pwd.equals("1234")){
                return true;
            }
            System.out.println("Your ID or password is incorrect.");
        }
        System.out.println("Please try again later.");

        return false;
    }
    // log out
    public static boolean logOut(Scanner sc){
        System.out.print("Do you want to log out? (Y|N): ");
        if(sc.next().toLowerCase().equals("y")){
            System.out.println("Logging out...");
            return true;
        }
        return false;
    } // end of log out
    
    public static boolean confirmationMessage(Scanner sc){
        System.out.print("Do you want to proceed? (Y|N): ");
        if(sc.next().toLowerCase().equals("y")){
            return true;
        }
        return false;
    }

    // ends program
    public static boolean endProgram(Scanner sc)
    {
        System.out.print("Do you want to end the program? (Y|N): ");
        if(sc.next().toLowerCase().equals("y")){
            System.out.println("Ending program...");
            return true;
        }
        return false;
        
    } // end of endProgram

}// end of class main
    
