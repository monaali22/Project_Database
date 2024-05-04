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

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;

/**
 * A JavaFX ConnectDB Application
 */
public class FullDB extends Application {
	/**
	 * @param args the command line arguments
	 * 
	 * 
	 */
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

	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "mona2632003MA";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "FinallyProject";
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

	public static void main(String[] args) {

		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
//		customerData = new ArrayList<>();
//		categoryData = new ArrayList<>();
//		customerPhoneData = new ArrayList<>();
//		categoryBrandData = new ArrayList<>();
//		categoryAudienceData = new ArrayList<>();

		try {
			BorderPane pane = new BorderPane();
			pane.setStyle("-fx-background-color: white;");
			pane.setLeft(tabs);
			Scene scene = new Scene(pane);
			stage.setScene(scene);

			tabs.setSide(Side.LEFT);
			tabs.getTabs().addAll(tabCustomer, tabEmployee, tabProduct, tabCategory, tabSupplier, tabOrder,
					tabOrderProduct);

			getCustomerData();
			getCategoryData();
			getOrderProductData();

			// convert data from arraylist to observable arraylist
			customerDataList = FXCollections.observableArrayList(customerData);
			categoryDataList = FXCollections.observableArrayList(categoryData);
			customerPhoneDataList = FXCollections.observableArrayList(customerPhoneData);
			categoryBrandDataList = FXCollections.observableArrayList(categoryBrandData);
			categoryAudienceDataList = FXCollections.observableArrayList(categoryAudienceData);
			orderProductDataList = FXCollections.observableArrayList(orderProductData);

			BorderPane customerScreen = CustomerScreen(stage);
			BorderPane categoryScreen = categoryScreen(stage);
			BorderPane order_productScreen = order_productScreen(stage);

			tabCustomer.setContent(customerScreen);
			tabCategory.setContent(categoryScreen);
			tabOrderProduct.setContent(order_productScreen);

			stage.show();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	//______________________________________________________________________________________________________________________________________

	@SuppressWarnings("unchecked")

	// Customer methods
	private BorderPane CustomerScreen(Stage stage) {

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
			((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setCustomerName(t.getNewValue()); // display only
			updateCustomerName(t.getRowValue().getCustomerID(), t.getNewValue());
		});

		emailCol.setMinWidth(150);
		emailCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));

		emailCol.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

		emailCol.setOnEditCommit((CellEditEvent<Customer, String> t) -> {
			((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());
			updateCustomerEmail(t.getRowValue().getCustomerID(), t.getNewValue());
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
			((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setCustomerAddress(t.getNewValue());
			updateCustomerAddress(t.getRowValue().getCustomerID(), t.getNewValue());
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
		RadioButton btnSearch = new RadioButton("Search customers");
		ToggleGroup group = new ToggleGroup();

		btnInsert.setToggleGroup(group);
		btnInsertPhone.setToggleGroup(group);
		btnDelete.setToggleGroup(group);
		btnDeletePhone.setToggleGroup(group);
		btnClear.setToggleGroup(group);
		btnClear2.setToggleGroup(group);
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
		btnSearch.setFont(btnFont);

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
		pane.add(btnSearch, 0, 9);
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

		btnSearch.setOnAction(e -> {
			addID.setDisable(false);
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

				if (!addPhone.getText().isBlank() && addPhone.getText().length() == 10) {
					Customer_PhoneNumber phone = new Customer_PhoneNumber(addID.getText(), addPhone.getText());
					insertCustomerPhone(phone);
					customerPhoneDataList.add(phone);
				} else if (addPhone.getText().length() != 10) {
					showMessage(stage, NONE, "Phone number accepts a number of 10 digits only");
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
			btnSearch.setSelected(false);
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
				if (!addName.getText().isBlank() && !addID.getText().isBlank()) {

					FindCustomerByIDAndName(addID.getText(), addName.getText(), customers);
					FindCustomerPhoneByIDAndName(addID.getText(), addName.getText(), phones);
					customerList = FXCollections.observableArrayList(customers);
					customerPhoneList = FXCollections.observableArrayList(phones);
					dataTable.setItems(customerList);
					phonesTable.setItems(customerPhoneList);

				} else if (!addName.getText().isBlank() && addID.getText().isBlank()) {

					FindCustomerByName(addName.getText(), customers);
					FindCustomerPhoneByName(addName.getText(), phones);
					customerList = FXCollections.observableArrayList(customers);
					customerPhoneList = FXCollections.observableArrayList(phones);
					dataTable.setItems(customerList);
					phonesTable.setItems(customerPhoneList);

				} else if (addName.getText().isBlank() && !addID.getText().isBlank()) {

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
			btnSearch.setSelected(false);
		});
		return root;

	}

	//________________________________________________________________________________________________________________________________________
	
	
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

	//__________________________________________________________________________________________________________________________________________
	
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

	//__________________________________________________________________________________________________________________________________________
	
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

	//_____________________________________________________________________________________________________________________________________
	
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

	//______________________________________________________________________________________________________________________________________
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

	//______________________________________________________________________________________________________________________________________
	
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

	//_________________________________________________________________________________________________________________________________
	
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

	//___________________________________________________________________________________________________________________________________
	
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
	
	//__________________________________________________________________________________________________________________________________

	private void insertCustomerPhone(Customer_PhoneNumber customerPhone)
			throws SQLIntegrityConstraintViolationException {

		try {
			System.out.println("Insert into Customers (customerID, Phonenumber) values ('"
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

//	private void getCustomerPhoneData() throws SQLException, ClassNotFoundException {
//		// TODO Auto-generated method stub
//
//		String SQL;
//
//		connectDB();
//		System.out.println("Connection established");
//
//		SQL = "select customerID, PhoneNumber from Customer_PhoneNumber order by customerID;";
//		Statement stmt = con.createStatement();
//		ResultSet rs = stmt.executeQuery(SQL);
//
//		customerPhoneData.clear();
//		while (rs.next())
//			customerPhoneData.add(new Customer_PhoneNumber(rs.getString(1), rs.getString(2)));
//
//		rs.close();
//		stmt.close();
//
//		con.close();
//		System.out.println("Connection closed" + customerPhoneData.size());
//	}

	
	//__________________________________________________________________________________________________________________________________
	
	private void connectDB() throws ClassNotFoundException, SQLException {

		dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
		Properties p = new Properties();
		p.setProperty("user", dbUsername);
		p.setProperty("password", dbPassword);
		p.setProperty("useSSL", "false");
		p.setProperty("autoReconnect", "true");
		//Class.forName("com.mysql.jdbc.Driver");
		 Class.forName("com.mysql.cj.jdbc.Driver");

		con = DriverManager.getConnection(dbURL, p);

	}
	
	//________________________________________________________________________________________________________________________________

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

	//___________________________________________________________________________________________________________________________________
	
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

	//______________________________________________________________________________________________________________________________________
	
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

//	public void updateCustomerPhoneNumber(String ID, String num) {
//
//		try {
//			System.out.println("update Customers set phoneNumber = '" + num + "' where customerID = '" + ID + "'");
//			connectDB();
//			ExecuteStatement("update Customers set phoneNumber = '" + num + "' where customerID = '" + ID + "';");
//			con.close();
//			System.out.println("Connection closed");
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}

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
	
	//_______________________________________________________________________________________________________________________________________

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
	
	//____________________________________________________________________________________________________________________________________

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
	
	//______________________________________________________________________________________________________________________________________

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
	
	//_________________________________________________________________________________________________________________________________

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
	private void confirmCustomerDeletion(Window owner, Modality modality, ArrayList<Customer> rows,TableView<Customer> table) {
		
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
	private void confirmCustomerPhoneDeletion(Window owner, Modality modality, ArrayList<Customer_PhoneNumber> rows,TableView<Customer_PhoneNumber> table) {
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
	
	//____________________________________________________________________________________________________________________________________
	

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
	//_--------------------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------------------

	
	
	
	
	
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
			((Category) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setCategoryName(t.getNewValue()); // display only
			updateCategoryName(t.getRowValue().getCategoryID(), t.getNewValue());
		});

		purposeCol.setMinWidth(230);
		purposeCol.setCellValueFactory(new PropertyValueFactory<Category, String>("mainPurpose"));

		purposeCol.setCellFactory(TextFieldTableCell.<Category>forTableColumn());

		purposeCol.setOnEditCommit((CellEditEvent<Category, String> t) -> {
			((Category) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMainPurpose(t.getNewValue());
			updateCategoryPurpose(t.getRowValue().getCategoryID(), t.getNewValue());
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
			System.out.println("Insert into Category_popularBrands (categoryID, popularBrands) values ('"
					+ brand.getCategoryID() + "','" + brand.getBrand() + "');");

			connectDB();
			ExecuteStatement("Insert into Category_popularBrands (categoryID, popularBrands) values ('"
					+ brand.getCategoryID() + "','" + brand.getBrand() + "');");
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
			System.out.println("delete from Category_popularBrands where categoryID ='" + row.getCategoryID() + "';");
			connectDB();
			ExecuteStatement("delete from Category_popularBrands where categoryID ='" + row.getCategoryID() + "';");
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
			System.out.println("delete from Category_TargetAudience where categoryID ='" + row.getCategoryID() + "';");
			connectDB();
			ExecuteStatement("delete from Category_TargetAudience where categoryID ='" + row.getCategoryID() + "';");
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
			((OrderProducts) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setQuantity(t.getNewValue());
			updateOrder_produtQuantity(t.getRowValue().getOrderID(), t.getRowValue().getProductID(), t.getNewValue());
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

	private void showMessage(Stage stage, Modality none, String string) {
		// TODO Auto-generated method stub

	}

	private void insertOrderProductData(OrderProducts order_product) throws SQLIntegrityConstraintViolationException {

		try {
			System.out.println(
					"Insert into order_product (orderID, productID, quantity) values ('" + order_product.getOrderID()
							+ "','" + order_product.getProductID() + "','" + order_product.getQuantity() + "');");

			connectDB();
			ExecuteStatement(
					"Insert into order_product (orderID, productID, quantity) values ('" + order_product.getOrderID()
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

		SQL = "select orderID, productID, quantity from order_product order by productID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		orderProductData.clear();
		while (rs.next())
			orderProductData
					.add(new OrderProducts(rs.getString(1), rs.getString(2), Integer.parseInt(rs.getString(3))));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + orderProductData.size());
	}

	public void updateOrder_produtQuantity(String orderID, String productID, int quantity) {

		try {
			System.out.println("update order_product set quantity = '" + quantity + "' where orderID = '" + orderID
					+ "' and productID = '" + productID + "';");
			connectDB();
			ExecuteStatement("update order_product set quantity = '" + quantity + "' where orderID = '" + orderID
					+ "' and productID = '" + productID + "';");
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
			System.out.println("delete from order_product where productID ='" + row.getProductID() + "' and orderID = '"
					+ row.getOrderID() + "';");
			connectDB();
			ExecuteStatement("delete from order_product where productID ='" + row.getProductID() + "' and orderID = '"
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

		SQL = "select orderID, productID, quantity from order_product where orderID = '" + OID + "' and productID = '"
				+ PID + "' order by productID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		op.clear();
		while (rs.next())
			op.add(new OrderProducts(rs.getString(1), rs.getString(2), Integer.parseInt(rs.getString(3))));

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

		SQL = "select orderID, productID, quantity from order_product where orderID = '" + OID
				+ "' order by productID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		op.clear();
		while (rs.next())
			op.add(new OrderProducts(rs.getString(1), rs.getString(2), Integer.parseInt(rs.getString(3))));

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

		SQL = "select orderID, productID, quantity from order_product where productID = '" + PID
				+ "' order by productID;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		op.clear();
		while (rs.next())
			op.add(new OrderProducts(rs.getString(1), rs.getString(2), Integer.parseInt(rs.getString(3))));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + op.size());
	}
}