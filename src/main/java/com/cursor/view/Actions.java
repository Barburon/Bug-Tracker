package com.cursor.view;

import com.cursor.model.Ticket;
import com.cursor.model.User;
import com.cursor.model.enums.Priority;
import com.cursor.model.enums.Status;
import com.cursor.service.TicketServiceImpl;
import com.cursor.service.UserServiceImpl;

import java.util.Scanner;

public class Actions {
    UserServiceImpl userService = new UserServiceImpl();
    Scanner scanner = new Scanner(System.in);
    TicketServiceImpl ticketService = new TicketServiceImpl();

    public String inputTicketName() {
        System.out.println("Bug ticket's name: ");
        return scanner.next();
    }

    public String inputTicketDescription() {
        System.out.println("Description: ");
        return scanner.next();
    }

    public User inputTicketAssignee() {
        System.out.println("User's ID to Assign: ");
        int usersID = scanner.nextInt();
        return userService.findById(usersID);
    }

    public User inputTicketReporter() {
        System.out.println("User's ID for Reporter");
        int usersID = scanner.nextInt();
        return userService.findById(usersID);
    }

    public Status inputTicketStatus() {
        System.out.println("Number of bug ticket Status(1 - ToDo, 2 -InProgress, 3 – In Review, 4 – Approved, 5 - Done): ");
        int menuStatus = scanner.nextInt();
        return switch (menuStatus) {
            case 1 -> Status.TODO;
            case 2 -> Status.IN_PROGRESS;
            case 3 -> Status.IN_REVIEW;
            case 4 -> Status.APPROVED;
            case 5 -> Status.DONE;
            default -> null;
        };
    }

    public Priority inputTicketPriority() {
        System.out.println("Number of bug ticket Priority(1 - Trivial, 2 - Minor, 3 – Major, 4 – Critical, 5 - Blocker)");
        int menuStatus = scanner.nextInt();
        return switch (menuStatus) {
            case 1 -> Priority.TRIVIAL;
            case 2 -> Priority.MINOR;
            case 3 -> Priority.MAJOR;
            case 4 -> Priority.CRITICAL;
            case 5 -> Priority.BLOCKER;
            default -> null;
        };
    }

    public int inputTimeSpent() {
        System.out.println("Time in hours to Time Spent");
        return scanner.nextInt();
    }

    public int inputTimeEstimated() {
        System.out.println("Time in hours to Time Estimated");
        return scanner.nextInt();
    }

    public void showActionsMenu() {
        boolean isActiveMenu = true;
        while (isActiveMenu) {
            showTicketsMenu();
            int menu = scanner.nextInt();
            switch (menu) {
                case 1 -> ticketService.create(new Ticket(inputTicketName(), inputTicketDescription(), inputTicketAssignee(), inputTicketReporter(),
                        inputTicketStatus(), inputTicketPriority(), inputTimeSpent(), inputTimeEstimated()));
                case 2 -> {
                    System.out.println("Lists of bug tickets: ");
                    ticketService.getAll().forEach(ticket -> System.out.println(ticket.toString()));
                    System.out.println(" ");
                }
                case 3 -> {
                    System.out.println("For search please enter bug ticket's ID: ");
                    int ticketID = scanner.nextInt();
                    ticketService.findById(ticketID);
                    System.out.println(" ");
                }
                case 4 -> {
                    int ticketID = scanner.nextInt();
                    ticketService.edit(ticketID, new Ticket());
                }
                case 5 -> {
                    System.out.println("For delete bug ticket please enter bug ticket's ID: ");
                    int ticketID = scanner.nextInt();
                    ticketService.delete(ticketID);
                    System.out.println("Bug ticket with id " + ticketID + " was deleted");
                    System.out.println(" ");
                }
                case 0 -> isActiveMenu = false;
                default -> System.out.println("Wrong number, please enter a number 0-5:");
            }
        }
    }

    private void showTicketsMenu() {
        System.out.println("============Ticket menu=============");
        System.out.println("Please choose next: ");
        System.out.println("'1' - Create new bug-ticket ");
        System.out.println("'2' - Show all bug-tickets");
        System.out.println("'3' - Find bug-ticket by ID");
        System.out.println("'4' - Edit bug-ticket by ID ");
        System.out.println("'5' - Delete bug-ticket by ID");
        System.out.println("'0' - Back to User's menu");
    }
}
