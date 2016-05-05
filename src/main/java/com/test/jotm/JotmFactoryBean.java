package com.test.jotm;

import javax.naming.NamingException;
import javax.transaction.SystemException;

import org.objectweb.jotm.Current;
import org.objectweb.jotm.Jotm;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

public class JotmFactoryBean implements FactoryBean, DisposableBean{
	
	private Current jotmCurrent;
	private Jotm jotm;
	
	public JotmFactoryBean() throws NamingException {  
        // Check for already active JOTM instance.  
        this.jotmCurrent = Current.getCurrent();  
  
        // If none found, create new local JOTM instance.  
        if (this.jotmCurrent == null) {  
            // Only for use within the current Spring context:  
            // local, not bound to registry.  
            this.jotm = new Jotm(true, false);  
            this.jotmCurrent = Current.getCurrent();  
        }  
    } 

	@Override
	public void destroy() throws Exception {
		if (this.jotm != null) {  
            this.jotm.stop();  
        }  
	}

	@Override
	public Object getObject() throws Exception {
		return this.jotmCurrent;  
	}

	@Override
	public Class getObjectType() {
		return this.jotmCurrent.getClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	 public Jotm getJotm() {  
	        return this.jotm;  
	 } 
	 
	 public void setDefaultTimeout(int defaultTimeout) {  
	        this.jotmCurrent.setDefaultTimeout(defaultTimeout);  
	        // The following is a JOTM oddity: should be used for demarcation  
	        // transaction only,  
	        // but is required here in order to actually get rid of JOTM's default  
	        // (60 seconds).  
	        try {  
	            this.jotmCurrent.setTransactionTimeout(defaultTimeout);  
	        } catch (SystemException ex) {  
	            // should never happen  
	        }  
	    }  

}
