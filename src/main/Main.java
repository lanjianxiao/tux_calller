package main;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weblogic.wtc.gwt.TuxedoConnection;
import weblogic.wtc.gwt.TuxedoConnectionFactory;
import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TypedString;

/**
 * Servlet implementation class Main
 */
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Context ctx = null;
		TuxedoConnection cdmaTuxedo = null;
		response.setCharacterEncoding("UTF-8"); 
		response.setContentType("text/plain; charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		try {
			String serviceName = request.getHeader("METHOD");
			String xmlMessage = request.getParameter("MESSAGE");
			if (serviceName == null) {
				throw new Exception("服务名不能为空");
			}
			if (xmlMessage == null || "".equals(xmlMessage)) {
				throw new Exception("报文体不能为空");
			}
			ctx = new InitialContext();
			TuxedoConnectionFactory cdmaTuxedoFactory = (TuxedoConnectionFactory) ctx.lookup("tuxedo.services.TuxedoConnection");
			cdmaTuxedo = cdmaTuxedoFactory.getTuxedoConnection();
			TypedString cdmaData = new TypedString(xmlMessage);
			Reply cdmaRtn = cdmaTuxedo.tpcall(serviceName, cdmaData, 0);
			cdmaData = (TypedString) cdmaRtn.getReplyBuffer();
			String out = cdmaData.toString();
			response.getWriter().write(out);
		} catch (NamingException | TPException e) {
			e.printStackTrace();
			response.getWriter().write("调用核心发生错误: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("调用核心发生错误: " + e.getMessage());
		}finally {
			if (cdmaTuxedo != null) {
				cdmaTuxedo.tpterm();
			}
		}
	}

}
