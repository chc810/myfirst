import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class UrlClazzLoaderTest {
	
	public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		String url = "http://123.56.112.31/test/";
		ClassLoader c1 = new URLClassLoader(new URL[]{new URL(url)}, ClassLoader.getSystemClassLoader());
		Class<?> clazz = c1.loadClass("PersonPo");
		Object o = clazz.newInstance();
		System.out.println(o);
		System.out.println(o.getClass().getClassLoader());
		PersonI p = (PersonI)o;
		p.setName("张三");
		System.out.println(p.getName() + "," + p.getAge());
		System.out.println(PersonI.class.getClassLoader());
		
	}

}
