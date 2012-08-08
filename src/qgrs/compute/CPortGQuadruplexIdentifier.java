package qgrs.compute;

/*
 * An Implementation of GQuadruplexIdentifier containing a Java port of Oleg's C implementation.
 * Tested for ~40 000 results, gives the same results as Oleg's implementation.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

import qgrs.data.Base;
import qgrs.data.BaseSymbol;
import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.job.CancelException;
import qgrs.job.CancelFlag;
import qgrs.job.Cancellable;
import qgrs.job.DefaultCancelFlag;


public class CPortGQuadruplexIdentifier implements QgrsIdentifier , Cancellable {

	//the constants that make up the difference between type 1 and type 2 qgrs
	//identification
	final int MAX_GRS_LENGTH;
	final int GGROUPMIN;

	//a constant to determine whether output should be written out to a file
	//(for testing) or not
	final boolean isForTesting;

	double dif,a,b;
	String localBuf;
	String loop1;
	String loop2;
	String loop3;
	int grp=0;

	/*
	 * If we are using the instance for testing, findGQuadruplexes is going to write the results 
	 * out to a file
	 */
	public CPortGQuadruplexIdentifier(int type,boolean isForTesting) throws Exception
	{

		switch(type)
		{
		case 1:
			GGROUPMIN=2;
			MAX_GRS_LENGTH=30;
			break;
		case 2:
			GGROUPMIN=3;
			MAX_GRS_LENGTH=45;
			break;
		case 3:
			GGROUPMIN=2;
			MAX_GRS_LENGTH=45;
			break;
		default:

			throw new Exception("Unable to create an instance: the only values"
					+"accepted for the parameter \"type\" are\"1\" and \"2\"");
		}


		this.isForTesting=isForTesting;


	}
	
	CancelFlag cancelFlag = new DefaultCancelFlag();
	
	@Override
	public void setCancelFlag(CancelFlag flag) {
		cancelFlag = flag;
	}
	
	@Override
	public void findGQuadruplexes(GeneSequence sequence) throws Exception {
		// TODO Auto-generated method stub
		int nextGqId = 1;
		StringBuilder seq=new StringBuilder("");

		int i,j,z,x,pos2,pos3,flag;
		int zero_loop, loop_size,start3;
		int g1 = 0,g2,g3,gap1,gap2,gap3;  //I INITIALIZE G1 BECAUSE ECLIPSE COMPLAINED IT MIGHT NOT HAVE BEEN INITIALIZED..?
		int count=0;
		int MIN_GRS_LENGTH=10;
		int FORMULA_LENGTH=MAX_GRS_LENGTH-9;

		List<GQuadruplex> quads=new LinkedList<GQuadruplex>();
		List<Base> bases=sequence.getBases();
		for(Base base: bases)
		{
			BaseSymbol symbol = base.getSymbol();
			seq.append(symbol.toString());
		}

		if ( this.cancelFlag.isRaised()) throw new CancelException();

		BufferedWriter out = null;
		// Create the output file for testing purposes
		if(isForTesting)
		{
			FileWriter fstream = new FileWriter("jOutput.txt");
			out = new BufferedWriter(fstream);
		}
		int seqlen=seq.length();


		/*NB: YOU NEED THIS BECAUSE THE ALGORITHM TRIES TO ACCESS UP TO A FEW POSITIONS AFTER THE END
		 * OF THE STRING! I FILL UP TO 3*1024*1024 JUST TO MAKE IT EXACTLY THE SAME AS THE C++ ONE
		 * WHICH ALSO TRIES TO ACCESS A FEW POSITIONS AFTER THE END OF THE STRING!
		 */
		for(int ii=seqlen;ii<1024;ii++)
		{
			seq.append("0");
		}
		//printf("Length: %d\n",seqlen);
		if ( this.cancelFlag.isRaised()) throw new CancelException();

		for (i=0;i<=seqlen-MIN_GRS_LENGTH;i++)
		{
			//printf("%d ",i);
			if (seq.charAt(i)!='G') continue;



			for (j=MIN_GRS_LENGTH;j<=MAX_GRS_LENGTH;j++)
			{

				//printf("{%d} ",j); 


				for (x=GGROUPMIN;x<7;x++)
				{
					// printf("[%d]",x); 

					if (x*4+2>j) break;//no need to consider, if it doesn't fit anyway

					//if (i==6) printf("DEB: %d ",x);

					//check if there is GG, GGG, GGGG in the beginning
					flag=0;
					for (z=i;z<i+x;z++) 
						if (seq.charAt(z)!='G') {flag=1;break;}
					if (flag==1) break;

					//if (i==6) printf("DEB2: %d ",x);


					//check if there is GG, GGG, GGGG in the end
					flag=0;
					for (z=i+j-1;z>=i+j-x;z--) 
					{
						if (seq.length() <= z || seq.charAt(z)!='G') {
							flag=1;
							break;
						}
					}
					//if (i==6) printf("DEB3: %d %d ",z,x);
					if (flag==1) break;

					//6+11-1




					for (pos2=x;pos2<=j-x*3-1;pos2++)
					{
						// printf("p2:%d ",pos2);
						//check if there is GG, GGG, GGGG at the pos2
						flag=0;
						for (z=i+pos2;z<i+x+pos2;z++) 
							if (seq.charAt(z)!='G') {flag=1;break;}
						if (flag==1) {/*pos2=i+x+pos2-1;*/continue;}

						//check if loop size is within limits
						loop_size=pos2-x;
						//if ( (loop_size<LOOP_MIN) || (loop_size>LOOP_MAX) ) continue;

						if (pos2==x) zero_loop=1; else zero_loop=0;


						//if we had a zero loop, make next one non-zero
						if (zero_loop==0) start3=x+pos2; else start3=x+pos2+1;

						for (pos3=start3;pos3<=j-x*2;pos3++)
						{
							//printf("p3:%d ",pos3);

							//check if there is GG, GGG, GGGG at the pos3
							flag=0;
							for (z=i+pos3;z<i+x+pos3;z++) if (seq.charAt(z)!='G') {flag=1;break;}
							if (flag==1) {/*pos3=i+x+pos2-1;*/continue;}

							//check if loop #2 size is within limits
							loop_size=pos3-pos2-x;
							//if ( (loop_size<LOOP_MIN) || (loop_size>LOOP_MAX) ) continue;

							//check if loop #3 size is within limits
							loop_size=j-pos3-x*2;
							//if ( (loop_size<LOOP_MIN) || (loop_size>LOOP_MAX) ) continue;

							//zero loop counter update
							if (pos3==pos2+x) zero_loop++; 

							//printf("zr:%d",zero_loop);

							//check if only one zero loop
							if ( ((zero_loop==0) && ((pos3<=j-2*x))) ||    //if no zero loops, QGRS can be right
									((zero_loop!=0) && ((pos3<j-2*x)))
									)                      

								g1=pos2-x;
							g2=pos3-pos2-x;
							g3=j-x-pos3-x;

							gap1=Math.abs(g1-g2);
							gap2=Math.abs(g1-g3);
							gap3=Math.abs(g3-g2);


							int nscore=FORMULA_LENGTH-(gap1+gap2+gap3)/2 + (x-2)*FORMULA_LENGTH;

							//printf("%d) POS:%d   P2:%d   P3:%d   GLEN:%d   LEN:%d   SCORE:%d\n",count,i,pos2,pos3,x,j,nscore);
							//printf("%d %d %d %d %d %d ",i,pos2,pos3,x,j,nscore);


							GQuadruplex gq=new GQuadruplex(sequence, nextGqId++);
							//NEW SETTERS FOR THE GQUADRUPLEX CLASS THAT ENSURE
							//POINTERS TO THE BASES WE ARE INTERESTED IN INSTEAD OF
							//INDICES
							gq.setStart(bases.get(i));
							gq.setTetrad2Start(bases.get(i+pos2));
							gq.setTetrad3Start(bases.get(i+pos3));
							gq.setTetrad4Start(bases.get(i+j-x));

							//OLD SETTERS THAT ONLY TELL US INDICES OF THE BASES
							//WE ARE INTERESTED IN BUT THESE INDICES ARE SUBJECT
							//TO CHANGE AFTER NEEDLEMAN ALIGNMENT IS PROCESSED
							//gq.setStartIndex(i);
							//gq.setP2(pos2);
							//gq.setP3(pos3);

							gq.setNumTetrads(x);
							gq.setLength(j);
							gq.setScore(nscore);

							if ( gq.isAcceptable() ) {
								quads.add(gq);
							}
							


							try
							{
								if(isForTesting)
								{
									out.append(i+ " "+pos2+ " "+pos3+" "+x+ " "+j+ " "+nscore+ " ");
									out.flush();
								}
							}
							catch (Exception e)
							{//Catch exception if any
								System.err.println("Error: " + e.getMessage());
							}
							count++;

							//zero loop counter update
							if (pos3==pos2+x) zero_loop--; 

						}//pos3				   

					}//pos2


				}//for x - size of G-group			

			}//for j  - length of QGRS
			if ( this.cancelFlag.isRaised()) throw new CancelException();
		}//for i - position of QGRS


		//Close the output stream
		if(isForTesting)
		{
			out.close();
		}

		sequence.setgQuads(quads);

	}
	

}
