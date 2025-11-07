import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class User {
    int id;
    String username, password, userType;

    User(int id, String username, String password, String userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
}

class Doctor extends User {
    String specialization;

    Doctor(int id, String username, String password, String userType, String specialization) {
        super(id, username, password, userType);
        this.specialization = specialization;
    }
}

class Patient extends User {
    String name;

    Patient(int id, String username, String password, String userType, String name) {
        super(id, username, password, userType);
        this.name = name;
    }
}

class Receptionist extends User {
    Receptionist(int id, String username, String password, String userType) {
        super(id, username, password, userType);
    }
}

class Pharmacist extends User {
    Pharmacist(int id, String username, String password, String userType) {
        super(id, username, password, userType);
    }
}

class Channel {
    int id, patientId, doctorId;
    String date, status;

    Channel(int id, int patientId, int doctorId, String date, String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.status = status;
    }
}

class Item {
    int id;
    String name;
    int stock;

    Item(int id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }
}

class Bill {
    int id, patientId;
    double amount;
    boolean paid;

    Bill(int id, int patientId, double amount, boolean paid) {
        this.id = id;
        this.patientId = patientId;
        this.amount = amount;
        this.paid = paid;
    }
}

class Prescription {
    int id, patientId, channelId;
    String medication;

    Prescription(int id, int patientId, int channelId, String medication) {
        this.id = id;
        this.patientId = patientId;
        this.channelId = channelId;
        this.medication = medication;
    }
}

class Notification {
    int id, patientId;
    String message;

    Notification(int id, int patientId, String message) {
        this.id = id;
        this.patientId = patientId;
        this.message = message;
    }
}

public class HospitalManagementSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Channel> channels = new ArrayList<>();
    private static ArrayList<Item> items = new ArrayList<>();
    private static ArrayList<Bill> bills = new ArrayList<>();
    private static ArrayList<Prescription> prescriptions = new ArrayList<>();
    private static ArrayList<Notification> notifications = new ArrayList<>();
    private static int nextUserId = 1, nextChannelId = 1, nextItemId = 1, nextBillId = 1, nextPrescriptionId = 1, nextNotificationId = 1;

    public static void main(String[] args) {
        loadDataFromFiles();
        login();
        saveDataToFiles();
    }

    static void login() {
        System.out.println("\n--- Welcome to the Hospital Management System ---");
        System.out.print("Are you a returning user? (yes/no): ");
        String isReturning = scanner.nextLine().toLowerCase();

        System.out.println("Please select your user type:\n1. Receptionist\n2. Doctor\n3. Pharmacist\n4. Patient");
        System.out.print("Enter your role to access the system: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            String userType;
            switch (choice) {
                case 1: userType = "receptionist"; break;
                case 2: userType = "doctor"; break;
                case 3: userType = "pharmacist"; break;
                case 4: userType = "patient"; break;
                default: System.out.println("Sorry, that's an invalid choice. Please try again."); login(); return;
            }

            if (isReturning.equals("yes")) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                User user = authenticateUser(username, password, userType);
                if (user != null) {
                    System.out.println("Welcome back, " + user.username + "! You are logged in as a " + user.userType + ".");
                    showUserMenu(user);
                } else {
                    System.out.println("Sorry, invalid credentials. Please try again.");
                    login();
                }
            } else {
                User user = createNewUser(userType);
                if (user != null) {
                    System.out.println("Account successfully created! Welcome, " + user.username + "!");
                    showUserMenu(user);
                } else {
                    System.out.println("Sorry, account creation failed. Please try again.");
                    login();
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Sorry, please enter a valid number. Try again.");
            login();
        }
    }

    static User createNewUser(String userType) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (users.stream().anyMatch(u -> u.username.equals(username))) {
            System.out.println("Sorry, this username is already taken. Please choose another.");
            return null;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user;
        switch (userType) {
            case "receptionist":
                user = new Receptionist(nextUserId++, username, password, userType);
                break;
            case "doctor":
                System.out.print("Enter specialization: ");
                String spec = scanner.nextLine();
                user = new Doctor(nextUserId++, username, password, userType, spec);
                break;
            case "pharmacist":
                user = new Pharmacist(nextUserId++, username, password, userType);
                break;
            case "patient":
                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                user = new Patient(nextUserId++, username, password, userType, name);
                break;
            default:
                return null;
        }
        users.add(user);
        return user;
    }

    static User authenticateUser(String username, String password, String userType) {
        for (User u : users) {
            if (u.username.equals(username) && u.password.equals(password) && u.userType.equals(userType)) {
                switch (userType) {
                    case "receptionist": return new Receptionist(u.id, u.username, u.password, u.userType);
                    case "doctor":
                        for (User d : users) if (d.id == u.id && d instanceof Doctor) return new Doctor(u.id, u.username, u.password, u.userType, ((Doctor)d).specialization);
                        break;
                    case "pharmacist": return new Pharmacist(u.id, u.username, u.password, u.userType);
                    case "patient":
                        for (User p : users) if (p.id == u.id && p instanceof Patient) return new Patient(u.id, u.username, u.password, u.userType, ((Patient)p).name);
                        break;
                }
            }
        }
        return null;
    }

    static void showUserMenu(User user) {
        switch (user.userType) {
            case "receptionist": receptionistMenu((Receptionist) user); break;
            case "doctor": doctorMenu((Doctor) user); break;
            case "pharmacist": pharmacistMenu((Pharmacist) user); break;
            case "patient": patientMenu((Patient) user); break;
        }
    }

    static void receptionistMenu(Receptionist receptionist) {
        System.out.println("\n--- Receptionist Dashboard ---");
        System.out.println("1. Add New User\n2. Delete Existing User\n3. Delete Patient\n4. Generate Bill\n5. View All Appointments\n6. Logout");
        System.out.print("Enter your choice: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1: addNewUser(receptionist); break;
                case 2: deleteExistingUser(receptionist); break;
                case 3: deletePatient(receptionist); break;
                case 4: generateBill(receptionist); break;
                case 5: viewAllAppointments(receptionist); break;
                case 6: logout(); break;
                default: System.out.println("Sorry, that's an invalid choice. Please try again."); receptionistMenu(receptionist);
            }
        } catch (NumberFormatException e) {
            System.out.println("Sorry, please enter a valid number. Try again.");
            receptionistMenu(receptionist);
        }
    }

    static void doctorMenu(Doctor doctor) {
        System.out.println("\n--- Doctor Dashboard ---");
        System.out.println("1. View Channels\n2. Add Prescription\n3. View Patient History\n4. Update Channel Status\n5. Send Notification\n6. Logout");
        System.out.print("Enter your choice: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1: viewChannels(doctor); break;
                case 2: addPrescription(doctor); break;
                case 3: viewPatientHistory(doctor); break;
                case 4: updateChannelStatus(doctor); break;
                case 5: sendNotification(doctor); break;
                case 6: logout(); break;
                default: System.out.println("Sorry, that's an invalid choice. Please try again."); doctorMenu(doctor);
            }
        } catch (NumberFormatException e) {
            System.out.println("Sorry, please enter a valid number. Try again.");
            doctorMenu(doctor);
        }
    }

    static void pharmacistMenu(Pharmacist pharmacist) {
        System.out.println("\n--- Pharmacist Dashboard ---");
        System.out.println("1. Add Item\n2. View Items\n3. Dispense Medication\n4. Update Item Stock\n5. Check Prescription\n6. Logout");
        System.out.print("Enter your choice: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1: addItem(pharmacist); break;
                case 2: viewItems(pharmacist); break;
                case 3: dispenseMedication(pharmacist); break;
                case 4: updateItemStock(pharmacist); break;
                case 5: checkPrescription(pharmacist); break;
                case 6: logout(); break;
                default: System.out.println("Sorry, that's an invalid choice. Please try again."); pharmacistMenu(pharmacist);
            }
        } catch (NumberFormatException e) {
            System.out.println("Sorry, please enter a valid number. Try again.");
            pharmacistMenu(pharmacist);
        }
    }

    static void patientMenu(Patient patient) {
        System.out.println("\n--- Patient Dashboard ---");
        System.out.println("1. View Channels\n2. View Prescriptions\n3. Pay Bill\n4. Request Appointment\n5. View Notifications\n6. Logout");
        System.out.print("Enter your choice: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1: viewChannels(patient); break;
                case 2: viewPrescriptions(patient); break;
                case 3: payBill(patient); break;
                case 4: requestAppointment(patient); break;
                case 5: viewNotifications(patient); break;
                case 6: logout(); break;
                default: System.out.println("Sorry, that's an invalid choice. Please try again."); patientMenu(patient);
            }
        } catch (NumberFormatException e) {
            System.out.println("Sorry, please enter a valid number. Try again.");
            patientMenu(patient);
        }
    }

    static void addNewUser(Receptionist receptionist) {
        System.out.println("Select user type to add:\n1. Receptionist\n2. Doctor\n3. Pharmacist\n4. Patient");
        System.out.print("Enter your choice (1-4): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            String userType;
            switch (choice) {
                case 1: userType = "receptionist"; break;
                case 2: userType = "doctor"; break;
                case 3: userType = "pharmacist"; break;
                case 4: userType = "patient"; break;
                default: System.out.println("Sorry, that's an invalid choice. Please try again."); receptionistMenu(receptionist); return;
            }
            User user = createNewUser(userType);
            if (user != null) System.out.println("User successfully added!");
            receptionistMenu(receptionist);
        } catch (NumberFormatException e) {
            System.out.println("Sorry, please enter a valid number. Try again.");
            receptionistMenu(receptionist);
        }
    }

    static void deleteExistingUser(Receptionist receptionist) {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();
        User user = users.stream().filter(u -> u.username.equals(username)).findFirst().orElse(null);
        if (user == null) {
            System.out.println("Sorry, user not found. Please try again.");
        } else {
            System.out.print("Confirm deletion (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                users.remove(user);
                channels.removeIf(c -> c.patientId == user.id || c.doctorId == user.id);
                bills.removeIf(b -> b.patientId == user.id);
                prescriptions.removeIf(p -> p.patientId == user.id);
                notifications.removeIf(n -> n.patientId == user.id);
                System.out.println("User successfully deleted!");
            } else {
                System.out.println("Deletion canceled.");
            }
        }
        receptionistMenu(receptionist);
    }

    static void deletePatient(Receptionist receptionist) {
        System.out.print("Enter patient username to delete: ");
        String username = scanner.nextLine();
        User user = users.stream().filter(u -> u.username.equals(username) && u.userType.equals("patient")).findFirst().orElse(null);
        if (user == null) {
            System.out.println("Sorry, patient not found. Please try again.");
        } else {
            System.out.print("Confirm deletion (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                users.remove(user);
                channels.removeIf(c -> c.patientId == user.id);
                bills.removeIf(b -> b.patientId == user.id);
                prescriptions.removeIf(p -> p.patientId == user.id);
                notifications.removeIf(n -> n.patientId == user.id);
                System.out.println("Patient successfully deleted!");
            } else {
                System.out.println("Deletion canceled.");
            }
        }
        receptionistMenu(receptionist);
    }

    static void generateBill(Receptionist receptionist) {
        System.out.print("Enter patient username: ");
        String patientUsername = scanner.nextLine();
        User patient = users.stream().filter(u -> u.username.equals(patientUsername) && u.userType.equals("patient")).findFirst().orElse(null);
        if (patient == null) {
            System.out.println("Sorry, patient not found. Please try again.");
        } else {
            System.out.print("Enter bill amount: ");
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                if (amount > 0) {
                    bills.add(new Bill(nextBillId++, patient.id, amount, false));
                    System.out.println("Bill successfully generated!");
                } else {
                    System.out.println("Sorry, invalid amount. Please enter a positive value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Sorry, invalid amount format. Please try again.");
            }
        }
        receptionistMenu(receptionist);
    }

    static void viewAllAppointments(Receptionist receptionist) {
        boolean found = false;
        for (Channel c : channels) {
            User patient = users.stream().filter(u -> u.id == c.patientId).findFirst().orElse(null);
            User doctor = users.stream().filter(u -> u.id == c.doctorId).findFirst().orElse(null);
            System.out.println("Appointment ID: " + c.id + ", Patient: " + (patient != null ? patient.username : "Unknown") +
                    ", Doctor: " + (doctor != null ? doctor.username : "Unknown") + ", Date: " + c.date + ", Status: " + c.status);
            found = true;
        }
        if (!found) System.out.println("No appointments are currently available.");
        receptionistMenu(receptionist);
    }

    static void viewChannels(User user) {
        boolean found = false;
        for (Channel c : channels) {
            if ((user.userType.equals("doctor") && c.doctorId == user.id) || (user.userType.equals("patient") && c.patientId == user.id)) {
                User patient = users.stream().filter(u -> u.id == c.patientId).findFirst().orElse(null);
                User doctor = users.stream().filter(u -> u.id == c.doctorId).findFirst().orElse(null);
                System.out.println("Channel ID: " + c.id + ", Patient: " + (patient != null ? patient.username : "Unknown") +
                        ", Doctor: " + (doctor != null ? doctor.username : "Unknown") + ", Date: " + c.date + ", Status: " + c.status);
                found = true;
            }
        }
        if (!found) System.out.println("No channels are currently available.");
        showUserMenu(user);
    }

    static void addItem(Pharmacist pharmacist) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter initial stock: ");
        try {
            int stock = Integer.parseInt(scanner.nextLine());
            if (stock >= 0) {
                items.add(new Item(nextItemId++, name, stock));
                System.out.println("Item successfully added!");
            } else {
                System.out.println("Sorry, invalid stock quantity. Please enter a non-negative value.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Sorry, invalid stock format. Please try again.");
        }
        pharmacistMenu(pharmacist);
    }

    static void viewItems(Pharmacist pharmacist) {
        boolean found = false;
        for (Item i : items) {
            System.out.println("Item ID: " + i.id + ", Name: " + i.name + ", Stock: " + i.stock);
            found = true;
        }
        if (!found) System.out.println("No items are currently available.");
        pharmacistMenu(pharmacist);
    }

    static void dispenseMedication(Pharmacist pharmacist) {
        System.out.print("Enter prescription ID: ");
        try {
            int prescriptionId = Integer.parseInt(scanner.nextLine());
            Prescription prescription = prescriptions.stream().filter(p -> p.id == prescriptionId).findFirst().orElse(null);
            if (prescription == null) {
                System.out.println("Sorry, prescription not found. Please try again.");
            } else {
                System.out.print("Enter item name: ");
                String itemName = scanner.nextLine();
                Item item = items.stream().filter(i -> i.name.equalsIgnoreCase(itemName) && i.stock > 0).findFirst().orElse(null);
                if (item == null) {
                    System.out.println("Sorry, item not found or out of stock. Please try again.");
                } else {
                    item.stock--;
                    System.out.println("Medication successfully dispensed: " + itemName);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Sorry, invalid prescription ID. Please try again.");
        }
        pharmacistMenu(pharmacist);
    }

    static void updateItemStock(Pharmacist pharmacist) {
        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();
        Item item = items.stream().filter(i -> i.name.equalsIgnoreCase(itemName)).findFirst().orElse(null);
        if (item == null) {
            System.out.println("Sorry, item not found. Please try again.");
        } else {
            System.out.print("Enter new stock quantity: ");
            try {
                int stock = Integer.parseInt(scanner.nextLine());
                if (stock >= 0) {
                    item.stock = stock;
                    System.out.println("Stock successfully updated!");
                } else {
                    System.out.println("Sorry, invalid stock quantity. Please enter a non-negative value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Sorry, invalid stock format. Please try again.");
            }
        }
        pharmacistMenu(pharmacist);
    }

    static void checkPrescription(Pharmacist pharmacist) {
        System.out.print("Enter patient username: ");
        String patientUsername = scanner.nextLine();
        User patient = users.stream().filter(u -> u.username.equals(patientUsername) && u.userType.equals("patient")).findFirst().orElse(null);
        if (patient == null) {
            System.out.println("Sorry, patient not found. Please try again.");
        } else {
            boolean found = false;
            for (Prescription p : prescriptions) {
                if (p.patientId == patient.id) {
                    System.out.println("Prescription ID: " + p.id + ", Channel ID: " + p.channelId + ", Medication: " + p.medication);
                    found = true;
                }
            }
            if (!found) System.out.println("No prescriptions found for this patient.");
        }
        pharmacistMenu(pharmacist);
    }

    static void addPrescription(Doctor doctor) {
        System.out.print("Enter channel ID: ");
        try {
            int channelId = Integer.parseInt(scanner.nextLine());
            Channel channel = channels.stream().filter(c -> c.id == channelId && c.doctorId == doctor.id).findFirst().orElse(null);
            if (channel == null) {
                System.out.println("Sorry, channel not found or not assigned to you. Please try again.");
            } else {
                System.out.print("Enter medication: ");
                String medication = scanner.nextLine();
                prescriptions.add(new Prescription(nextPrescriptionId++, channel.patientId, channelId, medication));
                System.out.println("Prescription successfully added!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Sorry, invalid channel ID. Please try again.");
        }
        doctorMenu(doctor);
    }

    static void viewPatientHistory(Doctor doctor) {
        System.out.print("Enter patient username: ");
        String patientUsername = scanner.nextLine();
        User patient = users.stream().filter(u -> u.username.equals(patientUsername) && u.userType.equals("patient")).findFirst().orElse(null);
        if (patient == null) {
            System.out.println("Sorry, patient not found. Please try again.");
        } else {
            System.out.println("Patient Channels:");
            for (Channel c : channels) {
                if (c.patientId == patient.id && c.doctorId == doctor.id) {
                    System.out.println("Channel ID: " + c.id + ", Date: " + c.date + ", Status: " + c.status);
                }
            }
            System.out.println("Patient Prescriptions:");
            for (Prescription p : prescriptions) {
                if (p.patientId == patient.id) {
                    System.out.println("Prescription ID: " + p.id + ", Channel ID: " + p.channelId + ", Medication: " + p.medication);
                }
            }
        }
        doctorMenu(doctor);
    }

    static void updateChannelStatus(Doctor doctor) {
        System.out.print("Enter channel ID: ");
        try {
            int channelId = Integer.parseInt(scanner.nextLine());
            Channel channel = channels.stream().filter(c -> c.id == channelId && c.doctorId == doctor.id).findFirst().orElse(null);
            if (channel == null) {
                System.out.println("Sorry, channel not found or not assigned to you. Please try again.");
            } else {
                System.out.print("Enter new status (Scheduled/Completed/Rescheduled): ");
                String status = scanner.nextLine();
                if (status.equalsIgnoreCase("Scheduled") || status.equalsIgnoreCase("Completed") || status.equalsIgnoreCase("Rescheduled")) {
                    channel.status = status;
                    System.out.println("Channel status successfully updated!");
                } else {
                    System.out.println("Sorry, invalid status. Please use Scheduled, Completed, or Rescheduled.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Sorry, invalid channel ID. Please try again.");
        }
        doctorMenu(doctor);
    }

    static void sendNotification(Doctor doctor) {
        System.out.print("Enter patient username: ");
        String patientUsername = scanner.nextLine();
        User patient = users.stream().filter(u -> u.username.equals(patientUsername) && u.userType.equals("patient")).findFirst().orElse(null);
        if (patient == null) {
            System.out.println("Sorry, patient not found. Please try again.");
        } else {
            System.out.print("Enter message: ");
            String message = scanner.nextLine();
            notifications.add(new Notification(nextNotificationId++, patient.id, message));
            System.out.println("Notification successfully sent!");
        }
        doctorMenu(doctor);
    }

    static void viewPrescriptions(Patient patient) {
        boolean found = false;
        for (Prescription p : prescriptions) {
            if (p.patientId == patient.id) {
                System.out.println("Prescription ID: " + p.id + ", Channel ID: " + p.channelId + ", Medication: " + p.medication);
                found = true;
            }
        }
        if (!found) System.out.println("No prescriptions are currently available.");
        patientMenu(patient);
    }

    static void payBill(Patient patient) {
        boolean found = false;
        for (Bill b : bills) {
            if (b.patientId == patient.id && !b.paid) {
                System.out.println("Bill ID: " + b.id + ", Amount: $" + b.amount);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No unpaid bills found.");
        } else {
            System.out.print("Enter bill ID to pay: ");
            try {
                int billId = Integer.parseInt(scanner.nextLine());
                Bill bill = bills.stream().filter(b -> b.id == billId && b.patientId == patient.id).findFirst().orElse(null);
                if (bill != null) {
                    bill.paid = true;
                    System.out.println("Bill successfully paid!");
                } else {
                    System.out.println("Sorry, bill not found. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Sorry, invalid bill ID. Please try again.");
            }
        }
        patientMenu(patient);
    }

    static void requestAppointment(Patient patient) {
        System.out.print("Enter doctor username: ");
        String doctorUsername = scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        User doctor = users.stream().filter(u -> u.username.equals(doctorUsername) && u.userType.equals("doctor")).findFirst().orElse(null);
        if (doctor == null) {
            System.out.println("Sorry, doctor not found. Please try again.");
        } else {
            channels.add(new Channel(nextChannelId++, patient.id, doctor.id, date, "Scheduled"));
            System.out.println("Appointment successfully requested!");
        }
        patientMenu(patient);
    }

    static void viewNotifications(Patient patient) {
        boolean found = false;
        for (Notification n : notifications) {
            if (n.patientId == patient.id) {
                System.out.println("Notification ID: " + n.id + ", Message: " + n.message);
                found = true;
            }
        }
        if (!found) System.out.println("No notifications are currently available.");
        patientMenu(patient);
    }

    static void logout() {
        System.out.println("Successfully logged out. Thank you!");
        saveDataToFiles();
        login();
    }

    static void loadDataFromFiles() {
        try {
            File file = new File("users.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        int id = Integer.parseInt(parts[0]);
                        String userType = parts[3];
                        User user;
                        switch (userType) {
                            case "receptionist": user = new Receptionist(id, parts[1], parts[2], userType); break;
                            case "doctor": user = new Doctor(id, parts[1], parts[2], userType, parts.length > 4 ? parts[4] : ""); break;
                            case "pharmacist": user = new Pharmacist(id, parts[1], parts[2], userType); break;
                            case "patient": user = new Patient(id, parts[1], parts[2], userType, parts.length > 4 ? parts[4] : ""); break;
                            default: continue;
                        }
                        users.add(user);
                        nextUserId = Math.max(nextUserId, id + 1);
                    }
                }
                reader.close();
            }
            file = new File("channels.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        int id = Integer.parseInt(parts[0]);
                        channels.add(new Channel(id, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], parts[4]));
                        nextChannelId = Math.max(nextChannelId, id + 1);
                    }
                }
                reader.close();
            }
            file = new File("items.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        int id = Integer.parseInt(parts[0]);
                        items.add(new Item(id, parts[1], Integer.parseInt(parts[2])));
                        nextItemId = Math.max(nextItemId, id + 1);
                    }
                }
                reader.close();
            }
            file = new File("bills.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        int id = Integer.parseInt(parts[0]);
                        bills.add(new Bill(id, Integer.parseInt(parts[1]), Double.parseDouble(parts[2]), Boolean.parseBoolean(parts[3])));
                        nextBillId = Math.max(nextUserId, id + 1);
                    }
                }
                reader.close();
            }
            file = new File("prescriptions.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 4);
                    if (parts.length == 4) {
                        int id = Integer.parseInt(parts[0]);
                        prescriptions.add(new Prescription(id, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]));
                        nextPrescriptionId = Math.max(nextPrescriptionId, id + 1);
                    }
                }
                reader.close();
            }
            file = new File("notifications.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 3);
                    if (parts.length == 3) {
                        int id = Integer.parseInt(parts[0]);
                        notifications.add(new Notification(id, Integer.parseInt(parts[1]), parts[2]));
                        nextNotificationId = Math.max(nextNotificationId, id + 1);
                    }
                }
                reader.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    static void saveDataToFiles() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"));
            for (User u : users) {
                String line = u.id + "," + u.username + "," + u.password + "," + u.userType;
                if (u instanceof Doctor) line += "," + ((Doctor)u).specialization;
                else if (u instanceof Patient) line += "," + ((Patient)u).name;
                writer.write(line + "\n");
            }
            writer.close();
            writer = new BufferedWriter(new FileWriter("channels.txt"));
            for (Channel c : channels) {
                writer.write(c.id + "," + c.patientId + "," + c.doctorId + "," + c.date + "," + c.status + "\n");
            }
            writer.close();
            writer = new BufferedWriter(new FileWriter("items.txt"));
            for (Item i : items) {
                writer.write(i.id + "," + i.name + "," + i.stock + "\n");
            }
            writer.close();
            writer = new BufferedWriter(new FileWriter("bills.txt"));
            for (Bill b : bills) {
                writer.write(b.id + "," + b.patientId + "," + b.amount + "," + b.paid + "\n");
            }
            writer.close();
            writer = new BufferedWriter(new FileWriter("prescriptions.txt"));
            for (Prescription p : prescriptions) {
                writer.write(p.id + "," + p.patientId + "," + p.channelId + "," + p.medication + "\n");
            }
            writer.close();
            writer = new BufferedWriter(new FileWriter("notifications.txt"));
            for (Notification n : notifications) {
                writer.write(n.id + "," + n.patientId + "," + n.message + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}