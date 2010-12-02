package net.jss.controller.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

 import net.jss.js.ScriptCore;

public class ValueTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String expr;

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}
	
	

	@Override
	public int doStartTag() throws JspException {
		
		String expr = this.getExpr();
		ScriptCore scriptCore = ScriptCore.getInstance();
		
		Object o = scriptCore.executeScript(expr);
		if(o!=null){
			try {
				JspWriter out = this.pageContext.getOut();
				out.write(o.toString());
			} catch (IOException e) {
				JspException e2 = new JspException(e.getMessage());
				e2.setStackTrace(e.getStackTrace());
				throw e2;
			}
			
		}
		
		return TagSupport.SKIP_BODY;
	}
	
	

	@Override
	public int doEndTag() throws JspException {
		return TagSupport.EVAL_PAGE;
	}
}