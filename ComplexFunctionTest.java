package Ex1;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Test;

public class ComplexFunctionTest {

	String[] arr= {"0.1-x","min(x+0.071,1)","plus(mul(max(x^2+7x -(11 +2x^3),x^5),10),15x^3+0.6)","x-3"};
	ComplexFunction[] cf=new ComplexFunction[5];
	@Test
	//test constructors, copy, toString and initFromString add different operations
	public void testComplexFunctionFunction() {
		arr[3]="comp("+arr[0]+","+"div(9,x))";
		ComplexFunction f= new ComplexFunction(new Polynom());
		for(int i=0;i<arr.length;i++) {
			cf[i]= new ComplexFunction(f.initFromString(arr[i]));
			assert(cf[i].equals(cf[i].initFromString(cf[i].toString())));
			cf[i].plus(cf[i]);
			assert(cf[i].equals(cf[i].initFromString("plus(".concat(arr[i]+",").concat(arr[i]+")"))));
		}
		for(int i=0;i<arr.length-1;i++) {
			assert(cf[i].equals(cf[i+1])==false);
			cf[i]=(ComplexFunction) cf[i+1].copy();
			assert(cf[i].equals(cf[i+1]));}
		
		for(int i=0;i<arr.length-1;i++) {
			cf[i].div(cf[i]);
//			System.out.println(cf[i]);
			assert(cf[i].equals(new Monom("1")));
			cf[i].mul(cf[i+1]);
			assert(cf[i+1].equals(cf[i]));
			cf[i]=(ComplexFunction) cf[i+1].copy();
			assert(cf[i].equals(cf[i+1]));
			double d;
			for(int j=0;j<3000;j++) {
				d=Math.random()*10000;
				try {
					assert(cf[i].f(d)==cf[i+1].f(d));
					assert(cf[i].f(-d)==cf[i+1].f(-d));
				}catch (ArithmeticException e) {;}
			}
		}
		cf[0]=(ComplexFunction) cf[0].initFromString("-x");
		cf[1]=(ComplexFunction) cf[0].copy();
		cf[0].mul(cf[0]);
		cf[1].comp(cf[0]);
		assert(cf[1].equals(cf[3].initFromString("-x^2")));
		
	}
	@Test
	public void testEqualsObject() {
		ComplexFunction cf,cf1,cf2;
		Polynom p=new Polynom("x-0.2");
		cf=new ComplexFunction(p);
		cf1=(ComplexFunction)cf.copy();
		cf2=(ComplexFunction)cf.copy();
		cf.div(cf1);
		cf1.mul(cf2);//cf1 will not be changed!
//		and so:
		assert(cf.equals(cf2)==false);
		p.multiply(p); // p is base function for cf but not for cf.copy cf will be changed
		assert(cf2.equals(cf)==true);
		assert(cf.equals(cf.copy()));// new copy will use the modified p
		cf=(ComplexFunction) cf.initFromString("mul(x+1,div(0,0))");
		cf1=(ComplexFunction) cf1.initFromString("div(x^2-1,x-1)");
		assert(cf1.equals(cf)==false);
	}

}
