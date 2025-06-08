# Course Utilization Management System

This Java Swing application allows users to manage course utilization data using a local SQLite database.

## 📌 Features

- Add new course utilization records
- Update existing records
- Delete records
- Retrieve records by Course ID
- Display data in a user-friendly table

## 🧰 Technologies Used

- Java (Swing for GUI)
- SQLite (JDBC)
- JTable for displaying records

## 🗂 Database Structure

Table Name: `course_utilization`

| Column Name         | Data Type |
|---------------------|-----------|
| ID (Primary Key)    | INTEGER   |
| cour_id             | TEXT      |
| cour_util_code      | TEXT      |
| unit_no             | INTEGER   |
| unit_name           | TEXT      |
| require_contact_hr  | INTEGER   |
| cour_out_id         | TEXT      |
| ref_id              | TEXT      |

## 📍 How to Run

1. Make sure you have Java installed.
2. Open the project in your IDE.
3. Ensure the SQLite JDBC driver is available.
4. Update the `DATABASE_URL` in the code if needed.
5. Run the `Coders_courseutilization` class.

## 📝 Notes

- The database file is located at:  
  `C:/Users/dell/OneDrive/Desktop/APPS/javaapp.db`
- You can change the file path as per your system setup.

---

✅ Built with simplicity for course management.  
📧 For any queries, feel free to contact the developer.
