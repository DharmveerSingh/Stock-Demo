package ca.gl.StockExchange.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gl.StockExchange.constant.AppConstants;
import ca.gl.StockExchange.dao.UserRepository;
import ca.gl.StockExchange.dao.UserStockListRepository;
import ca.gl.StockExchange.dao.UserStockRepository;
import ca.gl.StockExchange.model.User;
import ca.gl.StockExchange.model.UserStock;
import ca.gl.StockExchange.model.UserStockList;
import ca.gl.StockExchange.responses.UserResponse;
import ca.gl.StockExchange.utility.Utils;
import reactor.core.publisher.Mono;

@Service
public class UserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepo;

	
	@Autowired
	private UserStockListRepository usStockListRepo;
	
	public Mono<UserResponse> signUp(User user) {
		return userRepo.existsById(AppConstants.USER_PREFIX+user.getEmail()).flatMap(isFound -> createIfNotExist(user, isFound));
	
	}

	private Mono<UserResponse> createIfNotExist(User user, boolean isFound) {
		final String username = AppConstants.USER_PREFIX + user.getEmail();
		Mono<UserResponse> userResult = null;
		UserResponse res = new UserResponse();
		if (isFound) {
			log.error("User already exist with ID: {} ", username);
			res.setErrorMsg(AppConstants.ERROR_USER_EXIST);
			
		} else {
			log.info("Saving user with ID: {} ", username);
			user.setUserId(username);
			
			 userResult=userRepo.save(user).map(u -> {
				log.info("User saved. removing password field from response");
				user.setPassword(AppConstants.BLANK_STRING);
				res.setUser(user);
				return res;
			}).doOnError(err -> res.setErrorMsg(err.getMessage()));
			;
		}
		
		if(res.getErrorMsg()!=null) {
			return Mono.just(res);
		}
		else
			return userResult;
	}

	public Mono<UserResponse> validateUser(User user) {
		return Mono.just(validate(user));
	}

	private UserResponse validate(User user) {
		UserResponse res= new UserResponse();
		if(Utils.emptyString(user.getEmail())) {
			res.setErrorMsg(AppConstants.BLANK_EMAIL_ERROR);
		}else if(Utils.emptyString(user.getPassword())) {
			res.setErrorMsg(AppConstants.BLANK_PASSWORD_ERROR);
		}
		return res;
	}

	public Mono<UserStockList> saveUserStock(UserStock us) {
		return usStockListRepo.findById(AppConstants.PURCHASE_PREFIX + us.getUserId()).flatMap(usl -> updateUSL(us, usl)).switchIfEmpty(createUserStockList(us));
	}

	private  Mono<UserStockList> updateUSL(UserStock us, UserStockList usl) {
		if(usl!=null) {
		log.info("Update User stock list with id: "+ usl.getUserStockId());
		usl.getUserStocks().add(us);
		usl.setCount(usl.getCount()+1);
		return usStockListRepo.save(usl);
		}
		
		return Mono.empty();
	}

	private Mono<? extends UserStockList> createUserStockList(UserStock us) {
		List<UserStock> list= new ArrayList<UserStock>();
		list.add(us);
		UserStockList usl= new UserStockList(AppConstants.PURCHASE_PREFIX+ us.getUserId(), list,list.size());
		return usStockListRepo.save(usl);
	}

	public Mono<UserStockList> getUserStock(String username) {
		return usStockListRepo.findById(AppConstants.PURCHASE_PREFIX +username);
	}

	public Mono<UserStockList> removeUserStock(String userStockId, String stockId) {
		return usStockListRepo.findById(userStockId)
			.flatMap(usStock -> removeStock(stockId, usStock));
		
	}

	private Mono<UserStockList> removeStock(String stockId, UserStockList usStock) {
		List<UserStock> stockList = usStock.getUserStocks().stream()
				.filter(stock -> !stock.getUserStockId().equals(stockId)).collect(Collectors.toList());
		usStock.setUserStocks(stockList);
		usStock.setCount(stockList.size());
		return usStockListRepo.save(usStock);
	}

	public Mono<User> getUser(String userId) {
		return userRepo.findById(userId);
	}

	public Mono<Boolean> disableUser(String userId) {
		return userRepo.findById(userId).flatMap(user -> {
			user.setActive(Boolean.FALSE);
			return userRepo.save(user).flatMap(u -> Mono.just(Boolean.TRUE)).switchIfEmpty(Mono.just(Boolean.FALSE));
		}).switchIfEmpty(Mono.just(Boolean.FALSE));
	}

}
