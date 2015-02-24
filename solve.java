import cs1.Keyboard;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

public class solve
{
   public static void main(String[] args)
   {
      //System.out.println("Enter your equation:");
      //String eq=Keyboard.readString();
      //System.out.println("Enter the Variable you would like to find:");
      //String variable=Keyboard.readString();
      equation e=new equation();
      //diameter
      System.out.println(e.solve("d=2*4", "d"));
      System.out.println(e.solve("8=2*r", "r"));
      //diameter
      System.out.println(e.solve("d=2/3.14159", "d"));
      System.out.println(e.solve("7=c/3.14159", "c"));
      //radius
      System.out.println(e.solve("r=2/(2*3.14159)", "r"));
      System.out.println(e.solve("7=c/(2*3.14159)", "c"));
      //radius
      System.out.println(e.solve("r=(9/3.14159)^(1/2)", "r"));
      System.out.println(e.solve("9=(a/3.14159)^(1/2)", "a"));
      //Normal Stress
      System.out.println(e.solve("s=1/2", "s"));
      System.out.println(e.solve("37=f/3", "f"));
      System.out.println(e.solve("80=3/a", "a"));
      //Shear Stress
      System.out.println(e.solve("t=7*80*5", "t"));
      System.out.println(e.solve("2800=w*80*5", "w"));
      System.out.println(e.solve("2800=7*r*5", "r"));
      System.out.println(e.solve("2800=7*80*s", "s"));
      //Hydralic Radius
      System.out.println(e.solve("r=1/2", "r"));
      System.out.println(e.solve("2=a/7", "a"));
      System.out.println(e.solve("5=60/p", "p"));
      //kinetic energy
      System.out.println(e.solve("e=0.5*8*(30)^2", "e"));
      System.out.println(e.solve("964=0.5*m*(30)^2", "m"));
      System.out.println(e.solve("964=0.5*80*(v)^2", "v"));
      //velocity
      System.out.println(e.solve("v=2/5", "v"));
      System.out.println(e.solve("80=5/t", "t"));
      System.out.println(e.solve("80=x/5", "x"));
      //acceleration
      System.out.println(e.solve("a=(8-3)/2", "a"));
      System.out.println(e.solve("3=(9-3)/t", "t"));
      System.out.println(e.solve("3=(f-2)/2", "f"));
      System.out.println(e.solve("8=(9-i)/2", "i"));
      
      //test cases
      System.out.println(e.solve("3000=x*50*2*2*2", "x"));
      System.out.println(e.solve("1800000=300*x*60*(8/4)", "x"));
   }
}