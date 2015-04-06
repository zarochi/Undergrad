import java.util.ArrayList;

public class eq
{
   public String name;
   public String equation;
   public ArrayList<String> vars = new ArrayList<String>();
   public ArrayList<String> varnames = new ArrayList<String>();
   
   public eq(String n, String e)
   {
      name=n;
      equation=e;
   }
   
   public void addVars(String var, String n)
   {
      vars.add(var);
      varnames.add(n);
   }
   
   public void print()
   {
      System.out.println("Name: "+name);
      System.out.println("Equation: "+equation);
      for (int x=0;x<vars.size();x++)
      {
         System.out.println(vars.get(x)+" -> "+varnames.get(x));
      }
   }
}