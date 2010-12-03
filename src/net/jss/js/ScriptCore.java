package net.jss.js;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.jss.controller.FrontController;

public class ScriptCore {

	public static final String JSS_SCRIPTS_PATH = "/WEB-INF/jss/";
	public static final String SERVER_SCRIPTS_PATH = "/WEB-INF/server-scripts/";
	private static final String JSON2JS_SCRIPT_PATH = JSS_SCRIPTS_PATH + "jss-json2.js";
	private static final String SERVER_SCRIPT_PATH = JSS_SCRIPTS_PATH + "jss-server.js";
	private static final String APPLICATION_SCRIPT_PATH = SERVER_SCRIPTS_PATH + "application.js";

	private static final String APPLICATION_PACAKGE_NAME = "com.sampleapp";

	public static ScriptCore getInstance() {

		HttpSession session = FrontController.context.getSession();
		ScriptCore instance = (ScriptCore) session.getAttribute("ScriptCore");
		if (instance == null) {
			System.out.println("Creating new scripting environement");
			try {
				instance = new ScriptCore();
				session.setAttribute("ScriptCore", instance);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				instance = null;
			}
		}

		return instance;
	}

	public static boolean isInstanceInSession() {
		return FrontController.context.getSession().getAttribute("ScriptCore") != null;
	}

	ScriptEngine engine;

	private ScriptCore() throws ScriptException, FileNotFoundException {
		// create JavaScript engine
		this.engine = new ScriptEngineManager().getEngineByName("JavaScript");

		// Load the required scripts.
		ServletContext sc = FrontController.context.getServletContext();

		// json2.js
		Reader reader = new FileReader(new File(sc.getRealPath(JSON2JS_SCRIPT_PATH)));
		this.engine.eval(reader);

		// server.js
		reader = new FileReader(new File(sc.getRealPath(SERVER_SCRIPT_PATH)));
		this.engine.eval(reader);

		// ApplicationRoot.js
		//reader = new FileReader(new File(sc.getRealPath(APPLICATION_SCRIPT_PATH)));
		//this.engine.eval(reader);

		// Add the application's packages
		//this.engine.eval("importPackage(" + APPLICATION_PACAKGE_NAME + ");");
	}

	public Object executeScript(Reader fr) {
		try {
			return this.engine.eval(fr);
		} catch (ScriptException e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	public Object executeScript(String js) {

		try {
			Object o = engine.eval(js);
			System.out.println("Executed " + js + ", the result is: " + o);
			return o;
		} catch (ScriptException e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	public String toJSONString(Object jsObject) {
		try {
			System.out.println("Parsing " + jsObject + " to JSON string.");
			ScriptEngine e = new ScriptEngineManager().getEngineByName("JavaScript");

			// Load the required scripts.
			ServletContext sc = FrontController.context.getServletContext();

			// json2.js
			Reader reader = new FileReader(new File(sc.getRealPath(JSON2JS_SCRIPT_PATH)));
			e.eval(reader);
			String varName = "JSS_jsonObject";
			e.put(varName, jsObject);
			String r = e.eval("JSON.stringify(" + varName + ")").toString();
			System.out.println("The resulting JSON string is: " + r);
			return r;

		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		}
	}

	public void receiveServerScript(String js) {
		this.executeScript(js);
	}

	public void receiveServerScript(Reader fr) {
		this.executeScript(fr);
	}

	public String receiveProtectedScript(String js) {

		try {
			this.executeScript(js);
			return this.generateProtectedClientScript(new ScriptReflector(js).getDefinedMethods());
		} catch (ScriptException e) {
			e.printStackTrace();
			return e.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String receiveProtectedScript(Reader fr) {

		try {
			this.executeScript(fr);
			return this.generateProtectedClientScript(new ScriptReflector(fr).getDefinedMethods());
		} catch (ScriptException e) {
			e.printStackTrace();
			return e.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String generateProtectedClientScript(String[] protectedMethods) {
		String script = "";
		for (String m : protectedMethods) {
			script += "function " + m + "(){ return JSS.sajax('" + m + "', arguments); };";

		}
		return script;
	}
}
