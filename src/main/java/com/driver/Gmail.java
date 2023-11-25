package com.driver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Gmail extends Email {

    private int inboxCapacity;
    //maximum number of mails inbox can store
    private List<Mail> inbox;
    //Inbox: Stores mails. Each mail has date (Date), sender (String), message (String). It is guaranteed that message is distinct for all mails.
    private List<Mail> trash;
    private int mailInInboxCount;
    //Trash: Stores mails. Each mail has date (Date), sender (String), message (String)
    public Gmail(String emailId, int inboxCapacity) {
        super(emailId);
        this.mailInInboxCount = 0;
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
        if (mailInInboxCount >= inboxCapacity) {
            moveOldestMailToTrash();
            mailInInboxCount--;
        }
        inbox.add(mail);
        mailInInboxCount++;
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
                iterator.remove();
                mailInInboxCount--;
            }
        }
    }

    public String findLatestMessage(){
        // If the inbox is empty, return null
        // Else, return the message of the latest mail present in the inbox
        if (inbox.isEmpty()) {
            return null;
        } else {
            Mail mail = inbox.get(inbox.size() - 1);
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
                if (mailDate.compareTo(start) >= 0 && mailDate.compareTo(end) <= 0) {
                    System.out.println("Mail Date: " + mailDate + ", Start Date: " + start + ", End Date: " + end);
                    res++;
                }
            }
            return res;
        }
    }

    public int getInboxSize(){
        // Return number of mails in inbox
        return mailInInboxCount;
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
}
