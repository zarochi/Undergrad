import java.util.StringTokenizer;
import java.util.ArrayList;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

public class equation
{
   public static void main(String[] args)
   {
      equation e =new equation();
      System.out.println(e.solver("e=0.5*8*(30)^2", "e"));
      System.out.println(e.solve("e=0.5*8*(30)^2", "e"));
   }
   
   public String solver(String equation, String target)
   {
      StringTokenizer tokenizer = new StringTokenizer(equation, "=");   //this part takes the equation and seperates it to two sides
      String String1=tokenizer.nextToken();                             //based on the equal sign
      String String2=tokenizer.nextToken();
      if (String1.equalsIgnoreCase(target))
         return String2+"="+String1;
      else
      {
         int targetIndex=-1;  //stores the index of the target in String2
         if (contains(String1, target)) //determines which side the target/unknown character is on. Ensures string2 contains the target
         {                             
            String temp = String2;
            String2=String1;
            String1=temp;
         }
      
         ArrayList<Integer> addSub=new ArrayList<Integer>();   //holds the indexes of the + and - operators for grouping
         int startParenthesis=-1; //tracks where our parenthesis are
         int endParenthesis=100000000;
         if (!String2.substring(0,1).equals("+")&&!String2.substring(0,1).equals("-"))
            addSub.add(0); //handles for start of array when making the groups
         for (int x=0;x<String2.length();x++)
         {
            if (String2.substring(x,x+1).equals("(")&&x>startParenthesis)
               startParenthesis=x;
            if (String2.substring(x,x+1).equals(")"))
               endParenthesis=x;
            if (((x<startParenthesis||x>endParenthesis)||(startParenthesis==-1&&endParenthesis==100000000))&&(String2.substring(x,x+1).equals("+")||String2.substring(x,x+1).equals("-")))
               addSub.add(x);
         }
         if (addSub.get(addSub.size()-1)!=String2.length())
            addSub.add(String2.length()); //handles for end of array when making the groups
       
      //targetIndex=indexOfTarget(String2, target);
         ArrayList<String> groups = new ArrayList<String>();
         for (int x=0;x<addSub.size()-1;x++)
            groups.add(String2.substring(addSub.get(x), addSub.get(x+1)));  
         int loc=-1; //we use this part to solve for exponents that the target is not a part of
         boolean contained=false; //tracks whether or not the group contained the target
         String endofg="";//tracks the end of the group to be added back on
         for (int x=0;x<groups.size();x++) //gets the group that has the exponent and doesn't contain the target
         {
            if (!contains(groups.get(x), target)&&contains(groups.get(x), "^"))
               loc=x;
         
         }
         if (loc!=-1&&!contains(groups.get(loc),target))
         {
            int l=-1;
            for (int x=0;x<groups.get(loc).length()-1;x++)//finds the location of the ^ character
               if (groups.get(loc).substring(x,x+1).equals("^"))
                  l=x;
            String first = groups.get(loc).substring(0,l);
            String second = groups.get(loc).substring(l+1,groups.get(loc).length());
            System.out.println(first);
            System.out.println(second);
            System.out.println(groups.get(loc));
            double s = Float.parseFloat(evaluate(second));
            double f = Float.parseFloat(evaluate(first));
            groups.set(loc, Double.toString(Math.pow(f, s)));
            if (contained)
               groups.set(loc, groups.get(loc)+endofg);
         }
         
      //Now that we have groups, we can start applying addition/subtraction to the other side of the equation
         for (int x=0;x<groups.size();x++)
         {
            if (!contains(groups.get(x), target)) //process to the other side
               if (groups.get(x).substring(0,1).equals("+"))   //moves addition to the other side
                  String1+="-"+groups.get(x).substring(1,groups.get(x).length()); //reverses the sign and concatenates it to string1
               else if (groups.get(x).substring(0,1).equals("-"))
                  String1+="+"+groups.get(x).substring(1,groups.get(x).length()); //reverses the sign and concatenates it to string1
               else  //handles it if it is the first grouping
                  String1+="-"+groups.get(x).substring(0,groups.get(x).length()); //reverses the sign and concatenates it to string1
            else
               String2=groups.get(x); //we will be handling this later, assumes only 1 target
         }
      //At this point, String1 should contain everything that doesnt include the target
      //String2 should contain the group with the target so we can handle things like multiplication and division
         
         if (String2.substring(0,1).equals("-"))   //checks to see if String2 is being multiplied by -1, then flips the signs of String1
         {
            String1=flipSign(String1);
            String2=String2.substring(1,String2.length());
         }
         
      //the following handles the case for multiplication, division and parenthesis
      //to do this we are going to go with a grouping approach again
         ArrayList<String> group2 = new ArrayList<String>();
         ArrayList<Integer> mulDivPar = new ArrayList<Integer>(); //this will hold the indexes of our operators
         if (!String2.substring(0,1).equals("*")&&!String2.substring(0,1).equals("/")&&!String2.substring(0,1).equals("(")
               &&!String2.substring(0,1).equals(")"))
            mulDivPar.add(0); //handles for start of array when making the groups
         for (int x=0;x<String2.length();x++)
            if (String2.substring(x,x+1).equals("*")||String2.substring(x,x+1).equals("/")||String2.substring(x,x+1).equals("(")
               ||String2.substring(x,x+1).equals(")"))
               mulDivPar.add(x);
         if (mulDivPar.get(mulDivPar.size()-1)!=String2.length())
            mulDivPar.add(String2.length()); //handles for end of array when making the groups
         
         ArrayList<Integer> Parenthesis = new ArrayList<Integer>();  //this will store the location of parenthesis
         for (int x=0;x<String2.length();x++)
            if (String2.substring(x,x+1).equals("(")||String2.substring(x,x+1).equals(")"))
               Parenthesis.add(x);
      //We then use the information about the parenthesis in order to group again
         int min=1000000;
         int max=-1;
         for (int x=0;x<Parenthesis.size();x++) //gets min and max index of parenthesis so we don't seperate them by mistake
         {
            if (Parenthesis.get(x)>=max)
               max=Parenthesis.get(x);
            if (Parenthesis.get(x)<=min)
               min=Parenthesis.get(x);
         }
         if (!(min==1000000&&max==-1))
         {
            if (min==0)
               group2.add(String2.substring(min,max+1));
            else
               group2.add(String2.substring(min-1,max+1));
            for (int x=0;x<mulDivPar.size()-1;x++)
               if (!(mulDivPar.get(x)<=max&&mulDivPar.get(x)>=min))
                  group2.add(String2.substring(mulDivPar.get(x), mulDivPar.get(x+1)));
         }
         else
            for (int x=0;x<mulDivPar.size()-1;x++)
               if (x!=0)
                  group2.add(String2.substring(mulDivPar.get(x), mulDivPar.get(x+1)));
               else
                  group2.add(String2.substring(0, mulDivPar.get(x+1)));
         if (group2.get(0).substring(0,1).equals("*")||group2.get(0).substring(0,1).equals("/")) //swaps the groups if necessary
         {
            String temp = group2.get(0);
            group2.set(0,group2.get(1));
            group2.set(1,temp);
         }
      //there can only ever be 2-3 groups for our case, so we will do the below to get the parenthesis(if there are any) on the side
      //by itself. The right side will then contain either only the variable we are looking for, or a set of parenthesis
      //containing that variable. We can call the method until only the variable we are looking for is on the right side.
         
         if (group2.size()>1)
         {
            int operator=1000000000; //tracks where the operator we will be swapping is. helps remove it from the right side
            
            for (int x=0;x<String2.length()-1;x++)
               if (group2.get(1).length()>0&&group2.get(1).substring(0,1).equals(String2.substring(x,x+1)))
               {
                  operator=x;
                  break;
               }
            
            if (group2.get(1).contains(target))
            {
               if (group2.get(1).substring(0,1).equals("/"))
               {
                  String1="("+String2.substring(0,operator)+")/("+String1+")";
                  if (group2.size()>2&&!(group2.get(2).equals("/")||group2.get(2).equals("*")))
                     //String2=group2.get(1).substring(1,group2.get(1).length())+group2.get(2);
                     String2=group2.get(1).substring(1,group2.get(1).length())+addGroups(group2);
                  else
                     String2=group2.get(1).substring(1,group2.get(1).length());
               }
               if (group2.get(1).substring(0,1).equals("*"))
               {
                  String1="("+String1+")/"+String2.substring(0,operator);
                  if (group2.size()>2&&!(group2.get(2).equals("/")||group2.get(2).equals("*")))
                     //String2=group2.get(1).substring(1,group2.get(1).length())+group2.get(2);
                     String2=group2.get(1).substring(1,group2.get(1).length())+addGroups(group2);
                  else
                     String2=group2.get(1).substring(1,group2.get(1).length());
               }
            }
            else if (operator!=1000000000&&group2.size()>0&&group2.get(1).length()>1&&group2.get(1).substring(0,1).equals("/"))
            {
               String1="("+String1+")*"+group2.get(1).substring(1,group2.get(1).length());
               if (group2.size()>2&&!group2.get(2).equals("/"))
                  //String2=String2.substring(0, operator)+group2.get(2);
                  String2=String2.substring(0, operator)+addGroups(group2);
               else
                  String2=String2.substring(0,operator);
            }
            else if (operator!=1000000000&&group2.size()>0&&group2.get(1).length()>1&&group2.get(1).substring(0,1).equals("*"))
            {
               String1="("+String1+")/"+group2.get(1).substring(1,group2.get(1).length());
               if (group2.size()>2&&!group2.get(2).equals("*"))
                  //String2=String2.substring(0, operator)+group2.get(2);
                  String2=String2.substring(0, operator)+addGroups(group2);
               else
                  String2=String2.substring(0,operator);
            }
         }
         
      //This part handles it if the target has an exponent
         if (String2.contains(target)&&String2.contains("^"))
         {
            int location=-1;
            for (int x=0;x<String2.length()-1;x++)
               if (String2.substring(x, x+1).equals("^"))
                  location=x;
            String fir = String2.substring(0,location);
            String sec = String2.substring(location+1, String2.length());
            sec=evaluate("1/("+sec+")");
            String1="("+String1+")^"+sec;
            String2=fir;
         }
      
         if (String2.substring(0,1).equals("(")&&String2.substring(String2.length()-1,String2.length()).equals(")"))
            return String1+"="+String2.substring(1,String2.length()-1);
         else if (String2.substring(0,1).equals("+"))
            return String1+"="+String2.substring(1,String2.length());
         else   
            return String1+"="+String2; //returns the equation
      }
   }
   
