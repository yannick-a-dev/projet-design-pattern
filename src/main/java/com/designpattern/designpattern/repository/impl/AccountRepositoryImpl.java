package com.designpattern.designpattern.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.designpattern.designpattern.model.AccountStatus;
import com.designpattern.designpattern.model.AccountType;
import com.designpattern.designpattern.model.BankAccount;
import com.designpattern.designpattern.model.BankDirector;
import com.designpattern.designpattern.repository.AccountRepository;

public class AccountRepositoryImpl implements AccountRepository {

	private static /*final*/ AccountRepositoryImpl accountRepositoryImpl;
//	static {
//		System.out.println("Singleton instantiation");
//		accountRepositoryImpl = new AccountRepositoryImpl();
//	}
	
	private AccountRepositoryImpl() {
		
	}
	private Map<Long, BankAccount> bankAccountMap = new HashMap<>();
	
	private long accountsCount = 0;
	
	@Override
	public BankAccount save(BankAccount bankAccount) {
		Long accountId;
		synchronized (this) {
			accountId = ++accountsCount;
		}
		bankAccount.setAccountId(accountId);
		bankAccountMap.put(accountId, bankAccount);
		return bankAccount;
	}

	@Override
	public List<BankAccount> findAll() {
		return bankAccountMap.values().stream().toList();
	}

	@Override
	public Optional<BankAccount> findById(Long id) {
		BankAccount account = bankAccountMap.get(id);
		if(account == null) {
			return Optional.empty();
		}else {
			return Optional.of(account);
		}
	}

	@Override
	public List<BankAccount> searchAccounts(Predicate<BankAccount> predicate) {
		return bankAccountMap.values().stream().filter(predicate).collect(Collectors.toList());
	}

	@Override
	public BankAccount update(BankAccount account) {
		bankAccountMap.put(account.getAccountId(), account);
		return account;
	}

	@Override
	public void deleteById(Long id) {
		bankAccountMap.remove(id);
	}
	
	public void populateData() {
		for(int i = 0; i < 10; i++) {
		  BankAccount account = BankDirector.accountBuilder()
				  .balance(1000+Math.random()*90000)
				  .type(Math.random()>0.5?AccountType.SAVING_ACCOUNT: AccountType.CURRENT_ACCOUNT)
				  .status(Math.random()>0.5? AccountStatus.CREATED: AccountStatus.ACTIVATED)
				  .currency(Math.random()>0.5?"EURO": "USD")
				  .build(); 
		  save(account);
		}
		System.out.println("****************");
		System.out.println(Thread.currentThread().getName());
		System.out.println("Account Count = "+accountsCount);
		System.out.println("Size:"+bankAccountMap.values().size());
		System.out.println("****************");
	}
	
	public static AccountRepositoryImpl getInstance() {
	
		/*if(accountRepositoryImpl==null) {
			System.out.println("Singleton instantiation");
			accountRepositoryImpl = new AccountRepositoryImpl();
			accountRepositoryImpl.populateData();
		}*/
		return accountRepositoryImpl;
	}
}
