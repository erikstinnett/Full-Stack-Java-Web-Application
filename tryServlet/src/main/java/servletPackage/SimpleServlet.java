package servletPackage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/SimpleServlet")



// Erik Stinnett
// Java Project III

public class SimpleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Shows allData;
	private ShowWeek show;
	private String showTitle;
	private String staticToString;
	private String alteredToString;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SimpleServlet() {
		super();
		showTitle = "";
		staticToString = "";
		alteredToString = "";
		allData = new Shows("allData","./servletPackage/netflixAllWeeksGlobalProcessed.txt");
		show = new ShowWeek();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
	ServletException, IOException {
		PrintWriter pwriter = response.getWriter();
		// index.html: entering the webpage
		if(request.getParameter("UserPassBtn") != null) {
			//Password username
			response.setContentType("text/html");
			String uName = request.getParameter("userName");
			String password = request.getParameter("userPassword");

			// PASSWORD AND USERNAME VERIFICATION
			if (uName.equals("md") && password.equals("pw")) {
				HttpSession session=request.getSession();
				session.setAttribute("uname",uName);
				session.setAttribute("upass",password);				
				response.sendRedirect("homepage.html");
			}
			else {
				pwriter.print("Unauthorized.");
			}
		}
		
		// viewPage.jsp Button "Submit"
		else if (request.getParameter("seeShowsBtn")!= null){
			// Creates dropdown box
			Iterator<ShowWeek> iter = allData.getIterator();
			String value = "<select name=\"shows\">";
			while (iter.hasNext()) {
				ShowWeek s = iter.next();
				value += "<option value=\""+s.toString()+"\">"+s.getShowTitle()+"</option>";
			}
			value += "</select>\r\n";
			request.setAttribute("dropDownOptions",value); 	
			RequestDispatcher rd=request.getRequestDispatcher("/viewPage.jsp");
			rd.forward(request,response);
		}
		
		// viewPage.jsp: BUTTON TO GET SPECIFIC SHOW INFO 
		else if (request.getParameter("specShowBtn") != null) {
			String showChoice = request.getParameter("shows");
			request.setAttribute("showChoice",showChoice);			
			String desc = showChoice;
			request.setAttribute("showDesc", desc);
			RequestDispatcher rd=request.getRequestDispatcher("/viewPage.jsp");
			rd.forward(request,response);
		}
		
		// addPage.jsp: create a new show object
		else if (request.getParameter("submitShowBtn") != null) {
			// Gets value in textbox
			String getShow = request.getParameter("addShowTxt");
			request.setAttribute("getShow",getShow);
			// Splits textbox by commas
			String[] splitShow = getShow.split(",");
			show.setWeek(splitShow[0].trim());
			show.setCategory(splitShow[1].trim());
			show.setRank(splitShow[2].trim());
			show.setShowTitle(splitShow[3].trim());
			show.setSeasonTitle(splitShow[4].trim());
			show.setHrsViewed(splitShow[5].trim());
			show.setWeeksTop10(splitShow[6].trim());
			allData.addShowWeek(show);	// Adds the show object into data
			
			request.setAttribute("getShow", getShow);
			RequestDispatcher rd=request.getRequestDispatcher("/addPage.jsp");
			rd.forward(request,response);
		}
		// deletePage.jsp: Dropdown box
		else if (request.getParameter("seeShowsDelBtn")!= null) {
			Iterator<ShowWeek> iter = allData.getIterator();
			String value = "<select name=\"shows2\">";
			while (iter.hasNext()) {
				ShowWeek s = iter.next();
				value += "<option value=\""+s.toString()+"\">"+s.getShowTitle()+"</option>";
			}
			value += "</select>\r\n";
			request.setAttribute("dropDownOptions",value); 	
			RequestDispatcher rd=request.getRequestDispatcher("/deletePage.jsp");
			rd.forward(request,response);
		}
		// deletePage.jsp: Purge a show
		else if (request.getParameter("purgeShowBtn")!= null) {
			String showChoice = request.getParameter("shows2");
			request.setAttribute("showChoice",showChoice);	
			
			String[] splitShow = showChoice.split(",");
			String title = splitShow[3];
			showTitle = title;
			allData.purgeShow(title);	// Purge
			request.setAttribute("title", title);
			
			RequestDispatcher rd=request.getRequestDispatcher("/deletePage.jsp");
			rd.forward(request,response);
		}
		
		// deletePage.jsp: unpurge a show 
		else if(request.getParameter("unpurgeShowBtn") != null) {

			allData.unPurgeShow(showTitle);
			RequestDispatcher rd=request.getRequestDispatcher("/deletePage.jsp");
			rd.forward(request,response);
		}
		// changePage.jsp dropdown box
		else if (request.getParameter("seeShowsEditBtn") != null) {
			Iterator<ShowWeek> iter = allData.getIterator();
			String value = "<select name=\"shows2\">";
			while (iter.hasNext()) {
				ShowWeek s = iter.next();
				value += "<option value=\""+s.toString()+"\">"+s.getShowTitle()+"</option>";
			}
			value += "</select>\r\n";
			request.setAttribute("dropDownOptions",value); 	
			
			RequestDispatcher rd=request.getRequestDispatcher("/changePage.jsp");
			rd.forward(request,response);
		}
		// changePage.jsp: edit a show
		else if (request.getParameter("editShowBtn") != null) {
			String showChoice = request.getParameter("shows2");
			request.setAttribute("showChoice",showChoice);	
			
			RequestDispatcher rd=request.getRequestDispatcher("/changePage.jsp");
			rd.forward(request,response);
		}
		
		/*else if(request.getParameter("startUsing")!=null)
		{
			ArrayList<ShowWeek> myList = allData.getTopShows();
			String showChoice = request.getParameter("shows");	
		}*/
		
		// editPage.jsp: Submit the changes you made
		else if(request.getParameter("submitChangesBtn")!=null) {
			// Get the show that was displayed in the dropdown menu. 
			// This is the show to be altered.
			String staticShow = request.getParameter("hiddenShow");
			// Get the parameter within the textbox. This is what the user altered.
			String alteredShow = request.getParameter("editShowTxt");
			
			// Set the attributes
			request.setAttribute("hiddenShow",staticShow);
			request.setAttribute("editShowTxt", alteredShow);
			// Split the existing show by commas
			String[] splitShow = staticShow.split(",");
			// Find the existing show in our data
			ShowWeek show1 = allData.find(splitShow[3].trim(), splitShow[0].trim());
			// Split the edited show by commas
			String[] alterShowSplit = alteredShow.split(",");
			staticToString = show1.toString();
			// Set the show values by the edited show values
			show1.setWeek(alterShowSplit[0].trim());
			show1.setCategory(alterShowSplit[1].trim());
			show1.setRank(alterShowSplit[2].trim());
			show1.setShowTitle(alterShowSplit[3].trim());
			show1.setSeasonTitle(alterShowSplit[4].trim());
			show1.setHrsViewed(alterShowSplit[5].trim());
			show1.setWeeksTop10(alterShowSplit[6].trim());
			alteredToString = show1.toString();
			
			request.setAttribute("staticToString", staticToString);
			request.setAttribute("alteredToString", alteredToString);
			
			RequestDispatcher rd=request.getRequestDispatcher("/changePage.jsp");
			rd.forward(request,response);
		}
		
		// Else: error message displayed
		else {
			response.getWriter().append("ERROR");
		}
		//Path path = Paths.get("./servletPackage/netflixAllWeeksGlobalProcessed.txt");
		//System.out.println(path);
		allData.writeFile("./servletPackage/netflixAllWeeksGlobalProcessed.txt");
		//System.out.println(new File("").getAbsoluteFile());	
		}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
	ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		session.setAttribute("uName", "md");
		session.setAttribute("uPW", "pw");
		String userName = (String)session.getAttribute("uName");
		String userPW = (String)session.getAttribute("uPW");
		doGet(request, response);
	}
}