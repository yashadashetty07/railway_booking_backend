
 🚆 Train Booking System – Java Console Application

A complete **Train Booking System** built in Java using file-based storage (JSON). This project allows users to sign up, log in, search trains, book seats, cancel bookings, and fetch their booking history — all through a console-based interface.


 💡 Features

- 🔐 **User Authentication**
  - Sign-up with hashed password (custom utility)
  - Secure login with validation

- 🚆 **Train Search**
  - Search trains by **source** and **destination** stations

- 🎟️ **Seat Booking**
  - Select and book seats in a 2D layout (10×4 seating matrix)

- 📂 **Booking Management**
  - View previously booked tickets
  - Cancel ticket functionality included

- 💾 **Data Persistence**
  - JSON file-based storage:
    - `users.json` for users and their bookings
    - `trains.json` for train and seat availability


 🛠️ Technologies Used

-------------------------------------------------------------
| Tech             | Description                            |
|------------------|----------------------------------------|
| Java             | Core programming language              |
| OOP              | Object-Oriented Programming            |
| Jackson          | JSON parsing and serialization         |
| Console I/O      | User input/output via CLI              |
| JSON             | Lightweight data storage format        |
-------------------------------------------------------------

 📁 Project Structure

ticket.booking/
├── App.java
├── entities/
│   ├── User.java
│   ├── Train.java
│   └── Ticket.java
├── services/
│   ├── UserBookingService.java
│   └── TrainService.java
├── util/
│   └── UserServiceUtil.java
localDB/
├── users.json
└── trains.json


 ▶️ How to Run

1. Clone the repository:
   git clone https://github.com/yashadashetty07/railway_booking_backend

2. Open the project in IntelliJ or any Java-supported IDE

3. Run `App.java` to start the application


 🚀 Future Enhancements

- Admin panel for train management
- GUI version using JavaFX or Swing
- Switch to SQLite or MySQL for data persistence
- Email notifications for booking confirmation

 📧 Contact

Developed by Yash Adashetty
(www.linkedin.com/in/yashadashetty07)  
Let’s connect and collaborate!
