package com.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;

public class TTest {
	
	final Set s = new HashSet();
	
	Set set = new Set(){

		 public int size()                 { return s.size(); }
         public boolean isEmpty()          { return s.isEmpty(); }
         public boolean contains(Object o) { return s.contains(o); }
         public Object[] toArray()         { return s.toArray(); }
         public Object[] toArray(Object[] a)     { return s.toArray(a); }
         public String toString()          { return s.toString(); }
         public Iterator iterator()     { return s.iterator(); }
         public boolean equals(Object o)   { return s.equals(o); }
         public int hashCode()             { return s.hashCode(); }
         public void clear()               { s.clear(); }
         public boolean remove(Object o)   { return s.remove(o); }

         public boolean containsAll(Collection coll) {
             return s.containsAll(coll);
         }
         public boolean removeAll(Collection coll) {
             return s.removeAll(coll);
         }
         public boolean retainAll(Collection coll) {
             return s.retainAll(coll);
         }

         public boolean add(Object o){
             throw new UnsupportedOperationException();
         }
         public boolean addAll(Collection coll) {
             throw new UnsupportedOperationException();
         }
		
	};
	
	 public static void main(String[] args) 	{
		System.out.println(Runtime.getRuntime().availableProcessors()); 
		LinkedTransferQueue link = new LinkedTransferQueue();
		ConcurrentHashMap< String, String> m = new ConcurrentHashMap< String, String>();
		Map mm = new Hashtable();
		Integer.parseInt("0001111", 2);
	}

}
