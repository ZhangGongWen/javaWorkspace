package MyObjects;
class TestObj1 implements Cloneable{
	public int index;
	public TestObj1()
	{
		index = 0;
	}
    public TestObj1 clone(){
    	TestObj1 o = null;
        try{
            o = (TestObj1) super.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return o;
    }
	public TestObj1(TestObj1 obj)
	{
		index = obj.index;
	}
}
public class JustAtest {

	public static void main(String[] args)
	{
		JustAtest a = new JustAtest();
		a.func1();
	}
	public void func1()
	{
		TestObj1 test1 =new TestObj1();
		TestObj1 test2 =func3(test1);
		System.out.println(test1.index);
		System.out.println(test2.index);
	}
	public TestObj1 func2(TestObj1 obj)
	{
		TestObj1 temp = new TestObj1();
		temp = obj;
		temp.index=1;
		return temp;
	}
	public TestObj1 func3(TestObj1 obj)
	{
		TestObj1 temp = (TestObj1) obj.clone();
		temp.index=1;
		return temp;
	}
}
