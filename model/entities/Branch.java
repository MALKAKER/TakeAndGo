package com.javaproject.malki.takeandgo.model.entities;

/**
 * this class represents the Take&Go's branch
 * Created by malki on 04-Nov-17.
 */

public class Branch
{
    private Address branchAddress;
    private int parkingSpace;
    private long branchNumber;

    //constructor
//    public Branch(Address branchAddress, int parkingSpace, long branchNumber) throws Exception {
//        this.setBranchAddress(branchAddress);
//        this.setParkingSpace(parkingSpace);
//        this.setBranchNumber(branchNumber);
//    }
//
//    //copy constructor
//    public Branch(Branch newBranch) throws Exception {
//        this.setBranchAddress(newBranch.branchAddress);
//        this.setParkingSpace(newBranch.parkingSpace);
//        this.setBranchNumber(newBranch.branchNumber);
//    }
//
//    public Branch() {
//
//    }

    public Address getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(Address branchAddress) throws Exception {
        this.branchAddress = new Address(branchAddress);
    }

    public int getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(int parkingSpace)throws Exception {
        if (parkingSpace < 1)
        {
            throw new Exception("invalid value!");
        }
        this.parkingSpace = parkingSpace;
    }

    public long getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(long branchNumber) {
        this.branchNumber = branchNumber;
    }
    //get and set

    @Override
    public String toString()
    {
        String s = String.format("Number: %s\nAddress: %s\nAvailable Parking Space: %d",branchNumber,branchAddress.toString(), parkingSpace );
        return s;
    }
}
