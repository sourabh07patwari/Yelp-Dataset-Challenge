
package a3_db;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;


public class MainController implements Initializable {
    
    @FXML private Label label;
    @FXML private ListView listView1, listView2, listView3;
    @FXML ObservableList<String> lis1, lis2, lis3;
    @FXML ArrayList<String> mainBusinessCategoryArrayList;
    @FXML ArrayList<String> listView1CheckedItems = new ArrayList<String>();
    @FXML ArrayList<String> listView2CheckedItems = new ArrayList<String>();
    @FXML ArrayList<String> listView3CheckedItems = new ArrayList<String>();
    @FXML String mainBusinessCategorySimpleArray[];
    
    @FXML private TableView<Business> tableView1;
    @FXML private TableColumn<Business, String> table1ColumnSNO;
    @FXML private TableColumn<Business, String> table1ColumnName;
    @FXML private TableColumn<Business, String> table1ColumnCity;
    @FXML private TableColumn<Business, String> table1ColumnState;
    @FXML private TableColumn<Business, String> table1ColumnStars;
    @FXML ObservableList<Business> tableList1;
   
    @FXML ComboBox comboBox1, comboBox2, comboBox3, comboBox4,comboBox5,comboBox6 ;
    @FXML ObservableList<String> comboBoxWeek, comboBoxSelectAttribute, comboBoxState, comboBoxCity;
    @FXML ObservableList<Integer> comboBoxTimeFrom, comboBoxTimeTo;    
    @FXML ArrayList<Integer> timeArrayList;
    @FXML int timeListSimpleArray[];
    
    @FXML HashMap<String,String> locationHashMap;
    
    @FXML private Button closeButton, searchButton;

    @FXML private void getState(){
        ArrayList<String> ls = new ArrayList<>(); 
        for (String key : locationHashMap.keySet()) {
            if(!ls.contains(locationHashMap.get(key))){
                ls.add(locationHashMap.get(key));
            }
         }
        comboBoxState.setAll(ls);
    }
    @FXML private void getCity(String state){
        ArrayList<String> ls = new ArrayList<>(); 
        for (String key : locationHashMap.keySet()) {
            if (state.equals(locationHashMap.get(key))) {
                if(!ls.contains(key)){
                    ls.add(key);
                }
            }
         }
        comboBoxCity.setAll(ls);
    }
    
    
    @FXML private HashMap<String,String> find_location() throws Exception
	{
		HashMap<String,String> newmap = new HashMap<>();
		String mainQuery = "SELECT DISTINCT B.CITY, B.STATE FROM BUSINESS B";
		Connection conn = null;
		ResultSet fireResult = null;
		java.sql.Statement stmt = null;
		conn = getDBConnection();
		stmt = conn.prepareStatement(mainQuery);
		fireResult = stmt.executeQuery(mainQuery);
		ResultSetMetaData meta = fireResult.getMetaData();
		int nCol = meta.getColumnCount();
		
		while (fireResult.next()) 
		{
			
			String[] row = new String[nCol];
			for (int iCol = 1; iCol <= nCol; iCol++) 
			{	 
					row[iCol - 1] = fireResult.getObject(iCol).toString();
				}
			newmap.put(row[0], row[1]);
		}
		return newmap;
	}
    
    
    
