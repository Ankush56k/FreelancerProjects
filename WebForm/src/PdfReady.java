

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@WebServlet(description = "Create Pdf", urlPatterns = { "/PdfReady" })

public class PdfReady extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	static Connection con = null;
	static Statement stat = null;
	static ResultSet rs;
	ResultSetMetaData rsmd;
	static String Title;
	
	static Paragraph preface = new Paragraph();
    private static String FILE = "FirstPdf.pdf";
    
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                    Font.BOLD);
    
    public void init(ServletConfig config) throws ServletException 
	{
		try 
		{
		    Class.forName ("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ankshu23","12345");
			stat = con.createStatement();
			String query = "Select * from FORMTABLE";
			rs = stat.executeQuery(query);
		}
		catch(SQLException e)
		{
		  System.out.println("Its SQL Exception");
			return;
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println("Its Class not found Exception");
		}
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		Title = request.getParameter("title");
		try
		{
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			//addMetaData(document);
			addTitlePage(document);
			createTable();
			document.add(preface);
			document.close();
		}
		catch(Exception e)
		{
			System.out.println("Its Document not found Exception");
		}
	}
	
	/*private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
	}*/

	private static void addTitlePage(Document document)throws DocumentException 
	{
		addEmptyLine(preface, 1);
        preface = new Paragraph(Title, catFont);
        preface.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(preface, 1);
	}

	private static void createTable() throws BadElementException, SQLException 
	{
		Paragraph p = new Paragraph();
        PdfPTable table = new PdfPTable(4);
        float[] ar = {3,5,6,5};
        
        try
        {
			table.setTotalWidth(ar);
		} 
        catch (DocumentException e) 
        {
			System.out.println("Its Document Exception");
		}
        
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        //table.setPadding(4);
        //table.setSpacing(4);
        //table.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("User Id"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Full Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Email"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        
        c1 = new PdfPCell(new Phrase("Address"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        
        while (rs.next())
        {
			table.addCell(rs.getString(1));
			table.addCell(rs.getString(2));
			table.addCell(rs.getString(4));
			table.addCell(rs.getString(3));
		}
        p.add(table);
        preface.add(p);
	}

	private static void addEmptyLine(Paragraph paragraph, int number) 
	{
        for (int i = 0; i < number; i++) 
        {
        	paragraph.add(new Paragraph(" "));
        }
	}
}