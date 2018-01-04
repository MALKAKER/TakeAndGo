package com.javaproject.malki.takeandgo.model.entities;

/**
 * the class represents a clients in the  leasing company
 * Created by malki on 04-Nov-17.
 */

public class Client
{
    private String firstName;
    private String lastName;
    private String clientID;
    private long phoneNumber;
    private String email;
    private long creditCard;
    private String userName;
    private String password;
    //maybe to edit nickname to client
    //constructor
    public Client(String firstName, String lastName, String clientID, long phoneNumber, String email,
                  long creditCard, String userName, String password) throws Exception {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setClientID(clientID);
        this.setPhoneNumber(phoneNumber);
        this.setEmail(email);
        this.setCreditCard(creditCard);
        this.setPassword(password);
        this.setUserName(userName);
    }

    //copy constructor
    public Client(Client newClient) throws Exception {
        this.setFirstName(newClient.firstName);
        this.setLastName(newClient.lastName);
        this.setClientID(newClient.clientID);
        this.setPhoneNumber(newClient.phoneNumber);
        this.setEmail(newClient.email);
        this.setCreditCard(newClient.creditCard);
        this.setPassword(newClient.getPassword());
        this.setUserName(newClient.getUserName());
    }

    public Client() {

    }

    //empty client is not allowed
    //get and set
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) throws Exception {
        if (clientID.length() < 9)
        {
            throw new Exception("ERROR: Invalid ID number!\n");
        }
        this.clientID = clientID;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) throws Exception {
        //without the figure zero in the beggining of the number
        if (phoneNumber < 100000000)
        {
            throw new Exception("ERROR: Invalid phone number!\n");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if (!email.contains("@") && !email.endsWith(".com"))
        {
            throw new Exception("ERROR: Invalid e-mail address!\n");
        }
        this.email = email;
    }

    public long getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(long creditCard) {
        this.creditCard = creditCard;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) //TODO need to check if it is a strong password
    {
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return String.format("Name: %s %s\nID: %s\nPhone: %020d\nMail: %s\nUser: %s",
                firstName, lastName, clientID, phoneNumber, email, userName );
    }
}
