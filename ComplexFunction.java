package Ex1;
import Ex1.Operation;
public class ComplexFunction implements complex_function {
	private String toString(Operation op) {
		String s=op.name();
		if(s=="Times")return "mul";
		if(s=="Divid")return"div";
		return s.toLowerCase();		
	}
	private Operation op;
	private function left;
	private function right;
	
	public ComplexFunction(function func) {
		this.left=func.copy();
		this.op=Operation.None;
	}
	
	private ComplexFunction(function left, function right, Operation op) {
		this.op=op;
		this.left=left;
		if(right instanceof ComplexFunction) right=((ComplexFunction) right).left;
		this.right=right.copy();
	}

	public ComplexFunction(String s, function f1, function f2) {
		ComplexFunction cf =new ComplexFunction(f1);
		switch (s) {
		case "plus":cf.plus(f2);
			break;
			
		case "mul":cf.mul(f2);
			break;
		case "min":cf.min(f2);
			break;
		case "max":cf.max(f2);
			break;
		case "comp":cf.comp(f2);
			break;
		case "div":cf.div(f2);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + s);
		}
	}

	@Override
	public double f(double x) {
		switch (op) {
		case Plus: return this.left.f(x)+this.right.f(x);
		case Times: return this.left.f(x)*this.right.f(x);
		case Divid: return this.left.f(x)/this.right.f(x);
		case Max: return Math.max(this.left.f(x),this.right.f(x));
		case Min: return Math.min(this.left.f(x),this.right.f(x));
		case Comp: return this.left.f(this.right.f(x));
		case None: return this.left.f(x);
		case Error:	

		default:
			throw new IllegalArgumentException("Unexpected value: " + op);
		}
	}

	@Override
	public function initFromString(String s) {
		
		s=s.strip();
		return this.recInit(s);
		
		
		
	}

	private function recInit(String s) {
		if(!s.contains(",")) {
			return new Polynom().initFromString(s);
			
		}
		
		String op;
		int pr=s.indexOf('(');
		op=s.substring(0,pr);
		int mid=middel(s);
		
		ComplexFunction cf= new ComplexFunction(this.recInit(s.substring(pr+1,mid)));
		switch (op) {
		case "plus":cf.plus(this.recInit(s.substring(mid+1,s.length()-1)));
			break;
		case "mul":cf.mul(this.recInit(s.substring(mid+1,s.length()-1)));
			break;
		case "min":cf.min(this.recInit(s.substring(mid+1,s.length()-1)));
			break;
		case "max":cf.max(this.recInit(s.substring(mid+1,s.length()-1)));
			break;
		case "comp":cf.comp(this.recInit(s.substring(mid+1,s.length()-1)));
			break;
		case "div":cf.div(this.recInit(s.substring(mid+1,s.length()-1)));
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + op);
		}
		return cf;
	}

	private int middel(String s) {
		int balance=0;
		char c;
		for(int i=0;i<s.length();i++) {
			c=s.charAt(i);
			if(balance==1&&c==',')return i;
			if(c=='(')balance++;
			else if(c==')')balance--;
		}
		return -1;
	}

	@Override
	public function copy() {
		ComplexFunction cf= new ComplexFunction(this.left.copy(),this,op);
		return cf;
	}

	@Override
	public void plus(function f1) {
//		Polynom p=new Polynom();
//		if(f1 instanceof Monom)p.add((Monom)f1);
//		if(f1 instanceof Polynom_able)p.add((Polynom_able)f1);
//		if(this.left instanceof Polynom_able) {
//			((Polynom_able) this.left).add(p);
//			return;
//		}
		this.left = new ComplexFunction(this.left,f1,Operation.Plus);
	}

	@Override
	public void mul(function f1) {
		this.left = new ComplexFunction(this.left,f1,Operation.Times);
	}

	@Override
	public void div(function f1) {
		this.left = new ComplexFunction(this.left,f1,Operation.Divid);
	}

	@Override
	public void max(function f1) {
		this.left = new ComplexFunction(this.left,f1,Operation.Max);
	}

	@Override
	public void min(function f1) {
		this.left = new ComplexFunction(this.left,f1,Operation.Min);
	}

	@Override
	public void comp(function f1) {
		this.left = new ComplexFunction(this.left,f1,Operation.Comp);

	}

	@Override
	public function left() {
		if(this.left instanceof ComplexFunction) {
			return ((ComplexFunction)this.left).left;
		}
		return this.left;
	}

	@Override
	public function right() {
		if(this.left instanceof ComplexFunction) {
			return ((ComplexFunction)this.left).right;
		}
		return null;
	}

	@Override
	public Operation getOp() {
		if(this.left instanceof ComplexFunction) {
			return ((ComplexFunction)this.left).op;
		}
		return this.op;
	}
//	
	public String toString() {
		if(op==Operation.None)return this.left.toString();
		return (toString(this.op).concat("(".concat(this.left.toString()
				.concat(",".concat(this.right.toString().concat(")"))))));	
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof function)){return false;}
		function func=(function)obj;
		return this.partialyEquals(func);
	}

	private boolean partialyEquals(function f) {
		double d;
		if(!equalsInNeighborhood(this,f,0))return false;
		for(int r=1; r<100000;r*=10) {
			d=Math.random()*r+r;
			if(!equalsInNeighborhood(this,f,d))return false;
		}
		return true;
	}

	private boolean equalsInNeighborhood(ComplexFunction complexFunction, function f, double d) {
		double eps=0001;
		for(;d<1000*eps;d+=eps) {
			if(this.f(d)!=f.f(d)||this.f(-d)!=f.f(-d))return false;
		}
		return true;
	}

}
