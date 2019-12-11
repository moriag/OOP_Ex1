
package Ex1;

import java.util.Comparator;

/**
 * This class represents a simple "Monom" of shape a*x^b, where a is a real number and a is an integer (summed a none negative), 
 * see: https://en.wikipedia.org/wiki/Monomial 
 * The class implements function and support simple operations as: construction, value at x, derivative, add and multiply. 
 * @author Boaz
 *
 */
public class Monom implements function{
	public static final Monom ZERO = new Monom(0,0);
	public static final Monom MINUS1 = new Monom(-1,0);
	public static final Monom ONE = new Monom(1,0);
	public static final double EPSILON = 0.0000001;
	public static final Comparator<Monom> _Comp = new Monom_Comperator();
	public static Comparator<Monom> getComp() {return _Comp;}
	public Monom(double a, int b){
		this.set_coefficient(a);
		this.set_power(b);			
	}
	public Monom(Monom ot) {
		this(ot.get_coefficient(), ot.get_power());
	}
	
	public double get_coefficient() {
		return this._coefficient;
	}
	public int get_power() {
		return this._power;
	}
	/** 
	 * this method returns the derivative monom of this.
	 * @return
	 */
	public Monom derivative() {
		if(this.get_power()==0) {return getNewZeroMonom();}
		return new Monom(this.get_coefficient()*this.get_power(), this.get_power()-1);
	}
	public double f(double x) {
		double ans=0;
		double p = this.get_power();
		ans = this.get_coefficient()*Math.pow(x, p);
		return ans;
	} 
	public boolean isZero() {return this.get_coefficient() == 0;}
	// ***************** add your code below **********************
	public Monom(String s) {
		
		String temp=s;
		boolean x;
		
		try {x=s.contains("x");
		}
		catch(Exception e){	throw new RuntimeException("ERR cant extract Monom valeus from null");}
		
		if(!x) {// "a" format
			temp=s.concat("x^0");
			}
		else{
			if(s.endsWith("x")) //"ax", x formats
				temp=temp.concat("^1");
				
			if(s.charAt(0)=='x') //x^b, x formats
				temp="1".concat(temp);
			
			if(s.indexOf("-x")==0)
				temp="-1".concat(temp.substring(1));
		}
		
		int i = temp.indexOf("x^");//temp should ax^b format:
		
		try{if(s.contains(" "))throw new RuntimeException();
			this.set_coefficient(Double.parseDouble(temp.substring(0, i)));
		    this.set_power(Integer.parseInt(temp.substring(i+2)));
		}
		catch(Exception e) {
			throw new RuntimeException("ERR wrong format, excepteble formats: a, x, ax, x^b, ax^b, so that 'a' is a real number and 'b' is integer>=1. got:\""+s+"\"");
		}

	}
		
	
	public void add(Monom m) {
		if(m.isZero()||this.isZero() || this._power==m._power) {
			this.set_coefficient(this._coefficient+m._coefficient);
			this.set_power(Math.max(this._power, m._power));
			
		}
		else {
			throw new RuntimeException("ERR cant add monoms of nonequal powers");
		}
	}
	
	public void multipy(Monom d) {
		
		this.set_coefficient(this.get_coefficient()*d.get_coefficient());
		this.set_power(this.get_power()+d.get_power());
	}
	
	public String toString() {
		String ans=""+this.get_coefficient();
		if(this._power==0) 
			return ans;
		if(Math.abs(this._coefficient)==1) {
			if(this._coefficient==-1)ans="-x";
			else{ans="x";}
		}
		else {ans+="x";}
		
		if(this._power==1)
			return ans;
		
		return ans+"^"+this.get_power();
	}
	// you may (always) add other methods.

	//****************** Private Methods and Data *****************
	@Override
	public boolean equals(Object o) {
		Polynom ans=new Polynom();
		ans.add(this);
		return ans.equals(o);
	}

	private void set_coefficient(double a){
		if(Math.abs(a)<EPSILON)this._coefficient=0;
		else{this._coefficient = a;}
	}
	private void set_power(int p) {
		if(p<0) {throw new RuntimeException("ERR the power of Monom should not be negative, got: "+p);}
		if(this.isZero())
			this._power=0;
		else
			this._power = p;
	}
	private static Monom getNewZeroMonom() {return new Monom(ZERO);}
	private double _coefficient; 
	private int _power;
	@Override
	public function initFromString(String s) {
		return new Monom(s);
	}
	@Override
	public function copy() {
		return new Monom(this);
	}
	
	
}
