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
		this.op=Operation.None;
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
		case Divid: if(this.right.f(x)==0)throw new ArithmeticException("/ by zero");
			else return this.left.f(x)/this.right.f(x);
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
//		**a function that is not defined for every x will be equal to another function
//		if and only if that function is also not defined for every x
//		since base function can be changed after addition to ComplexFunction
//		the following check is costly and meaningless and so was removed.
//		boolean iszero=false;
//		try{iszero=((Polynom)f1).isZero();}catch (Exception e) {
//			if(!(f1 instanceof ComplexFunction))
//				iszero=f1.equals(f1.copy().initFromString("0"));
//		}
//		if(!iszero) {
		this.left = new ComplexFunction(this,f1,Operation.Divid);
//		}else { throw new ArithmeticException("divid by zero");}
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
		return toString(this.op).concat("(".concat(this.left.toString()
				.concat(",".concat(this.right.toString().concat(")")))));	
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof function)){return false;}
		boolean cf1,cf2;
		cf1=cf2=true;
		function f1,f2;
		f2=(function)obj;
		f1=this;
		if(this.getOp()==Operation.None) {
			f1=this.left;
			cf1=false;
			}
		if(f2 instanceof ComplexFunction&&((ComplexFunction)f2).getOp()==Operation.None) {
			cf2=false;
			f2=((ComplexFunction)f2).left;
		}
		if(!cf1&&!cf2) {
			if( ((f1 instanceof Polynom)||(f1 instanceof Monom)) &&
				((f2 instanceof Polynom)||(f2 instanceof Monom))){
				return f1.equals(f2);
				}
			}
		if(!cf1 && !(f1 instanceof Polynom) && !(f1 instanceof Monom) ){
			try {return f1.equals(f2);}catch (Exception e) {;}
			}
		if(!cf2 && !(f2 instanceof Polynom) && !(f2 instanceof Monom) ){
			try {return f2.equals(f1);}catch (Exception e) {;}	
			}
		return this.partialyEquals(f2,0.0001);//both ComplexFunction or can't be compared for some reason; 
	}
	
	private boolean partialyEquals(function f, double eps) {
		double x;
		for(x=0; x<10000;x+=(Math.random())+0.65) {
			if(!equalsInNeighborhood(this,f,x))return false;
			if(!equalsInNeighborhood(this,f,-x))return false;
			}
		for(int i=0; i<10; i++) {
			x=Math.random();
			if(!equalsInNeighborhood(this,f,x))return false;
			if(!equalsInNeighborhood(this,f,-x))return false;
			}
	
		return true;
			
	}
		
		
	private boolean equalsInNeighborhood(ComplexFunction complexFunction, function f, double x) {
		boolean zero1,zero2;
		double f1,f2;
		f1=f2=0;
		for(int i=0;i<2;i++) {
			try {
				f1+=this.f(x);
				zero1=false;
			}catch (ArithmeticException e) {
				zero1=true;
			}
			try {
				f2+=f.f(x);
				zero2=false;
			}catch (ArithmeticException e) {
				zero2=true;
			}
			if(zero1!=zero2) {
				f1=f2=0;
				if(x<0)x-=0.0001;
				else{x+=0.0001;}
				continue;
			}
			if(!((f1-f2<0.0000001) && (f1-f2>-0000001)))return false;
			return true;
		}
		return false;
	}

}
