package Ex1;


import org.junit.Test;

public class PolynomTest {

	@Test
	public void testPolynomString() {;}
	@Test
	public void testInitFromString() {
	String[] goodExpressions = {"0  "," x *x *2x^ 2*( +x) ", " ( 3-3.4x+ 1)*  ( (3.1x -1.2)-(3 x^2-3.1))","(3.1 x-1.2)-(3x ^2 -3.1)","3+(1.4x)-3-34x"};
	String[] badExpressions	= {"-x+()5","1*-2","15x+((6)+1","","--2","56x^","3x4"};	
	for(int i=0;i<goodExpressions.length;i++) {
		new Polynom().initFromString(goodExpressions[i]);
	}
	boolean exc;
	for(int i=0;i<badExpressions.length;i++) {
		exc=false;
		try{ new Polynom(badExpressions[i]);
		}catch(Exception e) {exc=true;}
		assert(exc);
		}
	}
	@Test
	public void testCopy() {;}
	@Test
	public void testMultiplyPolynom_able() {;}
	@Test
	public void testPolynom() {
		String expression = "(3-3.4x+1)*((3.1x-1.2)-(3x^2-3.1))";
		String expression2 = "(3.1x-1.2)-(3x^2-3.1)";
		Polynom p = new Polynom(expression);
		Polynom p2 = new Polynom(expression2);
		p.multiply(p2);
		p.add(p);
		p2=(Polynom)p.copy();
		assert(p.equals(p2));
		p2=(Polynom) p.initFromString("2*("+""+expression+")*("+expression2+")");
		assert(p.equals(p2));
		
	}
	@Test
	public void testIsZero() {;}
	@Test
	public void testSubstract() {;}
	@Test
	public void testAddPolynom_able() {
		Polynom p = new Polynom();
		String[] monoms = {"1","x","x^2", "0.5x^2"};
		for(int i=0;i<monoms.length;i++) {
			Monom m = new Monom(monoms[i]);
			p.add(m);
			p.add(p);
			assert(p.equals(p.initFromString(monoms[i]+"+"+monoms[i])));
			p.substract(p);
			assert(p.equals(Monom.ZERO));
		}
	}
	@Test
	public void testF() {;}
	@Test
	public void testToString() {;}
	@Test
	public void testEqualsObject() {;}
	@Test
	public void testMultiplyMonom() {;}
	@Test
	public void testIteretor() {;}
	@Test
	public void testAddMonom() {
		Polynom p1 = new Polynom(), p2 =  new Polynom();
		Polynom p3 = new Polynom("1"), p4 =  new Polynom("1");
		double m1, m2, a1, a2;
		m1=m2=1; a1=a2=0;
		String[] monoms1 = {"+2", "-x","-3.2x^2","+4","-1.5x^2"};
		String[] monoms2 = {"+5", "+1.7x","+3.2x^2","-3","-1.5x^2"};
		String add1, add2, mul1,mul2;
		add1= add2= mul1= mul2="";
		for(int i=0;i<monoms1.length;i++) {
			Monom m = new Monom(monoms1[i]);
			p1.add(m);
			p3.multiply(m);
			add1+=monoms1[i];
			a1+=m.f(Math.PI);
			mul1+="*("+monoms1[i]+")";
			m1*=m.f(Math.PI);
		}
		for(int i=0;i<monoms2.length;i++) {
			Monom m = new Monom(monoms2[i]);
			p2.add(m);
			p4.multiply(m);
			add2+=monoms2[i];
			a2+=m.f(Math.PI);
			mul2+="*("+monoms2[i]+")";
			m2*=m.f(Math.PI);
		}		
		p1.multiply(p2);
		p1.multiply(p1);
		a1*=a2;
		a1*=a1;
		add1="(("+add1+")*("+add2+"))";
		add1+="*"+add1;
		assert(p1.equals(p1.initFromString(add1)));
		p3.add(p4);
		p3.add(p3);
		m1+=m2;
		m1*=2;
		assert(p3.equals(p3.initFromString("2*("+mul1.substring(1)+"+"+mul2.substring(1)+")")));
		assert(Math.abs(a1-p1.f(Math.PI))<0.000001);
		assert(Math.abs(m1-p3.f(Math.PI))<0.000001);
		p2=(Polynom) p2.initFromString(p1.toString());
		assert(p2.equals(p1));
		p1.substract(p1);
		assert(p1.equals(Monom.ZERO));
	}


	@Test
	public void testDerivative() {
		Monom[] m= new Monom[10];
		Polynom p=new Polynom();
		Polynom p1=new Polynom();
		m[0]=Monom.ONE;
		p.add(m[0]);
		
		m[1]=(Monom)(m[0].initFromString("x"));
		for(int i=2; i<m.length ; i++) {
			m[i]=(Monom)(m[i-1].copy());
			m[i].multipy(m[1]);
		}
		assert(m[0].derivative().isZero());
		for(int i=1; i<m.length ; i++) {
			p.add(m[i]);
			m[i-1].multipy(new Monom(m[i].get_power(),0));
			assert(m[i-1].equals(m[i].derivative()));
			p1.add(m[i-1]);
			assert(p1.equals(p.derivative()));
		}
		assert(p.derivative().derivative().equals(p1.derivative()));
		
	}
	@Test
	public void testRoot() {;}
	@Test
	public void testArea() {
		Polynom p= new Polynom("(3-3.4x+1)*((3.1x-1.2)-(3x^2-3.1))");
		double r=p.root(0, -17,0.0001);
		assert(Math.abs(p.f(r))<0.0001);
		double a=2.032;
		assert(Math.abs(a-p.area(r, 0, 0.0001))<0.005);
		assert(Math.abs(a-p.area(-3, 0, 0.0001))<0.005);
	}

	





}
