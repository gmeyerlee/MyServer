package MyServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServer
 */
public class MyServer extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public MyServer() {
        // TODO Auto-generated constructor stub
    	System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		System.out.println("++++++++++++++++++++++++++++++++++++++++++");
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter(); 
		String longitude = request.getParameter("longitude");
		String latitude = request.getParameter("latitude");
		String id=request.getParameter("ID");
		System.out.println("longitude"+longitude);
		System.out.println("latitude"+latitude);
		System.out.println("id"+id);
		//判断用户名密码是否正确
//		if(username.equals("admin") && password.equals("123")) {
//			out.print("Login succeeded!");
//		}else {
//			out.print("Login failed!");
//		}
		File temp=new File("temp.txt");
		create(temp,100000);
		InputStream in = new FileInputStream(temp);
		int b;  
        while((b=in.read())!= -1)  
        {  
            out.write(b);  
        }           
        in.close();  
		out.println("got it");
		out.flush();
		out.close();
	}
	public static void create(File file, long length) throws IOException{  
        long start = System.currentTimeMillis();  
        RandomAccessFile r = null;  
        try {  
            r = new RandomAccessFile(file, "rw");  
            r.setLength(length);  
        } finally{  
            if (r != null) {  
                try {  
                    r.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        long end = System.currentTimeMillis();  
        System.out.println(end-start);            
    }  

}
