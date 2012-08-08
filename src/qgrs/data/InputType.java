package qgrs.data;
/*
 * The type of user input for our program
 */

import java.io.Serializable;

public enum InputType implements Serializable{
	rawSequence, //The user providing merely the bases of the sequences
	accessionORGI;//The user providing a accession or GI number to NCBI
}

