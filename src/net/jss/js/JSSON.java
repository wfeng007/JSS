package net.jss.js;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;

import net.jss.controller.FrontController;

import com.google.gson.Gson;

public class JSSON {
	
	/**
	 * Will convert a Javascript object to JSON using modified version of 
	 * David Crockford's of json2.js.
	 * @param jsObject
	 * @return a JSON representation of the object as a String.
	 */
	public static String toJSONString(Object jsObject) {
		try {
			System.out.println("Parsing " + jsObject.getClass().getName() + " to JSON string.");
			ScriptEngine e = new ScriptEngineManager().getEngineByName("JavaScript");

			// Load the required scripts.
			ServletContext sc = FrontController.context.getServletContext();

			// json2.js
			Reader reader = new FileReader(new File(sc.getRealPath(ScriptCore.JSON2JS_SCRIPT_PATH)));
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
	
	/**
	 * Converts a Java object to JSON, using Google's GSON library.
	 * @param javaObject
	 * @return a JSON representation of the object as a String.
	 */
	public static String javaToJSONString(Object javaObject) {
		Gson gson = new Gson();
		System.out.println("Parsing java object " + javaObject + " to JSON string.");
		System.out.println("The result is: " + gson.toJson(javaObject));
		return gson.toJson(javaObject);
	}
	
	/**
	 * Checks if an object is a Java object.
	 * @param o the object ot check against.
	 * @return boolean
	 */
	public static boolean isJava(Object o) {
		if (o.getClass().getName().startsWith("sun.org.mozilla.javascript.internal.")) {
			System.out.println("Checking if "+o.getClass().getName()+" is a java object. The result is false");
			return false;
		}
		System.out.println("Checking if "+o.getClass().getName()+" is a java object. The result is true");
		return true;
	}
	
	public static String analyze(Object o){
		return "Class is "+o.getClass()+" is Java object: "+isJava(o);
	}
}
