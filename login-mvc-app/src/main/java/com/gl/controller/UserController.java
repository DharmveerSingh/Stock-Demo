package com.gl.controller;

import java.security.Principal;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.gl.constants.AppConstants;
import com.gl.model.UserStock;
import com.gl.model.UserStockList;
import com.gl.service.UserService;

import reactor.core.publisher.Mono;

/**
 * The User Contoller.
 *
 * @author dharamveer.singh
 */
@Controller
@RequestMapping("/user")
public class UserController {

	/** The rest template. */
	@Autowired
	private RestTemplate restTemplate;

	/** The user service. */
	@Autowired
	private UserService userService;

	/** The se base url. */
	@Value("${stock.exchange.baseURL}")
	private String seBaseUrl;

	/** The se latest stocks. */
	@Value("${stock.exchange.latestStocks}")
	private String seLatestStocks;

	/** The se user purchase. */
	@Value("${stock.exchange.userStocks.purchase}")
	private String seUserPurchase;

	/** The se remove purchase. */
	@Value("${stock.exchange.userStocks.purchase.remove}")
	private String seRemovePurchase;

	/** The log. */
	private Logger log = LoggerFactory.getLogger(UserController.class);

	/**
	 * Home.
	 *
	 * @param request the request
	 * @param principal the principal
	 * @param session the session
	 * @return the model and view
	 */
	@GetMapping(value = "/home")
	public ModelAndView home(HttpServletRequest request, Principal principal,HttpSession session) {
		return userService.getModelAndView(request, principal,session);
	}


	/**
	 * Purchase stocks.
	 *
	 * @param stock the stock
	 * @param request the request
	 * @param principal the principal
	 * @return the mono
	 */
	@PostMapping("/purchase")
	@ResponseBody
	public Mono<String> purchaseStocks(@RequestBody UserStock stock, HttpServletRequest request, Principal principal) {
		String url = seBaseUrl + seUserPurchase;
		stock.setUserId(principal.getName());
		stock.setUserStockId(stock.getStockID() + Calendar.getInstance().getTimeInMillis());
		ResponseEntity<UserStockList> post = restTemplate.postForEntity(url, stock, UserStockList.class);
		System.out.println(post.getBody());
		return Mono.just("done");
	}

	/**
	 * Removes the purchase.
	 *
	 * @param stockId the stock id
	 * @param username the username
	 * @return the mono
	 */
	@GetMapping("/removePurchase")
	@ResponseBody
	public Mono<String> removePurchase(@RequestParam("userStockId") String stockId,
			@RequestParam("userName") String username) {
		System.out.println("Got your request.. keep kalm: " + stockId + ", username: " + username);
		StringBuilder urlBuilder = new StringBuilder(seBaseUrl);
		urlBuilder.append(seRemovePurchase).append("?userStockId=").append(AppConstants.PURCHASE_PREFIX)
				.append(AppConstants.USER_PREFIX).append(username).append("&stockId=").append(stockId);
		log.info("Removing purchase: " + urlBuilder.toString());

		ResponseEntity<String> get = restTemplate.getForEntity(urlBuilder.toString(), String.class);
		log.info("Result of remove purchase: " + get.getBody());
		return Mono.just("done");
	}
	
}