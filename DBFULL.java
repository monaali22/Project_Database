package application;

import static javafx.stage.Modality.NONE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.CharacterStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class DBFULL extends Application {

	private ArrayList<Customer> customerData = new ArrayList<>();
	private ObservableList<Customer> customerDataList;

	private ArrayList<Category> categoryData = new ArrayList<>();
	private ObservableList<Category> categoryDataList; 

	private ArrayList<Customer_PhoneNumber> customerPhoneData = new ArrayList<>();
	private ObservableList<Customer_PhoneNumber> customerPhoneDataList;

	private ArrayList<Category_PopularBrands> categoryBrandData = new ArrayList<>();
	private ObservableList<Category_PopularBrands> categoryBrandDataList;

	private ArrayList<Category_TargetAudience> categoryAudienceData = new ArrayList<>();
	private ObservableList<Category_TargetAudience> categoryAudienceDataList;

	private ArrayList<OrderProducts> orderProductData = new ArrayList<>();
	private ObservableList<OrderProducts> orderProductDataList;

	private ArrayList<Employee> employeeData = new ArrayList<>();
	private ObservableList<Employee> employeeDataList;

	private ArrayList<Employee_PhoneNumber> employeePhoneData = new ArrayList<>();
	private ObservableList<Employee_PhoneNumber> employeePhoneDataList;

	private ArrayList<Order> orderData = new ArrayList<>();
	private ObservableList<Order> orderDataList;
	
	private ArrayList<EmployeeOrder> employeeOrderData = new ArrayList<>();
	private ObservableList<EmployeeOrder> employeeOrderDataList;
	

	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "mona2632003MA";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "DB";
	private Connection con;

	private Button back = new Button("Go Back");
	private Font btnFont = Font.font("Times New Roman", FontWeight.NORMAL, 12.5);

	private TabPane tabs = new TabPane();
	Tab tabCustomer = new Tab("Customers");
	Tab tabCategory = new Tab("Categories");
	Tab tabEmployee = new Tab("Employees");
	Tab tabOrder = new Tab("Orders");
	Tab tabProduct = new Tab("Products");
	Tab tabSupplier = new Tab("Suppliers");
	Tab tabOrderProduct = new Tab("Order_Products");
	Tab tabEmployeeOrder = new Tab("Employee_Order");


	public static void main(String[] args) {

		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {


		try {
			BorderPane pane = new BorderPane();
			pane.setStyle("-fx-background-color: white;");
			pane.setLeft(tabs);
			Scene scene = new Scene(pane);
			stage.setScene(scene);

			tabs.setSide(Side.LEFT);
			tabs.getTabs().addAll(tabCustomer, tabEmployee, tabProduct, tabCategory, tabSupplier, tabOrder,
					tabOrderProduct , tabEmployeeOrder);

			getCustomerData();
			getCategoryData();
			getOrderProductData();
			getEmployeeData();
			getOrderData();
			getEmployeeOrderData();

			// convert data from arraylist to observable arraylist
			customerDataList = FXCollections.observableArrayList(customerData);
			categoryDataList = FXCollections.observableArrayList(categoryData);
			customerPhoneDataList = FXCollections.observableArrayList(customerPhoneData);
			categoryBrandDataList = FXCollections.observableArrayList(categoryBrandData);
			categoryAudienceDataList = FXCollections.observableArrayList(categoryAudienceData);
			orderProductDataList = FXCollections.observableArrayList(orderProductData);
			employeeDataList = FXCollections.observableArrayList(employeeData);
			employeePhoneDataList = FXCollections.observableArrayList(employeePhoneData);
			orderDataList = FXCollections.observableArrayList(orderData);
			employeeOrderDataList = FXCollections.observableArrayList(employeeOrderData);

			BorderPane customerScreen = customerScreen(stage);
			BorderPane categoryScreen = categoryScreen(stage);
			BorderPane order_productScreen = order_productScreen(stage);
			BorderPane employeeScreen = employeeScreen(stage);
			BorderPane orderScreen = orderScreen(stage);
			BorderPane employee_orderScreen = employee_orderScreen(stage);


			tabCustomer.setContent(customerScreen);
			tabCategory.setContent(categoryScreen);
			tabOrderProduct.setContent(order_productScreen);
			tabEmployee.setContent(employeeScreen);
			tabOrder.setContent(orderScreen);
			tabEmployeeOrder.setContent(employee_orderScreen);

			stage.show();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")

	// Customer methods
	private BorderPane customerScreen(Stage stage) {

		// the customer table view with its columns
		TableView<Customer> dataTable = new TableView<Customer>();
		dataTable.setPadding(new Insets(10));
		TableColumn<Customer, String> idCol = new TableColumn<Customer, String>("customerID");
		TableColumn<Customer, String> nameCol = new TableColumn<Customer, String>("customerName");
		TableColumn<Customer, String> emailCol = new TableColumn<Customer, String>("email");
		TableColumn<Customer, String> addressCol = new TableColumn<Customer, String>("customerAddress");

		// the customer_phones table with its columns
		TableView<Customer_PhoneNumber> phonesTable = new TableView<>();
		phonesTable.setPadding(new Insets(10));
		TableColumn<Customer_PhoneNumber, String> id2Col = new TableColumn<Customer_PhoneNumber, String>("CustomerID");
		TableColumn<Customer_PhoneNumber, String> phoneCol = new TableColumn<Customer_PhoneNumber, String>(
				"phoneNumber");

		dataTable.setEditable(true);
		dataTable.setMinSize(500, 500);
		dataTable.setMaxSize(1800, 550);

		phonesTable.setEditable(false);
		phonesTable.setMinSize(300, 500);
		phonesTable.setMaxSize(500, 550);

		HBox tablesBox = new HBox();
		tablesBox.setPadding(new Insets(10));
		tablesBox.getChildren().addAll(dataTable, phonesTable);

		// name of column for display
		idCol.setMinWidth(50);

		// to get the data from specific column
		idCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerID"));

		nameCol.setMinWidth(100);
		nameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));

		nameCol.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

		nameCol.setOnEditCommit((CellEditEvent<Customer, String> t) -> {
			try {
				((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setCustomerName(t.getNewValue()); // display only
				updateCustomerName(t.getRowValue().getCustomerID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		emailCol.setMinWidth(150);
		emailCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));

		emailCol.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

		emailCol.setOnEditCommit((CellEditEvent<Customer, String> t) -> {
			try {
				((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());
				updateCustomerEmail(t.getRowValue().getCustomerID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		id2Col.setMinWidth(50);

		id2Col.setCellValueFactory(new PropertyValueFactory<Customer_PhoneNumber, String>("customerID"));

		phoneCol.setMinWidth(130);
		phoneCol.setCellValueFactory(new PropertyValueFactory<Customer_PhoneNumber, String>("phoneNumber"));

//		phoneCol.setCellFactory(TextFieldTableCell.<Customer_PhoneNumber>forTableColumn());

		addressCol.setMinWidth(200);
		addressCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerAddress"));

		addressCol.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
		addressCol.setOnEditCommit((CellEditEvent<Customer, String> t) -> {
			try {
				((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setCustomerAddress(t.getNewValue());
				updateCustomerAddress(t.getRowValue().getCustomerID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		dataTable.getColumns().addAll(idCol, nameCol, emailCol, addressCol);
		phonesTable.getColumns().addAll(id2Col, phoneCol);

		// text fields for inserting new records
		final TextField addID = new TextField();
		final TextField addName = new TextField();
		final TextField addEmail = new TextField();
		final TextField addAddress = new TextField();
		final TextField addPhone = new TextField();

		// labels for the text fields
		Label lblID = new Label("Customer ID: ");
		Label lblName = new Label("Name: ");
		Label lblAddress = new Label("Address: ");
		Label lblEmail = new Label("Email: ");
		Label lblPhone = new Label("Phone Number: ");

		// create radioButtons (for opting a choice)
		Label buttonsLbl = new Label("What do you want to do?");
		RadioButton btnInsert = new RadioButton("Insert customer record   ");
		RadioButton btnInsertPhone = new RadioButton("Insert phone number");
		RadioButton btnDelete = new RadioButton("Delete selected customer record ");
		RadioButton btnDeletePhone = new RadioButton("Delete selected customer phone record ");
		RadioButton btnClear = new RadioButton("Clear all customers");
		RadioButton btnClear2 = new RadioButton("Clear all customers' phone numbers");
		RadioButton btnRefresh = new RadioButton("Refresh tables");
		RadioButton btnViewAll = new RadioButton("Show all data");
		Label lblSearch = new Label("Search by:");
		RadioButton btnSearchID = new RadioButton("Customer ID");
		RadioButton btnSearchName = new RadioButton("Customer name");
		ToggleGroup group = new ToggleGroup();

		btnInsert.setToggleGroup(group);
		btnInsertPhone.setToggleGroup(group);
		btnDelete.setToggleGroup(group);
		btnDeletePhone.setToggleGroup(group);
		btnClear.setToggleGroup(group);
		btnClear2.setToggleGroup(group);
		btnRefresh.setToggleGroup(group);
		btnViewAll.setToggleGroup(group);
		btnSearchID.setToggleGroup(group);
		btnSearchName.setToggleGroup(group);

		// this box hold the toggle buttons in
		VBox optionsBox = new VBox(5);

		Button btnAdd = new Button("Add");
		Button btnCancel = new Button("Cancel");
		Button btnFind = new Button("Find");
		btnAdd.setFont(btnFont);
		btnCancel.setFont(btnFont);
		btnFind.setFont(btnFont);

		// this box holds the insertion text fields with their labels
		HBox insertionBox = new HBox(5);
		insertionBox.getChildren().addAll(lblID, addID, lblName, addName, lblEmail, addEmail, lblAddress, addAddress,
				lblPhone, addPhone, btnAdd, btnCancel, btnFind);

		// root pane
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: white;");
		stage.setFullScreen(true);

		// set title
		Label label = new Label("Customers' Management Screen");
		label.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 30));
		label.setTextFill(Color.BLUE);
		label.setTextAlignment(TextAlignment.CENTER);

		HBox titleBox = new HBox();
		titleBox.getChildren().add(label);
		titleBox.setAlignment(Pos.CENTER);
		root.setTop(titleBox);
		titleBox.setPadding(new Insets(30));

		root.setPadding(new Insets(20));

//		Object[] arr = dataTable.getItems().toArray(); 
//		for(int i = 0; i < arr.length; i++)
//			if(((Customer)arr[i]).getCustomerID().equals("13a"))
//				dataTable

		addID.setPromptText("Customer ID");
		addID.setMaxWidth(idCol.getPrefWidth());

		addName.setMaxWidth(nameCol.getPrefWidth());
		addName.setPromptText("Name");

		addEmail.setMaxWidth(emailCol.getPrefWidth());
		addEmail.setPromptText("Email");

		addPhone.setMaxWidth(phoneCol.getPrefWidth());
		addPhone.setPromptText("Phone Number");

		addAddress.setMaxWidth(addressCol.getPrefWidth());
		addAddress.setPromptText("Address");

		addID.setDisable(true);
		addName.setDisable(true);
		addEmail.setDisable(true);
		addPhone.setDisable(true);
		addAddress.setDisable(true);
		btnAdd.setDisable(true);
		btnCancel.setDisable(true);
		btnFind.setDisable(true);

		btnInsert.setFont(btnFont);
		btnInsertPhone.setFont(btnFont);
		btnDelete.setFont(btnFont);
		btnDeletePhone.setFont(btnFont);
		btnClear.setFont(btnFont);
		btnClear2.setFont(btnFont);
		btnRefresh.setFont(btnFont);
		btnViewAll.setFont(btnFont);
		lblSearch.setFont(btnFont);
		btnSearchID.setFont(btnFont);
		btnSearchName.setFont(btnFont);

		buttonsLbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

		optionsBox.setPadding(new Insets(35));

		GridPane pane = new GridPane();
		pane.add(buttonsLbl, 0, 0);
		pane.add(btnInsert, 0, 1);
		pane.add(btnInsertPhone, 0, 2);
		pane.add(btnDelete, 0, 3);
		pane.add(btnDeletePhone, 0, 4);
		pane.add(btnRefresh, 0, 5);
		pane.add(btnViewAll, 0, 6);
		pane.add(btnClear, 0, 7);
		pane.add(btnClear2, 0, 8);
		pane.add(lblSearch, 0, 9);
		pane.add(btnSearchID, 0, 10);
		pane.add(btnSearchName, 0, 11);
		pane.setVgap(10);
		pane.setPadding(new Insets(20));

		optionsBox.getChildren().add(pane);

		// this box hold the data table with the insertion box
		VBox tableBox = new VBox();
		tableBox.getChildren().addAll(tablesBox, insertionBox);

		HBox box = new HBox(5);
		box.getChildren().addAll(tableBox, optionsBox);
		box.setAlignment(Pos.CENTER);
		root.setCenter(box);

		lblID.setFont(btnFont);
		lblName.setFont(btnFont);
		lblAddress.setFont(btnFont);
		lblEmail.setFont(btnFont);
		lblPhone.setFont(btnFont);

		btnDelete.setOnAction((ActionEvent e) -> {
			ObservableList<Customer> selectedCustomerRows = dataTable.getSelectionModel().getSelectedItems();
			if (!selectedCustomerRows.isEmpty()) {
				ArrayList<Customer> rows = new ArrayList<>(selectedCustomerRows);
				confirmCustomerDeletion(stage, NONE, rows, dataTable);
			}
		});

		btnDeletePhone.setOnAction((ActionEvent e) -> {
			ObservableList<Customer_PhoneNumber> selectedPhoneRows = phonesTable.getSelectionModel().getSelectedItems();
			if (!selectedPhoneRows.isEmpty()) {
				ArrayList<Customer_PhoneNumber> rows2 = new ArrayList<>(selectedPhoneRows);
				confirmCustomerPhoneDeletion(stage, NONE, rows2, phonesTable);
			}
		});

		btnRefresh.setOnAction((ActionEvent e) -> {
			dataTable.refresh();
			phonesTable.refresh();
		});

		btnViewAll.setOnAction(e -> {
			dataTable.setItems(customerDataList);
			phonesTable.setItems(customerPhoneDataList);
		});

		btnSearchID.setOnAction(e -> {
			addID.setDisable(false);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addAddress.setDisable(true);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
			btnFind.setDisable(false);
		});

		btnSearchName.setOnAction(e -> {
			addID.setDisable(true);
			addName.setDisable(false);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addAddress.setDisable(true);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
			btnFind.setDisable(false);
		});

		btnAdd.setOnAction((ActionEvent e) -> {

			addID.setDisable(true);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addAddress.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			try {
				if (btnInsert.isSelected()) {
					Customer cust;
					cust = new Customer(addID.getText(), addName.getText(), addAddress.getText(), addEmail.getText());
					insertCustomerData(cust);
					customerDataList.add(cust);
				}

				if (!addPhone.getText().isBlank() && addPhone.getText().length() == 10
						&& (addPhone.getText().startsWith("059") || addPhone.getText().startsWith("056"))) {
					Customer_PhoneNumber phone = new Customer_PhoneNumber(addID.getText(), addPhone.getText());
					insertCustomerPhone(phone);
					customerPhoneDataList.add(phone);
				} else {
					showMessage(stage, NONE, "Invalid phone number");
				}
			} catch (SQLException e1) {
				ShowMessage(stage, NONE, e1.getMessage());
			}
			addID.clear();
			addName.clear();
			addEmail.clear();
			addPhone.clear();
			addAddress.clear();
		});

		btnCancel.setOnAction(e -> {
			addID.clear();
			addName.clear();
			addEmail.clear();
			addPhone.clear();
			addAddress.clear();
			addID.setDisable(true);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addAddress.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			btnFind.setDisable(true);
			btnInsert.setSelected(false);
			btnSearchID.setSelected(false);
			btnSearchName.setSelected(false);
		});

		btnInsert.setOnAction(e -> {
			btnCancel.setDisable(false);
			addID.setDisable(false);
		});

		btnInsertPhone.setOnAction(e -> {
			btnCancel.setDisable(false);
			addID.setDisable(false);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(false);
			addAddress.setDisable(true);
			btnAdd.setDisable(false);
			btnCancel.setDisable(false);
		});

		btnClear.setOnAction((ActionEvent e) -> {
			showCustomerDialog(stage, NONE, dataTable);
		});

		btnClear2.setOnAction(e -> {
			showCustomerPhoneDialog(stage, NONE, phonesTable);
		});

		addID.setOnAction(e -> {
			if (addID.getText() != null && !addID.getText().isEmpty() && btnInsert.isSelected()) {
				addName.setDisable(false);
				addEmail.setDisable(false);
				addPhone.setDisable(false);
				addAddress.setDisable(false);
				btnAdd.setDisable(false);
				btnCancel.setDisable(false);
				addID.setDisable(true);
			}
		});

		btnFind.setOnAction(e -> {
			addID.setDisable(true);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addAddress.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);

			ArrayList<Customer> customers = new ArrayList<>();
			ArrayList<Customer_PhoneNumber> phones = new ArrayList<>();
			ObservableList<Customer> customerList;
			ObservableList<Customer_PhoneNumber> customerPhoneList;

			try {
//				if (!addName.getText().isBlank() && !addID.getText().isBlank()) {
//
//					FindCustomerByIDAndName(addID.getText(), addName.getText(), customers);
//					FindCustomerPhoneByIDAndName(addID.getText(), addName.getText(), phones);
//					customerList = FXCollections.observableArrayList(customers);
//					customerPhoneList = FXCollections.observableArrayList(phones);
//					dataTable.setItems(customerList);
//					phonesTable.setItems(customerPhoneList);

				if (btnSearchName.isSelected() && !addName.getText().isBlank()) {

					FindCustomerByName(addName.getText(), customers);
					FindCustomerPhoneByName(addName.getText(), phones);
					customerList = FXCollections.observableArrayList(customers);
					customerPhoneList = FXCollections.observableArrayList(phones);
					dataTable.setItems(customerList);
					phonesTable.setItems(customerPhoneList);

				} else if (btnSearchID.isSelected() && !addID.getText().isBlank()) {

					FindCustomerByID(addID.getText(), customers);
					FindCustomerPhoneByID(addID.getText(), phones);
					customerList = FXCollections.observableArrayList(customers);
					customerPhoneList = FXCollections.observableArrayList(phones);
					dataTable.setItems(customerList);
					phonesTable.setItems(customerPhoneList);

				}
				btnFind.setDisable(true);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			addID.clear();
			addName.clear();
			addEmail.clear();
			addPhone.clear();
			addAddress.clear();
			btnViewAll.setDisable(false);
			btnSearchID.setSelected(false);
			btnSearchName.setSelected(false);
		});
		return root;

	}
	
	//_________________________________________________________________________________________________________________________________

	private void insertCustomerData(Customer customer) throws SQLIntegrityConstraintViolationException {

		try {
			System.out.println("Insert into Customers (customerID, customerName, customerAddress, email) values ('"
					+ customer.getCustomerID() + "','" + customer.getCustomerName() + "','"
					+ customer.getCustomerAddress() + "','" + customer.getEmail() + "');");

			connectDB();
			ExecuteStatement("Insert into Customers (customerID, customerName, customerAddress, email) values ('"
					+ customer.getCustomerID() + "','" + customer.getCustomerName() + "','"
					+ customer.getCustomerAddress() + "','" + customer.getEmail() + "');");
			con.close();
			System.out.println("Connection closed" + customerData.size());

		} catch (SQLIntegrityConstraintViolationException n) {
			throw n;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//__________________________________________________________________________________________________________________________________

	private void getCustomerData() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select customerID, customerName, customerAddress, email from Customers order by customerID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		customerData.clear();
		while (rs.next())
			customerData.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

		rs.close();
		stmt.close();

		SQL = "select customerID, PhoneNumber from Customer_PhoneNumber order by customerID;";
		stmt = con.createStatement();
		rs = stmt.executeQuery(SQL);

		customerPhoneData.clear();
		while (rs.next())
			customerPhoneData.add(new Customer_PhoneNumber(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + customerData.size());

	}

	//_______________________________________________________________________________________________________________________________________
	
	
	private void FindCustomerByID(String ID, ArrayList<Customer> customers)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select customerID, customerName, customerAddress, email from Customers where CustomerID = '" + ID + "';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		customers.clear();
		while (rs.next())
			customers.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + customers.size());

	}

	//________________________________________________________________________________________________________________________________________
	
	private void FindCustomerPhoneByID(String ID, ArrayList<Customer_PhoneNumber> phones)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select customerID, PhoneNumber from Customer_PhoneNumber where CustomerID = '" + ID + "';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		phones.clear();
		while (rs.next())
			phones.add(new Customer_PhoneNumber(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + phones.size());

	}
	
	//____________________________________________________________________________________________________________________________________
	

	private void FindCustomerByName(String name, ArrayList<Customer> customers)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select customerID, customerName, customerAddress, email from Customers where CustomerName = '" + name
				+ "' order by customerID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		customers.clear();
		while (rs.next())
			customers.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + customers.size());

	}
	
	//_______________________________________________________________________________________________________________________________________

	private void FindCustomerPhoneByName(String name, ArrayList<Customer_PhoneNumber> phones)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select cp.CustomerID, cp.PhoneNumber from Customer_PhoneNumber cp, customers c where c.customerID = cp.customerID and c.customerName = '"
				+ name + "' order by cp.customerID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		phones.clear();
		while (rs.next())
			phones.add(new Customer_PhoneNumber(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + phones.size());

	}

	private void FindCustomerByIDAndName(String ID, String name, ArrayList<Customer> customers)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select customerID, customerName, customerAddress, email from Customers where CustomerID = '" + ID
				+ "' and CustomerName = '" + name + "';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		customers.clear();
		while (rs.next())
			customers.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + customers.size());

	}

	private void FindCustomerPhoneByIDAndName(String ID, String name, ArrayList<Customer_PhoneNumber> phones)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select cp.customerID, cp.PhoneNumber from Customer_PhoneNumber cp, Customers c where c.CustomerID = '"
				+ ID + "' and c.customerName = '" + name + "' and cp.customerID = c.customerID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		phones.clear();
		while (rs.next())
			phones.add(new Customer_PhoneNumber(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + phones.size());

	}

	private void insertCustomerPhone(Customer_PhoneNumber customerPhone)
			throws SQLIntegrityConstraintViolationException {

		try {
			System.out.println("Insert into Customer_PhoneNumber (customerID, Phonenumber) values ('"
					+ customerPhone.getCustomerID() + "','" + customerPhone.getPhoneNumber() + "');");

			connectDB();
			ExecuteStatement("Insert into Customer_PhoneNumber (customerID, Phonenumber) values ('"
					+ customerPhone.getCustomerID() + "','" + customerPhone.getPhoneNumber() + "');");
			con.close();
			System.out.println("Connection closed" + customerData.size());

		} catch (SQLIntegrityConstraintViolationException n) {
			throw n;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}



	private void connectDB() throws ClassNotFoundException, SQLException {

		dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
		Properties p = new Properties();
		p.setProperty("user", dbUsername);
		p.setProperty("password", dbPassword);
		p.setProperty("useSSL", "false");
		p.setProperty("autoReconnect", "true");
		 Class.forName("com.mysql.cj.jdbc.Driver");


		con = DriverManager.getConnection(dbURL, p);

	}

	public void ExecuteStatement(String SQL) throws SQLException {

		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
			stmt.close();

		} catch (SQLException s) {
			System.out.println("SQL statement is not executed!");
			// s.printStackTrace();
			throw s;

		}

	}

	public void updateCustomerName(String ID, String name) {

		try {
			System.out.println("update Customers set customerName = '" + name + "' where customerID = '" + ID + "'");
			connectDB();
			ExecuteStatement("update Customers set customerName = '" + name + "' where customerID = '" + ID + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateCustomerEmail(String ID, String email) {

		try {
			System.out.println("update Customers set email = '" + email + "' where customerID = '" + ID + "'");
			connectDB();
			ExecuteStatement("update  Customers set email = '" + email + "' where customerID = '" + ID + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	public void updateCustomerAddress(String ID, String address) {

		try {
			System.out
					.println("update Customers set customerAddress = '" + address + "' where customerID = " + ID + "'");
			connectDB();
			ExecuteStatement(
					"update Customers set customerAddress = '" + address + "' where customerID = " + ID + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void deleteCustomerRow(Customer row) throws SQLException {
		// TODO Auto-generated method stub

		try {
			System.out.println("delete from Customers where customerID ='" + row.getCustomerID() + "';");
			connectDB();
			ExecuteStatement("delete from Customers where customerID ='" + row.getCustomerID() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			throw e;
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}

	private void deleteCustomerPhoneRow(Customer_PhoneNumber row) throws SQLException {
		// TODO Auto-generated method stub

		try {
			System.out.println("delete from Customer_PhoneNumber where customerID ='" + row.getCustomerID()
					+ "' and phoneNumber = '" + row.getPhoneNumber() + "';");
			connectDB();
			ExecuteStatement("delete from Customer_PhoneNumber where customerID ='" + row.getCustomerID()
					+ "' and phoneNumber = '" + row.getPhoneNumber() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			throw e;
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}

	// showDialog is used to confirm clear process
	private void showCustomerDialog(Window owner, Modality modality, TableView<Customer> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			try {
				for (Customer row : customerDataList) {
					deleteCustomerRow(row);
					table.refresh();
				}
				table.getItems().removeAll(customerDataList);
				stage.close();
			} catch (SQLException n) {
				stage.close();
				ShowMessage(owner, modality, n.getMessage());
			}
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// showDialog is used to confirm clear process
	private void showCustomerPhoneDialog(Window owner, Modality modality, TableView<Customer_PhoneNumber> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			try {
				for (Customer_PhoneNumber row : customerPhoneDataList) {
					deleteCustomerPhoneRow(row);
					table.refresh();
				}
				table.getItems().removeAll(customerPhoneDataList);
				stage.close();
			} catch (SQLException n) {
				stage.close();
				ShowMessage(owner, modality, n.getMessage());
			}
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// confirmDeletion is used to confirm deletion process
	private void confirmCustomerDeletion(Window owner, Modality modality, ArrayList<Customer> rows,
			TableView<Customer> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			rows.forEach(row -> {
				try {
					deleteCustomerRow(row);
					table.getItems().remove(row);
					table.refresh();
					stage.close();
				} catch (SQLException n) {
					stage.close();
					ShowMessage(owner, modality, n.getMessage());
				}
			});
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// confirmCustomerPhoneDeletion is used to confirm deletion process
	private void confirmCustomerPhoneDeletion(Window owner, Modality modality, ArrayList<Customer_PhoneNumber> rows,
			TableView<Customer_PhoneNumber> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			rows.forEach(row -> {
				try {
					deleteCustomerPhoneRow(row);
					table.getItems().remove(row);
					table.refresh();
					stage.close();
				} catch (SQLException n) {
					stage.close();
					ShowMessage(owner, modality, n.getMessage());
				}
			});
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// ShowMessage is used when an error occurs
	private void ShowMessage(Window owner, Modality modality, String message) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);

		TextArea txtMsg = new TextArea(message);
		txtMsg.setFont(btnFont);
		txtMsg.setEditable(false);

		root.getChildren().addAll(txtMsg);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Error Message");
		stage.show();
	}
	
	//_____________________________________________//_______________________________________________________________________________________

	// Category
	private BorderPane categoryScreen(Stage stage) {

		// the category table view with its columns
		TableView<Category> dataTable = new TableView<Category>();
		dataTable.setPadding(new Insets(10));
		TableColumn<Category, String> idCol = new TableColumn<Category, String>("categoryID");
		TableColumn<Category, String> nameCol = new TableColumn<Category, String>("categoryName");
		TableColumn<Category, String> purposeCol = new TableColumn<Category, String>("mainPurpose");

		// the brands table view with its columns
		TableView<Category_PopularBrands> brandsTable = new TableView<>();
		brandsTable.setPadding(new Insets(10));
		TableColumn<Category_PopularBrands, String> idBCol = new TableColumn<Category_PopularBrands, String>(
				"categoryID");
		TableColumn<Category_PopularBrands, String> brandCol = new TableColumn<Category_PopularBrands, String>(
				"popularBrands");

		// the audience table view with its columns
		TableView<Category_TargetAudience> audienceTable = new TableView<>();
		audienceTable.setPadding(new Insets(10));
		TableColumn<Category_TargetAudience, String> idACol = new TableColumn<Category_TargetAudience, String>(
				"categoryID");
		TableColumn<Category_TargetAudience, String> audienceCol = new TableColumn<Category_TargetAudience, String>(
				"targetAudience");

		dataTable.setEditable(true);
		dataTable.setMinSize(300, 500);
		dataTable.setMaxSize(700, 500);

		brandsTable.setEditable(true);
		brandsTable.setMinSize(100, 500);
		brandsTable.setMaxSize(300, 500);

		audienceTable.setEditable(true);
		audienceTable.setMinSize(200, 500);
		audienceTable.setMaxSize(700, 500);

		// name of column for display
		idCol.setMinWidth(50);

		// to get the data from specific column
		idCol.setCellValueFactory(new PropertyValueFactory<Category, String>("categoryID"));

		nameCol.setMinWidth(150);
		nameCol.setCellValueFactory(new PropertyValueFactory<Category, String>("categoryName"));

		nameCol.setCellFactory(TextFieldTableCell.<Category>forTableColumn());

		nameCol.setOnEditCommit((CellEditEvent<Category, String> t) -> {
			try {
				((Category) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setCategoryName(t.getNewValue()); // display only
				updateCategoryName(t.getRowValue().getCategoryID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		purposeCol.setMinWidth(230);
		purposeCol.setCellValueFactory(new PropertyValueFactory<Category, String>("mainPurpose"));

		purposeCol.setCellFactory(TextFieldTableCell.<Category>forTableColumn());

		purposeCol.setOnEditCommit((CellEditEvent<Category, String> t) -> {
			try {
				((Category) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setMainPurpose(t.getNewValue());
				updateCategoryPurpose(t.getRowValue().getCategoryID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		idACol.setMinWidth(50);
		idACol.setCellValueFactory(new PropertyValueFactory<Category_TargetAudience, String>("categoryID"));

		idBCol.setMinWidth(50);
		idBCol.setCellValueFactory(new PropertyValueFactory<Category_PopularBrands, String>("categoryID"));

		brandCol.setMinWidth(120);
		brandCol.setCellValueFactory(new PropertyValueFactory<Category_PopularBrands, String>("brand"));

		audienceCol.setMinWidth(200);
		audienceCol.setCellValueFactory(new PropertyValueFactory<Category_TargetAudience, String>("audience"));

		dataTable.getColumns().addAll(idCol, nameCol, purposeCol);
		brandsTable.getColumns().addAll(idBCol, brandCol);
		audienceTable.getColumns().addAll(idACol, audienceCol);

		HBox tablesBox = new HBox();
		tablesBox.getChildren().addAll(dataTable, brandsTable, audienceTable);
		tablesBox.setPadding(new Insets(5));

		// text fields for inserting new records
		final TextField addID = new TextField();
		final TextField addName = new TextField();
		final TextArea addPurpose = new TextArea();
		final TextField addBrands = new TextField();
		final TextField addAudience = new TextField();

		// labels for the text fields
		Label lblID = new Label("Category ID: ");
		Label lblName = new Label("Name: ");
		Label lblPurpose = new Label("Main Purpose: ");
		Label lblBrands = new Label("Popular Brands: ");
		Label lblAudience = new Label("Target Audience: ");

		// create radioButtons (for opting a choice)
		Label buttonsLbl = new Label("What do you want to do?");
		RadioButton btnInsert = new RadioButton("Insert category record   ");
		RadioButton btnInsertBrand = new RadioButton("Insert brand");
		RadioButton btnInsertAudience = new RadioButton("Insert audience");
		RadioButton btnDelete = new RadioButton("Delete selected category record ");
		RadioButton btnDeleteBrand = new RadioButton("Delete selected brand record");
		RadioButton btnDeleteAudience = new RadioButton("Delete selected audience record");
		RadioButton btnClear = new RadioButton("Clear all categories");
		RadioButton btnClearBrands = new RadioButton("Clear all brands");
		RadioButton btnClearAudience = new RadioButton("Clear all audiences");
		RadioButton btnRefresh = new RadioButton("Refresh tables");
		RadioButton btnViewAll = new RadioButton("Show all data");
		Label lblSearch = new Label("Search by:");
		RadioButton btnSearchID = new RadioButton("Category ID");
		RadioButton btnSearchName = new RadioButton("Category name");
		RadioButton btnSearchAudience = new RadioButton("Category target audience");

		ToggleGroup group = new ToggleGroup();

		btnInsert.setToggleGroup(group);
		btnInsertBrand.setToggleGroup(group);
		btnInsertAudience.setToggleGroup(group);
		btnDelete.setToggleGroup(group);
		btnDeleteBrand.setToggleGroup(group);
		btnDeleteAudience.setToggleGroup(group);
		btnClear.setToggleGroup(group);
		btnClearBrands.setToggleGroup(group);
		btnClearAudience.setToggleGroup(group);
		btnRefresh.setToggleGroup(group);
		btnViewAll.setToggleGroup(group);
		btnSearchID.setToggleGroup(group);
		btnSearchName.setToggleGroup(group);
		btnSearchAudience.setToggleGroup(group);

		// this box hold the toggle buttons in
		VBox optionsBox = new VBox(5);

		Button btnAdd = new Button("Add");
		Button btnCancel = new Button("Cancel");
		Button btnFind = new Button("Find");

		// this box holds the insertion text fields with their labels
		HBox insertionBox = new HBox(5);
		insertionBox.getChildren().addAll(lblID, addID, lblName, addName, lblPurpose, addPurpose, lblBrands, addBrands,
				lblAudience, addAudience, btnAdd, btnCancel, btnFind);

		// root pane
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: white;");
		stage.setFullScreen(true);

		// set title
		Label label = new Label("Categories' Management Screen");
		label.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 30));
		label.setTextFill(Color.BLUE);
		label.setTextAlignment(TextAlignment.CENTER);

		HBox titleBox = new HBox();
		titleBox.getChildren().add(label);
		titleBox.setAlignment(Pos.CENTER);
		root.setTop(titleBox);
		titleBox.setPadding(new Insets(30));

		root.setPadding(new Insets(20));

		addID.setPromptText("Customer ID");
		addID.setMaxWidth(idCol.getPrefWidth());

		addName.setMaxWidth(nameCol.getPrefWidth());
		addName.setPromptText("Name");

		addPurpose.setMaxWidth(150);
		addPurpose.setPromptText("Main Purpose");

		addPurpose.setMaxHeight(addID.getHeight());

		addBrands.setMaxWidth(brandCol.getPrefWidth());
		addBrands.setPromptText("Popular Brands");

		addAudience.setMaxWidth(audienceCol.getPrefWidth());
		addAudience.setPromptText("Target Audience");

		addID.setDisable(true);
		addName.setDisable(true);
		addPurpose.setDisable(true);
		addBrands.setDisable(true);
		addAudience.setDisable(true);
		btnAdd.setDisable(true);
		btnCancel.setDisable(true);
		btnFind.setDisable(true);

		btnInsert.setFont(btnFont);
		btnDelete.setFont(btnFont);
		btnDeleteBrand.setFont(btnFont);
		btnDeleteAudience.setFont(btnFont);
		btnClear.setFont(btnFont);
		btnClearBrands.setFont(btnFont);
		btnClearAudience.setFont(btnFont);
		btnRefresh.setFont(btnFont);
		btnViewAll.setFont(btnFont);
		lblSearch.setFont(btnFont);
		btnSearchID.setFont(btnFont);
		btnSearchName.setFont(btnFont);
		btnSearchAudience.setFont(btnFont);

		buttonsLbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

		optionsBox.setPadding(new Insets(35));

		GridPane pane = new GridPane();
		pane.add(buttonsLbl, 0, 0);
		pane.add(btnInsert, 0, 1);
		pane.add(btnInsertBrand, 0, 2);
		pane.add(btnInsertAudience, 0, 3);
		pane.add(btnDelete, 0, 4);
		pane.add(btnDeleteBrand, 0, 5);
		pane.add(btnDeleteAudience, 0, 6);
		pane.add(btnRefresh, 0, 7);
		pane.add(btnViewAll, 0, 8);
		pane.add(btnClear, 0, 9);
		pane.add(btnClearBrands, 0, 10);
		pane.add(btnClearAudience, 0, 11);
		pane.add(lblSearch, 0, 12);
		pane.add(btnSearchID, 0, 13);
		pane.add(btnSearchName, 0, 14);
		pane.add(btnSearchAudience, 0, 15);
		pane.setVgap(10);
		pane.setPadding(new Insets(20));

		optionsBox.getChildren().add(pane);

		// this box hold the data table with the insertion box
		VBox tableBox = new VBox();
		tableBox.getChildren().addAll(tablesBox, insertionBox);

		HBox box = new HBox(5);
		box.getChildren().addAll(tableBox, optionsBox);
		box.setAlignment(Pos.CENTER);
		root.setCenter(box);

		lblID.setFont(btnFont);
		lblName.setFont(btnFont);
		lblPurpose.setFont(btnFont);
		lblBrands.setFont(btnFont);
		lblAudience.setFont(btnFont);

		btnDelete.setOnAction((ActionEvent e) -> {
			ObservableList<Category> selectedRows = dataTable.getSelectionModel().getSelectedItems();
			ArrayList<Category> rows = new ArrayList<>(selectedRows);
			confirmCategoryDeletion(stage, NONE, rows, dataTable);
		});

		btnDeleteBrand.setOnAction((ActionEvent e) -> {
			ObservableList<Category_PopularBrands> selectedRows = brandsTable.getSelectionModel().getSelectedItems();
			ArrayList<Category_PopularBrands> rows = new ArrayList<>(selectedRows);
			confirmCategoryBrandDeletion(stage, NONE, rows, brandsTable);
		});

		btnDeleteAudience.setOnAction((ActionEvent e) -> {
			ObservableList<Category_TargetAudience> selectedRows = audienceTable.getSelectionModel().getSelectedItems();
			ArrayList<Category_TargetAudience> rows = new ArrayList<>(selectedRows);
			confirmCategoryAudienceDeletion(stage, NONE, rows, audienceTable);
		});

		btnRefresh.setOnAction((ActionEvent e) -> {
			dataTable.refresh();
			brandsTable.refresh();
			audienceTable.refresh();
		});

		btnAdd.setFont(btnFont);
		btnCancel.setFont(btnFont);
		btnFind.setFont(btnFont);

		btnAdd.setOnAction((ActionEvent e) -> {
			addID.setDisable(true);
			addName.setDisable(true);
			addPurpose.setDisable(true);
			addBrands.setDisable(true);
			addAudience.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			try {
				if (btnInsert.isSelected()) {
					Category category;
					category = new Category(addID.getText(), addName.getText(), addPurpose.getText());
					insertCategoryData(category);
					categoryDataList.add(category);
				}

				if (!addBrands.getText().isBlank()) {
					Category_PopularBrands brand = new Category_PopularBrands(addID.getText(), addBrands.getText());
					insertCategoryBrand(brand);
					categoryBrandDataList.add(brand);
				}

				if (!addAudience.getText().isBlank()) {
					Category_TargetAudience audience = new Category_TargetAudience(addID.getText(),
							addAudience.getText());
					insertCategoryAudience(audience);
					categoryAudienceDataList.add(audience);
				}

			} catch (SQLException e1) {
				ShowMessage(stage, NONE, e1.getMessage());
			}
			addID.clear();
			addName.clear();
			addPurpose.clear();
			addBrands.clear();
			addAudience.clear();
		});

		btnCancel.setOnAction(e -> {
			addID.clear();
			addName.clear();
			addPurpose.clear();
			addBrands.clear();
			addAudience.clear();
			addID.setDisable(true);
			addName.setDisable(true);
			addPurpose.setDisable(true);
			addBrands.setDisable(true);
			addAudience.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			btnFind.setDisable(true);
		});

		btnInsert.setOnAction(e -> {
			addID.setDisable(false);
			addName.setDisable(false);
			addPurpose.setDisable(false);
			addBrands.setDisable(false);
			addAudience.setDisable(false);
			btnAdd.setDisable(false);
			btnCancel.setDisable(false);
			btnFind.setDisable(true);
		});

		btnInsertBrand.setOnAction(e -> {
			addID.setDisable(false);
			addName.setDisable(true);
			addPurpose.setDisable(true);
			addBrands.setDisable(false);
			addAudience.setDisable(true);
			btnAdd.setDisable(false);
			btnCancel.setDisable(false);
			btnFind.setDisable(true);
		});

		btnInsertAudience.setOnAction(e -> {
			addID.setDisable(false);
			addName.setDisable(true);
			addPurpose.setDisable(true);
			addBrands.setDisable(true);
			addAudience.setDisable(false);
			btnAdd.setDisable(false);
			btnCancel.setDisable(false);
			btnFind.setDisable(true);
		});

		btnClear.setOnAction((ActionEvent e) -> {
			showCategoryDialog(stage, NONE, dataTable);
		});

		btnClearBrands.setOnAction(e -> {
			showCategoryBrandsDialog(stage, NONE, brandsTable);
		});

		btnClearAudience.setOnAction(e -> {
			showCategoryAudienceDialog(stage, NONE, audienceTable);
		});

		btnViewAll.setOnAction(e -> {
			dataTable.setItems(categoryDataList);
			brandsTable.setItems(categoryBrandDataList);
			audienceTable.setItems(categoryAudienceDataList);
		});

		btnSearchID.setOnAction(e -> {
			addID.setDisable(false);
			btnFind.setDisable(false);
			addName.setDisable(true);
			addPurpose.setDisable(true);
			addBrands.setDisable(true);
			addAudience.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(false);
		});

		btnSearchName.setOnAction(e -> {
			addName.setDisable(false);
			btnFind.setDisable(false);
			addID.setDisable(true);
			addPurpose.setDisable(true);
			addBrands.setDisable(true);
			addAudience.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(false);
		});

		btnSearchAudience.setOnAction(e -> {
			addAudience.setDisable(false);
			btnFind.setDisable(false);
			addName.setDisable(true);
			addPurpose.setDisable(true);
			addBrands.setDisable(true);
			addID.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(false);
		});

		btnFind.setOnAction(e -> {
			ArrayList<Category> categoryArr = new ArrayList<>();
			ArrayList<Category_PopularBrands> brandsArr = new ArrayList<>();
			ArrayList<Category_TargetAudience> audienceArr = new ArrayList<>();
			ObservableList<Category> categoryList;
			ObservableList<Category_PopularBrands> brandsList;
			ObservableList<Category_TargetAudience> audienceList;
			try {
				if (btnSearchID.isSelected() && !addID.getText().isBlank()) {
					FindCategoryByID(addID.getText(), categoryArr);
					FindCategoryBrandsByID(addID.getText(), brandsArr);
					FindCategoryAudienceByID(addID.getText(), audienceArr);

					categoryList = FXCollections.observableArrayList(categoryArr);
					brandsList = FXCollections.observableArrayList(brandsArr);
					audienceList = FXCollections.observableArrayList(audienceArr);

					dataTable.setItems(categoryList);
					brandsTable.setItems(brandsList);
					audienceTable.setItems(audienceList);

					btnSearchID.setSelected(false);
				} else if (btnSearchName.isSelected()) {
					FindCategoryByName(addName.getText(), categoryArr);
					FindCategoryBrandsByName(addName.getText(), brandsArr);
					FindCategoryAudienceByName(addName.getText(), audienceArr);

					categoryList = FXCollections.observableArrayList(categoryArr);
					brandsList = FXCollections.observableArrayList(brandsArr);
					audienceList = FXCollections.observableArrayList(audienceArr);

					dataTable.setItems(categoryList);
					brandsTable.setItems(brandsList);
					audienceTable.setItems(audienceList);

					btnSearchName.setSelected(false);
				} else if (btnSearchAudience.isSelected()) {
					FindCategoryByAudience(addAudience.getText(), categoryArr);
					FindCategoryBrandsByAudience(addAudience.getText(), brandsArr);
					FindCategoryAudienceByAudience(addAudience.getText(), audienceArr);

					categoryList = FXCollections.observableArrayList(categoryArr);
					brandsList = FXCollections.observableArrayList(brandsArr);
					audienceList = FXCollections.observableArrayList(audienceArr);

					dataTable.setItems(categoryList);
					brandsTable.setItems(brandsList);
					audienceTable.setItems(audienceList);

					btnSearchAudience.setSelected(false);
				}
				addID.clear();
				addName.clear();
				addBrands.clear();
				addAudience.clear();
				addPurpose.clear();
				btnFind.setDisable(true);
				btnCancel.setDisable(true);
			} catch (ClassNotFoundException e1) {
				System.out.println(e1.getMessage());
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}

		});

		return root;

	}

	private void insertCategoryData(Category category) throws SQLIntegrityConstraintViolationException {

		try {
			System.out.println(
					"Insert into Category (categoryID, categoryName, mainPurpose) values ('" + category.getCategoryID()
							+ "','" + category.getCategoryName() + "','" + category.getMainPurpose() + "');");

			connectDB();
			ExecuteStatement(
					"Insert into Category (categoryID, categoryName, mainPurpose) values ('" + category.getCategoryID()
							+ "','" + category.getCategoryName() + "','" + category.getMainPurpose() + "');");
			con.close();
			System.out.println("Connection closed" + categoryData.size());

		} catch (SQLIntegrityConstraintViolationException n) {
			throw n;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void insertCategoryBrand(Category_PopularBrands brand) throws SQLIntegrityConstraintViolationException {

		try {
			System.out.println("Insert into Category_popularBrands (categoryID, brand) values ('"
					+ brand.getCategoryID() + "','" + brand.getBrand() + "');");

			connectDB();
			ExecuteStatement("Insert into Category_popularBrands (categoryID, brand) values ('" + brand.getCategoryID()
					+ "','" + brand.getBrand() + "');");
			con.close();
			System.out.println("Connection closed" + categoryBrandData.size());

		} catch (SQLIntegrityConstraintViolationException n) {
			throw n;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void insertCategoryAudience(Category_TargetAudience audience)
			throws SQLIntegrityConstraintViolationException {

		try {
			System.out.println("Insert into Category_TargetAudience (categoryID, targetAudience) values ('"
					+ audience.getCategoryID() + "','" + audience.getAudience() + "');");

			connectDB();
			ExecuteStatement("Insert into Category_TargetAudience (categoryID, targetAudience) values ('"
					+ audience.getCategoryID() + "','" + audience.getAudience() + "');");
			con.close();
			System.out.println("Connection closed" + categoryAudienceData.size());

		} catch (SQLIntegrityConstraintViolationException n) {
			throw n;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getCategoryData() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select categoryID, categoryName, mainPurpose from Category order by categoryID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		categoryData.clear();
		while (rs.next())
			categoryData.add(new Category(rs.getString(1), rs.getString(2), rs.getString(3)));

		rs.close();
		stmt.close();

		SQL = "select categoryID, brand from Category_popularBrands order by categoryID;";
		stmt = con.createStatement();
		rs = stmt.executeQuery(SQL);

		categoryBrandData.clear();
		while (rs.next())
			categoryBrandData.add(new Category_PopularBrands(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		SQL = "select categoryID, targetAudience from Category_TargetAudience order by categoryID;";
		stmt = con.createStatement();
		rs = stmt.executeQuery(SQL);

		categoryAudienceData.clear();
		while (rs.next())
			categoryAudienceData.add(new Category_TargetAudience(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + categoryData.size());
	}

	public void updateCategoryName(String ID, String name) {

		try {
			System.out.println("update Category set categoryName = '" + name + "' where categoryID = '" + ID + "'");
			connectDB();
			ExecuteStatement("update Category set categoryName = '" + name + "' where categoryID = '" + ID + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateCategoryPurpose(String ID, String purpose) {

		try {
			System.out.println("update Category set mainPurpose = '" + purpose + "' where categoryID = '" + ID + "'");
			connectDB();
			ExecuteStatement("update Category set mainPurpose = '" + purpose + "' where categoryID = '" + ID + "'");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

//	public void updateCategoryBrands(String ID, String brands) {
//
//		try {
//			System.out.println("update Category set popularBrands = '" + brands + "' where categoryID = '" + ID + "'");
//			connectDB();
//			ExecuteStatement("update Category set popularBrands = '" + brands + "' where categoryID = '" + ID + "'");
//			con.close();
//			System.out.println("Connection closed");
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void updateCategoryAudience(String ID, String audience) {
//
//		try {
//			System.out
//					.println("update Category set targetAudience = '" + audience + "' where cstegoryID = " + ID + "'");
//			connectDB();
//			ExecuteStatement("update Category set targetAudience = '" + audience + "' where cstegoryID = " + ID + "'");
//			con.close();
//			System.out.println("Connection closed");
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}

	private void FindCategoryByID(String categoryID, ArrayList<Category> category)
			throws SQLException, ClassNotFoundException {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select categoryID, categoryName, mainPurpose from category where categoryID = '" + categoryID
				+ "' order by categoryID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		category.clear();
		while (rs.next())
			category.add(new Category(rs.getString(1), rs.getString(2), rs.getString(3)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + category.size());
	}

	private void FindCategoryBrandsByID(String categoryID, ArrayList<Category_PopularBrands> brand)
			throws SQLException, ClassNotFoundException {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select categoryID, brand from category_popularBrands where categoryID = '" + categoryID
				+ "' order by categoryID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		brand.clear();
		while (rs.next())
			brand.add(new Category_PopularBrands(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + brand.size());
	}

	private void FindCategoryAudienceByID(String categoryID, ArrayList<Category_TargetAudience> audience)
			throws SQLException, ClassNotFoundException {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select categoryID, targetAudience from category_targetAudience where categoryID = '" + categoryID
				+ "' order by categoryID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		audience.clear();
		while (rs.next())
			audience.add(new Category_TargetAudience(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + audience.size());
	}

	private void FindCategoryByName(String name, ArrayList<Category> category)
			throws SQLException, ClassNotFoundException {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select categoryID, categoryName, mainPurpose from category where categoryName = '" + name
				+ "' order by categoryID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		category.clear();
		while (rs.next())
			category.add(new Category(rs.getString(1), rs.getString(2), rs.getString(3)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + category.size());
	}

	private void FindCategoryBrandsByName(String name, ArrayList<Category_PopularBrands> brand)
			throws SQLException, ClassNotFoundException {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select cb.categoryID, cb.brand from category_popularBrands cb, category c where c.categoryID = cb.categoryID and c.categoryName = '"
				+ name + "' order by categoryID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		brand.clear();
		while (rs.next())
			brand.add(new Category_PopularBrands(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + brand.size());
	}

	private void FindCategoryAudienceByName(String name, ArrayList<Category_TargetAudience> audience)
			throws SQLException, ClassNotFoundException {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select ca.categoryID, ca.targetAudience from category_targetAudience ca, category c where c.categoryID = ca.categoryID and c.categoryName = '"
				+ name + "' order by categoryID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		audience.clear();
		while (rs.next())
			audience.add(new Category_TargetAudience(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + audience.size());
	}

	private void FindCategoryByAudience(String audience, ArrayList<Category> category)
			throws SQLException, ClassNotFoundException {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select c.categoryID, c.categoryName, c.mainPurpose from category c, category_targetAudience ca where c.categoryID = ca.categoryID and ca.targetAudience = '"
				+ audience + "' order by categoryID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		category.clear();
		while (rs.next())
			category.add(new Category(rs.getString(1), rs.getString(2), rs.getString(3)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + category.size());
	}

	private void FindCategoryBrandsByAudience(String audience, ArrayList<Category_PopularBrands> brand)
			throws SQLException, ClassNotFoundException {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select cb.categoryID, cb.brand from category_popularBrands cb, category c, category_targetAudience ca where c.categoryID = cb.categoryID and c.categoryID = ca.categoryID and ca.targetAudience = '"
				+ audience + "' order by categoryID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		brand.clear();
		while (rs.next())
			brand.add(new Category_PopularBrands(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + brand.size());
	}

	private void FindCategoryAudienceByAudience(String audiencee, ArrayList<Category_TargetAudience> audience)
			throws SQLException, ClassNotFoundException {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select categoryID, targetAudience from category_targetAudience where targetAudience = '" + audiencee
				+ "' order by categoryID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		audience.clear();
		while (rs.next())
			audience.add(new Category_TargetAudience(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + audience.size());
	}

	private void deleteCategoryRow(Category row) throws SQLException {
		// TODO Auto-generated method stub

		try {
			System.out.println("delete from Category where categoryID ='" + row.getCategoryID() + "';");
			connectDB();
			ExecuteStatement("delete from Category where categoryID ='" + row.getCategoryID() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			throw e;
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}

	// showDialog is used to confirm clear process
	private void showCategoryDialog(Window owner, Modality modality, TableView<Category> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			try {
				for (Category row : categoryDataList) {
					deleteCategoryRow(row);
					table.refresh();
				}
				table.getItems().removeAll(categoryDataList);
				stage.close();
			} catch (SQLException n) {
				stage.close();
				ShowMessage(owner, modality, n.getMessage());
			}
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// confirmDeletion is used to confirm deletion process
	private void confirmCategoryDeletion(Window owner, Modality modality, ArrayList<Category> rows,
			TableView<Category> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			rows.forEach(row -> {
				try {
					deleteCategoryRow(row);
					table.getItems().remove(row);
					table.refresh();
					stage.close();
				} catch (SQLException n) {
					stage.close();
					ShowMessage(owner, modality, n.getMessage());
				}
			});
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	private void deleteCategoryBrandRow(Category_PopularBrands row) throws SQLException {

		try {
			System.out.println("delete from Category_popularBrands where categoryID ='" + row.getCategoryID()
					+ "' and brand = '" + row.getBrand() + "';");
			connectDB();
			ExecuteStatement("delete from Category_popularBrands where categoryID ='" + row.getCategoryID()
					+ "' and brand = '" + row.getBrand() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			throw e;
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}

	// showDialog is used to confirm clear process
	private void showCategoryBrandsDialog(Window owner, Modality modality, TableView<Category_PopularBrands> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			try {
				for (Category_PopularBrands row : categoryBrandDataList) {
					deleteCategoryBrandRow(row);
					table.refresh();
				}
				table.getItems().removeAll(categoryBrandDataList);
				stage.close();
			} catch (SQLException n) {
				stage.close();
				ShowMessage(owner, modality, n.getMessage());
			}
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// confirmDeletion is used to confirm deletion process
	private void confirmCategoryBrandDeletion(Window owner, Modality modality, ArrayList<Category_PopularBrands> rows,
			TableView<Category_PopularBrands> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			rows.forEach(row -> {
				try {
					deleteCategoryBrandRow(row);
					table.getItems().remove(row);
					table.refresh();
					stage.close();
				} catch (SQLException n) {
					stage.close();
					ShowMessage(owner, modality, n.getMessage());
				}
			});
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	private void deleteCategoryAudienceRow(Category_TargetAudience row) throws SQLException {

		try {
			System.out.println("delete from Category_TargetAudience where categoryID ='" + row.getCategoryID()
					+ "' and targetAudience = '" + row.getAudience() + "';");
			connectDB();
			ExecuteStatement("delete from Category_TargetAudience where categoryID ='" + row.getCategoryID()
					+ "' and targetAudience = '" + row.getAudience() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			throw e;
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}

	// showDialog is used to confirm clear process
	private void showCategoryAudienceDialog(Window owner, Modality modality, TableView<Category_TargetAudience> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			try {
				for (Category_TargetAudience row : categoryAudienceDataList) {
					deleteCategoryAudienceRow(row);
					table.refresh();
				}
				table.getItems().removeAll(categoryAudienceDataList);
				stage.close();
			} catch (SQLException n) {
				stage.close();
				ShowMessage(owner, modality, n.getMessage());
			}
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// confirmDeletion is used to confirm deletion process
	private void confirmCategoryAudienceDeletion(Window owner, Modality modality,
			ArrayList<Category_TargetAudience> rows, TableView<Category_TargetAudience> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			rows.forEach(row -> {
				try {
					deleteCategoryAudienceRow(row);
					table.getItems().remove(row);
					table.refresh();
					stage.close();
				} catch (SQLException n) {
					stage.close();
					ShowMessage(owner, modality, n.getMessage());
				}
			});
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

		
	// Order_product
	private BorderPane order_productScreen(Stage stage) {

		// the OrderProducts table view with its columns
		TableView<OrderProducts> dataTable = new TableView<OrderProducts>();
		dataTable.setPadding(new Insets(20));
		TableColumn<OrderProducts, String> OIDCol = new TableColumn<OrderProducts, String>("orderID");
		TableColumn<OrderProducts, String> PIDCol = new TableColumn<OrderProducts, String>("productID");
		TableColumn<OrderProducts, Integer> quantityCol = new TableColumn<OrderProducts, Integer>("quantity");

		dataTable.setEditable(true);
		dataTable.setMinSize(500, 500);
		dataTable.setMaxSize(500, 500);

		// name of column for display
		OIDCol.setMinWidth(50);

		// to get the data from specific column
		OIDCol.setCellValueFactory(new PropertyValueFactory<OrderProducts, String>("orderID"));

		// name of column for display
		PIDCol.setMinWidth(50);

		// to get the data from specific column
		PIDCol.setCellValueFactory(new PropertyValueFactory<OrderProducts, String>("productID"));

		quantityCol.setMinWidth(50);
		quantityCol.setCellValueFactory(new PropertyValueFactory<OrderProducts, Integer>("quantity"));

		quantityCol.setCellFactory(
				TextFieldTableCell.<OrderProducts, Integer>forTableColumn(new IntegerStringConverter()));

		quantityCol.setOnEditCommit((CellEditEvent<OrderProducts, Integer> t) -> {
			try {
				((OrderProducts) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setQuantity(t.getNewValue());
				updateOrder_produtQuantity(t.getRowValue().getOrderID(), t.getRowValue().getProductID(),
						t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		dataTable.getColumns().addAll(OIDCol, PIDCol, quantityCol);

		// text fields for inserting new records
		final TextField addOID = new TextField();
		final TextField addPID = new TextField();
		final TextField addQuantity = new TextField();

		// labels for the text fields
		Label lblOID = new Label("Order ID: ");
		Label lblPID = new Label("Product ID: ");
		Label lblQuantity = new Label("Quantity: ");

		// create radioButtons (for opting a choice)
		Label buttonsLbl = new Label("What do you want to do?");
		RadioButton btnInsert = new RadioButton("Insert record   ");
		RadioButton btnDelete = new RadioButton("Delete selected record ");
		RadioButton btnClear = new RadioButton("Clear all records");
		RadioButton btnRefresh = new RadioButton("Refresh table");
		RadioButton btnViewAll = new RadioButton("Show all data");
		RadioButton btnSearch = new RadioButton("Search records");
		ToggleGroup group = new ToggleGroup();

		btnInsert.setToggleGroup(group);
		btnDelete.setToggleGroup(group);
		btnClear.setToggleGroup(group);
		btnRefresh.setToggleGroup(group);
		btnViewAll.setToggleGroup(group);
		btnSearch.setToggleGroup(group);

		// this box hold the toggle buttons in
		VBox optionsBox = new VBox(5);

		Button btnAdd = new Button("Add");
		Button btnCancel = new Button("Cancel");
		Button btnFind = new Button("Find");
		btnAdd.setFont(btnFont);
		btnCancel.setFont(btnFont);
		btnFind.setFont(btnFont);

		// this box holds the insertion text fields with their labels
		HBox insertionTextBox = new HBox(5);
		insertionTextBox.getChildren().addAll(lblOID, addOID, lblPID, addPID, lblQuantity, addQuantity);
		insertionTextBox.setPadding(new Insets(10));
		insertionTextBox.setAlignment(Pos.CENTER);

		HBox insertionButtonsBox = new HBox(5);
		insertionButtonsBox.getChildren().addAll(btnAdd, btnFind, btnCancel);
		insertionButtonsBox.setAlignment(Pos.CENTER);

		VBox insertionBox = new VBox(5);
		insertionBox.getChildren().addAll(insertionTextBox, insertionButtonsBox);
		insertionBox.setAlignment(Pos.CENTER);

		// root pane
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: white;");
		stage.setFullScreen(true);

		// set title
		Label label = new Label("Order_Product Management Screen");
		label.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 30));
		label.setTextFill(Color.BLUE);
		label.setTextAlignment(TextAlignment.CENTER);

		HBox titleBox = new HBox();
		titleBox.getChildren().add(label);
		titleBox.setAlignment(Pos.CENTER);
		root.setTop(titleBox);
		titleBox.setPadding(new Insets(30));

		root.setPadding(new Insets(20));

		addOID.setPromptText("Order ID");
		addOID.setMaxWidth(OIDCol.getPrefWidth());

		addPID.setMaxWidth(PIDCol.getPrefWidth());
		addPID.setPromptText("Product ID");

		addQuantity.setMaxWidth(quantityCol.getPrefWidth());
		addQuantity.setPromptText("Email");

		addOID.setDisable(true);
		addPID.setDisable(true);
		addQuantity.setDisable(true);
		btnAdd.setDisable(true);
		btnCancel.setDisable(true);
		btnFind.setDisable(true);

		btnInsert.setFont(btnFont);
		btnDelete.setFont(btnFont);
		btnClear.setFont(btnFont);
		btnRefresh.setFont(btnFont);
		btnViewAll.setFont(btnFont);
		btnSearch.setFont(btnFont);

		buttonsLbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

		optionsBox.setPadding(new Insets(35));

		GridPane pane = new GridPane();
		pane.add(buttonsLbl, 0, 0);
		pane.add(btnInsert, 0, 1);
		pane.add(btnDelete, 0, 2);
		pane.add(btnRefresh, 0, 3);
		pane.add(btnViewAll, 0, 4);
		pane.add(btnClear, 0, 5);
		pane.add(btnSearch, 0, 6);
		pane.setVgap(10);
		pane.setPadding(new Insets(20));

		optionsBox.getChildren().add(pane);

		// this box hold the data table with the insertion box
		VBox tableBox = new VBox();
		tableBox.getChildren().addAll(dataTable, insertionBox);

		HBox box = new HBox(5);
		box.getChildren().addAll(tableBox, optionsBox);
		box.setAlignment(Pos.CENTER);
		root.setCenter(box);

		lblOID.setFont(btnFont);
		lblPID.setFont(btnFont);
		lblQuantity.setFont(btnFont);

		btnDelete.setOnAction((ActionEvent e) -> {
			ObservableList<OrderProducts> selectedCustomerRows = dataTable.getSelectionModel().getSelectedItems();
			if (!selectedCustomerRows.isEmpty()) {
				ArrayList<OrderProducts> rows = new ArrayList<>(selectedCustomerRows);
				confirmOrderProductDeletion(stage, NONE, rows, dataTable);
			}
		});

		btnRefresh.setOnAction((ActionEvent e) -> {
			dataTable.refresh();
		});

		btnViewAll.setOnAction(e -> {
			dataTable.setItems(orderProductDataList);
		});

		btnSearch.setOnAction(e -> {
			addOID.setDisable(false);
			addPID.setDisable(false);
			addQuantity.setDisable(true);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
			btnFind.setDisable(false);
		});

		btnAdd.setOnAction((ActionEvent e) -> {

			addOID.setDisable(true);
			addPID.setDisable(true);
			addQuantity.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			try {
				if (btnInsert.isSelected()) {
					OrderProducts op;
					if (addQuantity.getText().isBlank())
						op = new OrderProducts(addOID.getText(), addPID.getText());
					else
						op = new OrderProducts(addOID.getText(), addPID.getText(),
								Integer.parseInt(addQuantity.getText()));
					insertOrderProductData(op);
					orderProductDataList.add(op);
					btnInsert.setSelected(false);
				}
			} catch (SQLException e1) {
				ShowMessage(stage, NONE, e1.getMessage());
			} catch (NumberFormatException e2) {
				ShowMessage(stage, NONE, "Quantity has to be an integer value");
			}
			addOID.clear();
			addPID.clear();
			addQuantity.clear();
		});

		btnCancel.setOnAction(e -> {
			addOID.clear();
			addPID.clear();
			addQuantity.clear();
			addOID.setDisable(true);
			addPID.setDisable(true);
			addQuantity.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			btnFind.setDisable(true);
			btnInsert.setSelected(false);
			btnSearch.setSelected(false);
		});

		btnInsert.setOnAction(e -> {
			btnCancel.setDisable(false);
			addOID.setDisable(false);
			addPID.setDisable(false);
			addQuantity.setDisable(false);
			btnAdd.setDisable(false);
			btnFind.setDisable(true);
		});

		btnClear.setOnAction((ActionEvent e) -> {
			showOrderProductDialog(stage, NONE, dataTable);
		});

		btnFind.setOnAction(e -> {
			addOID.setDisable(true);
			addPID.setDisable(true);
			addQuantity.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);

			ArrayList<OrderProducts> op = new ArrayList<>();
			ObservableList<OrderProducts> opList;

			try {
				if (!addOID.getText().isBlank() && !addPID.getText().isBlank()) {

					FindOrderProductByPIDAndOID(addOID.getText(), addPID.getText(), op);
					opList = FXCollections.observableArrayList(op);
					dataTable.setItems(opList);

				} else if (!addOID.getText().isBlank() && addPID.getText().isBlank()) {

					FindOrderProductByOID(addOID.getText(), op);
					opList = FXCollections.observableArrayList(op);
					dataTable.setItems(opList);

				} else if (addOID.getText().isBlank() && !addPID.getText().isBlank()) {

					FindOrderProductByPID(addPID.getText(), op);
					opList = FXCollections.observableArrayList(op);
					dataTable.setItems(opList);

				}

				btnFind.setDisable(true);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			addOID.clear();
			addPID.clear();
			addQuantity.clear();
			btnViewAll.setDisable(false);
			btnSearch.setSelected(false);
		});
		return root;

	}

	//_____________________________________________________________________________________________________________
	
	private void showMessage(Stage stage, Modality none, String string) {
		// TODO Auto-generated method stub

	}

	private void insertOrderProductData(OrderProducts order_product) throws SQLIntegrityConstraintViolationException {

		try {
			System.out.println(
					"Insert into order_product (ID ,orderID, quantity) values ('" + order_product.getOrderID()
							+ "','" + order_product.getProductID() + "','" + order_product.getQuantity() + "');");

			connectDB();
			ExecuteStatement(
					"Insert into order_product (ID ,orderID, quantity) values ('" + order_product.getOrderID()
							+ "','" + order_product.getProductID() + "','" + order_product.getQuantity() + "');");
			con.close();
			System.out.println("Connection closed" + customerData.size());

		} catch (SQLIntegrityConstraintViolationException n) {
			throw n;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getOrderProductData() throws SQLException, ClassNotFoundException {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select ID ,orderID, quantity from order_product order by ID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		orderProductData.clear();
		while (rs.next())
			orderProductData
					.add(new OrderProducts(rs.getString(2), rs.getString(1), Integer.parseInt(rs.getString(3))));

		rs.close();
		stmt.close();
		con.close();
		System.out.println("Connection closed" + orderProductData.size());
	}
	
	
	

	public void updateOrder_produtQuantity(String orderID, String productID, int quantity) {

		try {
			System.out.println("update order_product set quantity = '" + quantity + "' where orderID = '" + orderID
					+ "' and ID = '" + productID + "';");
			connectDB();
			ExecuteStatement("update order_product set quantity = '" + quantity + "' where orderID = '" + orderID
					+ "' and ID = '" + productID + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void deleteOrderProductRow(OrderProducts row) throws SQLException {

		try {
			System.out.println("delete from order_product where ID ='" + row.getProductID() + "' and orderID = '"
					+ row.getOrderID() + "';");
			connectDB();
			ExecuteStatement("delete from order_product where ID ='" + row.getProductID() + "' and orderID = '"
					+ row.getOrderID() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			throw e;
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}

	// showDialog is used to confirm clear process
	private void showOrderProductDialog(Window owner, Modality modality, TableView<OrderProducts> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			try {
				for (OrderProducts row : orderProductDataList) {
					deleteOrderProductRow(row);
					table.refresh();
				}
				table.getItems().removeAll(orderProductDataList);
				stage.close();
			} catch (SQLException n) {
				stage.close();
				ShowMessage(owner, modality, n.getMessage());
			}
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// confirmDeletion is used to confirm deletion process
	private void confirmOrderProductDeletion(Window owner, Modality modality, ArrayList<OrderProducts> rows,
			TableView<OrderProducts> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			rows.forEach(row -> {
				try {
					deleteOrderProductRow(row);
					table.getItems().remove(row);
					table.refresh();
					stage.close();
				} catch (SQLException n) {
					stage.close();
					ShowMessage(owner, modality, n.getMessage());
				}
			});
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	private void FindOrderProductByPIDAndOID(String OID, String PID, ArrayList<OrderProducts> op)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select ID ,orderID, quantity from order_product where orderID = '" + OID + "' and productID = '"
				+ PID + "' order by productID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		op.clear();
		while (rs.next())
			op.add(new OrderProducts(rs.getString(2), rs.getString(1), Integer.parseInt(rs.getString(3))));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + op.size());
	}

	private void FindOrderProductByOID(String OID, ArrayList<OrderProducts> op)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select ID ,orderID, quantity from order_product where orderID = '" + OID
				+ "' order by orderID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		op.clear();
		while (rs.next())
			op.add(new OrderProducts(rs.getString(2), rs.getString(1), Integer.parseInt(rs.getString(3))));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + op.size());
	}
	

	private void FindOrderProductByPID(String PID, ArrayList<OrderProducts> op)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select ID ,orderID, quantity from order_product where ID = '" + PID
				+ "' order by ID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		op.clear();
		while (rs.next())
			op.add(new OrderProducts(rs.getString(2), rs.getString(1), Integer.parseInt(rs.getString(3))));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + op.size());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// employees;
	private BorderPane employeeScreen(Stage stage) {

		// the employees table view with its columns
		TableView<Employee> dataTable = new TableView<Employee>();
		dataTable.setPadding(new Insets(10));
		TableColumn<Employee, String> idCol = new TableColumn<Employee, String>("employeeID");
		TableColumn<Employee, String> nameCol = new TableColumn<Employee, String>("employeeName");
		TableColumn<Employee, String> emailCol = new TableColumn<Employee, String>("email");
		TableColumn<Employee, String> positionCol = new TableColumn<Employee, String>("employeePosition");
		TableColumn<Employee, Double> salaryCol = new TableColumn<Employee, Double>("Salary");
		TableColumn<Employee, Character> genderCol = new TableColumn<Employee, Character>("gender");

		// the employee_phones table with its columns
		TableView<Employee_PhoneNumber> phonesTable = new TableView<>();
		phonesTable.setPadding(new Insets(10));
		TableColumn<Employee_PhoneNumber, String> id2Col = new TableColumn<Employee_PhoneNumber, String>("employeeID");
		TableColumn<Employee_PhoneNumber, String> phoneCol = new TableColumn<Employee_PhoneNumber, String>(
				"phoneNumber");

		dataTable.setEditable(true);
		dataTable.setMinSize(500, 500);
		dataTable.setMaxSize(1800, 550);

		phonesTable.setEditable(false);
		phonesTable.setMinSize(200, 500);
		phonesTable.setMaxSize(400, 550);

		HBox tablesBox = new HBox();
		tablesBox.setPadding(new Insets(10));
		tablesBox.getChildren().addAll(dataTable, phonesTable);

		// name of column for display
		idCol.setMinWidth(50);

		// to get the data from specific column
		idCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeID"));

		nameCol.setMinWidth(120);
		nameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeName"));

		nameCol.setCellFactory(TextFieldTableCell.<Employee>forTableColumn());

		nameCol.setOnEditCommit((CellEditEvent<Employee, String> t) -> {
			try {
				((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setEmployeeName(t.getNewValue()); // display only
				updateEmployeeName(t.getRowValue().getEmployeeID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		emailCol.setMinWidth(200);
		emailCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));

		emailCol.setCellFactory(TextFieldTableCell.<Employee>forTableColumn());

		emailCol.setOnEditCommit((CellEditEvent<Employee, String> t) -> {
			try {
				((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());
				updateEmployeeEmail(t.getRowValue().getEmployeeID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		id2Col.setMinWidth(50);

		id2Col.setCellValueFactory(new PropertyValueFactory<Employee_PhoneNumber, String>("employeeID"));

		phoneCol.setMinWidth(110);
		phoneCol.setCellValueFactory(new PropertyValueFactory<Employee_PhoneNumber, String>("phoneNumber"));

//		phoneCol.setCellFactory(TextFieldTableCell.<Customer_PhoneNumber>forTableColumn());

		positionCol.setMinWidth(150);
		positionCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeePosition"));

		positionCol.setCellFactory(TextFieldTableCell.<Employee>forTableColumn());
		positionCol.setOnEditCommit((CellEditEvent<Employee, String> t) -> {
			try {
				((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setEmployeePosition(t.getNewValue());
				updateEmployeePosition(t.getRowValue().getEmployeeID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		salaryCol.setMinWidth(50);
		salaryCol.setCellValueFactory(new PropertyValueFactory<Employee, Double>("salary"));

		salaryCol.setCellFactory(TextFieldTableCell.<Employee, Double>forTableColumn(new DoubleStringConverter()));
		salaryCol.setOnEditCommit((CellEditEvent<Employee, Double> t) -> {
			try {
				((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSalary(t.getNewValue());
				updateEmployeeSalary(t.getRowValue().getEmployeeID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		genderCol.setMinWidth(50);
		genderCol.setCellValueFactory(new PropertyValueFactory<Employee, Character>("gender"));

		genderCol
				.setCellFactory(TextFieldTableCell.<Employee, Character>forTableColumn(new CharacterStringConverter()));
		genderCol.setOnEditCommit((CellEditEvent<Employee, Character> t) -> {
			try {
				((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setGender(t.getNewValue());
				updateEmployeeGender(t.getRowValue().getEmployeeID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		dataTable.getColumns().addAll(idCol, nameCol, emailCol, positionCol, salaryCol, genderCol);
		phonesTable.getColumns().addAll(id2Col, phoneCol);

		// text fields for inserting new records
		final TextField addID = new TextField();
		final TextField addName = new TextField();
		final TextField addEmail = new TextField();
		final TextField addPosition = new TextField();
		final TextField addPhone = new TextField();
		final TextField addSalary = new TextField();
		final TextField addGender = new TextField();

		// labels for the text fields
		Label lblID = new Label("Employee ID: ");
		Label lblName = new Label("Name: ");
		Label lblPosition = new Label("Position: ");
		Label lblEmail = new Label("Email: ");
		Label lblPhone = new Label("Phone Number: ");
		Label lblSalary = new Label("Salary: ");
		Label lblGender = new Label("Gender: ");

		// create radioButtons (for opting a choice)
		Label buttonsLbl = new Label("What do you want to do?");
		RadioButton btnInsert = new RadioButton("Insert Employee record   ");
		RadioButton btnInsertPhone = new RadioButton("Insert phone number");
		RadioButton btnDelete = new RadioButton("Delete selected employee record ");
		RadioButton btnDeletePhone = new RadioButton("Delete selected cemployee phone record ");
		RadioButton btnClear = new RadioButton("Clear all employees");
		RadioButton btnClear2 = new RadioButton("Clear all employees' phone numbers");
		RadioButton btnRefresh = new RadioButton("Refresh tables");
		RadioButton btnViewAll = new RadioButton("Show all data");
		RadioButton btnMinSalary = new RadioButton("Get minimum salary");
		RadioButton btnCountMales = new RadioButton("Get number of males");
		RadioButton btnCountFemales = new RadioButton("Get number of females");
		Label lblSearch = new Label("Search by:");
		RadioButton btnSearchID = new RadioButton("Employee ID");
		RadioButton btnSearchName = new RadioButton("Employee name");
		RadioButton btnSearchPosition = new RadioButton("Employee position");
		RadioButton btnSearchGender = new RadioButton("Employee gender");
		ToggleGroup group = new ToggleGroup();

		btnInsert.setToggleGroup(group);
		btnInsertPhone.setToggleGroup(group);
		btnDelete.setToggleGroup(group);
		btnDeletePhone.setToggleGroup(group);
		btnClear.setToggleGroup(group);
		btnClear2.setToggleGroup(group);
		btnRefresh.setToggleGroup(group);
		btnViewAll.setToggleGroup(group);
		btnSearchID.setToggleGroup(group);
		btnSearchName.setToggleGroup(group);
		btnSearchPosition.setToggleGroup(group);
		btnMinSalary.setToggleGroup(group);
		btnCountMales.setToggleGroup(group);
		btnCountFemales.setToggleGroup(group);
		btnSearchGender.setToggleGroup(group);

		// this box hold the toggle buttons in
		VBox optionsBox = new VBox(5);

		Button btnAdd = new Button("Add");
		//btnAdd.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");

		
		Button btnCancel = new Button("Cancel");
		
		
		Button btnFind = new Button("Find");
		btnAdd.setFont(btnFont);
		btnCancel.setFont(btnFont);
		btnFind.setFont(btnFont);

		TextField tfMinSalary = new TextField();
		tfMinSalary.setMaxWidth(50);
		tfMinSalary.setEditable(false);
		HBox minBox = new HBox(10);
		minBox.getChildren().addAll(btnMinSalary, tfMinSalary);

		TextField tfCountMales = new TextField();
		tfCountMales.setMaxWidth(50);
		tfCountMales.setEditable(false);
		HBox malesBox = new HBox(10);
		malesBox.getChildren().addAll(btnCountMales, tfCountMales);

		TextField tfCountFemales = new TextField();
		tfCountFemales.setMaxWidth(50);
		tfCountFemales.setEditable(false);
		HBox femalesBox = new HBox(10);
		femalesBox.getChildren().addAll(btnCountFemales, tfCountFemales);

		// this box holds the insertion text fields with their labels
		HBox insertionFields = new HBox(5);
		insertionFields.getChildren().addAll(lblID, addID, lblName, addName, lblEmail, addEmail, lblPosition,
				addPosition, lblPhone, addPhone, lblSalary, addSalary, lblGender, addGender);
		insertionFields.setAlignment(Pos.CENTER);

		HBox insertionButtons = new HBox(5);
		insertionButtons.getChildren().addAll(btnAdd, btnFind, btnCancel);
		insertionButtons.setAlignment(Pos.CENTER);

		VBox insertionBox = new VBox(5);
		insertionBox.getChildren().addAll(insertionFields, insertionButtons);

		// root pane
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: white;");
		stage.setFullScreen(true);

		// set title
		Label label = new Label("Employees' Management Screen");
		label.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 30));
		label.setTextFill(Color.BLUE);
		label.setTextAlignment(TextAlignment.CENTER);

		HBox titleBox = new HBox();
		titleBox.getChildren().add(label);
		titleBox.setAlignment(Pos.CENTER);
		root.setTop(titleBox);
		titleBox.setPadding(new Insets(30));

		root.setPadding(new Insets(20));

		addID.setPromptText("Employee ID");
		addID.setMaxWidth(idCol.getPrefWidth());

		addName.setMaxWidth(nameCol.getPrefWidth());
		addName.setPromptText("Name");

		addEmail.setMaxWidth(emailCol.getPrefWidth());
		addEmail.setPromptText("Email");

		addPhone.setMaxWidth(phoneCol.getPrefWidth());
		addPhone.setPromptText("Phone Number");

		addPosition.setMaxWidth(positionCol.getPrefWidth());
		addPosition.setPromptText("Position");

		addSalary.setMaxWidth(salaryCol.getPrefWidth());
		addSalary.setPromptText("Salary");

		addGender.setMaxWidth(genderCol.getPrefWidth());
		addGender.setPromptText("M/F");

		addID.setDisable(true);
		addName.setDisable(true);
		addEmail.setDisable(true);
		addPhone.setDisable(true);
		addPosition.setDisable(true);
		addGender.setDisable(true);
		addSalary.setDisable(true);
		btnAdd.setDisable(true);
		btnCancel.setDisable(true);
		btnFind.setDisable(true);

		btnInsert.setFont(btnFont);
		btnInsertPhone.setFont(btnFont);
		btnDelete.setFont(btnFont);
		btnDeletePhone.setFont(btnFont);
		btnClear.setFont(btnFont);
		btnClear2.setFont(btnFont);
		btnRefresh.setFont(btnFont);
		btnViewAll.setFont(btnFont);
		lblSearch.setFont(btnFont);
		btnSearchID.setFont(btnFont);
		btnSearchName.setFont(btnFont);
		btnSearchPosition.setFont(btnFont);
		btnMinSalary.setFont(btnFont);
		btnSearchGender.setFont(btnFont);
		btnCountMales.setFont(btnFont);
		btnCountFemales.setFont(btnFont);

		buttonsLbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

		optionsBox.setPadding(new Insets(35));

		GridPane pane = new GridPane();
		pane.add(buttonsLbl, 0, 0);
		pane.add(btnInsert, 0, 1);
		pane.add(btnInsertPhone, 0, 2);
		pane.add(btnDelete, 0, 3);
		pane.add(btnDeletePhone, 0, 4);
		pane.add(btnRefresh, 0, 5);
		pane.add(btnViewAll, 0, 6);
		pane.add(btnClear, 0, 7);
		pane.add(btnClear2, 0, 8);
		pane.add(minBox, 0, 9);
		pane.add(malesBox, 0, 10);
		pane.add(femalesBox, 0, 11);
		pane.add(lblSearch, 0, 12);
		pane.add(btnSearchID, 0, 13);
		pane.add(btnSearchName, 0, 14);
		pane.add(btnSearchGender, 0, 15);
		pane.add(btnSearchPosition, 0, 16);

		pane.setVgap(10);
		pane.setPadding(new Insets(20));

		optionsBox.getChildren().add(pane);

		// this box hold the data table with the insertion box
		VBox tableBox = new VBox();
		tableBox.getChildren().addAll(tablesBox, insertionBox);

		HBox box = new HBox(5);
		box.getChildren().addAll(tableBox, optionsBox);
		box.setAlignment(Pos.CENTER);
		root.setCenter(box);

		lblID.setFont(btnFont);
		lblName.setFont(btnFont);
		lblPosition.setFont(btnFont);
		lblEmail.setFont(btnFont);
		lblPhone.setFont(btnFont);
		lblSalary.setFont(btnFont);
		lblGender.setFont(btnFont);

		btnDelete.setOnAction((ActionEvent e) -> {
			ObservableList<Employee> selectedEmployeeRows = dataTable.getSelectionModel().getSelectedItems();
			if (!selectedEmployeeRows.isEmpty()) {
				ArrayList<Employee> rows = new ArrayList<>(selectedEmployeeRows);
				confirmEmployeeDeletion(stage, NONE, rows, dataTable);
			}
		});

		btnDeletePhone.setOnAction((ActionEvent e) -> {
			ObservableList<Employee_PhoneNumber> selectedPhoneRows = phonesTable.getSelectionModel().getSelectedItems();
			if (!selectedPhoneRows.isEmpty()) {
				ArrayList<Employee_PhoneNumber> rows2 = new ArrayList<>(selectedPhoneRows);
				confirmEmployeePhoneDeletion(stage, NONE, rows2, phonesTable);
			}
		});

		btnRefresh.setOnAction((ActionEvent e) -> {
			dataTable.refresh();
			phonesTable.refresh();
			tfMinSalary.clear();
			tfCountFemales.clear();
			tfCountMales.clear();
		});

		btnViewAll.setOnAction(e -> {
			dataTable.setItems(employeeDataList);
			phonesTable.setItems(employeePhoneDataList);
		});

		btnSearchID.setOnAction(e -> {
			addID.setDisable(false);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addPosition.setDisable(true);
			addSalary.setDisable(true);
			addGender.setDisable(true);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
			btnFind.setDisable(false);
		});

		btnSearchName.setOnAction(e -> {
			addID.setDisable(true);
			addName.setDisable(false);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addPosition.setDisable(true);
			addGender.setDisable(true);
			addSalary.setDisable(true);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
			btnFind.setDisable(false);
		});

		btnSearchPosition.setOnAction(e -> {
			addID.setDisable(true);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addPosition.setDisable(false);
			addSalary.setDisable(true);
			addGender.setDisable(true);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
			btnFind.setDisable(false);
		});
		
		btnSearchGender.setOnAction(e->{
			addID.setDisable(true);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addPosition.setDisable(true);
			addSalary.setDisable(true);
			addGender.setDisable(false);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
			btnFind.setDisable(false);
			
		});

		btnAdd.setOnAction((ActionEvent e) -> {

			addID.setDisable(true);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addPosition.setDisable(true);
			addSalary.setDisable(true);
			addGender.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			try {
				if (btnInsert.isSelected()) {
					Employee emp;
					emp = new Employee(addID.getText(), addName.getText(), addEmail.getText(), addPosition.getText(),
							Double.parseDouble(addSalary.getText()), addGender.getText().trim().charAt(0));
					insertEmployeeData(emp);
					employeeDataList.add(emp);
				}

				if (!addPhone.getText().isBlank() && addPhone.getText().length() == 10
						&& (addPhone.getText().startsWith("059") || addPhone.getText().startsWith("056"))) {
					Employee_PhoneNumber phone = new Employee_PhoneNumber(addID.getText(), addPhone.getText());
					insertEmployeePhone(phone);
					employeePhoneDataList.add(phone);
				} else
					showMessage(stage, NONE, "Invalid phone number");

				btnInsert.setSelected(false);
				btnInsertPhone.setSelected(false);
			} catch (SQLException e1) {
				ShowMessage(stage, NONE, e1.getMessage());
			} catch (IllegalArgumentException n) {
				ShowMessage(stage, NONE, n.getMessage());
			}
			addID.clear();
			addName.clear();
			addEmail.clear();
			addPhone.clear();
			addPosition.clear();
			addSalary.clear();
			addGender.clear();
		});

		btnCancel.setOnAction(e -> {
			addID.clear();
			addName.clear();
			addEmail.clear();
			addPhone.clear();
			addPosition.clear();
			addSalary.clear();
			addID.setDisable(true);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addPosition.setDisable(true);
			addSalary.setDisable(true);
			addGender.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			btnFind.setDisable(true);
			btnInsert.setSelected(false);
			btnSearchID.setSelected(false);
			btnSearchName.setSelected(true);
			btnSearchPosition.setSelected(true);
		});

		btnInsert.setOnAction(e -> {
			btnCancel.setDisable(false);
			addID.setDisable(false);
			addName.setDisable(false);
			addEmail.setDisable(false);
			addPhone.setDisable(false);
			addPosition.setDisable(false);
			addGender.setDisable(false);
			addSalary.setDisable(false);
			btnAdd.setDisable(false);
			btnCancel.setDisable(false);
			
		});

		btnInsertPhone.setOnAction(e -> {
			btnCancel.setDisable(false);
			addID.setDisable(false);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(false);
			addPosition.setDisable(true);
			addGender.setDisable(true);
			addSalary.setDisable(true);
			btnAdd.setDisable(false);
			btnCancel.setDisable(false);
		});

		btnClear.setOnAction((ActionEvent e) -> {
			showEmployeeDialog(stage, NONE, dataTable);
		});

		btnClear2.setOnAction(e -> {
			showEmployeePhoneDialog(stage, NONE, phonesTable);
		});

		addID.setOnAction(e -> {
			if (addID.getText() != null && !addID.getText().isEmpty() && btnInsert.isSelected()) {
				addName.setDisable(false);
				addEmail.setDisable(false);
				addPhone.setDisable(false);
				addPosition.setDisable(false);
				addSalary.setDisable(false);
				addGender.setDisable(false);
				btnAdd.setDisable(false);
				btnCancel.setDisable(false);
				addID.setDisable(true);
			}
		});

		btnFind.setOnAction(e -> {
			addID.setDisable(true);
			addName.setDisable(true);
			addEmail.setDisable(true);
			addPhone.setDisable(true);
			addPosition.setDisable(true);
			addSalary.setDisable(true);
			addGender.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);

			ArrayList<Employee> employees = new ArrayList<>();
			ArrayList<Employee_PhoneNumber> phones = new ArrayList<>();
			ObservableList<Employee> employeeList;
			ObservableList<Employee_PhoneNumber> employeePhoneList;

			try {
				if (btnSearchPosition.isSelected() && !addPosition.getText().isBlank()) {

					FindEmployeeByPosition(addPosition.getText(), employees);
					FindEmployeePhoneByPosition(addPosition.getText(), phones);
					employeeList = FXCollections.observableArrayList(employees);
					employeePhoneList = FXCollections.observableArrayList(phones);
					dataTable.setItems(employeeList);
					phonesTable.setItems(employeePhoneList);
					btnSearchPosition.setSelected(false);

				} else if (btnSearchName.isSelected() && !addName.getText().isBlank()) {

					FindEmployeeByName(addName.getText(), employees);
					FindEmployeePhoneByName(addName.getText(), phones);
					employeeList = FXCollections.observableArrayList(employees);
					employeePhoneList = FXCollections.observableArrayList(phones);
					dataTable.setItems(employeeList);
					phonesTable.setItems(employeePhoneList);
					btnSearchName.setSelected(false);

					
					
				} else if (btnSearchID.isSelected() && !addID.getText().isBlank()) {

					FindEmployeeByID(addID.getText(), employees);
					FindEmployeePhoneByID(addID.getText(), phones);
					employeeList = FXCollections.observableArrayList(employees);
					employeePhoneList = FXCollections.observableArrayList(phones);
					dataTable.setItems(employeeList);
					phonesTable.setItems(employeePhoneList);
					btnSearchID.setSelected(false);

				} else if (btnSearchGender.isSelected() && !addGender.getText().isBlank()) {

					FindEmployeeByGender(addGender.getText(), employees);
					FindEmployeePhoneByGender(addGender.getText(), phones);
					employeeList = FXCollections.observableArrayList(employees);
					employeePhoneList = FXCollections.observableArrayList(phones);
					dataTable.setItems(employeeList);
					phonesTable.setItems(employeePhoneList);
					btnSearchID.setSelected(false);

				}
				btnFind.setDisable(true);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			addID.clear();
			addName.clear();
			addEmail.clear();
			addPhone.clear();
			addPosition.clear();
			addGender.clear();
			btnViewAll.setDisable(false);
			btnSearchID.setSelected(false);
			btnSearchName.setSelected(false);
			btnSearchPosition.setSelected(false);
			btnSearchGender.setSelected(false);
		});

		btnMinSalary.setOnAction(e -> {
			double value = findMinEmployeeSalary();
			if (value > 0)
				tfMinSalary.setText(value + "");

		});

		btnCountMales.setOnAction(e -> {
			double count = findCountMaleEmployees();
			if (count >= 0)
				tfCountMales.setText(count + "");

		});

		btnCountFemales.setOnAction(e -> {
			double count = findCountFemaleEmployees();
			if (count >= 0)
				tfCountFemales.setText(count + "");

		});

		return root;

	}

	private void insertEmployeeData(Employee emp) throws SQLIntegrityConstraintViolationException {

		try {
			System.out.println(
					"Insert into Employees (employeeID, employeeName, employeePosition, email, salary) values ('"
							+ emp.getEmployeeID() + "','" + emp.getEmployeeName() + "','" + emp.getEmployeePosition()
							+ "','" + emp.getEmail() + "','" + emp.getSalary() + "');");

			connectDB();
			ExecuteStatement(
					"Insert into Employees (employeeID, employeeName, employeePosition, email, salary) values ('"
							+ emp.getEmployeeID() + "','" + emp.getEmployeeName() + "','" + emp.getEmployeePosition()
							+ "','" + emp.getEmail() + "','" + emp.getSalary() + "');");
			con.close();
			System.out.println("Connection closed" + customerData.size());

		} catch (SQLIntegrityConstraintViolationException n) {
			throw n;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getEmployeeData() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select employeeID, employeeName, employeePosition, email, salary, gender from Employees order by employeeID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		employeeData.clear();
		while (rs.next()) {
			if (rs.getString(6) != null)
				employeeData.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3),
						rs.getDouble(5), rs.getString(6).charAt(0)));
			else
				employeeData.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3),
						rs.getDouble(5), ' '));
		}
		rs.close();
		stmt.close();

		SQL = "select employeeID, PhoneNumber from Employee_PhoneNumber order by employeeID;";
		stmt = con.createStatement();
		rs = stmt.executeQuery(SQL);

		employeePhoneData.clear();
		while (rs.next())
			employeePhoneData.add(new Employee_PhoneNumber(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + employeeData.size());

	}

	private void FindEmployeeByID(String ID, ArrayList<Employee> emps) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select employeeID, employeeName, employeePosition, email, salary, gender from employees where employeeID = '"
				+ ID + "';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		emps.clear();
		while (rs.next()) {
			if (rs.getString(6) != null)
				emps.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3),
						rs.getDouble(5), rs.getString(6).charAt(0)));
			else
				emps.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3),
						rs.getDouble(5), ' '));
		}

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + emps.size());

	}

	private void FindEmployeePhoneByID(String ID, ArrayList<Employee_PhoneNumber> phones)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select employeeID, PhoneNumber from Employee_PhoneNumber where employeeID = '" + ID + "';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		phones.clear();
		while (rs.next())
			phones.add(new Employee_PhoneNumber(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + phones.size());

	}

	private void FindEmployeeByName(String name, ArrayList<Employee> emps) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select employeeID, employeeName, employeePosition, email, salary, gender from Employees where employeeName = '"
				+ name + "' order by employeeID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		emps.clear();
		while (rs.next()) {
			if (rs.getString(6) != null)
				emps.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3),
						rs.getDouble(5), rs.getString(6).charAt(0)));
			else
				emps.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3),
						rs.getDouble(5), ' '));
		}

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + emps.size());

	}

	private void FindEmployeePhoneByName(String name, ArrayList<Employee_PhoneNumber> phones)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select ep.employeeID, ep.PhoneNumber from Employee_PhoneNumber ep, Employees e where e.employeeID = ep.employeeID and e.employeeName = '"
				+ name + "' order by ep.employeeID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		phones.clear();
		while (rs.next())
			phones.add(new Employee_PhoneNumber(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + phones.size());

	}

	private void FindEmployeeByGender(String gender, ArrayList<Employee> emps)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select employeeID, employeeName, employeePosition, email, salary, gender from Employees where gender = '"
				+ gender + "' order by employeeID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		emps.clear();
		while (rs.next()) {
			if (rs.getString(6) != null)
				emps.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3),
						rs.getDouble(5), rs.getString(6).charAt(0)));
			else
				emps.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3),
						rs.getDouble(5), ' '));
		}

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + emps.size());

	}

	private void FindEmployeePhoneByGender(String gender, ArrayList<Employee_PhoneNumber> phones)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select ep.employeeID, ep.PhoneNumber from Employee_PhoneNumber ep, Employees e where e.employeeID = ep.employeeID and e.gender = '"
				+ gender + "' order by ep.employeeID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		phones.clear();
		while (rs.next())
			phones.add(new Employee_PhoneNumber(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + phones.size());

	}


	private void FindEmployeeByPosition(String position, ArrayList<Employee> emps)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select employeeID, employeeName, employeePosition, email, salary, gender from employees where employeePosition = '"
				+ position + "';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		emps.clear();
		while (rs.next()) {
			if (rs.getString(6) != null)
				emps.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3),
						rs.getDouble(5), rs.getString(6).charAt(0)));
			else
				emps.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(3),
						rs.getDouble(5), ' '));
		}

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + emps.size());

	}

	private void FindEmployeePhoneByPosition(String position, ArrayList<Employee_PhoneNumber> phones)
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select ep.employeeID, ep.PhoneNumber from Employee_PhoneNumber ep, employees e where e.employeeID = ep.employeeID and e.employeePosition = '"
				+ position + "';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		phones.clear();
		while (rs.next())
			phones.add(new Employee_PhoneNumber(rs.getString(1), rs.getString(2)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + phones.size());

	}

	private double findMinEmployeeSalary() {
		try {

			double min = -1;

			String SQL;

			connectDB();

			System.out.println("Connection established");

			SQL = "select  min(salary) from employees;";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			while (rs.next())
				min = rs.getDouble(1);
			rs.close();
			stmt.close();

			con.close();
			System.out.println("Connection closed");

			return min;
		} catch (SQLException n) {
			n.printStackTrace();
			return -1;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private double findCountMaleEmployees() {
		try {

			double count = 0;

			String SQL;

			connectDB();

			System.out.println("Connection established");

			SQL = "select count(gender) from employees where gender = 'M';";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			while (rs.next())
				count = rs.getDouble(1);
			rs.close();
			stmt.close();

			con.close();
			System.out.println("Connection closed");

			return count;
		} catch (SQLException n) {
			n.printStackTrace();
			return 0;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private double findCountFemaleEmployees() {
		try {

			double count = 0;

			String SQL;

			connectDB();

			System.out.println("Connection established");

			SQL = "select count(gender) from employees where gender = 'F';";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			while (rs.next())
				count = rs.getDouble(1);
			rs.close();
			stmt.close();

			con.close();
			System.out.println("Connection closed");

			return count;
		} catch (SQLException n) {
			n.printStackTrace();
			return 0;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private void insertEmployeePhone(Employee_PhoneNumber employeePhone)
			throws SQLIntegrityConstraintViolationException {

		try {
			System.out.println("Insert into Employee_PhoneNumber (employeeID, Phonenumber) values ('"
					+ employeePhone.getEmployeeID() + "','" + employeePhone.getPhoneNumber() + "');");

			connectDB();
			ExecuteStatement("Insert into Employee_PhoneNumber (employeeID, Phonenumber) values ('"
					+ employeePhone.getEmployeeID() + "','" + employeePhone.getPhoneNumber() + "');");
			con.close();
			System.out.println("Connection closed" + customerData.size());

		} catch (SQLIntegrityConstraintViolationException n) {
			throw n;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateEmployeeName(String ID, String name) {

		try {
			System.out.println("update Employees set employeeName = '" + name + "' where employeeID = '" + ID + "'");
			connectDB();
			ExecuteStatement("update Employees set employeeName = '" + name + "' where employeeID = '" + ID + "'");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateEmployeeEmail(String ID, String email) {

		try {
			System.out.println("update Employees set email = '" + email + "' where employeeID = '" + ID + "'");
			connectDB();
			ExecuteStatement("update Employees set email = '" + email + "' where employeeID = '" + ID + "'");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateEmployeePosition(String ID, String position) {

		try {
			System.out.println(
					"update Employees set employeePosition = '" + position + "' where employeeID = '" + ID + "'");
			connectDB();
			ExecuteStatement(
					"update Employees set employeePosition = '" + position + "' where employeeID = '" + ID + "'");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateEmployeeSalary(String ID, double salary) {

		try {
			System.out.println("update Employees set salary = " + salary + " where employeeID = '" + ID + "'");
			connectDB();
			ExecuteStatement("update Employees set salary = " + salary + " where employeeID = '" + ID + "'");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateEmployeeGender(String ID, char gender) {

		try {
			System.out.println("update Employees set gender = '" + gender + "' where employeeID = '" + ID + "'");
			connectDB();
			ExecuteStatement("update Employees set gender = '" + gender + "' where employeeID = '" + ID + "'");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void deleteEmployeeRow(Employee row) throws SQLException {
		// TODO Auto-generated method stub

		try {
			System.out.println("delete from Employees where employeeID ='" + row.getEmployeeID() + "';");
			connectDB();
			ExecuteStatement("delete from Employees where employeeID ='" + row.getEmployeeID() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			throw e;
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}

	private void deleteEmployeePhoneRow(Employee_PhoneNumber row) throws SQLException {
		// TODO Auto-generated method stub

		try {
			System.out.println("delete from Employee_PhoneNumber where employeeID = '" + row.getEmployeeID()
					+ "' and phoneNumber = '" + row.getPhoneNumber() + "';");
			connectDB();
			ExecuteStatement("delete from Employee_PhoneNumber where employeeID = '" + row.getEmployeeID()
					+ "' and phoneNumber = '" + row.getPhoneNumber() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			throw e;
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}

	// showDialog is used to confirm clear process
	private void showEmployeeDialog(Window owner, Modality modality, TableView<Employee> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			try {
				for (Employee row : employeeDataList) {
					deleteEmployeeRow(row);
					table.refresh();
				}
				table.getItems().removeAll(employeeDataList);
				stage.close();
			} catch (SQLException n) {
				stage.close();
				ShowMessage(owner, modality, n.getMessage());
			}
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// showDialog is used to confirm clear process
	private void showEmployeePhoneDialog(Window owner, Modality modality, TableView<Employee_PhoneNumber> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			try {
				for (Employee_PhoneNumber row : employeePhoneDataList) {
					deleteEmployeePhoneRow(row);
					table.refresh();
				}
				table.getItems().removeAll(employeePhoneDataList);
				stage.close();
			} catch (SQLException n) {
				stage.close();
				ShowMessage(owner, modality, n.getMessage());
			}
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// confirmDeletion is used to confirm deletion process
	private void confirmEmployeeDeletion(Window owner, Modality modality, ArrayList<Employee> rows,
			TableView<Employee> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			rows.forEach(row -> {
				try {
					deleteEmployeeRow(row);
					table.getItems().remove(row);
					table.refresh();
					stage.close();
				} catch (SQLException n) {
					stage.close();
					ShowMessage(owner, modality, n.getMessage());
				}
			});
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// confirmEmployeePhoneDeletion is used to confirm deletion process
	private void confirmEmployeePhoneDeletion(Window owner, Modality modality, ArrayList<Employee_PhoneNumber> rows,
			TableView<Employee_PhoneNumber> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			rows.forEach(row -> {
				try {
					deleteEmployeePhoneRow(row);
					table.getItems().remove(row);
					table.refresh();
					stage.close();
				} catch (SQLException n) {
					stage.close();
					ShowMessage(owner, modality, n.getMessage());
				}
			});
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// orders
	private BorderPane orderScreen(Stage stage) {

		// the orders table view with its columns
		TableView<Order> dataTable = new TableView<Order>();
		dataTable.setPadding(new Insets(10));
		TableColumn<Order, String> OIDCol = new TableColumn<Order, String>("orderID");
		TableColumn<Order, String> CIDCol = new TableColumn<Order, String>("CustomerID");
		TableColumn<Order, String> dateCol = new TableColumn<Order, String>("orderDate");
		TableColumn<Order, String> statCol = new TableColumn<Order, String>("orderStatus");

		dataTable.setEditable(true);
		dataTable.setMinSize(500, 500);
		dataTable.setMaxSize(1800, 550);

		HBox tablesBox = new HBox();
		tablesBox.setPadding(new Insets(10));
		tablesBox.getChildren().addAll(dataTable);

		// name of column for display
		OIDCol.setMinWidth(50);

		// to get the data from specific column
		OIDCol.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));

		CIDCol.setMinWidth(50);
		CIDCol.setCellValueFactory(new PropertyValueFactory<Order, String>("customerID"));

		CIDCol.setCellFactory(TextFieldTableCell.<Order>forTableColumn());

		CIDCol.setOnEditCommit((CellEditEvent<Order, String> t) -> {
			try {
				((Order) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCustomerID(t.getNewValue()); // display
																															// only
				updateOrderCustomer(t.getRowValue().getOrderID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		dateCol.setMinWidth(150);
		dateCol.setCellValueFactory(new PropertyValueFactory<Order, String>("orderDate"));

		dateCol.setCellFactory(TextFieldTableCell.<Order>forTableColumn());

		dateCol.setOnEditCommit((CellEditEvent<Order, String> t) -> {
			try {
				((Order) t.getTableView().getItems().get(t.getTablePosition().getRow())).setOrderDate(t.getNewValue());
				updateOrderDate(t.getRowValue().getOrderID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		statCol.setMinWidth(150);
		statCol.setCellValueFactory(new PropertyValueFactory<Order, String>("OrderStatus"));

		statCol.setCellFactory(TextFieldTableCell.<Order>forTableColumn());
		statCol.setOnEditCommit((CellEditEvent<Order, String> t) -> {
			try {
				((Order) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setOrderStatus(t.getNewValue());
				updateOrderStatus(t.getRowValue().getOrderID(), t.getNewValue());
			} catch (IllegalArgumentException n) {
				showMessage(stage, NONE, n.getMessage());
			} catch (NullPointerException n) {
				showMessage(stage, NONE, "You need to enter a value");
			}
		});

		dataTable.getColumns().addAll(OIDCol, CIDCol, dateCol, statCol);

		// text fields for inserting new records
		final TextField addOID = new TextField();
		final TextField addCID = new TextField();
		final TextField addDate = new TextField();
		final TextField addStatus = new TextField();

		// labels for the text fields
		Label lblOID = new Label("Order ID: ");
		Label lblCID = new Label("Customer ID: ");
		Label lblDate = new Label("Order Date: ");
		Label lblStat = new Label("Order status: ");

		// create radioButtons (for opting a choice)
		Label buttonsLbl = new Label("What do you want to do?");
		RadioButton btnInsert = new RadioButton("Insert Order record   ");
		RadioButton btnDelete = new RadioButton("Delete selected order record ");
		RadioButton btnClear = new RadioButton("Clear all orders");
		RadioButton btnRefresh = new RadioButton("Refresh tables");
		RadioButton btnViewAll = new RadioButton("Show all data");
		Label lblSearch = new Label("Search by:");
		RadioButton btnSearchOID = new RadioButton("Order ID");
		RadioButton btnSearchCID = new RadioButton("Customer ID");
		RadioButton btnSearchDate = new RadioButton("Order Date");
		RadioButton btnSearchStat = new RadioButton("Order Status");
		//RadioButton btnSearchDate = new RadioButton("Order Date");

		
		ToggleGroup group = new ToggleGroup();

		btnInsert.setToggleGroup(group);
		btnDelete.setToggleGroup(group);
		btnClear.setToggleGroup(group);
		btnRefresh.setToggleGroup(group);
		btnViewAll.setToggleGroup(group);
		btnSearchOID.setToggleGroup(group);
		btnSearchCID.setToggleGroup(group);
		btnSearchDate.setToggleGroup(group);
		btnSearchStat.setToggleGroup(group);

		// this box hold the toggle buttons in
		VBox optionsBox = new VBox(5);

		Button btnAdd = new Button("Add");
		Button btnCancel = new Button("Cancel");
		Button btnFind = new Button("Find");
		btnAdd.setFont(btnFont);
		btnCancel.setFont(btnFont);
		btnFind.setFont(btnFont);

		// this box holds the insertion text fields with their labels
		HBox insertionfields = new HBox(5);
		insertionfields.getChildren().addAll(lblOID, addOID, lblCID, addCID, lblDate, addDate, lblStat, addStatus);
		insertionfields.setAlignment(Pos.CENTER);

		HBox insertionButtons = new HBox(5);
		insertionButtons.getChildren().addAll(btnAdd, btnCancel, btnFind);
		insertionButtons.setAlignment(Pos.CENTER);

		VBox insertionBox = new VBox(5);
		insertionBox.getChildren().addAll(insertionfields, insertionButtons);

		// root pane
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: white;");
		stage.setFullScreen(true);

		// set title
		Label label = new Label("Orders' Management Screen");
		label.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 30));
		label.setTextFill(Color.BLUE);
		label.setTextAlignment(TextAlignment.CENTER);

		HBox titleBox = new HBox();
		titleBox.getChildren().add(label);
		titleBox.setAlignment(Pos.CENTER);
		root.setTop(titleBox);
		titleBox.setPadding(new Insets(30));

		root.setPadding(new Insets(20));

		addOID.setPromptText("Order ID");
		addOID.setMaxWidth(OIDCol.getPrefWidth());

		addCID.setMaxWidth(CIDCol.getPrefWidth());
		addCID.setPromptText("Customer ID");

		addDate.setMaxWidth(dateCol.getPrefWidth() + 20);
		addDate.setPromptText("yyyy-mm-dd");

		addStatus.setMaxWidth(statCol.getPrefWidth());
		addStatus.setPromptText("Status");

		addOID.setDisable(true);
		addCID.setDisable(true);
		addDate.setDisable(true);
		addStatus.setDisable(true);
		btnAdd.setDisable(true);
		btnCancel.setDisable(true);
		btnFind.setDisable(true);

		btnInsert.setFont(btnFont);
		btnDelete.setFont(btnFont);
		btnClear.setFont(btnFont);
		btnRefresh.setFont(btnFont);
		btnViewAll.setFont(btnFont);
		lblSearch.setFont(btnFont);
		btnSearchOID.setFont(btnFont);
		btnSearchCID.setFont(btnFont);
		btnSearchDate.setFont(btnFont);
		btnSearchStat.setFont(btnFont);

		buttonsLbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

		optionsBox.setPadding(new Insets(35));

		GridPane pane = new GridPane();
		pane.add(buttonsLbl, 0, 0);
		pane.add(btnInsert, 0, 1);
		pane.add(btnDelete, 0, 2);
		pane.add(btnRefresh, 0, 3);
		pane.add(btnViewAll, 0, 4);
		pane.add(btnClear, 0, 5);
		pane.add(lblSearch, 0, 6);
		pane.add(btnSearchOID, 0, 7);
		pane.add(btnSearchCID, 0, 8);
		pane.add(btnSearchDate, 0, 10);
		pane.add(btnSearchStat, 0, 9);
		pane.setVgap(10);
		pane.setPadding(new Insets(20));

		optionsBox.getChildren().add(pane);

		// this box hold the data table with the insertion box
		VBox tableBox = new VBox(10);
		tableBox.getChildren().addAll(dataTable, insertionBox);

		HBox box = new HBox(5);
		box.getChildren().addAll(tableBox, optionsBox);
		box.setAlignment(Pos.CENTER);
		root.setCenter(box);

		lblOID.setFont(btnFont);
		lblCID.setFont(btnFont);
		lblDate.setFont(btnFont);
		lblStat.setFont(btnFont);

		btnDelete.setOnAction((ActionEvent e) -> {
			ObservableList<Order> selectedOrderRows = dataTable.getSelectionModel().getSelectedItems();
			if (!selectedOrderRows.isEmpty()) {
				ArrayList<Order> rows = new ArrayList<>(selectedOrderRows);
				confirmOrderDeletion(stage, NONE, rows, dataTable);
			}
		});

		btnRefresh.setOnAction((ActionEvent e) -> {
			dataTable.refresh();
		});

		btnViewAll.setOnAction(e -> {
			dataTable.setItems(orderDataList);
		});

		btnSearchOID.setOnAction(e -> {
			addOID.setDisable(false);
			addCID.setDisable(true);
			addDate.setDisable(true);
			addStatus.setDisable(true);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
			btnFind.setDisable(false);
		});

		btnSearchCID.setOnAction(e -> {
			addOID.setDisable(true);
			addCID.setDisable(false);
			addDate.setDisable(true);
			addStatus.setDisable(true);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
			btnFind.setDisable(false);
		});

		btnSearchDate.setOnAction(e -> {
			addOID.setDisable(true);
			addCID.setDisable(true);
			addStatus.setDisable(true);
			addDate.setDisable(false);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
     		btnFind.setDisable(false);
		});

		btnSearchStat.setOnAction(e -> {
			addOID.setDisable(true);
			addCID.setDisable(false);
			addDate.setDisable(true);
			addStatus.setDisable(false);
			btnCancel.setDisable(false);
			btnAdd.setDisable(true);
			btnFind.setDisable(false);
		});

		btnAdd.setOnAction((ActionEvent e) -> {

			addOID.setDisable(true);
			addCID.setDisable(true);
			addDate.setDisable(true);
			addStatus.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			try {
				if (btnInsert.isSelected()) {
					Order order;
					order = new Order(addOID.getText(), addCID.getText(), addDate.getText(), addStatus.getText());
					insertOrderData(order);
					orderDataList.add(order);
				}
			} catch (SQLException e1) {
				ShowMessage(stage, NONE, e1.getMessage());
			}
			addOID.clear();
			addCID.clear();
			addDate.clear();
			addStatus.clear();
		});

		btnCancel.setOnAction(e -> {
			addOID.clear();
			addCID.clear();
			addDate.clear();
			addStatus.clear();
			addOID.setDisable(true);
			addCID.setDisable(true);
			addDate.setDisable(true);
			addStatus.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			btnFind.setDisable(true);
			btnInsert.setSelected(false);
			btnSearchOID.setSelected(false);
			btnSearchCID.setSelected(true);
			btnSearchStat.setSelected(true);
			btnSearchDate.setSelected(true);
		});

		btnInsert.setOnAction(e -> {
			btnCancel.setDisable(false);
			addOID.setDisable(false);
		});

		btnClear.setOnAction((ActionEvent e) -> {
			showOrderDialog(stage, NONE, dataTable);
		});

		addOID.setOnAction(e -> {
			if (addOID.getText() != null && !addOID.getText().isEmpty() && btnInsert.isSelected()) {
				addCID.setDisable(false);
				addDate.setDisable(false);
				addStatus.setDisable(false);
				btnAdd.setDisable(false);
				btnCancel.setDisable(false);
				addOID.setDisable(true);
			}
		});

		btnFind.setOnAction(e -> {
			addCID.setDisable(true);
			addDate.setDisable(true);
			addOID.setDisable(true);
			addStatus.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);

			ArrayList<Order> orders = new ArrayList<>();
			ObservableList<Order> orderList;

			try {
				if (btnSearchOID.isSelected() && !addOID.getText().isBlank()) {

					FindOrderByOID(addOID.getText(), orders);
					orderList = FXCollections.observableArrayList(orders);
					dataTable.setItems(orderList);

				} else if (btnSearchCID.isSelected() && !addCID.getText().isBlank()) {

					FindOrderByCID(addCID.getText(), orders);
					orderList = FXCollections.observableArrayList(orders);
					dataTable.setItems(orderList);

				} else if (btnSearchStat.isSelected() && !addStatus.getText().isBlank()) {

					FindOrderByStatus(addStatus.getText(), orders);
					orderList = FXCollections.observableArrayList(orders);
					dataTable.setItems(orderList);
				}
				
				else if (btnSearchDate.isSelected() && !addDate.getText().isBlank()) {
					FindOrderByDate(addDate.getText() , orders);
					orderList = FXCollections.observableArrayList(orders);
					dataTable.setItems(orderList);
					
				}
				
				
				btnFind.setDisable(true);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			addCID.clear();
			addDate.clear();
			addOID.clear();
			addStatus.clear();
			btnViewAll.setDisable(false);
			btnSearchCID.setSelected(false);
			btnSearchDate.setSelected(false);
			btnSearchOID.setSelected(false);
			btnSearchStat.setSelected(false);
		});
		return root;

	}

	private void insertOrderData(Order order) throws SQLException {

		try {
			System.out.println("Insert into Orders (orderID, customerID, orderDate, orderStatus) values ('"
					+ order.getOrderID() + "','" + order.getCustomerID() + "','" + order.getOrderDate() + "','"
					+ order.getOrderStatus() + "');");

			connectDB();
			ExecuteStatement("Insert into Orders (orderID, customerID, orderDate, orderStatus) values ('"
					+ order.getOrderID() + "','" + order.getCustomerID() + "','" + order.getOrderDate() + "','"
					+ order.getOrderStatus() + "');");
			con.close();
			System.out.println("Connection closed" + orderData.size());

		} catch (SQLIntegrityConstraintViolationException n) {
			throw n;
		} catch (SQLException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getOrderData() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select orderID, customerID, orderDate, orderStatus from Orders order by orderID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		orderData.clear();
		while (rs.next())
			orderData.add(new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + orderData.size());

	}

	private void FindOrderByOID(String ID, ArrayList<Order> orders) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select orderID, customerID, orderDate, orderStatus from orders where orderID = '" + ID + "';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		orders.clear();
		while (rs.next())
			orders.add(new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + orders.size());

	}

	private void FindOrderByCID(String ID, ArrayList<Order> orders) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select orderID, customerID, orderDate, orderStatus from orders where customerID = '" + ID
				+ "' order by orderID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		orders.clear();
		while (rs.next())
			orders.add(new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + orders.size());

	}

	private void FindOrderByStatus(String status, ArrayList<Order> orders) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select orderID, customerID, orderDate, orderStatus from orders where orderStatus = '" + status
				+ "' order by orderID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		orders.clear();
		while (rs.next())
			orders.add(new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + orders.size());

	}
	
	private void FindOrderByDate(String Date, ArrayList<Order> orders) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select orderID, customerID, orderDate, orderStatus from orders where OrderDate = '" + Date
				+ "' order by orderID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		orders.clear();
		while (rs.next())
			orders.add(new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + orders.size());

	}


	public void updateOrderCustomer(String OID, String CID) {

		try {
			System.out.println("update Orders set customerID = '" + CID + "' where orderID = '" + OID + "'");
			connectDB();
			ExecuteStatement("update Orders set customerID = '" + CID + "' where orderID = '" + OID + "'");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateOrderDate(String ID, String date) {

		try {
			System.out.println("update Orders set orderDate = '" + date + "' where orderID = '" + ID + "'");
			connectDB();
			ExecuteStatement("update Orders set orderDate = '" + date + "' where orderID = '" + ID + "'");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateOrderStatus(String ID, String status) {

		try {
			System.out.println("update Orders set orderStatus = '" + status + "' where orderID = " + ID + "'");
			connectDB();
			ExecuteStatement("update Orders set orderStatus = '" + status + "' where orderID = " + ID + "'");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void deleteOrderRow(Order row) throws SQLException {
		// TODO Auto-generated method stub

		try {
			System.out.println("delete from Orders where orderID ='" + row.getOrderID() + "';");
			connectDB();
			ExecuteStatement("delete from Orders where orderID ='" + row.getOrderID() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			throw e;
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}

	// showDialog is used to confirm clear process
	private void showOrderDialog(Window owner, Modality modality, TableView<Order> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			try {
				for (Order row : orderDataList) {
					deleteOrderRow(row);
					table.refresh();
				}
				table.getItems().removeAll(orderDataList);
				stage.close();
			} catch (SQLException n) {
				stage.close();
				ShowMessage(owner, modality, n.getMessage());
			}
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	// confirmDeletion is used to confirm deletion process
	private void confirmOrderDeletion(Window owner, Modality modality, ArrayList<Order> rows, TableView<Order> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			rows.forEach(row -> {
				try {
					deleteOrderRow(row);
					table.getItems().remove(row);
					table.refresh();
					stage.close();
				} catch (SQLException n) {
					stage.close();
					ShowMessage(owner, modality, n.getMessage());
				}
			});
		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}
	
	
	// Employee Order 

	private BorderPane employee_orderScreen (Stage stage) {
		
	// the OrderProducts table view with its columns
			TableView<EmployeeOrder> dataTable = new TableView<EmployeeOrder>();
			dataTable.setPadding(new Insets(20));
			TableColumn<EmployeeOrder, String> EIDCol = new TableColumn<EmployeeOrder, String>("employeeID");
			TableColumn<EmployeeOrder, String> OIDCol = new TableColumn<EmployeeOrder, String>("orderID");

			dataTable.setEditable(true);
			dataTable.setMinSize(500, 500);
			dataTable.setMaxSize(500, 500);

			// name of column for display
			EIDCol.setMinWidth(50);

			// to get the data from specific column
			EIDCol.setCellValueFactory(new PropertyValueFactory<EmployeeOrder, String>("employeeID"));

			// name of column for display
			EIDCol.setMinWidth(50);

			// to get the data from specific column
			EIDCol.setCellValueFactory(new PropertyValueFactory<EmployeeOrder, String>("employeeID"));
			
			
			
			
			OIDCol.setMinWidth(50);

			// to get the data from specific column
			OIDCol.setCellValueFactory(new PropertyValueFactory<EmployeeOrder, String>("orderID"));

			// name of column for display
			OIDCol.setMinWidth(50);

			// to get the data from specific column
			OIDCol.setCellValueFactory(new PropertyValueFactory<EmployeeOrder, String>("orderID"));
			

			

			dataTable.getColumns().addAll(EIDCol, OIDCol);

			// text fields for inserting new records
			final TextField addEMID = new TextField();
			final TextField addORID = new TextField();

			// labels for the text fields
			Label lblEMID = new Label("Employee ID: ");
			Label lblORID = new Label("Order ID: ");

			// create radioButtons (for opting a choice)
			Label buttonsLbl = new Label("What do you want to do?");
			RadioButton btnInsert = new RadioButton("Insert record   ");
			RadioButton btnDelete = new RadioButton("Delete selected record ");
			RadioButton btnClear = new RadioButton("Clear all records");
			RadioButton btnRefresh = new RadioButton("Refresh table");
			RadioButton btnViewAll = new RadioButton("Show all data");
			RadioButton btnSearch = new RadioButton("Search records");
			ToggleGroup group = new ToggleGroup();

			btnInsert.setToggleGroup(group);
			btnDelete.setToggleGroup(group);
			btnClear.setToggleGroup(group);
			btnRefresh.setToggleGroup(group);
			btnViewAll.setToggleGroup(group);
			btnSearch.setToggleGroup(group);

			// this box hold the toggle buttons in
			VBox optionsBox = new VBox(5);

			Button btnAdd = new Button("Add");
			Button btnCancel = new Button("Cancel");
			Button btnFind = new Button("Find");
			btnAdd.setFont(btnFont);
			btnCancel.setFont(btnFont);
			btnFind.setFont(btnFont);

			// this box holds the insertion text fields with their labels
			HBox insertionTextBox = new HBox(5);
			insertionTextBox.getChildren().addAll(lblEMID, addEMID, lblORID, addORID);
			insertionTextBox.setPadding(new Insets(10));
			insertionTextBox.setAlignment(Pos.CENTER);

			HBox insertionButtonsBox = new HBox(5);
			insertionButtonsBox.getChildren().addAll(btnAdd, btnFind, btnCancel);
			insertionButtonsBox.setAlignment(Pos.CENTER);

			VBox insertionBox = new VBox(5);
			insertionBox.getChildren().addAll(insertionTextBox, insertionButtonsBox);
			insertionBox.setAlignment(Pos.CENTER);

			// root pane
			BorderPane root = new BorderPane();
			root.setStyle("-fx-background-color: white;");
			stage.setFullScreen(true);

			// set title
			Label label = new Label("Employee_Order Management Screen");
			label.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 30));
			label.setTextFill(Color.BLUE);
			label.setTextAlignment(TextAlignment.CENTER);

			HBox titleBox = new HBox();
			titleBox.getChildren().add(label);
			titleBox.setAlignment(Pos.CENTER);
			root.setTop(titleBox);
			titleBox.setPadding(new Insets(30));

			root.setPadding(new Insets(20));

			addEMID.setPromptText("Employee ID");
			addEMID.setMaxWidth(EIDCol.getPrefWidth());

			addORID.setMaxWidth(EIDCol.getPrefWidth());
			addORID.setPromptText("Order ID");

			

			addEMID.setDisable(true);
			addORID.setDisable(true);
			btnAdd.setDisable(true);
			btnCancel.setDisable(true);
			btnFind.setDisable(true);

			btnInsert.setFont(btnFont);
			btnDelete.setFont(btnFont);
			btnClear.setFont(btnFont);
			btnRefresh.setFont(btnFont);
			btnViewAll.setFont(btnFont);
			btnSearch.setFont(btnFont);

			buttonsLbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

			optionsBox.setPadding(new Insets(35));

			GridPane pane = new GridPane();
			pane.add(buttonsLbl, 0, 0);
			pane.add(btnInsert, 0, 1);
			pane.add(btnDelete, 0, 2);
			pane.add(btnRefresh, 0, 3);
			pane.add(btnViewAll, 0, 4);
			pane.add(btnClear, 0, 5);
			pane.add(btnSearch, 0, 6);
			pane.setVgap(10);
			pane.setPadding(new Insets(20));

			optionsBox.getChildren().add(pane);

			// this box hold the data table with the insertion box
			VBox tableBox = new VBox();
			tableBox.getChildren().addAll(dataTable, insertionBox);

			HBox box = new HBox(5);
			box.getChildren().addAll(tableBox, optionsBox);
			box.setAlignment(Pos.CENTER);
			root.setCenter(box);

			lblEMID.setFont(btnFont);
			lblORID.setFont(btnFont);

			btnDelete.setOnAction((ActionEvent e) -> {
				ObservableList<EmployeeOrder> selectedEmployeeOrderRows = dataTable.getSelectionModel().getSelectedItems();
				if (!selectedEmployeeOrderRows.isEmpty()) {
					ArrayList<EmployeeOrder> rows = new ArrayList<>(selectedEmployeeOrderRows);
					//confirmOrderProductDeletion(stage, NONE, rows, dataTable);
					confirmEmployeeOrderDeletion(stage, NONE, rows, dataTable);
				}
			});

			btnRefresh.setOnAction((ActionEvent e) -> {
				dataTable.refresh();
			});

			btnViewAll.setOnAction(e -> {
				dataTable.setItems(employeeOrderDataList);
			});

			btnSearch.setOnAction(e -> {
				addEMID.setDisable(false);
				addORID.setDisable(false);
				btnCancel.setDisable(false);
				btnAdd.setDisable(true);
				btnFind.setDisable(false);
			});

			btnAdd.setOnAction((ActionEvent e) -> {

				addEMID.setDisable(true);
				addORID.setDisable(true);
				btnAdd.setDisable(true);
				btnCancel.setDisable(true);
				try {
					if (btnInsert.isSelected()) {
						EmployeeOrder op;
						
							op = new EmployeeOrder(addEMID.getText(), addORID.getText());
						insertEmployeeOrderData(op);
						employeeOrderDataList.add(op);
						btnInsert.setSelected(false);
					}
				} catch (SQLException e1) {
					ShowMessage(stage, NONE, e1.getMessage());
				} catch (NumberFormatException e2) {
					
				}
				addEMID.clear();
				addORID.clear();
			});

			btnCancel.setOnAction(e -> {
				addEMID.clear();
				addORID.clear();
				addEMID.setDisable(true);
				addORID.setDisable(true);
				btnAdd.setDisable(true);
				btnCancel.setDisable(true);
				btnFind.setDisable(true);
				btnInsert.setSelected(false);
				btnSearch.setSelected(false);
			});

			btnInsert.setOnAction(e -> {
				btnCancel.setDisable(false);
				addEMID.setDisable(false);
				addORID.setDisable(false);
				btnAdd.setDisable(false);
				btnFind.setDisable(true);
			});

			btnClear.setOnAction((ActionEvent e) -> {
				showEmployeeOrderDialog(stage, NONE, dataTable);
			});

			btnFind.setOnAction(e -> {
				addEMID.setDisable(true);
				addORID.setDisable(true);
				btnAdd.setDisable(true);
				btnCancel.setDisable(true);

				ArrayList<EmployeeOrder> op = new ArrayList<>();
				ObservableList<EmployeeOrder> opList;

				try {
					if (!addEMID.getText().isBlank() && !addORID.getText().isBlank()) {

						FindEmployeeOrderByEIDAndOID(addEMID.getText(), addORID.getText(), op);
						opList = FXCollections.observableArrayList(op);
						dataTable.setItems(opList);

					} else if (!addEMID.getText().isBlank() && addORID.getText().isBlank()) {
						FindEmployeeOrderByEMID(addEMID.getText(), op);
						
						opList = FXCollections.observableArrayList(op);
						dataTable.setItems(opList);

					} else if (addEMID.getText().isBlank() && !addORID.getText().isBlank()) {
						FindEmployeeOrderByORID(addORID.getText(), op);

						opList = FXCollections.observableArrayList(op);
						dataTable.setItems(opList);

					}

					btnFind.setDisable(true);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				addEMID.clear();
				addORID.clear();
				btnViewAll.setDisable(false);
				btnSearch.setSelected(false);
			});
			return root;		
	}


	
	private void getEmployeeOrderData() throws SQLException , ClassNotFoundException {
		
		String SQL;
		
		connectDB();
		System.out.println("Connetion established");
		
		SQL = "select EmployeeID , OrderID from employee_order order by EmployeeID  ";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		
		employeeOrderData.clear();
		while (rs.next())
			employeeOrderData.add(new EmployeeOrder (rs.getString(1) , rs.getString(2)) );
		
		rs.close();
		stmt.close();
		con.close();
		System.out.println("Connection closed"+employeeOrderData.size());
		
	}
	
	private void insertEmployeeOrderData(EmployeeOrder employee_order) throws SQLIntegrityConstraintViolationException {
		try {
			System.out.println(
					"Insert into employee_order (EmployeeID, OrderID) values ('" + employee_order.getEmployeeID()
							+ "','" + employee_order.getOrderID() +"');");

			connectDB();
			ExecuteStatement(
					"Insert into employee_order (EmployeeID, OrderID) values ('" + employee_order.getEmployeeID()
							+ "','" + employee_order.getOrderID() +"');");
			con.close();
			System.out.println("Connection closed" + employeeOrderData.size());

		} catch (SQLIntegrityConstraintViolationException n) {
			throw n;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
	}
	private void deleteEmployeeOrderRow(EmployeeOrder row) throws SQLException {

		try {
			System.out.println("delete from employee_order where EmployeeID ='" + row.getEmployeeID() + "' and OrderID = '"
					+ row.getOrderID() + "';");
			connectDB();
			ExecuteStatement("delete from employee_order where EmployeeID ='" + row.getEmployeeID() + "' and OrderID = '"
					+ row.getOrderID() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			throw e;
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}
	
	// showDialog is used to confirm clear process
		private void showEmployeeOrderDialog(Window owner, Modality modality, TableView<EmployeeOrder> table) {
			// Create a Stage with specified owner and modality
			Stage stage = new Stage();
			stage.initOwner(owner);
			stage.initModality(modality);
			// Label modalityLabel = new Label(modality.toString());

			Button yesButton = new Button("Confirm");
			yesButton.setOnAction(e -> {
				try {
					for (EmployeeOrder row : employeeOrderDataList) {
						deleteEmployeeOrderRow(row);
						table.refresh();
					}
					table.getItems().removeAll(employeeOrderDataList);
					stage.close();
				} catch (SQLException n) {
					stage.close();
					ShowMessage(owner, modality, n.getMessage());
				}
			});

			Button noButton = new Button("Cancel");
			noButton.setOnAction(e -> stage.close());

			HBox root = new HBox();
			root.setPadding(new Insets(10, 10, 10, 10));
			root.setAlignment(Pos.CENTER);
			root.setSpacing(10);

			root.getChildren().addAll(yesButton, noButton);
			Scene scene = new Scene(root, 250, 100);
			stage.setScene(scene);
			stage.setTitle("Confirm Delete?");
			stage.show();
		}
		
		private void confirmEmployeeOrderDeletion(Window owner, Modality modality, ArrayList<EmployeeOrder> rows,
				TableView<EmployeeOrder> table) {
			// Create a Stage with specified owner and modality
			Stage stage = new Stage();
			stage.initOwner(owner);
			stage.initModality(modality);

			Button yesButton = new Button("Confirm");
			yesButton.setOnAction(e -> {
				rows.forEach(row -> {
					try {
						deleteEmployeeOrderRow(row);
						table.getItems().remove(row);
						table.refresh();
						stage.close();
					} catch (SQLException n) {
						stage.close();
						ShowMessage(owner, modality, n.getMessage());
					}
				});
			});

			Button noButton = new Button("Cancel");
			noButton.setOnAction(e -> stage.close());

			HBox root = new HBox();
			root.setPadding(new Insets(10, 10, 10, 10));
			root.setAlignment(Pos.CENTER);
			root.setSpacing(10);

			root.getChildren().addAll(yesButton, noButton);
			Scene scene = new Scene(root, 250, 100);
			stage.setScene(scene);
			stage.setTitle("Confirm Delete?");
			stage.show();
		}

	
		private void FindEmployeeOrderByEMID(String EMID, ArrayList<EmployeeOrder> op)throws SQLException, ClassNotFoundException {
			// TODO Auto-generated method stub

			String SQL;

			connectDB();
			System.out.println("Connection established");

			SQL = "select EmployeeID, OrderID from employee_order where EmployeeID = '" + EMID
					+ "' order by EmployeeID;";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);

			op.clear();
			while (rs.next())
				op.add(new EmployeeOrder(rs.getString(1), rs.getString(2)));

			rs.close();
			stmt.close();

			con.close();
			System.out.println("Connection closed" + op.size());
		}
		
		private void FindEmployeeOrderByORID(String ORID, ArrayList<EmployeeOrder> op)throws SQLException, ClassNotFoundException {
			// TODO Auto-generated method stub

			String SQL;

			connectDB();
			System.out.println("Connection established");

			SQL = "select EmployeeID, OrderID from employee_order where OrderID = '" + ORID
					+ "' order by OrderID;";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);

			op.clear();
			while (rs.next())
				op.add(new EmployeeOrder(rs.getString(1), rs.getString(2)));

			rs.close();
			stmt.close();

			con.close();
			System.out.println("Connection closed" + op.size());
		}
		
		
		private void FindEmployeeOrderByEIDAndOID(String EID, String OID, ArrayList<EmployeeOrder> op)
				throws SQLException, ClassNotFoundException {
			// TODO Auto-generated method stub

			String SQL;

			connectDB();
			System.out.println("Connection established");

			SQL = "select EmployeeID, OrderID from employee_order where EmployeeID = '" + EID + "' and OrderID = '"
					+ OID + "' order by productID;";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);

			op.clear();
			while (rs.next())
				op.add(new EmployeeOrder(rs.getString(1), rs.getString(2)));

			rs.close();
			stmt.close();

			con.close();
			System.out.println("Connection closed" + op.size());
		}
	

}
