# 🏥 Hospital Management System

A console-based Java application that simulates a real-world hospital environment with role-based access for Receptionists, Doctors, Pharmacists, and Patients.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [User Roles](#user-roles)
- [Project Structure](#project-structure)
- [Data Storage](#data-storage)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Class Design](#class-design)
- [Technologies Used](#technologies-used)

---

## Overview

The Hospital Management System (HMS) is a fully functional, menu-driven Java application built using Object-Oriented Programming principles. It supports multiple user roles, persistent file-based data storage, appointment scheduling, prescription management, billing, and pharmacy inventory control.

---

## Features

- 🔐 Secure login and registration for all user types
- 📅 Appointment scheduling and status tracking
- 💊 Prescription creation and management
- 🧾 Bill generation and payment
- 🏪 Pharmacy inventory management (add, update, dispense)
- 🔔 Doctor-to-patient notification system
- 💾 Persistent data storage using `.txt` files
- 🗑️ User and patient deletion with cascading data cleanup

---

## User Roles

### 🧑‍💼 Receptionist
| Action | Description |
|--------|-------------|
| Add New User | Register a new Receptionist, Doctor, Pharmacist, or Patient |
| Delete User | Remove any user and their associated data |
| Delete Patient | Remove a specific patient and their records |
| Generate Bill | Create a bill for a patient |
| View All Appointments | See all scheduled appointments across the system |

### 🩺 Doctor
| Action | Description |
|--------|-------------|
| View Channels | See all appointments assigned to the doctor |
| Add Prescription | Write a prescription for a patient via channel ID |
| View Patient History | Review a patient's past channels and prescriptions |
| Update Channel Status | Change status to Scheduled / Completed / Rescheduled |
| Send Notification | Send a message directly to a patient |

### 💊 Pharmacist
| Action | Description |
|--------|-------------|
| Add Item | Add a new medication/item to inventory |
| View Items | List all items with current stock levels |
| Dispense Medication | Dispense medicine based on a valid prescription |
| Update Item Stock | Modify the stock quantity of an existing item |
| Check Prescription | View all prescriptions for a specific patient |

### 🧑‍⚕️ Patient
| Action | Description |
|--------|-------------|
| View Channels | See all personal appointments |
| View Prescriptions | View all prescriptions issued |
| Pay Bill | View and pay outstanding bills |
| Request Appointment | Book an appointment with a doctor |
| View Notifications | Read messages sent by doctors |

---

## Project Structure

```
HospitalManagementSystem/
│
├── HospitalManagementSystem.java   # Main application source file
├── README.md                       # Project documentation
│
# Generated at runtime:
├── users.txt                       # Stored user accounts
├── channels.txt                    # Stored appointments
├── items.txt                       # Pharmacy inventory
├── bills.txt                       # Patient bills
├── prescriptions.txt               # Doctor prescriptions
└── notifications.txt               # Patient notifications
```

---

## Data Storage

All data is persisted in plain-text `.txt` files using comma-separated values (CSV format). Files are loaded on startup and saved on logout or exit.

| File | Format |
|------|--------|
| `users.txt` | `id,username,password,userType[,extra]` |
| `channels.txt` | `id,patientId,doctorId,date,status` |
| `items.txt` | `id,name,stock` |
| `bills.txt` | `id,patientId,amount,paid` |
| `prescriptions.txt` | `id,patientId,channelId,medication` |
| `notifications.txt` | `id,patientId,message` |

---

## Getting Started

### Prerequisites

- Java JDK 8 or higher installed
- A terminal or command prompt

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/<your-username>/HospitalManagementSystem.git

# 2. Navigate to the project folder
cd HospitalManagementSystem

# 3. Compile the Java file
javac HospitalManagementSystem.java

# 4. Run the application
java HospitalManagementSystem
```

---

## Usage

On launch, the system will prompt you to:

1. Choose whether you are a **returning user** or a **new user**
2. Select your **role** (Receptionist, Doctor, Pharmacist, or Patient)
3. Login with credentials or register a new account
4. Navigate your **role-specific dashboard**

**Example flow for a Patient:**
```
Are you a returning user? (yes/no): no
Select role: 4 (Patient)
Enter username: john_doe
Enter password: pass123
Enter name: John Doe
→ Patient Dashboard appears
→ Select: 4. Request Appointment
→ Enter doctor username and date
→ Appointment booked!
```

---

## Class Design

```
User (base class)
├── Receptionist
├── Doctor          (+ specialization)
├── Pharmacist
└── Patient         (+ name)

Supporting Classes:
├── Channel         (appointment record)
├── Item            (pharmacy inventory)
├── Bill            (patient billing)
├── Prescription    (doctor prescription)
└── Notification    (doctor-to-patient message)
```

---

## Technologies Used

- **Language:** Java (JDK 8+)
- **Paradigm:** Object-Oriented Programming (OOP)
- **Storage:** File I/O (`BufferedReader`, `BufferedWriter`)
- **Collections:** `ArrayList`
- **Input:** `Scanner` (console-based)

---


> Built as a Java OOP learning project demonstrating inheritance, file persistence, and role-based system design.
