import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import cs1.Keyboard;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class gui
{
   ArrayList<eq> equations = new ArrayList<eq>();

   public static void main(String[] args)
   {
      gui g = new gui();
      g.getVar();
      g.listEq();
      String solvable = g.setEquation(g.selectEquation());
      System.out.println(solvable);
      System.out.println("Answer: "+g.singleVarCheck(solvable));
   }
   
   public void getVar() //look at database, fill equation array with them
   {
      try
      {
         BufferedReader reader = new BufferedReader(new FileReader("equations.txt"));
         String line = reader.readLine();
         while (line != null)
         {
            StringTokenizer colon = new StringTokenizer(line, ":");
            if (colon.countTokens() != 3)
               System.out.println("Incorrect number of parameters in data file");
            else
            {
               String line1 = colon.nextToken();
               String line2 = colon.nextToken();
               String line3 = colon.nextToken();
               eq temp = new eq(line1, line2);
               StringTokenizer semiColon = new StringTokenizer(line3, ";");
               while (semiColon.hasMoreTokens())
               {
                  temp.addVars(semiColon.nextToken(),semiColon.nextToken());
               }
               equations.add(temp);
            }
            line = reader.readLine();
         }
         reader.close();
      }
      catch (Exception e)
      {
         System.out.println(e.toString());
      }
   }
   
   public void print()
   {
      for (int x=0;x<equations.size();x++)
      {
         equations.get(x).print();
      }
   }
   
   public void listEq()
   {
      for (int x=0;x<equations.size();x++)
      {
         System.out.println(x+": "+equations.get(x).name+" ["+equations.get(x).equation+"]");
      }
   }
   
   public int selectEquation()
   {
      System.out.println("Select an equation by typing its number above.");
      int selection = Keyboard.readInt();
      equations.get(selection).print();
      return selection;
   }
   
   public String setEquation(int s)//fills in the user's values
   {
      ArrayList<String> entries = new ArrayList<String>();
      String modified = equations.get(s).equation;
      for (int x=0;x<equations.get(s).vars.size();x++)
      {
         System.out.println("Enter the value for: "+equations.get(s).varnames.get(x)+" ["+equations.get(s).vars.get(x)+"] or x if there is no data for this variable.");
         String tmp = Keyboard.readString();
         if (tmp.equals("x"))
         {
            entries.add(null);
         }
         else
         {
            entries.add(tmp);
         }
      }
      for (int x=0;x<entries.size();x++)
      {
         String lookingFor = equations.get(s).vars.get(x);
         if (entries.get(x) != null)
         {
            modified=modified.replaceAll(lookingFor, entries.get(x));
         }
      }
      return modified;
   }
   
   public String solve(String eq)
   {
      equation e = new equation();
      int var=-1;
      Pattern pattern = Pattern.compile("[a-zA-Z]");
      Matcher matcher = pattern.matcher(eq);
      if(matcher.find())
      {
         var=matcher.start();//this will give you index
      }
      return e.solve(eq, eq.substring(var,var+1));
   }
   
   public String singleVarCheck(String eq)//takes the equation and sees if only 1 variable is left. if not tries to use other equations to fill vars
   {
      Pattern pattern = Pattern.compile("[a-zA-Z]");
      Matcher matcher = pattern.matcher(eq);
      ArrayList<Integer> vars = new ArrayList<Integer>();
      while(matcher.find())
      {
         vars.add(matcher.start());
      }
      if (vars.size() == 1)
      {
         eq=solve(eq);
      }
      else
      {
         System.out.println("2 or more variables are left blank. This program can only solve for 1 variable at this time.");
      }
      return eq;
   }
}