    @FXML private void closeButtonAction(){

        Stage stage = (Stage) closeButton.getScene().getWindow();
      
        stage.close();
    }
    
    
    @FXML private void tableView1SelectAction(Event event, String bid) throws IOException, SQLException {
        
        
    
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Review.fxml"));
        Parent home_page_parent = loader.load();
        Scene home_page_scene = new Scene(home_page_parent);
        ReviewController controller = loader.getController();
        
        
        
        
        controller.initObservableReviewList(getReviewsArrayList(bid));
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        
        app_stage.setScene(home_page_scene);
        app_stage.show();
     }
    
    
    @FXML private String checkNull(Object obj){
        return obj == null ? "null" : obj.toString();
    }
    @FXML private void searchButtonAction(ActionEvent event) throws IOException, Exception {
            tableList1.clear();
            
            ArrayList<String[]> tempBusinessTableData = new ArrayList<>();
            
            tempBusinessTableData = getBusinessList(
                  listView1CheckedItems,
                  listView2CheckedItems,
                  listView3CheckedItems,
                  checkNull(comboBox1.getSelectionModel().getSelectedItem()),
                  checkNull(comboBox3.getSelectionModel().getSelectedItem()),
                  checkNull(comboBox2.getSelectionModel().getSelectedItem()),
                  comboBox4.getSelectionModel().getSelectedItem().toString()
             );
            Collections.sort(tempBusinessTableData,new Comparator<String[]>() {
            public int compare(String[] strings, String[] otherStrings) {
                return strings[1].compareTo(otherStrings[1]);
            }
                });
           for(int i = 0; i < tempBusinessTableData.size(); i++){
             String[] str = tempBusinessTableData.get(i);
             tableList1.add(new Business(new Integer(i+1),str[0] ,str[1], str[2], str[3], str[4]));
            }
          
          listView2CheckedItems.clear();
          listView3CheckedItems.clear();
          lis2.clear();
          lis3.clear();
     }
    
    
    @FXML private ArrayList<Review> getReviewsArrayList(String a) throws SQLException {
        ArrayList<Review> listOfReviewAttributes = new ArrayList<Review>();
        Connection conn = null;
        ResultSet result = null;
        java.sql.Statement stmt = null;
        if (a != null){
            String mainQuery = "SELECT R.R_DATE, R.STARS, R.R_TEXT, R.USER_ID, R.USEFUL_VOTE FROM REVIEWS R WHERE R.BID LIKE ";
            mainQuery = mainQuery + "'" + a + "'";
            conn = getDBConnection();
            stmt = conn.prepareStatement(mainQuery);
            result = stmt.executeQuery(mainQuery);		
            int nCol = result.getMetaData().getColumnCount();
            
            while (result.next()){
                String[] row = new String[nCol];
                for (int iCol = 1; iCol <= nCol; iCol++) {
                    if (iCol == 3) {
                        StringBuffer strOut = new StringBuffer();
			String aux;
                        try {
			BufferedReader br = new BufferedReader(result.getClob("R_TEXT").getCharacterStream());
			while ((aux = br.readLine()) != null) {
				strOut.append(aux);
				strOut.append(System.getProperty("line.separator"));
			}
			} catch (Exception e) {
			e.printStackTrace();
                        }
                        String clobStr = strOut.toString();
			
			row[iCol - 1] = clobStr;
                    }
                    else{
                    row[iCol - 1] = result.getObject(iCol).toString();
                    }
                    
                }
                listOfReviewAttributes.add(new Review(row[0],row[1],row[2],row[3],row[4]));
            }
        }
        return listOfReviewAttributes;
    }
    
