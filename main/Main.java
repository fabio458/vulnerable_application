package main;

import javax.servlet.http.*;

public class Main {
	public static void main (String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		String username = null;
		
		App app = new App();
		app.processRequest(request, response);
		username = app.greetings(request,response);
		
		User user = new User();
		user = null;
		user = app.readUser(username);
		System.out.println(user.getName() + "|" + user.getAge());
		
		String document = app.readDocument(request, response);
		System.out.println(document);
		
		app.openUrl(request, response);
	}
}
