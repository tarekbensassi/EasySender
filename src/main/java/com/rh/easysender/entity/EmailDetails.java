package com.rh.easysender.entity;


import java.io.File;

//Importing required classes
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

//Class
public class EmailDetails {

	// Class data members
	
	private String name;
	private String email;
	private String message;
	private String subject;

}

