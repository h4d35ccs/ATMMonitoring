package com.ncr.ATMMonitoring.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ncr.ATMMonitoring.pojo.FinancialDevice;
import com.ncr.ATMMonitoring.pojo.Hotfix;
import com.ncr.ATMMonitoring.pojo.InternetExplorer;
import com.ncr.ATMMonitoring.pojo.JxfsComponent;
import com.ncr.ATMMonitoring.pojo.OperatingSystem;
import com.ncr.ATMMonitoring.pojo.Query;
import com.ncr.ATMMonitoring.pojo.Software;
import com.ncr.ATMMonitoring.pojo.Terminal;
import com.ncr.ATMMonitoring.pojo.XfsComponent;
import com.ncr.ATMMonitoring.pojo.annotation.ComboQueryOption;
import com.ncr.ATMMonitoring.service.QueryService;

/**
 * The Class QueryController.
 * 
 * Controller for handling query related HTTP petitions.
 * 
 * @author Jorge L√≥pez Fern√°ndez (lopez.fernandez.jorge@gmail.com)
 */
@Controller
public class QueryController extends GenericController {

	/** The logger. */
	static private Logger logger = Logger.getLogger(QueryController.class
			.getName());

	/** The default field for sorting terminals in csv downloads. */
	public static final String DEFAULT_SORT = "serialNumber";

	/** The default sorting order for terminals in csv downloads. */
	public static final String DEFAULT_ORDER = "asc";

	private static final String COMBO_TYPE_TERMINAL = "terminal";
	private static final String COMBO_TYPE_FINANCIALDEVICE = "financialDevice";
	private static final String COMBO_TYPE_XFSCOMPONENT = "xfsComponent";
	private static final String COMBO_TYPE_JXFSCOMPONENT = "jxfsComponent";
	private static final String COMBO_TYPE_HOTFIX = "hotfix";
	private static final String COMBO_TYPE_IEXPLORER = "internetExplorer";
	private static final String COMBO_TYPE_OS = "operatingSystem";
	private static final String COMBO_TYPE_SOFTWARE = "software";
	private static final String COMBO_TYPE_XFSSW = "xfsSw";
	private static final String COMBO_TYPE_FEATSW = "featSw";
	private static final String COMBO_TYPE_HWDEVICE = "hardwareDevice";

	/** The query page size. */
	@Value("${config.queriesPageSize}")
	private int pageSize;

	/** The query service. */
	@Autowired
	private QueryService queryService;

	/**
	 * Create query URL.
	 * 
	 * @param map
	 *            the map
	 * @param request
	 *            the request
	 * @param principal
	 *            the principal
	 * @return the petition response
	 */
	@RequestMapping(value = "/queries/create", method = RequestMethod.GET)
	public String createQuery(Map<String, Object> map,
			HttpServletRequest request, Principal principal) {
		String userMsg = "";
		Locale locale = RequestContextUtils.getLocale(request);

		if (principal != null) {
			userMsg = this.getUserGreeting(principal, request);
		}

		String datePattern = ((SimpleDateFormat) DateFormat.getDateInstance(
				DateFormat.SHORT, locale)).toLocalizedPattern();

		if (!datePattern.contains("dd")) {
			datePattern = datePattern.replace("d", "dd");
		}

		map.put("userMsg", userMsg);
		map.put("query", new Query());
		map.put("values", Query.getComboboxes());

		return "newQuery";

	}

	/**
	 * Show user query URL.
	 * 
	 * @param queryId
	 *            the query id
	 * @param map
	 *            the map
	 * @param request
	 *            the request
	 * @param principal
	 *            the principal
	 * @return the petition response
	 */
	@RequestMapping(value = "/queries/show", method = RequestMethod.GET)
	public String showUserQuery(Integer queryId, Map<String, Object> map,
			HttpServletRequest request, Principal principal) {

		String userMsg = "";
		Query query = null;

		if (queryId != null) {
			query = queryService.getQuery(queryId);
		}

		// if (principal != null) {
		// userMsg = this.getUserGreeting(principal, request);
		// }
		Gson gson = new GsonBuilder().create();
		map.put("userMsg", userMsg);
		map.put("query", query);
		map.put("queryJson", (gson.toJson(this.getQueryCombosActualValues(query))));
		// map.put("values", Query.getComboboxes());
		return "queries";

	}

