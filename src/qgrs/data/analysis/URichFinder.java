package qgrs.data.analysis;



public class URichFinder 
{
	String basePairs;
	  
	  public URichFinder (String bp)
	  {
	    basePairs = bp;
	  }
	  
	  
	  //returns an array of integers; each integer holds
	  //the index of the beginning of a U-rich sequence
	  //in the string basePairs, prints out the string
	  //to check
	  public URich getUs (int score)
	  {
		  URich bestU = null;
		  int maxFound = -1;
		  
		  
	    int length = basePairs.length();
	    
	    
	    //checks if each char in the string is the
	    //beginning of a Urich sequence
	    for (int i = 0; i < (length-5); i ++) 
	    {
	      //creates a hexamer
	      String tmp = basePairs.substring (i, (i+5));
	      int s = findUHex(tmp);
	      if (s >= score) //amount of Us is sufficient
	      {
	    	  if ( s > maxFound ) {
	    		  bestU = new URich(i, s, tmp);
	    	  }
	      }
	    }
	    
	    return bestU;
	  }

	  
	  //returns the amount of U's in a hexamer
	  //for s to be a hexamer it must have 5 chars
	  private int findUHex (String s) 
	  {
	    int u = 0;
	    
	    for (int i = 0; i < 5; i ++)
	    {
	      if (s.charAt (i) == 'U')
	        u++;
	    }
	    
	    return u;
	  }

}
