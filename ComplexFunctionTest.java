package Ex1;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexFunctionTest {

	String[] arr= {"min(x+0.071,1)","plus(mul(max(x^2+7x -(11 +2x^3),x^5),10),15x^3)","x",""};
	ComplexFunction[] cf=new ComplexFunction[5];
	@Test
	public void testComplexFunctionFunction() {
		arr[3]="comp("+arr[0]+","+"div(9,x))";
		ComplexFunction f= new ComplexFunction(new Polynom());
		for(int i=0;i<arr.length;i++) {
			cf[i]= new ComplexFunction(f.initFromString(arr[i]));
			assert(cf[i].equals(cf[i].initFromString(cf[i].toString())));
			if(i>0)assert(cf[i].equals(cf[i-1].initFromString(cf[i-1].toString()))==false);
			cf[i].plus(cf[i]);
			System.out.println(cf[i].toString());
		}
		for(int i=1;i<arr.length;i++) {
			assert(cf[i].equals(cf[i-1])==false);
			cf[i]=(ComplexFunction) cf[i-1].copy();
			assert(cf[i].equals(cf[i-1]));}
		
		for(int i=0;i<arr.length-1;i++) {
			cf[i].div(cf[i+1]);
			System.out.println(cf[i]);
			assert(cf[i].equals(new Monom("1")));
			cf[i].mul(cf[i+1]);
			assert(cf[i+1].equals(cf[i]));
			cf[i]=(ComplexFunction) cf[i+1].copy();
			assert(cf[i].equals(cf[i+1]));
			double d;
			for(int j=0;j<3000;j++) {
				d=Math.random()*10000;
				assert(cf[i].f(d)==cf[i+1].f(d));
				assert(cf[i].f(-d)==cf[i+1].f(-d));
			}
		}
		cf[0]=(ComplexFunction) cf[0].initFromString("-x");
		cf[1]=(ComplexFunction) cf[0].copy();
		cf[0].mul(cf[0]);
		cf[1].comp(cf[0]);
		assert(cf[1].equals(cf[3].initFromString("-x^2")));
		
	}

}
