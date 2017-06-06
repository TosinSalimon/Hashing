
import java.io.*;
import java.util.*;

public class Hashing {
    static int size = 433099;
    static String[] hashTable = new String[size];
    static String[] array = new String[216555]; 

    public static void main (String[] args) throws IOException {
      File testFile = new File("C:\\Users\\tosin\\Documents\\dictionary.txt");    


      getContents(testFile);

      System.out.println("Which type of open addressing would you like to use?");
      System.out.println("1) Linear Probing");
      System.out.println("2) Quadratic Probing");
      System.out.println("3) Double Hashing");
      Scanner in = new Scanner(System.in);
      int strategy = in.nextInt();

      switch(strategy){
          case 1: 
            fillLinearProbing();
            break;
          case 2: 
            fillQuadraticProbing();
            break;
          case 3: 
            fillDoubleHash();
            break;
      }     
      in.nextLine();
      System.out.print("\nEnter a word to find: ");
      String word = in.nextLine();
      while(!word.equals("quit")){
          find(word, strategy);
          System.out.print("\nEnter a word to find: ");
          word = in.nextLine();
      }
    }
 
     public static void find(String word, int strategy){

         int probes = 1;
         int index = getHashKey(word);

         System.out.println();
         while(hashTable[index]!=null&&!hashTable[index].equals(word)){
             System.out.println("Checking slot "+index+"...collision with "+hashTable[index]);

            if(strategy==1){

                index++;
                probes++;
                index=index%size;

            }else if(strategy==2){
                index=index+(probes*probes);
                probes++;
                index=index%size;
            }else if(strategy==3){
                index=index+getDoubleHashKey(word);
                probes++;
                index=index%size;
            }
        }
        if(hashTable[index]==null){
            System.out.println("NOT IN HASHTABLE");

        }else{
            System.out.println("The word "+word+" was found in slot "+index+" of the hashtable");
        }     
        System.out.println("Number of hash table probes: "+probes);

     }
     
     
     public static int getHashKey(String word){
    	  int sum = 0;
    	  int total=0;
    	 for(int i = 0; i<word.length(); i++){
    		 int c = (int)word.charAt(i);
    		 
    		 total = total + modMult(c, 27, size);
    		 
    		 int num = modPow(total, i, size);
    		 sum = sum+ num;
    	 }
    	 int index = sum %size;
    	 return index;
      }

     public static int getDoubleHashKey(String word){
        return (int)word.charAt(word.length()-1);
     }
     
     
     
     
      public static void fillLinearProbing(){
         int totalcollisions=0;

         for(int i=0; i<array.length;i++){

            int collisions=0;
            int index = getHashKey(array[i]);

            while(hashTable[index]!=null){

                collisions++;
                index++;
                index=index%size;

            }
            hashTable[index]=array[i];
            if(i%100==0){
                System.out.println(array[i] + " was placed in slot "+index+" of the hash table after "+collisions+" collisions");
            }
            totalcollisions+=collisions;

        }
         System.out.println("The total number of collisions was "+ totalcollisions);
      }
      
      public static void fillQuadraticProbing(){
          int totalcollisions=0;
          for(int i=0; i<array.length;i++){
            int collisions=0;
            int index = getHashKey(array[i]);
            int queries=1;
            while(hashTable[index]!=null){
                collisions++;
                index=index+(queries*queries);
                index=index%size;
                queries++;
            }
            hashTable[index]=array[i];
            if(i%100==0){
                System.out.println(array[i] + " was placed in slot "+index+" of the hash table after "+collisions+" collisions");
            } 
            totalcollisions+=collisions;
         }
         System.out.println("The total number of collisions was "+ totalcollisions);
      }
      
      public static void fillDoubleHash(){
         int totalcollisions=0;
         for(int i=0; i<array.length;i++){
            int collisions=0;
            int index = getHashKey(array[i]);
            int doubleHash = getDoubleHashKey(array[i]);
            while(hashTable[index]!=null){
                collisions++;
                index=index+doubleHash;
                index=index%size;
            }
            hashTable[index]=array[i];
            if(i%100==0){
                System.out.println(array[i] + " was placed in slot "+index+" of the hash table after "+collisions+" collisions");
            }
            totalcollisions+=collisions;
         }
         System.out.println("The total number of collisions was "+ totalcollisions);
      }

    public static int modPow(int number, int power, int modulus){

        if(power==0)
            return 1;
        else if (power%2==0) {
            int halfpower=modPow(number, power/2, modulus);
            return modMult(halfpower,halfpower,modulus);
        }else{
            int halfpower=modPow(number, power/2, modulus);
            int firstbit = modMult(halfpower,halfpower,modulus);
            return modMult(firstbit,number,modulus);
        }
    }
    
    public static int modMult(int first, int second, int modulus){

        if(second==0)
            return 0;
        else if (second%2==0) {
            int half=modMult(first, second/2, modulus);
            return (half+half)%modulus;
        }else{
            int half=modMult(first, second/2, modulus);
            return (half+half+first)%modulus;
        }
     }



  public static String getContents(File aFile) {
    //...checks on aFile are elided
    StringBuffer contents = new StringBuffer();

    //declared here only to make visible to finally clause
    BufferedReader input = null;
    try {
      //use buffering, reading one line at a time
      //FileReader always assumes default encoding is OK!
      input = new BufferedReader( new FileReader(aFile) );
      String line = null; //not declared within while loop
      /*
      * readLine is a bit quirky :
      * it returns the content of a line MINUS the newline.
      * it returns null only for the END of the stream.
      * it returns an empty String if two newlines appear in a row.
      */
      int i = 0;
      while (( line = input.readLine()) != null){
        array[i]=line;
        i++;
        contents.append(System.getProperty("line.separator"));
      }
    }
    catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
    finally {
      try {
        if (input!= null) {
          //flush and close both "input" and its underlying FileReader
          input.close();
        }
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return contents.toString();
  }


  public static void setContents(File aFile)
                                 throws FileNotFoundException, IOException {

    //declared here only to make visible to finally clause; generic reference
    Writer output = null;
    try {
      //use buffering
      //FileWriter always assumes default encoding is OK!
      output = new BufferedWriter( new FileWriter(aFile) );
      int i=0;
      while(array[i]!=null){
        output.write( array[i] );
        output.write(System.getProperty("line.separator"));
        i++;
      }
    }
    finally {
      //flush and close both "output" and its underlying FileWriter
      if (output != null) output.close();
    }
  }
}