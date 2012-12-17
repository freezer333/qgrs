package qgrs.data;


import java.io.Serializable;

public enum InputType implements Serializable{
	rawSequence, //The user providing merely the bases of the sequences
	accessionORGI;//The user providing a accession or GI number to NCBI
}