    @FXML public ArrayList<String[]> getBusinessList(ArrayList<String> a, ArrayList<String> b, ArrayList<String> c, String d, String e, String f, String g) throws Exception
	{
		ArrayList<String> main_catarray = new ArrayList<String>();
		ArrayList<String> sub_catarray = new ArrayList<String>();
		ArrayList<String> att_catarray = new ArrayList<String>();
		ArrayList<String[]> bus_list = new ArrayList<String[]>();
		main_catarray = a;
		sub_catarray = b;
		att_catarray = c;
		String between_values;
		String b_v = g;
		if(b_v.equals("All Attributes"))
		{
			between_values = " INTERSECT ";
		}
		else
		{
			between_values = " UNION ";
		}
		
		
		String day = d;
		String to = e;
		String from = f;
                float from_sel, to_sel;
		if(!from.equals("null"))
                {
                   from_sel = Float.parseFloat(from); 
                }
		else
                {
                    from_sel = -1;
                }
                if(!to.equals("null"))
                {
                   to_sel = Float.parseFloat(to); 
                }
		else
                {
                     to_sel = 25;
                }
		
                
		
                
		Connection conn = null;
		ResultSet fireResult;
		java.sql.Statement stmt = null;
		String query = null;
                
                if(att_catarray.isEmpty() && sub_catarray.isEmpty() && main_catarray.isEmpty())
                {
                    String[] temp = {"1","nothing","nothing","nothing","nothin"};
                    bus_list.add(temp);
                    return bus_list;
                }
		if(!att_catarray.isEmpty())
		{
			String mainQuery = null;
			String select_query = " SELECT DISTINCT B.BID, B.B_NAME, B.CITY, B.STATE, B.STARS ";
			String from_query = " FROM B_MAIN_CATEGORY M, BUSINESS B, B_SUB_CATEGORY S, B_ATTRIBUTES A ";
			String where_query = " WHERE M.BID = B.BID AND M.BID = S.BID AND A.BID = B.BID ";

			if (!day.equals("null"))
			{
				from_query = from_query + ", B_HOURS H ";
				where_query = where_query + " AND B.BID = H.BID AND H.D_O_W LIKE '"+ day + "' AND H.FROM_H <= " + from_sel + " AND H.TO_H >=" + to_sel;
			}
			else
			{
				if(from_sel >= 0)
				{
					from_query = from_query + ", B_HOURS H ";
					where_query = where_query + " AND B.BID = H.BID AND H.FROM_H <= " + from_sel;
					if(to_sel <= 24)
					{
						where_query = where_query + " AND H.TO_H >= " + to_sel;
					}
				}
				else if(to_sel <= 24)
				{
					from_query = from_query + ", B_HOURS H ";
					where_query = where_query + " AND B.BID = H.BID AND H.TO_H >=" + to_sel;
				}
			}			
			
			query = select_query + from_query + where_query + " AND M.C_NAME LIKE ";
			mainQuery = query;
			String subquery2 = " AND A.A_NAME LIKE ";
				String subquery = " AND S.C_NAME LIKE ";

				for (int i = 0; i < main_catarray.size(); i++) 
				{
					for(int j = 0; j < sub_catarray.size(); j++)
					{	
						for(int k = 0; k < att_catarray.size(); k++)
						{
							if (i == main_catarray.size() - 1 && j == sub_catarray.size() -1 && k == att_catarray.size() -1) 
							{
								query = query + "'" + main_catarray.get(i) + "'" + subquery + "'" + sub_catarray.get(j)+ "'" + subquery2 + "'" + att_catarray.get(k) + "'";
							} else 
							{
								query = query + "'" + main_catarray.get(i) + "'" + subquery + "'" + sub_catarray.get(j) + "'" + subquery2 + "'" + att_catarray.get(k) + "'" + between_values + mainQuery;
							}
						}
					}
				}
		}
		else if( !sub_catarray.isEmpty())
		{
			String mainQuery = null;
			String select_query = " SELECT DISTINCT B.BID, B.B_NAME, B.CITY, B.STATE, B.STARS ";
			String from_query = " FROM B_MAIN_CATEGORY M, BUSINESS B, B_SUB_CATEGORY S ";
			String where_query = "WHERE M.BID = B.BID AND M.BID = S.BID ";

			if (!day.equals("null"))
			{
				from_query = from_query + ", B_HOURS H ";
				where_query = where_query + " AND B.BID = H.BID AND H.D_O_W = '"+ day + "' AND H.FROM_H <= " + from_sel + " AND H.TO_H >=" + to_sel;
			}
			else
			{
				if(from_sel >= 0)
				{
					from_query = from_query + ", B_HOURS H ";
					where_query = where_query + " AND B.BID = H.BID AND H.FROM_H <= " + from_sel;
					if(to_sel <= 24)
					{
						where_query = where_query + " AND H.TO_H >= " + to_sel;
					}
				}
				else if(to_sel <= 24)
				{
					from_query = from_query + ", B_HOURS H ";
					where_query = where_query + " AND B.BID = H.BID AND H.TO_H >=" + to_sel;
				}
			}			
			
			query = select_query + from_query + where_query + " AND M.C_NAME = ";
			mainQuery = query;
				String subquery = " AND S.C_NAME = ";
				for (int i = 0; i < main_catarray.size(); i++) 
				{
					for(int j = 0; j < sub_catarray.size(); j++)
					{	
						if (i == main_catarray.size() - 1 && j == sub_catarray.size() -1) 
						{
							query = query + "'" + main_catarray.get(i) + "'" + subquery + "'" + sub_catarray.get(j) + "'" ;
						} else 
						{
							query = query + "'" + main_catarray.get(i) + "'" + subquery + "'" + sub_catarray.get(j) + "'" + between_values + mainQuery;
						}
					}
				}
				
		}
		else if(!main_catarray.isEmpty())
		{
			String select_query = " SELECT DISTINCT B.BID, B.B_NAME, B.CITY, B.STATE, B.STARS ";
			String from_query = " FROM B_MAIN_CATEGORY M, BUSINESS B ";
			String where_query = "WHERE M.BID = B.BID ";

			
			if (!day.equals("null"))
			{
				from_query = from_query + ", B_HOURS H ";
				where_query = where_query + " AND B.BID = H.BID AND H.D_O_W = '"+ day + "' AND H.FROM_H <= " + from_sel + " AND H.TO_H >=" + to_sel;
			}
			else
			{
				if(from_sel >= 0)
				{
					from_query = from_query + ", B_HOURS H ";
					where_query = where_query + " AND B.BID = H.BID AND H.FROM_H <= " + from_sel;
					if(to_sel <= 24)
					{
						where_query = where_query + " AND H.TO_H >= " + to_sel;
					}
				}
				else if(to_sel <= 24)
				{
					from_query = from_query + ", B_HOURS H ";
					where_query = where_query + " AND B.BID = H.BID AND H.TO_H >=" + to_sel;
				}
			}
			
			
			query = select_query + from_query + where_query + " AND M.C_NAME = ";
			String mainQuery = query;
			for (int i = 0; i < main_catarray.size(); i++) 
			{
				if (i == main_catarray.size() - 1) 
				{
					query = query + "'" + main_catarray.get(i) + "'";
				} else {
					query = query + "'" + main_catarray.get(i) + "'" + between_values + mainQuery;
				}
			}	
		}
		
	
		conn = getDBConnection();
		stmt = conn.prepareStatement(query);
		fireResult = stmt.executeQuery(query);
                
		ResultSetMetaData meta = fireResult.getMetaData();
                int nCol = meta.getColumnCount();
                
		while (fireResult.next()) 
		{
                    String[] row;
                row = new String[5];
			for (int iCol = 1; iCol <= nCol; iCol++) {
				 
				if (iCol != nCol)
                                {
                                  
					row[iCol - 1] = fireResult.getString(iCol).toString();
                                       
                                }else{
                                    
                                    Double tempDouble = fireResult.getDouble(iCol);                                 
                                    row[iCol - 1] = String.valueOf(tempDouble);
                                    
                                }

				
                                 
			}
                       
                       bus_list.add(row);
                       
		}
               
		return bus_list;
	}
    @FXML public ArrayList<String> getExtraAttributes (ArrayList<String> a, ArrayList<String> b, String c) throws Exception
	{
		ArrayList<String> att_catarray = new ArrayList<String>();
		ArrayList<String> main_catarray = new ArrayList<String>();
		main_catarray = b;
		String between_values;
                String b_v = c;
		if(b_v.equals("All Attributes"))
		{
			between_values = " INTERSECT ";
		}
		else
		{
			between_values = " UNION ";
		}
		between_values = " " + between_values + " ";
		
		Connection conn = null;
		ResultSet fireResult = null;
		java.sql.Statement stmt = null;
		if (a != null) 
		{
			String mainQuery = "SELECT DISTINCT A.A_NAME FROM B_MAIN_CATEGORY M, B_SUB_CATEGORY S, B_ATTRIBUTES A WHERE M.BID = S.BID AND S.BID = A.BID AND M.C_NAME LIKE ";
			
			String query = mainQuery;
			String subquery = " AND S.C_NAME LIKE ";
			for (int i = 0; i < main_catarray.size(); i++) 
			{
				for(int j = 0; j < a.size(); j++)
				{	
					if (i == main_catarray.size() - 1 && j == a.size() -1) 
					{
						query = query + "'" + main_catarray.get(i) + "'" + subquery + "'" + a.get(j) + "'" ;
						
					} else {
						query = query + "'" + main_catarray.get(i) + "'" + subquery + "'" + a.get(j) + "'" + between_values + mainQuery;
						
					}
				}
			}			
			
			conn = getDBConnection();
			stmt = conn.prepareStatement(query);
			fireResult = stmt.executeQuery(query);
			
			ResultSetMetaData meta = fireResult.getMetaData();
			while (fireResult.next()) 
			{
				for (int i = 1; i <= meta.getColumnCount(); i++) 
				{
					att_catarray.add(fireResult.getString(i));
				}
			}			
		}
		return att_catarray;
	}
        