	/**
	 * Delete user query URL.
	 * 
	 * @param queryId
	 *            the query id
	 * @param map
	 *            the map
	 * @param request
	 *            the request
	 * @param principal
	 *            the principal
	 * @return the petition response
	 */
	@RequestMapping(value = "/queries/delete")
	public String deleteUserQuery(Integer queryId, Map<String, Object> map,
			HttpServletRequest request, Principal principal,
			final RedirectAttributes redirectAttributes) {

		Query query = null;
		if (queryId != null) {
			query = queryService.getQuery(queryId);
			if (query != null) {
				try {
					queryService.deleteQuery(query);
					redirectAttributes.addFlashAttribute("success",
							"success.deletingNewQuery");

				} catch (Throwable e) {
					redirectAttributes.addFlashAttribute("error",
							"error.deletingQuery");
				}
			}
		}
		return "redirect:/queries/list";
		// if (principal != null) {
		// userMsg = this.getUserGreeting(principal, request);
		// }
		// PagedListHolder<Query> pagedListHolder = new PagedListHolder<Query>(
		// new ArrayList<Query>(userQueries));
		// int page = 0;
		//
		// pagedListHolder.setPage(page);
		// pagedListHolder.setPageSize(pageSize);
		// map.put("pagedListHolder", pagedListHolder);
		//
		// map.put("userMsg", userMsg);
		//
		// return "queryList";

	}

	/**
	 * Select user query URL.
	 * 
	 * @param query
	 *            the query
	 * @param map
	 *            the map
	 * @param request
	 *            the request
	 * @param principal
	 *            the principal
	 * @return the petition response
	 */
	@RequestMapping(value = "/queries/create", method = RequestMethod.POST)
	public String selectUserQuery(@ModelAttribute("query") Query query,
			Map<String, Object> map, HttpServletRequest request,
			Principal principal) {
		Set<Query> userQueries = null;
		String userMsg = "";

		Locale locale = RequestContextUtils.getLocale(request);

		if (query.getId() != null) {
			query = queryService.getQuery(query.getId());
			if (WebUtils.hasSubmitParameter(request, "delete")) {
				queryService.deleteQuery(query);
				query = new Query();
			}
		} else {
			query = new Query();
		}

		if (principal != null) {
			userMsg = this.getUserGreeting(principal, request);
		}

		String datePattern = ((SimpleDateFormat) DateFormat.getDateInstance(
				DateFormat.SHORT, locale)).toLocalizedPattern();

		if (!datePattern.contains("dd")) {
			datePattern = datePattern.replace("d", "dd");
		}

		map.put("userMsg", userMsg);
		map.put("query", query);
		map.put("userQueries", userQueries);
		map.put("datePattern", datePattern);
		map.put("values", Query.getComboboxes());

		return "queries";

	}

	/**
	 * Redirect to queries URL.
	 * 
	 * @return the petition response
	 */
	@RequestMapping("/queries")
	public String redirectToQueries() {

		return "redirect:/queries/list";
	}

