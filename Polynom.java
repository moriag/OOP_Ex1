package Ex1;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.function.Predicate;
import Ex1.Monom;
//import sun.tools.tree.CaseStatement;
/**
 * This class represents a Polynom with add, multiply functionality, it also should support the following:
 * 1. Riemann's Integral: https://en.wikipedia.org/wiki/Riemann_integral
 * 2. Finding a numerical value between two values (currently support root only f(x)=0).
 * 3. Derivative
 * 
 * @author Boaz
 *
 */
public class Polynom implements Polynom_able{
	private HashMap<Integer, Monom> polynom;
	

	/**
	 * Zero (empty polynom)
	 */

	public Polynom() {
		;
	}
	/**
	 * init a Polynom from a String such as:
	 *  {"x", "3+1.4X^3-34x", "(2x^2-4)*(-1.2x-7.1)", "(3-3.4x+1)*((3.1x-1.2)-(3X^2-3.1))"};
	 * @param s: is a string represents a Polynom
	 */
	public Polynom(String s) {
		int level=0;//balance and sequence check
		s=s.replace(" ","");
		if(s.isEmpty())throw new RuntimeException("ERR this string is empty");
		if(s.endsWith("+")||s.endsWith("-")||s.endsWith("*"))throw new RuntimeException("ERR wrong formatgot:  "+s);
		char c;
		for(int i=0;i<s.length();i++) {
			c=s.charAt(i);
//			if(c==' ')throw new RuntimeException("ERR no spaces allowed");
			if(c=='(') {level++; continue;}
			if(c==')') {level--;
				if(level<0||s.charAt(i-1)=='('||(s.length()>i+1&&s.charAt(i+1)=='(')) {
					throw new RuntimeException("ERR unbalanced parentheses or ileagal sequence \"()\",\")(\"");
				}continue;
			}if(i>0&&(c=='+'||c=='*'||c=='-')) {
				if(i>0&&(s.charAt(i-1)=='+'||s.charAt(i-1)=='*'||s.charAt(i-1)=='-'))
					throw new RuntimeException("ERR a sequence of math operation signs (such as: \"*-\",\"+*\",\"-+\",\"--\"...) is not allowed");
			}
		}if(level!=0)throw new RuntimeException("ERR unbalanced parentheses or ileagal sequence \"()\",\")(\"");
		
		ArrayList<Polynom> polynomArray= new ArrayList<Polynom>();//array to store extracted polynoms
		s="("+s+")";
		
		//splitting and creating Monoms
				String pattern = "[^\\(\\)\\+\\-\\*]+";//not sign nor parentheses
				Pattern p =Pattern.compile(pattern);
				Matcher m = p.matcher(s);
				int startIndex =0;
				int endIndex =0;
//				int count = 0;
				String revised="";
				while(m.find()) {//count++;//iteration count
					String monom = m.group();
		            Polynom polynom = new Polynom();
		            polynom.add(new Monom(monom));
		            //replacing monom with @index
		            startIndex = m.start();
		            revised+= s.substring(endIndex, startIndex) + "@"+polynomArray.size();//add index indicator
		            endIndex = m.end();
		            polynomArray.add(polynom);//store for later use
				}revised+=s.substring(endIndex);
//				System.out.println(count);
				
				extractPolynom(revised,polynomArray);
				
				this.polynom=polynomArray.get(polynomArray.size()-1).polynom;
	}
	/**
	 *extract polynom from math expression containing index indicators to the polynom array
	 * @param s
	 * @param polynomArray
	 */
	private void extractPolynom(String s, ArrayList<Polynom> polynomArray) {
		
		Pattern p=Pattern.compile("\\(([^\\(\\)]+)\\)");//start and ends with parentheses no parentheses in the middle
		Matcher m = p.matcher(s);
	
		while(m.find()) {
			String polynom = m.group(1);  //no parentheses
			
			Polynom poly=getPolynomFromExpression(polynom,polynomArray);
			
			//replace and eliminate parenthesized expressions
			s= s.replace(m.group(0), "@"+polynomArray.size());
			polynomArray.add(poly);//store extracted polynom..
			m=p.matcher(s);
		}
    }
//         
	
        
	/**
	 * 
	 * @param polynom string , expression to translate to polynom
	 * @param polynomArray containing polynoms indicated to by string expression 
	 * @return Polynom created from simple expression (no parentheses)
	 */
	private Polynom getPolynomFromExpression(String polynom, ArrayList<Polynom> polynomArray) {
		
		Pattern p=Pattern.compile("([\\+\\-])([^\\+\\-]+)");//start with + or - 
		
		if(polynom.charAt(0)!='-')polynom="+"+polynom;//so the first will be found
		Matcher m = p.matcher(polynom);
		Polynom polynomToAdd = new Polynom();
		
		while(m.find()) {
			 Polynom polynomToMulty=new Polynom();
			 polynomToMulty.add(Monom.ONE);        
			 for(String monom : m.group(0).split("\\*")) {//keep math operation order
				
				 //get polynom indicated in string from the array 
				 Polynom polynomFromArray=polynomArray.get(Integer.parseInt(monom.split("@")[1]));
				 
				 if(monom.split("@")[0].equals("-")) {
					 polynomFromArray.multiply(Monom.MINUS1);//add or subtract
				 }
				 polynomToMulty.multiply(polynomFromArray);// multiply polynom 

			 }
			 polynomToAdd.add(polynomToMulty);
        }	 
		return polynomToAdd;
	}


	
	/**
	 * returns double, the value of f(x) for this polynom
	 */
	@Override
	public double f(double x) {
		Monom ans=new Monom(Monom.ZERO);
		this.iteretor().forEachRemaining(m->{ans.add(new Monom(m.f(x),0));});
		return Double.parseDouble(ans.toString());
	}
	