    @FXML public ArrayList<String> getExtraMainAttributes (ArrayList<String> a, String b) throws Exception
	{
		ArrayList<String> sub_catarray = new ArrayList<String>();
		String between_values;
                String b_v = b;
		if(b_v.equals("All Attributes"))
		{
			between_values = " INTERSECT ";
		}
		else
		{
			between_values = " UNION ";
		}
		between_values = " " + between_values + " ";
		Connection conn = null;
		ResultSet fireResult = null;
		java.sql.Statement stmt = null;
		if (a != null) 
		{
			String mainQuery = "SELECT DISTINCT S.C_NAME FROM B_MAIN_CATEGORY M,B_SUB_CATEGORY S WHERE M.BID = S.BID AND M.C_NAME = ";
			String query = mainQuery;
			for (int i = 0; i < a.size(); i++) 
			{
				if (i == a.size() - 1) 
				{
					query = query + "'" + a.get(i) + "'";
				} else {
					query = query + "'" + a.get(i) + "'" + between_values + mainQuery;
				}
			}	
			conn = getDBConnection();
			stmt = conn.prepareStatement(query);
			fireResult = stmt.executeQuery(query);
			ResultSetMetaData meta = fireResult.getMetaData();
			while (fireResult.next()) 
			{
				for (int i = 1; i <= meta.getColumnCount(); i++) 
				{
					sub_catarray.add(fireResult.getString(i));
				}
			}
		}else{
                    return sub_catarray;
                }
		return sub_catarray;			
	}
    
