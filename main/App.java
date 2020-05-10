package main;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class App {
	private String[] items;
	
	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String user = request.getParameter("user");
        String item = request.getParameter("item");
        Connection conn = null;
        String url = "jdbc:mysql://10.0.5.170:3306/";
        String dbName = "db_items", driver = "com.mysql.jdbc.Driver";
        String userName = "admin", password = "admin";
        
		Class.forName(driver).newInstance();

        try {
			conn = DriverManager.getConnection(url + dbName, userName, password);
			 
	        Statement st = conn.createStatement();
	        String query = "SELECT * FROM Items WHERE userId = '" + user + "' AND itemId = '" + item + "'";
	        ResultSet res = st.executeQuery(query);

	        while (res.next()) {
	            String s = res.getString("item");
	            items[items.length] = s;
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        try {
        	PrintWriter out = response.getWriter();
		    out.println("Hello " + items[0] + "!");
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String greetings(HttpServletRequest request, HttpServletResponse response) {
		String user = request.getParameter("user");
		
	    PrintWriter out;
		try {
			out = response.getWriter();
		    out.println("Hello " + user + "!");
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		OutputStream os = null;
        try {
        	String cwd = System.getProperty("user.dir");
        	String filepath = cwd + "user.log";
            File log_file = new File(filepath);
        	os = new FileOutputStream(log_file);
            os.write(user.getBytes(), 0, user.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
	}
	
	public User readUser(String username){
		String user_path = System.getProperty("user.dir") + username + ".xml";
		File user_file = new File(user_path);
		User user = null;
		
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(User.class);
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		    user = (User) jaxbUnmarshaller.unmarshal(user_file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		user.printInfo();
		if(user.getName().toUpperCase().equals("admin")) {
			return null;
		}
		else {
		    return user;			
		}
	}

	public String readDocument(HttpServletRequest request, HttpServletResponse response) {
		char[] array = new char[100];
		
		String filePath = request.getParameter("file_path");
		String document_path = System.getProperty("user.dir") + filePath;
		try {
			FileInputStream file = new FileInputStream(document_path);
			InputStreamReader input = new InputStreamReader(file);
			input.read(array);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array.toString();
	}

	public String openUrl(HttpServletRequest request, HttpServletResponse response) {
		String final_url = null;
		String inputUrl = request.getParameter("url");
		try  
        {   
            URL url = new URL(inputUrl);   
            URLConnection urlcon=url.openConnection();  
            final_url = urlcon.getURL().toString();
            BufferedReader br = new BufferedReader(new InputStreamReader (urlcon.getInputStream()));   
            String i = null;   
            while ((i = br.readLine()) != null)    
            {   
                System.out.println(i);   
            }   
        }     
        catch (Exception e)    
        {   
            System.out.println(e);   
        }
		finally {
			return final_url;
		}
		
	}
}
