

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(description = "DataBase Connectivity", urlPatterns = { "/Database1" })

public class Database1 extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	Connection con = null;
	Statement stat = null;
	ResultSet rs;
	
	public void init(ServletConfig config) throws ServletException 
	{
		try 
		{
		    Class.forName ("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ankshu23","12345");
			stat = con.createStatement();
		}
		catch(SQLException e)
		{
			System.out.println("SQL Exception");
			return;
		}
		catch (ClassNotFoundException e) 
		{
			System.out.println("Its class not found exception");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		int id = Integer.parseInt(request.getParameter("uid"));
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
			
		try 
		{
			String query = "INSERT INTO FORMTABLE (ID, NAME, ADDRESS, EMAIL) VALUES (?,?,?,?)";
			PreparedStatement dbStatement = con.prepareStatement(query);
			dbStatement.setInt(1,id);
			dbStatement.setString(2,name);
			dbStatement.setString(3,address);
			dbStatement.setString(4,email);
			
			rs = dbStatement.executeQuery();
			
		}
		catch (SQLException e) 
		{
			System.out.println("Its SQL Exception");
		}
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
