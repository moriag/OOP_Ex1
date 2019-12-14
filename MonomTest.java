package Ex1;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class MonomTest {
	@Test
	public void testCopy() {;}
	@Test
	public void testInitFromString() {;}
	@Test
	public void testToString() {;}
	@Test
	public void testMonomString() {;}
	@Test
	public void testIsZero() {
		String[] monoms = {"2", "-x","-3.2x^2","0x^5"};
		for(int i=0;i<monoms.length;i++) {
			Monom m = new Monom(monoms[i]);
			String s = m.toString();
			m = new Monom(s);
			Monom m1=(Monom)m.copy();
			assert(m.equals(m1));
			assert(!m.isZero()||(i==3 && m.isZero()));
		}
	}

	@Test
	public void testMonomMonom() {;}
	@Test
	public void testEqualsObject() {
		ArrayList<Monom> monoms = new ArrayList<Monom>();
		monoms.add(new Monom(0,5));
		monoms.add(new Monom(-1,0));
		monoms.add(new Monom(-1.3,1));
		monoms.add(new Monom(-2.2,2));
		
		for(int i=0;i<monoms.size();i++) {
			Monom m = new Monom(monoms.get(i));
			String s = m.toString();
			Polynom m1 = new Polynom(s);
			assert(m.equals(m1));
			assert(!m.isZero()||(i==0 && m.isZero()));
		}
	}
	public void testAdd() {;}
	@Test
	public void testMonomDoubleInt() {
		String[] monoms = {"", null,"x4","12x^-6","x^","3^x","345d5"};
		boolean exc=false;
		for(int i=0;i<monoms.length;i++) {
			try{new Monom(monoms[i]);
			}catch(Exception e) {exc=true;}
			assert(exc);
			exc=false;
		}
		Monom m=new Monom(6,5);
		try{
		m.add(new Monom(1,4));
		}catch(Exception e) {exc=true;}
		m =new Monom(0,6);
		m.add(new Monom(1.5,5));
		assert(m.equals(new Monom(1.5,5)));
	}

	@Test
	public void testGet_coefficient() {;}

	@Test
	public void testGet_power() {;}

	@Test
	public void testDerivative() {;}

	@Test
	public void testF() {;}

	@Test
	public void testMultipy() {;}

	



	



}
