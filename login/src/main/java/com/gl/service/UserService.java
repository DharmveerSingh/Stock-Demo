package com.gl.service;

import static com.gl.configuration.MessageListener.CACHE;
import static com.gl.configuration.MessageListener.STOCKNAMES;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.gl.constants.AppConstants;
import com.gl.model.User;
import com.gl.model.UserStockList;
import com.gl.model.UserStockResponse;
import com.gl.repository.UserRepository;

import ca.gl.fileUploader.model.Stock;

// TODO: Auto-generated Javadoc
/**
 * The Class UserService.
 *
 * @author dharamveer.singh
 */
@Service("userService")
public class UserService {

	/** The log. */
	private Logger log = LoggerFactory.getLogger(UserService.class);

	/** The rest template. */
	@Autowired
	private RestTemplate restTemplate;

	/** The se base url. */
	@Value("${stock.exchange.baseURL}")
	private String seBaseUrl;

	/** The se get user. */
	@Value("${stock.exchange.user.getUser}")
	private String seGetUser;

	/** The se disable user. */
	@Value("${stock.exchange.user.disable}")
	private String seDisableUser;
	
	/** The se user purchase. */
	@Value("${stock.exchange.userStocks.purchase}")
	private String seUserPurchase;

	/** The se latest stocks. */
	@Value("${stock.exchange.latestStocks}")
	private String seLatestStocks;

	/** The b crypt password encoder. */
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/** The repo. */
	@Autowired
	private UserRepository repo;

	/**
	 * Instantiates a new user service.
	 *
	 * @param bCryptPasswordEncoder the b crypt password encoder
	 */
	@Autowired
	public UserService(BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	/**
	 * get user by email.
	 *
	 * @param email the email
	 * @return the optional
	 */
	public Optional<User> findUserByEmail(String email) {
		return repo.findById(email);
	}

	/**
	 * Save user.
	 *
	 * @param user the user
	 * @return the user
	 */
	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(Boolean.TRUE);
		user.setRoles(Arrays.asList("ADMIN"));
		return repo.save(user);
	}

	/**
	 * get data by pagination.
	 *
	 * @param request the request
	 * @param principal the principal
	 * @param session the session
	 * @return the model and view
	 */
	public ModelAndView getModelAndView(HttpServletRequest request, Principal principal, HttpSession session) {
		String offsetString = request.getParameter(AppConstants.PARAM_OFFSET);
		String pageSizeString = request.getParameter(AppConstants.PARAM_PAGE_SIZE);
		int offset, pagesize;

		if (offsetString != null && pageSizeString != null) {
			pagesize = Integer.parseInt(pageSizeString);
			offset = Integer.parseInt(offsetString) * pagesize;
			pagesize = offset + pagesize;
		} else {
			offset = 0;// Default offset
			pagesize = 20;// default page size
		}

		return getModelAndView(getLatestStocksFromChache(offset, pagesize), getUserStocks(principal), session);
	}

	/**
	 * get home page for user by email.
	 *
	 * @param latestStocks the latest stocks
	 * @param userStockList the user stock list
	 * @param session the session
	 * @return the model and view
	 */
	private ModelAndView getModelAndView(List<Stock> latestStocks, List<UserStockResponse> userStockList,
			HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> oUser = findUserByEmail(auth.getName());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(AppConstants.USER_STOCK_LIST, userStockList);
		if (!oUser.isPresent()) {
			session.invalidate();
			modelAndView.setViewName("redirect:login");
			return modelAndView;
		}
		User user = oUser.get();
		StringBuilder userMesBuilder = new StringBuilder();

		modelAndView.addObject(AppConstants.WELCOME,
				userMesBuilder.append(AppConstants.Welcome_MSG).append(user.getName()).append(AppConstants.SPACE)
						.append(user.getLastName()).append(AppConstants.OPENING_BRACES).append(user.getEmail())
						.append(AppConstants.CLOSING_BRACES).toString());
		modelAndView.addObject(AppConstants.USERNAME, user.getEmail());

		modelAndView.addObject(AppConstants.STOCK_LIST, latestStocks);
		modelAndView.setViewName(AppConstants.USER_HOME_VIEW);
		return modelAndView;
	}

	/**
	 * get all stocks purchased by user.
	 *
	 * @param principal the principal
	 * @return the user stocks
	 */
	private List<UserStockResponse> getUserStocks(Principal principal) {
		String url = seBaseUrl + seUserPurchase + "?user=" + principal.getName();

		ResponseEntity<UserStockList> getUserStock = restTemplate.getForEntity(url, UserStockList.class);
		List<UserStockResponse> response = new ArrayList<>();

		if (getUserStock != null && getUserStock.getBody() != null) {
			getUserStock.getBody().getUserStocks().forEach(userStk -> {
				UserStockResponse usr = new UserStockResponse();
				Stock ss = CACHE.get(userStk.getStockID());
				userStk.setCurrentPrice(ss.getPrice());
				usr.setStock(ss, userStk);
				response.add(usr);
			});

		}
		return response;
	}

	/**
	 * Get latest stocks from cache in paginated way.
	 *
	 * @param offset the offset
	 * @param pagesize the pagesize
	 * @return the latest stocks from chache
	 */
	private List<Stock> getLatestStocksFromChache(int offset, int pagesize) {

		if (CACHE.size() == 0) {
			updateChache();
		}
		final List<Stock> list = new ArrayList<>();
		List<String> stockNameList = null;
		if (offset < STOCKNAMES.size() && pagesize < STOCKNAMES.size()) {
			stockNameList = STOCKNAMES.subList(offset, pagesize);
			stockNameList.stream().forEach(key -> list.add(CACHE.get(key)));
		} else {
			stockNameList = STOCKNAMES.subList(0, STOCKNAMES.size() < 20 ? STOCKNAMES.size() : 20);
			stockNameList.stream().forEach(key -> list.add(CACHE.get(key)));
		}
		return list;
	}

	/**
	 * update cache from database.
	 */
	public void updateChache() {
		String url = seBaseUrl + seLatestStocks;
		Stock[] stockList = restTemplate.getForObject(url, Stock[].class);
		if (stockList.length > 0) {
			Arrays.stream(stockList).forEach(st -> {
				STOCKNAMES.add(st.getStockID());
				CACHE.put(st.getStockID(), st);
			});
		}
	}

	/**
	 * Add records to cache after microservices starts.
	 */
	@PostConstruct
	public void addRecordsToCache() {
		log.info("Going to update cache: " + STOCKNAMES.size());
		updateChache();
		log.info("Updated cache to: " + STOCKNAMES.size());
	}

	/**
	 * get user.
	 *
	 * @param userId the user id
	 * @return the user
	 */
	public User getUser(String userId) {
		String url = seBaseUrl + seGetUser + userId;
		User user = restTemplate.getForObject(url, User.class);
		log.info("Get user for {} result: {}" + userId, user);
		return user;

	}

	/**
	 * disable user.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	public Boolean disable(String userId) {
		String url = seBaseUrl + seDisableUser + userId;
		return restTemplate.getForObject(url, Boolean.class);

	}
}
