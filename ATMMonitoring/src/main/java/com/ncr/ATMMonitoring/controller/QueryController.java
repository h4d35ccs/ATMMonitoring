package com.ncr.ATMMonitoring.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
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
import com.ncr.ATMMonitoring.pojo.HardwareDevice;
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
import com.ncr.ATMMonitoring.utils.Operation;

/**
 * The Class QueryController.
 * 
 * Controller for handling query related HTTP petitions.
 * 
 * @author Jorge López Fernández (lopez.fernandez.jorge@gmail.com)
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

		if (principal != null) {
			userMsg = this.getUserGreeting(principal, request);
		}

		map.put("userMsg", userMsg);
		map.put("query", query);
		map.put("values", Query.getComboboxes());

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
	 * @param comboType
	 * @return
	 */
	@RequestMapping(value = "/queries/combos/{comboType}/{locale}", method = RequestMethod.GET)
	@ResponseBody
	public String getComboForQueries(@PathVariable("comboType") String comboType,@PathVariable("locale") String localeParam){
		
		final Map <String, Object> responseMap = new HashMap<String, Object>();
		final Map<String,String> options = new HashMap<String,String>();
		final String selectOptionsKey ="selectoptions";
		String messageKeyBase ="label."+comboType;
		
		Locale locale = null;
		if(localeParam == null){
			
			locale = Locale.getDefault();
		}else{
			
			 locale = new Locale(localeParam);
		}
		
		ResourceBundle rb = ResourceBundle.getBundle("messages", locale);
		
		
		switch(comboType){
			case "terminal":
				addToComboOption(Terminal.class, messageKeyBase,options, rb );
//				options.put("terminalType",rb.getString(messageKeyBase+".terminalType"));
//				options.put("terminalVendor",rb.getString(messageKeyBase+".terminalVendor"));
//				options.put("frontReplenish",rb.getString(messageKeyBase+".frontReplenish"));
//				options.put("geographicputress",rb.getString(messageKeyBase+".geographicAddress"));
//				options.put("branch",rb.getString(messageKeyBase+".branch"));
//				options.put("bank",rb.getString(messageKeyBase+".bank"));
//				options.put("manufacturingSite",rb.getString(messageKeyBase+".manufacturingSite"));
//				options.put("productClassDescription",rb.getString(messageKeyBase+".productClassDescription"));
//				options.put("serialNumber",rb.getString(messageKeyBase+".serialNumber"));;
//				options.put("tracerNumber",rb.getString(messageKeyBase+".tracerNumber"));
//				options.put("ip",rb.getString(messageKeyBase+".ip"));
//				options.put("mac",rb.getString(messageKeyBase+".mac"));
			break;
			case "financialDevice":
				addToComboOption(FinancialDevice.class, messageKeyBase,options, rb );
//				options.put("caption", rb.getString(messageKeyBase+".caption"));
//				options.put("description", rb.getString(messageKeyBase+".description"));
//				options.put("deviceInstance",rb.getString(messageKeyBase+".deviceInstance"));
//				options.put("deviceStatus", rb.getString(messageKeyBase+".deviceStatus"));
//				options.put("firmwareVersion", rb.getString(messageKeyBase+".firmwareVersion"));
//				options.put("hotSwappable", rb.getString(messageKeyBase+".hotSwappable"));
//				options.put("manufacturer", rb.getString(messageKeyBase+".manufacturer"));
//				options.put("model", rb.getString(messageKeyBase+".model"));
//				options.put("name", rb.getString(messageKeyBase+".name"));
//				options.put("pmStatus", rb.getString(messageKeyBase+".pmStatus"));
//				options.put("replaceable", rb.getString(messageKeyBase+".replaceable"));
//				options.put("removable", rb.getString(messageKeyBase+".removable"));
//				options.put("serialNumber", rb.getString(messageKeyBase+".serialNumber"));
//				options.put("universalId", rb.getString(messageKeyBase+".universalId"));
//				options.put("variant", rb.getString(messageKeyBase+".variant"));
//				options.put("version", rb.getString(messageKeyBase+".version"));
				
				break;
			case "xfsComponent":
				addToComboOption(XfsComponent.class, messageKeyBase,options, rb );
//				options.put("xfsClass", rb.getString(messageKeyBase+".xfsClass");
//				options.put("acceptMedia", boolOperations);
//				options.put("algorithms", rb.getString(messageKeyBase+".algorithms");
//				options.put("autobeep", rb.getString(messageKeyBase+".autobeep");
//				options.put("autodeposit", boolOperations);
//				options.put("autoretractPeriod", numOperations);
//				options.put("auxiliaries", rb.getString(messageKeyBase+".auxiliaries");
//				options.put("backImageColorFormat", rb.getString(messageKeyBase+".backImageColorFormat");
//				options.put("backscanColor", rb.getString(messageKeyBase+".backscanColor");
//				options.put("cameras", rb.getString(messageKeyBase+".cameras");
//				options.put("camdata", rb.getString(messageKeyBase+".camdata");
//				options.put("canFilterSymbologies", boolOperations);
//				options.put("cards", numOperations);
//				options.put("cashin", boolOperations);
//				options.put("charSupport", rb.getString(messageKeyBase+".charSupport");
//				options.put("chipio", boolOperations);
//				options.put("chipPower", rb.getString(messageKeyBase+".chipPower");
//				options.put("chipProtocol", rb.getString(messageKeyBase+".chipProtocol");
//				options.put("codeLineFormat", rb.getString(messageKeyBase+".codeLineFormat");
//				options.put("coins", boolOperations);
//				options.put("compound", boolOperations);
//				options.put("comparemagneticstripe", boolOperations);
//				options.put("control", rb.getString(messageKeyBase+".control");
//				options.put("cursor", boolOperations);
//				options.put("cylinders", boolOperations);
//				options.put("defaultBackscanColor", rb.getString(messageKeyBase+".defaultBackscanColor");
//				options.put("defaultFrontscanColor", rb.getString(messageKeyBase+".defaultFrontscanColor");
//				options.put("deptransport", boolOperations);
//				options.put("derivationAlgorithms", rb.getString(messageKeyBase+".derivationAlgorithms");
//				options.put("isPrepareDispense", rb.getString(messageKeyBase+".isPrepareDispense);
//				options.put("dipMode", rb.getString(messageKeyBase+".dipMode");
//				options.put("dispenseTo", rb.getString(messageKeyBase+".dispenseTo");
//				options.put("display", rb.getString(messageKeyBase+".display");
//				options.put("displayLight", boolOperations);
//				options.put("doors", rb.getString(messageKeyBase+".doors");
//				options.put("ejectPosition", rb.getString(messageKeyBase+".ejectPosition);
//				options.put("emvHashAlgorithm", rb.getString(messageKeyBase+".);
//				options.put("emvImportSchemes", rb.getString(messageKeyBase+".);
//				options.put("encioProtocols", rb.getString(messageKeyBase+".);
//				options.put("encoder", boolOperations);
//				options.put("encodenames", rb.getString(messageKeyBase+".);
//				options.put("endorser", boolOperations);
//				options.put("envelopesupply", rb.getString(messageKeyBase+".);
//				options.put("exchangeTypes", rb.getString(messageKeyBase+".);
//				options.put("extents", rb.getString(messageKeyBase+".);
//				options.put("extra", rb.getString(messageKeyBase+".);
//				options.put("fontnames", rb.getString(messageKeyBase+".);
//				options.put("forms", boolOperations);
//				options.put("frontImageColorFormat", rb.getString(messageKeyBase+".);
//				options.put("frontscanColor", rb.getString(messageKeyBase+".);
//				options.put("guidlights", rb.getString(messageKeyBase+".);
//				options.put("hasCashBox", boolOperations);
//				options.put("hasInsertedSensor", boolOperations);
//				options.put("hasShutter", boolOperations);
//				options.put("hasTakenSensor", boolOperations);
//				options.put("hsmVendor", rb.getString(messageKeyBase+".);
//				options.put("idConnect", boolOperations);
//				options.put("idKey", rb.getString(messageKeyBase+".);
//				options.put("imagecapture", rb.getString(messageKeyBase+".);
//				options.put("imageSource", rb.getString(messageKeyBase+".);
//				options.put("imageType", rb.getString(messageKeyBase+".);
//				options.put("indicators", rb.getString(messageKeyBase+".);
//				options.put("insertOrientation", rb.getString(messageKeyBase+".);
//				options.put("intermediateStacker", numOperations);
//				options.put("isApplicationRefuse", boolOperations);
//				options.put("isAutofeed", boolOperations);
//				options.put("isCardTakenSensor", boolOperations);
//				options.put("isCompareSignatures", boolOperations);
//				options.put("isHsmJournaling", boolOperations);
//				options.put("isIntermediateStacker", boolOperations);
//				options.put("isItemsTakenSensor", boolOperations);
//				options.put("isKeyImportThroughParts", boolOperations);
//				options.put("isMediaPresented", boolOperations);
//				options.put("isPinCanPersistAfterUse", boolOperations);
//				options.put("isPresentControl", boolOperations);
//				options.put("isRescan", boolOperations);
//				options.put("isRetractCountsItems", boolOperations);
//				options.put("isRetractToTransport", boolOperations);
//				options.put("isSafeDoor", boolOperations);
//				options.put("isSetPinBlockDataRequired", boolOperations);
//				options.put("isStamp", boolOperations);
//				options.put("isTypeCombined", boolOperations);
//				options.put("itemInfoTypes", rb.getString(messageKeyBase+".);
//				options.put("keyBlockImportFormats", rb.getString(messageKeyBase+".);
//				options.put("keyCheckModes", rb.getString(messageKeyBase+".);
//				options.put("keyLock", boolOperations);
//				options.put("keyNum", numOperations);
//				options.put("keys", rb.getString(messageKeyBase+".);
//				options.put("logical", rb.getString(messageKeyBase+".);
//				options.put("magneticstriperead", boolOperations);
//				options.put("magneticstripewrite", boolOperations);
//				options.put("max2Retract", numOperations);
//				options.put("maxBills", numOperations);
//				options.put("maxCashInItems", numOperations);
//				options.put("maxCoins", numOperations);
//				options.put("maxDataLength", numOperations);
//				options.put("maxDispenseItems", numOperations);
//				options.put("maxMediaOnStacker", numOperations);
//				options.put("maxNumChars", numOperations);
//				options.put("maxPictures", numOperations);
//				options.put("maxRetract", rb.getString(messageKeyBase+".);
//				options.put("mediaTaken", boolOperations);
//				options.put("memoryChipProtocols", rb.getString(messageKeyBase+".);
//				options.put("micr", boolOperations);
//				options.put("moveItems", rb.getString(messageKeyBase+".);
//				options.put("multiPage", boolOperations);
//				options.put("numLeds", numOperations);
//				options.put("ocr", rb.getString(messageKeyBase+".);
//				options.put("outputPositions",rb.getString(messageKeyBase+".);
//				options.put("paperSources", rb.getString(messageKeyBase+".);
//				options.put("pinFormats", rb.getString(messageKeyBase+".);
//				options.put("pockets", numOperations);
//				options.put("powerOff", rb.getString(messageKeyBase+".);
//				options.put("powerOn", rb.getString(messageKeyBase+".);
//				options.put("powerSaveControl", boolOperations);
//				options.put("presentationAlgorithms", rb.getString(messageKeyBase+".);
//				options.put("printer", boolOperations);
//				options.put("printOnRetracts", boolOperations);
//				options.put("printSize", rb.getString(messageKeyBase+".);
//				options.put("programaticallyDeactivate", boolOperations);
//				options.put("provider", rb.getString(messageKeyBase+".);
//				options.put("readForm", rb.getString(messageKeyBase+".);
//				options.put("readTracks", rb.getString(messageKeyBase+".);
//				options.put("refill", boolOperations);
//				options.put("resetControl", rb.getString(messageKeyBase+".);
//				options.put("resolutions", rb.getString(messageKeyBase+".);
//				options.put("retract", boolOperations);
//				options.put("retractAreas", rb.getString(messageKeyBase+".);
//				options.put("retractBins", rb.getString(messageKeyBase+".);
//				options.put("retractenvelope", rb.getString(messageKeyBase+".);
//				options.put("retractStackerActions", rb.getString(messageKeyBase+".);
//				options.put("retractTransportActions", rb.getString(messageKeyBase+".);
//				options.put("retractToDeposit", boolOperations);
//				options.put("rsaAuthenticationScheme", rb.getString(messageKeyBase+".);
//				options.put("rsaCryptAlgorithm", rb.getString(messageKeyBase+".);
//				options.put("rsaKeycheckMode", rb.getString(messageKeyBase+".);
//				options.put("rsaSignatureAlgorithm", rb.getString(messageKeyBase+".);
//				options.put("securityType", rb.getString(messageKeyBase+".);
//				options.put("sensors", rb.getString(messageKeyBase+".);
//				options.put("shutterControl", boolOperations);
//				options.put("signatureScheme", rb.getString(messageKeyBase+".);
//				options.put("stamp", rb.getString(messageKeyBase+".);
//				options.put("positions", rb.getString(messageKeyBase+".);
//				options.put("symbologies", rb.getString(messageKeyBase+".);
//				options.put("toner", boolOperations);
//				options.put("type", rb.getString(messageKeyBase+".);
//				options.put("validationAlgorithms", rb.getString(messageKeyBase+".);
//				options.put("vandalCheck", boolOperations);
//				options.put("windowsPrinter", rb.getString(messageKeyBase+".);
//				options.put("writeForm", rb.getString(messageKeyBase+".);
//				options.put("writeMode", rb.getString(messageKeyBase+".);
//				options.put("writeTracks", rb.getString(messageKeyBase+".);
				break;
			case "jxfsComponent":
				addToComboOption(JxfsComponent.class, messageKeyBase,options, rb );
				break;
			case "hotfix":
				addToComboOption(Hotfix.class, messageKeyBase,options, rb );
				break;
			case "internetExplorer":
				options.put("majorVersion", rb.getString(messageKeyBase+".majorVersion"));		
				break;
			case "operatingSystem":
				addToComboOption(OperatingSystem.class, messageKeyBase,options, rb );
				break;
			case "software":
				addToComboOption(Software.class, messageKeyBase,options, rb );
				break;
			case "xfsSw":
				addToComboOption(Software.class, messageKeyBase,options, rb );
				break;
			case "featSw":
				addToComboOption(Software.class, messageKeyBase,options, rb );
				break;
			case "hardwareDevice":
				options.put("Win32_ComputerSystem", rb.getString(messageKeyBase+".Win32_ComputerSystem"));
				options.put("Win32_Processor", rb.getString(messageKeyBase+".Win32_Processor"));
				options.put("Win32_PhysicalMemory", rb.getString(messageKeyBase+".Win32_PhysicalMemory"));
				options.put("Win32_DiskDrive", rb.getString(messageKeyBase+".Win32_DiskDrive"));
				options.put("Win32_LogicalDisk", rb.getString(messageKeyBase+".Win32_LogicalDisk"));
				options.put("Win32_BaseBoard", rb.getString(messageKeyBase+".Win32_BaseBoard"));
				options.put("Win32_NetworkAdapter", rb.getString(messageKeyBase+".Win32_NetworkAdapter"));
				options.put("Win32_FloppyDrive", rb.getString(messageKeyBase+".Win32_FloppyDrive"));
				options.put("Win32_CDROMDrive", rb.getString(messageKeyBase+".Win32_CDROMDrive"));
				options.put("Win32_SoundDevice", rb.getString(messageKeyBase+".Win32_SoundDevice"));
				options.put("Win32_DisplayConfiguration", rb.getString(messageKeyBase+".Win32_DisplayConfiguration"));
				options.put("Win32_USBController", rb.getString(messageKeyBase+".Win32_USBController"));
				options.put("Win32_USBHub", rb.getString(messageKeyBase+".Win32_USBHub"));
				options.put("Win32_SerialPort", rb.getString(messageKeyBase+".Win32_SerialPort"));
				options.put("Win32_ParallelPort", rb.getString(messageKeyBase+".Win32_ParallelPort"));
				options.put("Win32_1394Controller", rb.getString(messageKeyBase+".Win32_1394Controller"));
				options.put("Win32_SCSIController", rb.getString(messageKeyBase+".Win32_SCSIController"));
				options.put("Win32_DesktopMonitor", rb.getString(messageKeyBase+".Win32_DesktopMonitor"));
				options.put("Win32_Keyboard", rb.getString(messageKeyBase+".Win32_Keyboard"));
				options.put("Win32_PointingDevice", rb.getString(messageKeyBase+".Win32_PointingDevice"));
				options.put("Win32_SystemSlot", rb.getString(messageKeyBase+".Win32_SystemSlot"));
				options.put("Win32_Bios", rb.getString(messageKeyBase+".Win32_Bios"));
				options.put("Win32_VideoController", rb.getString(messageKeyBase+".Win32_VideoController"));
				break;
				
			default:		
				
		}
		responseMap.put(selectOptionsKey,options);
		responseMap.put("optionslength", options.size());
		Gson gson = new GsonBuilder().create();
		return gson.toJson(responseMap);
	}
	/**
	 * 
	 * @param entity
	 * @param messageKeyBase
	 * @param options
	 * @param rb
	 */
	private void addToComboOption(Class<?> entity, String messageKeyBase, Map<String,String> options, ResourceBundle rb ){
		for(String option: this.entityFieldNamesToComboBox(entity)){
			try{
				options.put(option, rb.getString(messageKeyBase+"."+option));
			}catch(MissingResourceException e){
				logger.debug("the field name: "+option+" does not have an property key to show in the combo, is an valid combo option?");
			}
		}
	}
	/**
	 * 
	 * @param entityClass
	 * @return
	 */
	private List<String> entityFieldNamesToComboBox(Class<?> entityClass){
		Field[] fields = entityClass.getDeclaredFields();
		final List<String> comboOptions = new ArrayList<String>();
		
		for(Field field : fields){
			System.out.println(field.getAnnotations());
			if(field.isAnnotationPresent(ComboQueryOption.class)){
				comboOptions.add(field.getName());
			}
		}
		return comboOptions;
	}

}
