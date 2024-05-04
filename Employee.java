package application;

public class Employee {

	// all parameters for Employee class :-
		private String employeeID;
		private String employeeName;
		private String email;
		private String employeePosition;
		private double salary;
		private char gender;

		// Parameterised constructor:-
		public Employee(String employeeID, String employeeName, String email, String EmployeePosition, double salary,
				char gender) {
			super();
			setEmployeeID(employeeID);
			setEmployeeName(employeeName);
			setEmail(email);
			setEmployeePosition(EmployeePosition);
			setSalary(salary);
			setGender(gender);
		}

		// Getters and setters :-

		public String getEmployeeID() {
			return employeeID;
		}

		public void setEmployeeID(String employeeID) {
			this.employeeID = employeeID;
		}

		public String getEmployeeName() {
			return employeeName;
		}

		public void setEmployeeName(String employeeName) {
			this.employeeName = employeeName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getEmployeePosition() {
			return employeePosition;
		}

		public void setEmployeePosition(String employeePosition) {
			this.employeePosition = employeePosition;
		}

		public double getSalary() {
			return salary;
		}

		public void setSalary(double salary) {
			this.salary = salary;
		}

		public char getGender() {
			return gender;
		}

		public void setGender(char gender) {
			if (gender == 'F' || gender == 'M' || gender == ' ')
				this.gender = gender;
			else
				throw new IllegalArgumentException("Gender can oly be a 'F' for a female or 'M' for a male");
		}

		@Override
		public String toString() {
			return "Employee [employeeID=" + employeeID + ", employeeName=" + employeeName + ", email=" + email
					+ ", employeePosition=" + employeePosition + ", salary=" + salary + ", gender=" + gender + "]";
		}
	}