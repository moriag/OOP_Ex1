package Ex1;
import Ex1.Operation;
public class ComplexFunction implements complex_function {
	
	private Operation op;
	private function left;
	private function right;
	
	
	/**
	 * @param op
	 * @return the string matching the Operation op (not op.name())
	 */
	private String toString(Operation op) {
		String s=op.name();
		if(s=="Times")return "mul";
		if(s=="Divid")return"div";
		return s.toLowerCase();		
	}
	
	/**
	 * @param f
	 * creates new ComplexFunction instance containing f
	 */
	public ComplexFunction(function f) {
		this.left=f;
		this.op=Operation.None;
	}
	
	/**
	 * creates the ComplexFunction representation of op(f1,f2) 
	 * @param op
	 * @param f1
	 * @param f2
	 */
	public ComplexFunction(Operation op, function f1, function f2) {
		this.op=Operation.None;
		this.left= new ComplexFunction(f1,f2,op);
	}
	
	
	/**
	 * creates the ComplexFunction representation of op(f1,f2), keeps ComplexFunction structure
	 * @param left
	 * @param right
	 * @param op
	 */
	private ComplexFunction(function left, function right, Operation op) {
		this.op=op;
		if(left instanceof ComplexFunction) this.left=((ComplexFunction) left).left;
		else {this.left=left;}
		if(right instanceof ComplexFunction) this.right=((ComplexFunction) right).left;
		else {this.right=right;}
	}
	/**
	 * creates the ComplexFunction representation of op(f1,f2) s is the string representation of op 
	 * @param s
	 * @param f1
	 * @param f2
	 */
	public ComplexFunction(String s, function f1, function f2) {
		switch (s) {
		case "plus":this.left=new ComplexFunction(f1,f2,Operation.Plus);
			break;
		case "mul":this.left=new ComplexFunction(f1,f2,Operation.Times);
			break;
		case "min":this.left=new ComplexFunction(f1,f2,Operation.Min);
			break;
		case "max":this.left=new ComplexFunction(f1,f2,Operation.Max);
			break;
		case "comp":this.left=new ComplexFunction(f1,f2,Operation.Comp);
			break;
		case "div":this.left=new ComplexFunction(f1,f2,Operation.Divid);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + s);
		}
	}


	/**
	 *returns the real value y=f(x)
	 */
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

	/**
	 *initialize this ComplexFunction from String s(s1,s2) s1 and s2 are function strings, s can be: "plus","mul","div","min","max","comp".
	 *assumes expression is balanced and the inner functions strings represents Monoms or Polynoms.
	 */
	@Override
	public function initFromString(String s) {
		s=s.replaceAll(" ", "");
		return new ComplexFunction(recInit(s));
	}

	private function recInit(String s) {
		if(!s.contains(",")) {
			return new Polynom().initFromString(s);
		}
		
		String op;
		int pr=s.indexOf('(');
		op=s.substring(0,pr);
		int mid=middel(s);
		return new ComplexFunction(op,this.recInit(s.substring(pr+1,mid)),this.recInit(s.substring(mid+1,s.length()-1)));
//		ComplexFunction cf= new ComplexFunction(this.recInit(s.substring(pr+1,mid)));
//		switch (op) {
//		case "plus":cf.plus(this.recInit(s.substring(mid+1,s.length()-1)));
//			break;
//		case "mul":cf.mul(this.recInit(s.substring(mid+1,s.length()-1)));
//			break;
//		case "min":cf.min(this.recInit(s.substring(mid+1,s.length()-1)));
//			break;
//		case "max":cf.max(this.recInit(s.substring(mid+1,s.length()-1)));
//			break;
//		case "comp":cf.comp(this.recInit(s.substring(mid+1,s.length()-1)));
//			break;
//		case "div":cf.div(this.recInit(s.substring(mid+1,s.length()-1)));
//			break;
//		default:
//			throw new IllegalArgumentException("Unexpected value: " + op);
//		}
//		return cf;
	}

	/**
	 * @param s
	 * @return i, the index of ',' separating the main functions strings
	 */
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
	

	
	/**
	 *creates a deep copy of this
	 */
	@Override
	public function copy() {
		return new ComplexFunction(recCopy(this));
	}

	private function recCopy(ComplexFunction cf) {
		function r=this.right;
		if(r!=null) r=r.copy();
		return new ComplexFunction ( this.left.copy() , r ,this.op);
		}

	@Override
	public void plus(function f1) {
		this.left = new ComplexFunction(this.left,f1,Operation.Plus);
	}

	@Override
	public void mul(function f1) {
		this.left = new ComplexFunction(this,f1,Operation.Times);
	}

	@Override
	public void div(function f1) {
		this.left = new ComplexFunction(this,f1,Operation.Divid);
	}

	@Override
	public void max(function f1) {
		this.left = new ComplexFunction(this,f1,Operation.Max);
	}

	@Override
	public void min(function f1) {
		this.left = new ComplexFunction(this,f1,Operation.Min);
	}

	@Override
	public void comp(function f1) {
		this.left = new ComplexFunction(this,f1,Operation.Comp);

	}

	@Override
	public function left() {
		if( !(this.left instanceof ComplexFunction) )return this.left;
		if(this.right==null)return new ComplexFunction(((ComplexFunction)this.left).left);
		return new ComplexFunction(((ComplexFunction)this.left));
	}

	@Override
	public function right() {
		function right;
		if( !(this.left instanceof ComplexFunction) || (this.op != Operation.None))right= this.right;
		else {right= ((ComplexFunction)this.left).right;}
		if(right instanceof ComplexFunction)return new ComplexFunction(right);
		return right;
	}

	@Override
	public Operation getOp() {
		if( !(this.left instanceof ComplexFunction) || (this.op != Operation.None))return this.op;
		return ((ComplexFunction)this.left).op;
	}
	
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
