package com.cursor.service;

import com.cursor.dao.impls.TicketDaoDb;
import com.cursor.dao.impls.TicketDaoInMem;
import com.cursor.dao.impls.UserDaoDb;
import com.cursor.dao.impls.UserDaoInMem;
import com.cursor.model.Ticket;
import com.cursor.model.User;
import com.cursor.model.enums.Priority;
import com.cursor.model.enums.Status;
import com.cursor.service.exceptions.NotFoundException;
import com.cursor.service.interfaces.Dashboard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardImpl implements Dashboard {
//    private TicketDaoInMem tickets = TicketDaoInMem.getInstance();
//    private UserDaoInMem users = UserDaoInMem.getInstance();
    private final TicketDaoDb tickets = TicketDaoDb.getInstance();
    private final UserDaoDb users = UserDaoDb.getInstance();

    @Override
    public int getTotalTime(User user) throws SQLException {
        int time = 0;
        for (Ticket ticket : tickets.getAll()) {
            if (ticket.getAssignee().getId()==(user.getId())) {
//                if (ticket.getAssignee().equals(user)) {
                time += ticket.getTimeEstimated();
            }
        }
        return time;
    }

    @Override
    public int getSpentTime(User user) throws SQLException {
        int time = 0;
        for (Ticket ticket : tickets.getAll()) {
            if (ticket.getAssignee().getId()==(user.getId())) {
//                if (ticket.getAssignee().equals(user)) {
                time += ticket.getTimeSpent();
            }
        }
        return time;
    }

    @Override
    public Ticket mostTimeExpensiveTicket() throws SQLException {
        return tickets.getAll()
                .stream()
                .max(Comparator
                        .comparing(Ticket::getTimeEstimated))
                .orElseThrow(() -> new NotFoundException("Most time-expensive ticket not found"));
    }

    @Override
    public List<Ticket> getTicketsByUser(User user) throws SQLException {
        List<Ticket> ticketsByUser = new ArrayList<>();
        for (Ticket ticket : tickets.getAll()) {
            if (ticket.getAssignee().getId()==(user.getId())) {
//                if (ticket.getAssignee().equals(user)) {
                ticketsByUser.add(ticket);
            }
        }
        return ticketsByUser;
    }

    @Override
    public String getSystemStatistics() throws SQLException {
        int usersCount = users.getAll().size();
        int ticketCount = tickets.getAll().size();
        return "There are in System " + usersCount + " users and " + ticketCount + " tickets";
    }

    @Override
    public String getUserStatistics(User user) throws SQLException {
        List<Ticket> ticketsByUser = getTicketsByUser(user);
        String ticketsToDo = getStatusStatistics(ticketsByUser, Status.TODO);
        String inProgressTickets = getStatusStatistics(ticketsByUser, Status.IN_PROGRESS);
        String inReviewTickets = getStatusStatistics(ticketsByUser, Status.IN_REVIEW);
        String approvedTickets = getStatusStatistics(ticketsByUser, Status.APPROVED);
        String doneTickets = getStatusStatistics(ticketsByUser, Status.DONE);
        List<Ticket> createTickets = tickets.getAll()
                .stream()
//                .filter(ticket -> ticket.getReporter().equals(user))
                .filter(ticket -> ticket.getReporter().getId()==(user.getId()))
                .collect(Collectors.toList());
        return "User " + user.getUsername() +
                " creates " + createTickets.size() + " tickets, " +
                "works on  " + ticketsByUser.size() + " tickets. \n " +
                ticketsToDo +
                inProgressTickets +
                inReviewTickets +
                approvedTickets +
                doneTickets;
    }

    @Override
    public List<Ticket> getTicketsByStatus(Status status) throws SQLException {
        return tickets.getAll()
                .stream()
                .filter(ticket -> ticket.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getTicketsByPriority(Priority priority) throws SQLException {
        return tickets.getAll()
                .stream()
                .filter(ticket -> ticket.getPriority().equals(priority))
                .collect(Collectors.toList());
    }

    private String getStatusStatistics(List<Ticket> tickets, Status status) {
        return tickets
                .stream()
                .filter(ticket -> ticket.getStatus().equals(status))
                .collect(Collectors.teeing(
                        Collectors.counting(),
                        Collectors.toList(),
                        (number, sortTickets) -> status.toString() + " " + number +
                                " tickets:  " + sortTickets.toString() + "\n"));
    }
}