    @FXML private Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:scott", "scott", "tiger");
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mainBusinessCategoryArrayList = new ArrayList<String>();
        mainBusinessCategorySimpleArray = new String[]{
            "Active Life"
            ,"Arts & Entertainment"
            ,"Automotive"
            ,"Car Rental"
            ,"Cafes"
            ,"Beauty & Spas"
            ,"Convenience Stores"
            ,"Dentists"
            ,"Doctors"
            ,"Drugstores"
            ,"Department Stores"
            ,"Education"
            ,"Event Planning & Services"
            ,"Flowers & Gifts"
            ,"Food"
            ,"Health & Medical"
            ,"Home Services"
            ,"Home & Garden"
            ,"Hospitals"
            ,"Hotels & Travel"
            ,"Hardware Stores"
            ,"Grocery"
            ,"Medical Centers"
            ,"Nurseries & Gardening"
            ,"Nightlife"
            ,"Restaurants"
            ,"Shopping"
            ,"Transportation"};
        for(String str: mainBusinessCategorySimpleArray){
                mainBusinessCategoryArrayList.add(str);  //something like this?
           }
        
        lis1 = FXCollections.observableArrayList(mainBusinessCategoryArrayList);
        listView1.setItems(lis1);

        listView1.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(String item) {
                    BooleanProperty observable = new SimpleBooleanProperty();
                    observable.addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            listView1CheckedItems.add(item);
                            listView2CheckedItems.clear();
                            listView3CheckedItems.clear();
                            lis2.clear();
                            lis3.clear();
                            try { 
                                ArrayList<String> tempRes = getExtraMainAttributes(listView1CheckedItems, comboBox4.getSelectionModel().getSelectedItem().toString());
                                if(tempRes==null){
                                   
                                    
                                } else{
                                 
                                 lis2.addAll(tempRes);
                                
                                }
                                
                            } catch (Exception ex) {
                                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                               
                        } else {
                                listView1CheckedItems.remove(item);
                                listView2CheckedItems.clear();
                                listView3CheckedItems.clear();
                                lis2.clear();
                                lis3.clear();
                                
                                try {        
                                
                                if(listView1CheckedItems.isEmpty()){
                                   
                                    
                                   
                                } else{
                                 lis2.clear();
                                 lis3.clear();
                                 lis2.addAll(getExtraMainAttributes(listView1CheckedItems, comboBox4.getSelectionModel().getSelectedItem().toString()));
                                 
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                            lis3.remove(item);
    }
                    });
                    
                    return observable;
                }
            }));
        
        lis2 = FXCollections.observableArrayList();
        listView2.setItems(lis2);

        listView2.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(String item) {
                    BooleanProperty observable = new SimpleBooleanProperty();
                    observable.addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            listView2CheckedItems.add(item);
                            listView3CheckedItems.clear();
                            lis3.clear();
                            try { 
                                ArrayList<String> tempRes = getExtraAttributes(listView2CheckedItems,listView1CheckedItems, comboBox4.getSelectionModel().getSelectedItem().toString());
                                if(tempRes==null){
                                    lis3.clear();
                                     } else{
                                 lis3.clear();
                                 lis3.addAll(tempRes);
                                 }
                                
                            } catch (Exception ex) {
                                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                              } else {
                            listView2CheckedItems.remove(item);
                            listView3CheckedItems.clear();
                            lis3.clear();
                                try {        
                                if(listView2CheckedItems.isEmpty()){
                                    
                                } else{
                                 lis3.addAll(getExtraAttributes(listView2CheckedItems,listView1CheckedItems, comboBox4.getSelectionModel().getSelectedItem().toString()));
                                 }
                            } catch (Exception ex) {
                                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        }
                    });
                    return observable;
                }
            }));
        
        
       
        lis3 = FXCollections.observableArrayList();
        listView3.setItems(lis3);
        listView3.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(String item) {
                    BooleanProperty observable = new SimpleBooleanProperty();
                    observable.addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            listView3CheckedItems.add(item);
                        } else {
                            listView3CheckedItems.remove(item);
                        }
                    });
                    return observable;
                }
            }));
        
        
        //SETUP FOR BUSINESS TABLEVIEW
        tableList1 =  FXCollections.observableArrayList();
        table1ColumnSNO.setCellValueFactory(new PropertyValueFactory<>("sno"));
        table1ColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        table1ColumnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        table1ColumnState.setCellValueFactory(new PropertyValueFactory<>("state"));
        table1ColumnStars.setCellValueFactory(new PropertyValueFactory<>("stars"));
        
        tableView1.setItems(tableList1);
        tableView1.setRowFactory(tv -> {
            TableRow<Business> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    try {   
                        tableView1SelectAction(event,tableView1.getSelectionModel().getSelectedItem().getBid());
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return row ;
        });

//        Review tempReview = new Review("date","4","some text","Anmol", "6");
//        Review[] tempReviewArray = new Review[]{tempReview};
//        tableList1.add(new Business("1" ,"Business1", "Santa Clara", "CA", "4.5"));
        
        //COMBOBOX - WEEK
        comboBoxWeek =  FXCollections.observableArrayList("Monday", "Teusday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        comboBox1.setItems(comboBoxWeek);
        comboBox1.setPromptText("Day Of Week");
        
        
        
        try {
            //GET LOCATION DATA
            locationHashMap = find_location();
            } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //COMBOBOX - STATE
        comboBoxState =  FXCollections.observableArrayList();
        comboBox5.setItems(comboBoxState);
        comboBox5.setPromptText("State");
        comboBox5.setOnAction( e -> {
         getCity(comboBox5.getValue().toString());   
         
         });
        getState();
        
        //COMBOBOX - CITY
        comboBoxCity =  FXCollections.observableArrayList();
        comboBox6.setItems(comboBoxCity);
        comboBox6.setPromptText("City");
        
        
        
        
        //COMBOBOX - TIME FROM
        timeListSimpleArray = new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
        timeArrayList = new ArrayList<Integer>();
        for(int i =  0; i < timeListSimpleArray.length; i++){
                timeArrayList.add(timeListSimpleArray[i]);  //something like this?
           }
        
        comboBoxTimeFrom =  FXCollections.observableArrayList(timeArrayList);
        comboBox2.setItems(comboBoxTimeFrom);
        comboBox2.setPromptText("From");
       
        
        
        //COMBOBOX - TIME TO
        comboBoxTimeTo =  FXCollections.observableArrayList();
        comboBox3.setItems(comboBoxTimeTo);
        comboBox3.setPromptText("To");
        
         comboBox2.setOnAction( e -> {
             ArrayList<Integer> tempArrayList = new ArrayList<Integer>();
            for(Integer i : timeArrayList){
                if( i > (Integer)comboBox2.getValue()){
                    tempArrayList.add(i);
                }
            }
         comboBoxTimeTo.setAll(tempArrayList);
         });
         
         
        comboBoxSelectAttribute = FXCollections.observableArrayList("All Attributes","Any Attributes"); 
        comboBox4.setItems(comboBoxSelectAttribute);
        comboBox4.setPromptText("Select Attributes");
        comboBox4.getSelectionModel().selectFirst();
        
        //END OF INITIALIZE BLOCK
    }    








    
}
