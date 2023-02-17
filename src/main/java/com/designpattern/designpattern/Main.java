package com.designpattern.designpattern;

import java.io.IOException;
import java.util.List;

import com.designpattern.designpattern.model.AccountStatus;
import com.designpattern.designpattern.model.BankAccount;
import com.designpattern.designpattern.repository.impl.AccountRepositoryImpl;
import com.designpattern.designpattern.util.JsonSerializer;

public class Main {

	public static void main(String[] args) throws IOException {

		JsonSerializer<BankAccount> bankAccountJsonSerializer = new JsonSerializer<>();
		AccountRepositoryImpl accountRepositoryImpl = AccountRepositoryImpl.getInstance();
		for(int i=0; i < 10; i++) {
			new Thread(()->{
				accountRepositoryImpl.populateData();
			}).start();
		}
		System.out.println("Tape a character:");
		System.in.read();
		
		List<BankAccount> bankAccounts = accountRepositoryImpl.findAll();
		bankAccounts.stream()
		   .map(bankAccountJsonSerializer::toJson)
		   .forEach(System.out::println);
		
		/*System.out.println("=============");
	   BankAccount account = accountRepositoryImpl.findById(1L).orElse(null);
	   if(account!=null) {
		   System.out.println(bankAccountJsonSerializer.toJson(account));
	   }*/
	}
}
