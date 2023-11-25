package com.driver;

import java.util.*;

public class Gmail extends Email {

    private int inboxCapacity;
    //maximum number of mails inbox can store
    List<Mail> inbox;
    //Inbox: Stores mails. Each mail has date (Date), sender (String), message (String). It is guaranteed that message is distinct for all mails.
    List<Mail> trash;
    //Trash: Stores mails. Each mail has date (Date), sender (String), message (String)
    public Gmail(String emailId, int inboxCapacity) {
        super(emailId);
        this.inboxCapacity = inboxCapacity;
        inbox = new ArrayList<>();
        trash = new ArrayList<>();
    }

    public void receiveMail(Date date, String sender, String message){
        // If the inbox is full, move the oldest mail in the inbox to trash and add the new mail to inbox.
        // It is guaranteed that:
        // 1. Each mail in the inbox is distinct.
        // 2. The mails are received in non-decreasing order. This means that the date of a new mail is greater than equal to the dates of mails received already.
        Mail mail = new Mail(date, sender, message);
        if (inbox.size() == inboxCapacity) {
            moveOldestMailToTrash();
        } else {
            inbox.add(mail);
        }
    }

    private void moveOldestMailToTrash() {
        Mail oldestMail = inbox.remove(0);
        trash.add(oldestMail);
    }

    public void deleteMail(String message){
        // Each message is distinct
        // If the given message is found in any mail in the inbox, move the mail to trash, else do nothing
        Iterator<Mail> iterator = inbox.iterator();
        while (iterator.hasNext()) {
            Mail mail = iterator.next();
            String messageInMail = mail.getMessage();
            if (messageInMail.equals(message)) {
                trash.add(mail);
                iterator.remove(); // Use iterator's remove method to avoid ConcurrentModificationException
            }
        }
    }

    public String findLatestMessage(){
        // If the inbox is empty, return null
        // Else, return the message of the latest mail present in the inbox
        if (inbox.isEmpty()) {
            return null;
        } else {
            Mail mail = inbox.get(inbox.size()-1);
            return mail.getMessage();
        }
    }

    public String findOldestMessage(){
        // If the inbox is empty, return null
        // Else, return the message of the oldest mail present in the inbox
        if (inbox.isEmpty()) {
            return null;
        } else {
            return inbox.get(0).getMessage();
        }
    }

    public int findMailsBetweenDates(Date start, Date end){
        //find number of mails in the inbox which are received between given dates
        //It is guaranteed that start date <= end date
        if (inbox.isEmpty()) {
            return 0;
        } else {
            int res = 0;
            for (Mail mail : inbox) {
                Date mailDate = mail.getDate();
                if (mailDate.compareTo(start) > 0 && mailDate.compareTo(end) < 0) {
                    res++;
                }
            }
            return res;
        }
    }

    public int getInboxSize(){
        // Return number of mails in inbox
        return inbox.size();
    }

    public int getTrashSize(){
        // Return number of mails in Trash
        return trash.size();
    }

    public void emptyTrash(){
        // clear all mails in the trash
        trash.clear();
    }

    public int getInboxCapacity() {
        // Return the maximum number of mails that can be stored in the inbox
        return inboxCapacity;
    }

    public static void main(String[] args) {
        Gmail gmail = new Gmail("Vahid@gmail.com", 3);
        Date date1 = createDate(2023, 10, 5); // 5th November 2023
        Date date2 = createDate(2023, 10, 10); // 10th November 2023
        Date date3 = createDate(2023, 10, 15); // 15th November 2023

        // Receive sample mails with specific dates
        gmail.receiveMail(date1, "Sender1", "Hello, this is message 1.");
        gmail.receiveMail(date2, "Sender2", "Hello, this is message 2.");
        gmail.receiveMail(date3, "Sender3", "Hello, this is message 3.");

        Date from = createDate(2023, 10, 6);
        Date to = createDate(2023, 10, 23);

        System.out.println(gmail.findMailsBetweenDates(from, to));
        System.out.println(gmail.getInboxSize());
        gmail.deleteMail("Hello, this is message 1.");
        System.out.println(gmail.getInboxSize());
    }

    private static Date createDate(int year, int month, int day) {
        // Month in Java Date class is 0-based, so we subtract 1 from the month
        return new Date(year - 1900, month - 1, day);
    }
}
