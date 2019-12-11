package Ex1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;
//import org.json.simple.parser.ParseException;
//import org.json.simple.parser.JSONParser;
public class Functions_GUI implements functions {
	ArrayList<function> coll;
	@Override
	public int size() {
		return coll.size();
	}

	@Override
	public boolean isEmpty() {
		return this.size()==0;
	}

	@Override
	public boolean contains(Object o) {
		for(function f: this.coll) {
			if(f.equals(o))return true;
		}
		return false;
	}

	@Override
	public Iterator<function> iterator() {
		return this.coll.iterator();
	}

	@Override
	public Object[] toArray() {
		return coll.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return coll.toArray(a);
	}

	@Override
	public boolean add(function e) {
		return coll.add(e);
	}

	@Override
	public boolean remove(Object o) {
		for(function f: coll) {
			if(f.equals(o))return coll.remove(f);
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o: c) {
			if(this.contains(o))continue;
			return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends function> c) {
		boolean ans=false;
		for(function f: c) {
			ans=ans||this.add(f.copy());
		}
		return ans;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean ans=false;
		Object[] obj=c.toArray();
		for(Object o: obj) {
			ans=ans||this.remove(o);
		}		
		return ans;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		ArrayList<function> temp=new ArrayList<function>() ;
		for (Object o: c) {
			if(this.contains(o))temp.add((function)o);
		}
		int prevSize=this.size();
		this.coll=temp;
		return this.size()!=prevSize;

	}

	@Override
	public void clear() {
		coll.clear();
	}

	@Override
	public void initFromFile(String file) throws IOException {
		FileInputStream fileName = new FileInputStream(file);
        ObjectInputStream object = new ObjectInputStream(fileName);
        try {
			Functions_GUI fg = (Functions_GUI) object.readObject();
			this.coll=fg.coll;
		} catch (ClassNotFoundException e) {
			object.close();
			e.printStackTrace();
		}
        
		
	}

	@Override
	public void saveToFile(String file) throws IOException {
		 FileOutputStream fileName = new FileOutputStream(file);
		 ObjectOutputStream object = new ObjectOutputStream(fileName);
         object.writeObject(this);
         object.close();

	}

	@Override
	public void drawFunctions(int width, int height, Range rx, Range ry, int resolution) {
		StdDraw.setCanvasSize(width,height);
		double minx,maxx,step;
		minx=rx.get_min();
		maxx=rx.get_max();
		step=(maxx-minx)/resolution;
		StdDraw.setXscale(minx,maxx);
		StdDraw.setYscale(ry.get_min(), ry.get_max());
		double x0,x1;
		for(function f: this.coll) {
			for(x0= minx;x0<maxx;x0=x1) {
				x1=x0+step;
				StdDraw.line(x0,f.f(x0) ,x1, f.f(x1));
			}
		}
	}

	@Override
	public void drawFunctions(String json_file) {
		// TODO Auto-generated method stub
		
	}

}
