package Action;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/MyPresentation")
public class MyPresentation extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public MyPresentation() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	
    	HttpSession session = request.getSession(true);
    	String user = (String) session.getAttribute("UsernameSession");
    	int uid=0;
    	String topic=null, purpose=null;
    	Blob image = null;
    	InputStream is = null;
    	OutputStream os = null;

    	try {
    		// Connect to Oracle
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "aniruddha16");
    		con.setAutoCommit(false);
    		Statement st = con.createStatement();
    		ResultSet rs = st.executeQuery("select user_id from userlogin where username='"+user+"'");
    		while(rs.next()){
    			uid = rs.getInt(1);
    		}
    		ResultSet rs1 = st.executeQuery("select * from presentation where user_id='"+uid+"'");
    		while(rs1.next()){
    			topic = rs1.getString(2);
    			System.out.println(	topic );
    			purpose = rs1.getString(4);
    			System.out.println(	purpose );
    			image = rs1.getBlob(7);

    			byte buf[] = new byte[(int)image.length()];
    			is = image.getBinaryStream();
    			int read = is.read(buf, 0, (int)image.length());
    			response.setContentType("image/png");
    			response.getOutputStream().write(buf, 0, (int)image.length());
    			response.getOutputStream().flush();
    			/*is.read(buf);
    			os.write(buf);*/
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		processRequest(request, response);
	}

}