   public String addGroups(ArrayList<String> g) //takes the group and outputs all but the items at indexes 0 and 1 to be able to concatenate
   {
      int size = g.size();
      String out="";
      for (int x=2;x<size;x++)
         out+=g.get(x);
      return out;
   }
   
   public boolean contains(String string, String target) //tests if a character is part of a string
   {
      boolean isthere=false;
      for (int x=0;x<string.length();x++)
         if (string.substring(x,x+1).equalsIgnoreCase(target))
            isthere=true;
      return isthere;
   }
   
   public int indexOfTarget(String string, String target) //tests if a character is part of a string and returns its index
   {
      int index=-1;
      for (int x=0;x<string.length();x++)
         if (string.substring(x,x+1).equalsIgnoreCase(target))
            index=x;
      return index;
   }
   
   public String flipSign(String string)
   {
      for (int x=0;x<string.length();x++)
      {
         if (string.substring(x,x+1).equals("+"))
            string=string.substring(0,x)+"-"+string.substring(x+1,string.length());
         if (string.substring(x,x+1).equals("-"))
            string=string.substring(0,x)+"+"+string.substring(x+1,string.length());
      }
      return string;
   }
   
   public String evaluate(String eq)   //uses javascript to evaluate the left side. This will return an error if the left side contains anything other than numbers and operators.
   {
      try         
      {
         if (eq.length()==1)
            return eq;
         else
         {
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            String infix;
            if (eq.substring(eq.length()-2,eq.length()-1).equals("="))
               infix = eq.substring(0, eq.length()-2);
            else
               infix = eq;
               
            if (contains(infix, "^")) //The javascript eval function cannot evaluate exponents. This part compinsates for that
            {
               int location=-1;
               for (int x=0;x<infix.length()-1;x++)
                  if (infix.substring(x,x+1).equals("^"))
                     location=x;
               if (infix.substring(location-1, location).equals(")"))
               {
                  int endp=0; //tracks the number of closing parenthesis before the ^ character
                  int start=0; //this will log the start location
                  for (int x=location;x>0;x--)
                     if (infix.substring(x-1,x).equals(")"))
                        endp++;
                  int y=location; //used to count the while loop
                  while (endp!=0&&y!=0)
                  {
                     if (infix.substring(y-1,y).equals("("))
                        endp--;
                     if (endp==0)
                        start=y;
                     y--;
                  }
                  if (infix.substring(location+1, location+2).equals("("))
                  {
                     int startp=0; //tracks the number of starting parenthesis
                     int end=0; //tracks the end of the exponent
                     for (int x=location;x<infix.length();x++)
                     {
                        if (infix.substring(x, x+1).equals("("))
                           startp++;
                     }
                     y=location;
                     while(startp!=0)
                     {
                        if (infix.substring(y, y+1).equals(")"))
                           startp--;
                        if (startp==0)
                           end=y;
                        y++;   
                     }
                     return evaluate(infix.substring(0, start-1)+Math.pow(Double.valueOf(evaluate(infix.substring(start, location-1))), Double.valueOf(evaluate(infix.substring(location+1, end+1))))+infix.substring(end+1, infix.length()));
                  }
                  else
                  {
                     int stopper=0;//tracks where the next operator is
                     for (int x=location;x<infix.length()-1;x++)
                        if (infix.substring(x,x+1).equals("+")||infix.substring(x,x+1).equals("-")||infix.substring(x,x+1).equals("*")||infix.substring(x,x+1).equals("/")||infix.substring(x,x+1).equals("="))
                        {
                           stopper=x;
                           break;
                        }
                     
                     if (stopper!=0)
                        return evaluate(infix.substring(0, start-1)+Math.pow(Double.valueOf(evaluate(infix.substring(start, location-1))), Double.valueOf(evaluate(infix.substring(location+1, stopper-1))))+infix.substring(stopper-1, infix.length())); 
                     else
                        return evaluate(infix.substring(0, start-1)+Math.pow(Double.valueOf(evaluate(infix.substring(start, location-1))), Double.valueOf(evaluate(infix.substring(location+1, infix.length())))));
                  }
               }
               else
               {
                  int start=0,end=infix.length();
                  for (int x=location;x>0;x--)
                     if (infix.substring(x,x+1).equals("+")||infix.substring(x,x+1).equals("-")||infix.substring(x,x+1).equals("*")||infix.substring(x,x+1).equals("/")||infix.substring(x,x+1).equals("="))
                     {
                        start=x;
                        break;
                     }
                  for (int x=location;x<infix.length()-1;x++)
                     if (infix.substring(x,x+1).equals("+")||infix.substring(x,x+1).equals("-")||infix.substring(x,x+1).equals("*")||infix.substring(x,x+1).equals("/")||infix.substring(x,x+1).equals("="))
                     {
                        end=x;
                        break;
                     }
                  return evaluate(infix.substring(0, start)+Math.pow(Double.valueOf(evaluate(infix.substring(start, location))), Double.valueOf(evaluate(infix.substring(location+1, end))))+infix.substring(end, infix.length()));
               }
            }   
            return engine.eval(infix).toString();
         }
      }
      catch(Exception ex)
      {
         System.out.println(ex.toString());
         return null;
      }
   }
   
   public String solve(String eq, String variable)
   {
      boolean solved=false;
      while (!solved) //runs the solver method in the equation class until it has the variable we are looking for as the only variable on the right side.
      {
         eq=solver(eq, variable);
         if (eq.length()>1&&eq.substring(eq.length()-1,eq.length()).equals(variable)&&eq.substring(eq.length()-2,eq.length()-1).equals("="))
            solved=true;
      }
      System.out.println(eq);
      return evaluate(eq);
   }
}