	/**
	 * List queries URL.
	 * 
	 * @param map
	 *            the map
	 * @param request
	 *            the request
	 * @param principal
	 *            the principal
	 * @param p
	 *            the page number
	 * @return the petition response
	 */
	@RequestMapping("/queries/list")
	public String listQueries(Map<String, Object> map,
			HttpServletRequest request, Principal principal, String p) {
		Set<Query> userQueries = null;
		String userMsg = "";

		if (principal != null) {
			userMsg = this.getUserGreeting(principal, request);
			userQueries = this.queryService.getQueriesByUser(principal
					.getName());
		}

		PagedListHolder<Query> pagedListHolder = new PagedListHolder<Query>(
				new ArrayList<Query>(userQueries));
		int page = 0;
		if (p != null) {
			try {
				page = Integer.parseInt(p);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		pagedListHolder.setPage(page);
		pagedListHolder.setPageSize(pageSize);
		map.put("pagedListHolder", pagedListHolder);
		map.put("userMsg", userMsg);
		return "queryList";
	}

	/**
	 * Save or update query URL.
	 * 
	 * @param query
	 *            the query
	 * @param map
	 *            the map
	 * @param request
	 *            the request
	 * @param principal
	 *            the principal
	 * @param redirectAttributes
	 *            the redirect attributes
	 * @param p
	 *            the page number
	 * @param sort
	 *            the sort
	 * @param order
	 *            the order
	 * @return the petition response
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "/queries/results", method = RequestMethod.POST)
	public String saveOrUpdateQuery(@ModelAttribute("query") Query query,
			@RequestParam("action") String action, Map<String, Object> map,
			HttpServletRequest request, Principal principal,
			RedirectAttributes redirectAttributes, String p, String sort,
			String order) throws Exception {

		Locale locale = RequestContextUtils.getLocale(request);
		if (StringUtils.isNotEmpty(action) && action.equals("save")) {
			if (principal != null) {
				if (query.getId() != null) {
					try {
						logger.debug("Updating query - " + query.getName());
						this.queryService.updateQuery(query,
								principal.getName());
						redirectAttributes.addFlashAttribute("success",
								"success.updatingQuery");
					} catch (Exception e) {
						redirectAttributes.addFlashAttribute("error",
								"error.updatingQuery");
					}
				} else {
					query.setCreationDate(new Date());
					query.setTrueLocale(locale);
					try {
						logger.debug("Guardando nueva query- "
								+ query.getName());
						queryService.addQuery(query, principal.getName());
						redirectAttributes.addFlashAttribute("success",
								"success.savingNewQuery");
					} catch (Exception e) {
						redirectAttributes.addFlashAttribute("error",
								"error.savingNewQuery");
					}
				}
			}
			return "redirect:/queries/list";
		} else if (StringUtils.isNotEmpty(action) && action.equals("execute")) {
			logger.debug("Executing query... " + query.getName());
			String sortValue = (sort == null) ? DEFAULT_SORT : sort;
			String orderValue = (order == null) ? DEFAULT_ORDER : order;
			List<Terminal> terminals = queryService.executeQuery(query, locale,
					sortValue, orderValue);

			logger.debug("terminals " + terminals);

			if (terminals == null) {
				throw new Exception("Query execution returned a NULL list.");
			}

			PagedListHolder<Terminal> pagedListHolder = new PagedListHolder<Terminal>(
					terminals);
			int page = 0;
			if (p != null) {
				try {
					page = Integer.parseInt(p);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			pagedListHolder.setPage(page);
			pagedListHolder.setPageSize(pageSize);

			logger.debug("pageListHolder " + pagedListHolder);

			map.put("userMsg", this.getUserGreeting(principal, request));
			map.put("pagedListHolder", pagedListHolder);
			map.put("query", query);
			map.put("values", Query.getComboboxes());
			map.put("p", page);
			map.put("sort", sortValue);
			map.put("order", orderValue);

		} else if (StringUtils.isNotEmpty(action) && action.equals("delete")) {
			logger.debug("Deleting query -" + query.getName());
			return "redirect:/queries/delete?queryId=" + query.getId();
		}
		return "queryResults";

	}

	/**
	 * Wrong download query results as csv URL.
	 * 
	 * @return the petition response
	 */
	@RequestMapping(value = "/queries/results", method = RequestMethod.GET)
	public String redirectFromWrongResults() {

		return "redirect:/queries/create";
	}

	/**
	 * Wrong download query results as csv URL.
	 * 
	 * @return the petition response
	 */
	@RequestMapping(value = "/queries/results/export", method = RequestMethod.GET)
	public String redirectFromWrongResultsExport() {
		return "redirect:/queries/create";
	}

	/**
	 * Download query results as csv URL.
	 * 
	 * @param query
	 *            the query
	 * @param response
	 *            the response
	 * @param request
	 *            the request
	 * @param sort
	 *            the fields for sorting the results
	 * @param order
	 *            the sorting order
	 */
	@RequestMapping(value = "/queries/results/export", method = RequestMethod.POST)
	public void downloadResultsCsv(@ModelAttribute("query") Query query,
			HttpServletResponse response, HttpServletRequest request,
			String sort, String order) {
		try {
			String sortValue = (sort == null) ? DEFAULT_SORT : sort;
			String orderValue = (order == null) ? DEFAULT_ORDER : order;
			response.setContentType("text/csv;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=\"terminals.csv\"");
			OutputStream resOs = response.getOutputStream();
			OutputStream buffOs = new BufferedOutputStream(resOs);
			OutputStreamWriter outputwriter = new OutputStreamWriter(buffOs);
			outputwriter.write(Terminal.getCsvHeader());
			List<Terminal> terminals = queryService.executeQuery(query,
					RequestContextUtils.getLocale(request), sortValue,
					orderValue);
			for (Terminal terminal : terminals) {
				outputwriter.write("\n" + terminal.getCsvDescription());
			}
			outputwriter.flush();
			outputwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the content for the query combo
	 * 
	 * @param comboType
	 * @return
	 */
	@RequestMapping(value = "/queries/combos/{comboType}/{locale}", method = RequestMethod.GET)
	@ResponseBody
	public String getComboForQueries(
			@PathVariable("comboType") String comboType,
			@PathVariable("locale") String localeParam) {

		Map<String, String> options = null;
		String messageKeyBase = "label." + comboType;
		Locale locale = getLocale(localeParam);

		switch (comboType) {
		case COMBO_TYPE_TERMINAL:
			options = generateFieldComboOptions(Terminal.class, messageKeyBase,
					locale);
			break;
		case COMBO_TYPE_FINANCIALDEVICE:
			options = generateFieldComboOptions(FinancialDevice.class,
					messageKeyBase, locale);
			break;
		case COMBO_TYPE_XFSCOMPONENT:
			options = generateFieldComboOptions(XfsComponent.class,
					messageKeyBase, locale);
			break;
		case COMBO_TYPE_JXFSCOMPONENT:
			options = generateFieldComboOptions(JxfsComponent.class,
					messageKeyBase, locale);
			break;
		case COMBO_TYPE_HOTFIX:
			options = generateFieldComboOptions(Hotfix.class, messageKeyBase,
					locale);
			break;
		case COMBO_TYPE_IEXPLORER:
			options = generateFieldComboOptions(InternetExplorer.class,
					messageKeyBase, locale);
			break;
		case COMBO_TYPE_OS:
			options = generateFieldComboOptions(OperatingSystem.class,
					messageKeyBase, locale);
			break;
		case COMBO_TYPE_SOFTWARE:
			options = generateFieldComboOptions(Software.class, messageKeyBase,
					locale);
			break;
		case COMBO_TYPE_XFSSW:
			options = generateFieldComboOptions(Software.class, messageKeyBase,
					locale);
			break;
		case COMBO_TYPE_FEATSW:
			options = generateFieldComboOptions(Software.class, messageKeyBase,
					locale);
			break;
		case COMBO_TYPE_HWDEVICE:
			options = generateHardwareDeviceComboOptions(locale, messageKeyBase);
			break;

		default:
			logger.warn("unrecongnized option to generate query combobox: "
					+ comboType);
		}
		return generateComboboxOptionsJSON(this.sortHashMapByValues(options));
	}

	@RequestMapping(value = "/queries/combos/comparison/{comboType}/{fieldname}/{locale}", method = RequestMethod.GET)
	@ResponseBody
	public String getComboOptionsComparisionOperation(
			@PathVariable("comboType") String comboType,
			@PathVariable("fieldname") String fieldname,
			@PathVariable("locale") String localeParam) {

		Map<String, String> options = null;
		Locale locale = getLocale(localeParam);

		switch (comboType) {
		case COMBO_TYPE_TERMINAL:
			options = generateComboOptionsByDataType(Terminal.class, fieldname,
					locale);
			break;
		case COMBO_TYPE_FINANCIALDEVICE:
			options = generateComboOptionsByDataType(FinancialDevice.class,
					fieldname, locale);
			break;
		case COMBO_TYPE_XFSCOMPONENT:
			options = generateComboOptionsByDataType(XfsComponent.class,
					fieldname, locale);
			break;
		case COMBO_TYPE_JXFSCOMPONENT:
			options = generateComboOptionsByDataType(JxfsComponent.class,
					fieldname, locale);
			break;
		case COMBO_TYPE_HOTFIX:
			options = generateComboOptionsByDataType(Hotfix.class, fieldname,
					locale);
			break;
		case COMBO_TYPE_IEXPLORER:
			options = generateComboOptionsByDataType(InternetExplorer.class,
					fieldname, locale);
			break;
		case COMBO_TYPE_OS:
			options = generateComboOptionsByDataType(OperatingSystem.class,
					fieldname, locale);
			break;
		case COMBO_TYPE_SOFTWARE:
			options = generateComboOptionsByDataType(Software.class, fieldname,
					locale);
			break;
		case COMBO_TYPE_XFSSW:
			options = generateComboOptionsByDataType(Software.class, fieldname,
					locale);
			break;
		case COMBO_TYPE_FEATSW:
			options = generateComboOptionsByDataType(Software.class, fieldname,
					locale);
			break;
		case COMBO_TYPE_HWDEVICE:
			// options = generateHardwareDeviceComboOptions(locale,
			// messageKeyBase);
			break;

		default:
			logger.warn("unrecongnized option to generate query combobox: "
					+ comboType);
		}

		return this.generateComboboxOptionsJSON(this
				.sortHashMapByValues(options));
	}

	/**
	 * Gets the default locale
	 * 
	 * @param localeParam
	 *            String with a valid locale such as en, es,fr, etc
	 * @return
	 */
	private Locale getLocale(String localeParam) {
		Locale locale = null;
		if (localeParam == null) {

			locale = Locale.getDefault();
		} else {
			locale = new Locale(localeParam);
		}
		return locale;
	}

	/**
	 * Returns the map with the options to use in a query combobox
	 * 
	 * @param entity
	 * @param messageKeyBase
	 * @param options
	 * @param rb
	 */
	private Map<String, String> generateFieldComboOptions(Class<?> entity,
			String messageKeyBase, Locale locale) {

		Map<String, String> options = new HashMap<>();
		ResourceBundle rb = this.getResourceBundle(locale);

		for (String option : this.entityFieldNamesToComboBox(entity)) {
			try {
				options.put(option, rb.getString(messageKeyBase + "." + option));
			} catch (MissingResourceException e) {
				logger.debug("the field name: "
						+ option
						+ " does not have an property key to show in the combo, is an valid combo option?");
			}
		}
		return options;
	}

	/**
	 * generates the map of options to use in a query data types combobox
	 * 
	 * @param entity
	 * @param fieldname
	 * @param locale
	 * @return
	 */
	private Map<String, String> generateComboOptionsByDataType(Class<?> entity,
			String fieldname, Locale locale) {
		Map<String, String> options = new HashMap<>();
		ResourceBundle rb = this.getResourceBundle(locale);
		final String messageKeyBase = "label.query.operation";
		for (String option : this.entityFieldDataTypeKeysToComboBox(entity,
				fieldname)) {
			try {
				options.put(option, rb.getString(messageKeyBase + "." + option));
			} catch (MissingResourceException e) {
				logger.debug("the fieldtype name: "
						+ option
						+ " does not have an property key to show in the combo, is an valid combo option?");
			}
		}
		return options;
	}

	/**
	 * Gets all the field names from the class that have present the annotation
	 * {@link ComboQueryOption} and put them into a list
	 * 
	 * @param entityClass
	 * @return List<String>
	 */
	private List<String> entityFieldNamesToComboBox(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		final List<String> comboOptions = new ArrayList<String>();

		for (Field field : fields) {

			if (field.isAnnotationPresent(ComboQueryOption.class)) {
				comboOptions.add(field.getName());
			}
		}
		return comboOptions;
	}

	/**
	 * Gets the options keys for a combobox asociated to datatypes
	 * 
	 * @param entityClass
	 * @param fieldname
	 * @return
	 */
	private List<String> entityFieldDataTypeKeysToComboBox(
			Class<?> entityClass, String fieldname) {

		List<String> dataTypeOptionsKeys = new ArrayList<String>();
		try {

			Field field = entityClass.getDeclaredField(fieldname);
			ComboQueryOption queryOptionAnnoation = field
					.getAnnotation(ComboQueryOption.class);

			if (queryOptionAnnoation.versionComparison()) {

				dataTypeOptionsKeys = QueryController
						.getVersionOperationsComboBoxOptions();

			} else {

				Class<?> type = field.getType();

				if (type.equals(String.class)) {
					dataTypeOptionsKeys = QueryController
							.getStringOperationsComboBoxOptions();
				} else if (type.equals(Boolean.class)
						|| type.equals(boolean.class)) {

				} else if ((type.equals(Integer.class) || type
						.equals(int.class))
						|| (type.equals(Float.class) || type
								.equals(float.class))
						|| (type.equals(Double.class) || type
								.equals(double.class))) {

					dataTypeOptionsKeys = QueryController
							.getNumericalOperationsComboBoxOptions();

				} else if (type.equals(Date.class)
						|| type.equals(Timestamp.class)) {

					dataTypeOptionsKeys = QueryController
							.getDateOperationsComboBoxOptions();
				}
			}

		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataTypeOptionsKeys;
	}

	/**
	 * 
	 * @param locale
	 * @return
	 */
	private ResourceBundle getResourceBundle(Locale locale) {
		return ResourceBundle.getBundle("messages", locale);
	}

	/**
	 * 
	 * @param locale
	 * @param messageKeyBase
	 * @return
	 */
	private Map<String, String> generateHardwareDeviceComboOptions(
			Locale locale, String messageKeyBase) {

		Map<String, String> options = new HashMap<String, String>();
		ResourceBundle rb = this.getResourceBundle(locale);

		options.put(
				ComboQueryOption.GROUP_HARDWARE_COMPUTER_SYSTEM,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_COMPUTER_SYSTEM));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_PROCESSOR,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_PROCESSOR));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_PHYSICAL_MEMORY,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_PHYSICAL_MEMORY));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_DISK_DRIVE,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_DISK_DRIVE));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_LOGICAL_DISK,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_LOGICAL_DISK));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_BASE_BOARD,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_BASE_BOARD));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_NETWORK_ADAPTER,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_NETWORK_ADAPTER));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_FLOPPY_DRIVE,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_FLOPPY_DRIVE));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_CDROM_DRIVE,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_CDROM_DRIVE));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_SOUND_DEVICE,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_SOUND_DEVICE));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_DISPLAY_CONFIGURATION,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_DISPLAY_CONFIGURATION));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_USB_CONTROLLER,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_USB_CONTROLLER));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_USB_HUB,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_USB_HUB));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_SERIAL_PORT,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_SERIAL_PORT));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_PARALLEL_PORT,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_PARALLEL_PORT));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_1394_CONTROLLER,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_1394_CONTROLLER));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_SCSI_CONTROLLER,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_SCSI_CONTROLLER));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_DESKTOP_MONITOR,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_DESKTOP_MONITOR));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_KEYBOARD,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_KEYBOARD));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_POINTING_DEVICE,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_POINTING_DEVICE));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_SYSTEM_SLOT,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_SYSTEM_SLOT));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_BIOS,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_BIOS));
		options.put(
				ComboQueryOption.GROUP_HARDWARE_VIDEO_CONTROLLER,
				rb.getString(messageKeyBase + "."
						+ ComboQueryOption.GROUP_HARDWARE_VIDEO_CONTROLLER));
		return options;
	}

	/**
	 * Gets the query operations related to String types
	 * 
	 * @return
	 */
	private static List<String> getStringOperationsComboBoxOptions() {
		List<String> option = new ArrayList<String>();
		option.add("greater_str");// Alphabetically After//
		option.add("geq_str");// Alphabetically After or Equals//
		option.add("less_str");// Alphabetically Before//
		option.add("leq_str");// Alphabetically Before or Equals//
		option.add("contains");// Contains//
		option.add("contains_case");// Contains (case sensitive)//
		option.add("ends_with");// Ends With//
		option.add("ends_with_case");// Ends With (case sensitive)//
		option.add("eq_str");// Equals//
		option.add("eq_str_case");// Equals (case sensitive)//
		option.add("is_null");// Is Null//
		option.add("starts_with");// Starts With//
		option.add("starts_with_case");// Starts With (case sensitive)//
		return option;

	}

	/**
	 * Gets the query operations related to date types
	 * 
	 * @return List<String>
	 */
	private static List<String> getDateOperationsComboBoxOptions() {
		List<String> option = new ArrayList<String>();
		option.add("date_greater");// After Date;//
		option.add("date_geq");// After or Exact Date;//
		option.add("date_less");// Before Date;//
		option.add("date_leq");// Before or Exact Date;//
		option.add("date_eq");// Exact Date;//
		option.add("is_null");// Is Null;//
		return option;
	}

	/**
	 * Gets the query operations related to Software versions related types
	 * 
	 * @return List<String>
	 */
	private static List<String> getVersionOperationsComboBoxOptions() {
		List<String> option = new ArrayList<String>();
		option.add("v_greater");// After Version;//
		option.add("v_geq");// After or Exact Version;//
		option.add("v_less");// Before Version;//
		option.add("v_leq");// Before or Exact Version;//
		option.add("v_eq");// Exact Version;//
		option.add("is_null");// Is Null;//
		option.add("v_under");// Under Version;//
		return option;
	}

	/**
	 * Gets the query operations related to numerical types
	 * 
	 * @return List<String>
	 */
	private static List<String> getNumericalOperationsComboBoxOptions() {
		List<String> option = new ArrayList<String>();
		option.add("less");// <;//
		option.add("eq");// =;//
		option.add("greater");// );//;//
		option.add("is_null");// Is Null;//
		option.add("leq");// ≤;//
		option.add("geq");// ≥;//
		return option;
	}

	/**
	 * Sorts the contents of a map based on the values
	 * 
	 * @param passedMap
	 * @return
	 */
	private LinkedHashMap<String, String> sortHashMapByValues(
			Map<String, String> passedMap) {
		List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
		List<String> mapValues = new ArrayList<String>(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();

		Iterator<String> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator<String> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					passedMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put((String) key, (String) val);
					break;
				}

			}

		}
		return sortedMap;
	}

	private Map<String, String> getQueryCombosActualValues(Query query) {
		Map<String, String> comboActualValues = new HashMap<String, String>();
		Field[] fields = query.getClass().getDeclaredFields();
		Locale locale = this.getLocale(query.getLocale());
		ResourceBundle rb = this.getResourceBundle(locale);
		Pattern patternComboFields = Pattern.compile("Combo\\d1");
		Pattern patternComboComparison = Pattern.compile("Combo2\\d");
		Matcher matcherComboFields = null;
		Matcher matcherComboComparison = null;

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];

			if (field.isAnnotationPresent(Column.class)
					&& !field.isAnnotationPresent(Id.class)) {
				String fieldName = field.getName();
				// gets only those field needed to fill the combos
				if (fieldName.contains("Combo") || fieldName.contains("Field")
						|| fieldName.contains("CB")) {

					String getMethodName = "get"
							+ fieldName.substring(0, 1).toUpperCase()
							+ fieldName.substring(1, fieldName.length());
					try {

						Method getMethod = query.getClass().getDeclaredMethod(
								getMethodName, new Class<?>[] {});
						String getMethodValue = getMethod.invoke(query,
								new Object[] {}).toString();
						comboActualValues.put(fieldName, getMethodValue);
						// i try yo get the labels for the combos only if there
						// is some value
						if (!getMethodValue.equals("")) {
							matcherComboFields = patternComboFields
									.matcher(fieldName);
							matcherComboComparison = patternComboComparison
									.matcher(fieldName);
							String messageBundleKey = "label.";

							if (matcherComboFields.find()) {

								String groupBase = fieldName
										.split(patternComboFields.pattern())[0];
								messageBundleKey += groupBase + "."
										+ getMethodValue;

								comboActualValues.put(fieldName + "Label",
										rb.getString(messageBundleKey));

								comboActualValues.put(fieldName + "Group",
										groupBase);
							} else if (matcherComboComparison.find()) {
								String groupBase = fieldName
										.split(patternComboComparison.pattern())[0];
								messageBundleKey += "query.operation"
										+ getMethodValue;
								comboActualValues.put(fieldName + "Label",
										rb.getString(messageBundleKey));

								comboActualValues.put(fieldName + "Group",
										groupBase);
							}

						}

					} catch (NoSuchMethodException | SecurityException
							| IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						comboActualValues.put(fieldName,"");
						e.printStackTrace();
					}

					
				}
			}
		}
		
		
		return comboActualValues;
	}
}