	@Override
	public void add(Polynom_able p1) {	
		if(p1.isZero())
			return;
		p1.iteretor().forEachRemaining(m->{if(m!=null)this.add(new Monom(m));});
	}

	@Override
	public void add(Monom m1) {
		if(m1==null) {throw new RuntimeException("ERR: can't add null value to Polynom");}
		else{
			if(this.isZero()) 
				this.polynom= new HashMap<Integer, Monom>(4);
			Monom m= this.polynom.getOrDefault(m1.get_power(), new Monom(Monom.ZERO));
			m.add(m1);
			if(m.isZero()) {this.polynom.remove(m1.get_power());return;}
			this.polynom.put(m1.get_power(),m );
			
		}
	}

	@Override
	public void substract(Polynom_able p1) {
		p1.iteretor().forEachRemaining(m->{if(m!=null)this.add(new Monom(-1*m.get_coefficient(),m.get_power()));});		
	}

	@Override
	public void multiply(Polynom_able p1) {
		if(this.isZero()||p1.isZero()) {
			this.polynom=null;
			return;
		}
		Polynom multi=new Polynom();//to use in iterations
		
		Polynom_able a= (Polynom_able) p1.copy();		

		this.iteretor().forEachRemaining(m ->{
			a.multiply(Monom.ZERO);
			a.add(p1);
			a.multiply(m);
			multi.add(a);
			});
		this.polynom=multi.polynom;		
	}

		
	@Override
	public void multiply(Monom m1) {
		if(this.isZero()||m1.isZero()) {
			this.polynom=null;
			return;
		}
		Polynom multi=new Polynom();

		this.iteretor().forEachRemaining(m->{
			Monom temp=new Monom(m1);
			temp.multipy(m);
			multi.add(temp);
			});
		this.polynom=multi.polynom;		
	}

	@Override
	public boolean equals(Object obj) {
		Polynom p= new Polynom();
		p.add(this);
		p.multiply(Monom.MINUS1);
		if(obj instanceof Monom) {p.add((Monom)obj); return p.isZero();}
		if(obj instanceof Polynom) {p.add((Polynom)obj); return p.isZero();}
		if(obj instanceof function)return ((function)obj).equals(this);
		return false;
		
	}

	@Override
	public boolean isZero() {
		if(this.polynom==null||this.polynom.isEmpty())return true;
		return false;
	}

	@Override
	public double root(double x0, double x1, double eps) {
		double f1= f(x1);
		double f0= f(x0);
		if(f1*f0>0||eps<=0)throw new RuntimeException("ERR need to be x1,x0,eps so that x1*x0<=0, eps>0");
		double x,fx;
		while(true){
			x=(x1+x0)/2;
			fx= f(x);
			if(Math.abs(fx)<eps)return x;
			if(f0*fx>0) {f0=fx; x0=x;}
			else {f1=fx; x1=x;}
		}
	}

	@Override
	public Polynom_able copy() {
		Polynom ans= new Polynom();
		this.iteretor().forEachRemaining(m->{if(m!=null)ans.add(new Monom(m));});
		return ans;
	}

	@Override
	public Polynom_able derivative() {
		Polynom ans= new Polynom();
		this.iteretor().forEachRemaining(m->{
			if(m!=null) ans.add(m.derivative());
		});
		return ans;
	}
//
//	
//	
//	
	@Override
	public double area(double x0, double x1, double eps) {
		if(x0>=x1||eps<=0)throw new RuntimeException("ERR need to be x1,x0,eps so that x0<x1, eps>0");
		double riemann=0;
		double step=x0+eps;
		while(x0<x1) {
			riemann+=(Math.max(0,f(x0))*eps + Math.max(0, f(step))*eps)/2;
			x0=step;
			step+=eps;
		}
		return riemann;
	}

	
	@Override
	public Iterator<Monom> iteretor() {
		if(this.isZero()) {
		     Polynom p=new Polynom();
		     p.add(new Monom(Monom.ZERO));
		     return p.polynom.values().iterator();
		}
		return 	this.polynom.values().iterator(); 
	}

	public String toString() {
		if(this.isZero())return "0";
		ArrayList <Integer> keys = new ArrayList<Integer>();
		keys.addAll(this.polynom.keySet());
		Collections.sort(keys);
		
		String monom, p=this.polynom.get(keys.get(0)).toString();
		
		for (int i = 1; i < keys.size(); i++) {
			monom=this.polynom.get(keys.get(i)).toString();
			if(monom.charAt(0)!='-')p+='+';
			p+=monom;
		}
		return p;
	}
	@Override
	public function initFromString(String s) {
		return new Polynom(s);
	}
	
}
