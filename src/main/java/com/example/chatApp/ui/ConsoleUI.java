package com.example.chatApp.ui;

import com.example.chatApp.domain.Friendship;
import com.example.chatApp.domain.User;
import com.example.chatApp.domain.validators.DuplicateException;
import com.example.chatApp.domain.validators.NonExistentEntityException;
import com.example.chatApp.domain.validators.ValidationException;
import com.example.chatApp.service.Service;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUI {
    private final Service service;
    public ConsoleUI(Service service) {
        this.service = service;
    }
    public void main() {
        while(true) {
            try {
                showMenu();
                switch (getOptionFromUser()) {
                    case "1":
                        addUserUI();
                        break;
                    case "2":
                        addFriendshipUI();
                        break;
                    case "3":
                        removeUserUI();
                        break;
                    case "4":
                        removeFriendshipUI();
                        break;
                    case "5":
                        communityNumberUI();
                        break;
                    case "6":
                        biggestCommunityUI();
                        break;
                    case "7":
                        showUsersUI();
                        break;
                    case "8":
                        showFriendshipsUI();
                        break;
                    case "9":
                        updateUserUI();
                        break;
                    default:
                        System.out.println("Wrong option. Please try again.");
                }
            }
            catch(RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void updateUserUI() {
        Scanner in = new Scanner(System.in);
        System.out.print("Id user: ");
        String id = in.nextLine();
        System.out.print("New first Name: ");
        String updatedFirstName = in.nextLine();
        System.out.print("New last Name: ");
        String updatedLastName = in.nextLine();

        service.updateUser(Long.valueOf(id), updatedFirstName, updatedLastName);
    }
    private void showUsersUI() {
        Iterable<User> users = service.getUsers();
        for (User user : users)
            System.out.println(user);
    }
    private void addFriendshipUI() {
        Scanner in = new Scanner(System.in);
        System.out.print("ID Friend 1: ");
        Long id1 = in.nextLong();
        System.out.print("ID Friend 2: ");
        Long id2 = in.nextLong();
        service.addFriendship(id1, id2);
    }

    private void removeFriendshipUI() {
        Scanner in = new Scanner(System.in);
        System.out.print("ID Friend 1: ");
        Long id1 = in.nextLong();
        System.out.print("ID Friend 2: ");
        Long id2 = in.nextLong();
        service.removeFriendship(id1, id2);
    }

    private void communityNumberUI() {
        System.out.print("Numarul de comunitati este: ");
        int NumberOfConnectedComponents = service.getNumberOfConnectedComponents();
        System.out.println(NumberOfConnectedComponents);
    }

    private void removeUserUI() {
        Scanner in = new Scanner(System.in);
        System.out.print("User ID: ");
        Long id = in.nextLong();
        in.nextLine();
        service.removeUser(id);
    }

    private void addUserUI() {
        Scanner in = new Scanner(System.in);
        System.out.print("First Name: ");
        String firstName = in.nextLine();
        System.out.print("Last Name: ");
        String lastName = in.nextLine();
        System.out.print("Email: ");
        String email = in.nextLine();
        System.out.print("Password: ");
        String password = in.nextLine();
        service.addUser(firstName, lastName, email, password);
    }

    private void showMenu() {
        System.out.println("1. Add an user");
        System.out.println("2. Add a friendship");
        System.out.println("3. Remove an user");
        System.out.println("4. Remove a friendship");
        System.out.println("5. Show the number of communities");
        System.out.println("6. Show the biggest community");
        System.out.println("7. Show users");
        System.out.println("8. Show friendships");
        System.out.println("9. Update an user");
    }

    private void biggestCommunityUI() {
        System.out.println("Cea mai mare comunitate este: ");
        Iterable<User> users = service.getBiggestCommunity();
        for(User user: users) {
            System.out.println(user);
        }
    }

    private String getOptionFromUser() {
        Scanner in = new Scanner(System.in);
        String option = in.nextLine();
        return option;
    }

    private void showFriendshipsUI() {
        for (Friendship friendship : service.getFriendships())
            System.out.println(friendship);
    }
}
