/**
 * 
 */
package com.ncr.ATMMonitoring.serviceFacade;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.ncr.ATMMonitoring.pojo.Dashboard;
import com.ncr.ATMMonitoring.pojo.Widget;
import com.ncr.ATMMonitoring.pojo.WidgetCategory;
import com.ncr.ATMMonitoring.service.DashboardService;
import com.ncr.ATMMonitoring.service.UserService;
import com.ncr.ATMMonitoring.service.WidgetService;

/**
 * Service that is an implementation of the Facade pattern that provides a
 * simplified interface for the interaction between the services
 * {@link DashboardService} , {@link WidgetService} and {@link UserService}
 * 
 * @author Otto Abreu
 * 
 */
public interface DashboardWidgetFacade {
	/**
	 * Constant used to set an widget as visible VISIBLE = true;
	 */
	boolean VISIBLE = true;
	/**
	 * Constant used to set an widget as not visible HIDE = false;;
	 */
	boolean HIDE = false;
	/**
	 * COPY_WIDGETS = true;
	 */
	boolean COPY_WIDGETS = true;

	/**
	 * RETURN_DEFAULTS = true;
	 */
	boolean RETURN_DEFAULTS = false;

	/**
	 * Constant used to mark as default SET_WIDGET_AS_DEFAULT = true;
	 */
	boolean SET_WIDGET_AS_DEFAULT = true;
	/**
	 * Constant used to remove the default mark from a widget
	 * SET_WIDGET_AS_NO_DEFAULT = false;
	 */
	boolean SET_WIDGET_AS_NO_DEFAULT = false;
	/**
	 * Used as key for knowing the list of visible widgets VISIBLE_WIDGETS =
	 * "visible";
	 */
	String VISIBLE_WIDGETS = "visible";

	/**
	 * Used as key for knowing the list of invisible widgets INVISIBLE_WIDGETS =
	 * "invisible";
	 */
	String INVISIBLE_WIDGETS = "invisible";

	/**
	 * Return the dashboard associated to the given user. If the user does not
	 * have a dashboard, a default will be returned
	 * 
	 * @param username
	 *            String with the username
	 * @return Dashboard
	 */
	Dashboard getDashboard(String username);

	/**
	 * Modifies the position of the widget inside the dashboard
	 * @param username  String with the username
	 * @param widgetId  int with a valid widget id
	 * @param oldPosition old position in the dashboard
	 * @param newPosition new position in the dashboard
	 */
	  void updateWidgetPosition(String username ,int  widgetId, int oldPosition,
			int newPosition);

	/**
	 * Returns two list with the visible and invisible widgets of the dashboard<br>
	 * The list are inside a map, to retrieve the Visible list, please use
	 * {@link DashboardWidgetFacade#VISIBLE_WIDGETS} as key, to retrieve the
	 * Invisible list, please use
	 * {@link DashboardWidgetFacade#INVISIBLE_WIDGETS} as key
	 * 
	 * @returnMap<String, List<Widget>>
	 */
	Map<String, List<Widget>> getVisiblesAndInvisiblesWidgets(String username);

	/**
	 * Marks a widget as visible or invisible <br>
	 * To specify if the method will make a widget visible please use
	 * {@link DashboardWidgetFacade#VISIBLE}, to hide it use
	 * {@link DashboardWidgetFacade#HIDE}
	 * 
	 * @param widgetsId
	 *            int with a valid widget id
	 * @param username
	 *            String with the username
	 * @param hideShow
	 */
	void hideShowWidget(int widgetId, String username, boolean hideShow);

	/**
	 * Modifies the number of columns un the dashboard
	 * 
	 * @param username
	 *            S tring
	 * @param columns
	 *            int
	 */
	void changeDashboardColumms(String username, int columns);
	/**
	 * Execute a query for the given widget
	 * @param username
	 * @param widgetId
	 * @param locale
	 * @return List<?> 
	 */
	public List<?> executeQueryForWidget(String username,int widgetId, Locale locale);
	
	/**
	 * Associates the widgets that matches the given ids with the given user
	 * 
	 * @param username
	 *            String with the username
	 * @param widgetsId
	 *            List<Integer> with valid widget id
	 */
	void addWidgetToUser(String username, List<Integer> widgetsId);

	/**
	 * TODO finish the javadoc <br>
	 * To specify if the method will return a widget
	 * {@link DashboardWidgetFacade#COPY_WIDGETS}, to
	 * {@link DashboardWidgetFacade#FIND_ACTUAL}
	 * 
	 * @param username
	 *            String with the username
	 * @param copyWidgets
	 * @return List<Widget>
	 */
	List<Widget> findAndCopyDefaultWidgets(String username, boolean copyWidgets);

	/**
	 * Remove the association between the user and the widget
	 * 
	 * @param widgetsId
	 *            int with a valid widget id
	 * @param username
	 *            String with the username
	 */
	void removeWidgetFromUser(List<Integer> widgetsId, String username);

	/**
	 * TODO finish the javadoc <br>
	 * To specify if the method will mark a widget as default please use
	 * {@link DashboardWidgetFacade#SET_WIDGET_AS_DEFAULT}, to remove the
	 * default mark {@link DashboardWidgetFacade#SET_WIDGET_AS_NO_DEFAULT}
	 * 
	 * @param widgetsId
	 *            int with a valid widget id
	 * @param username
	 *            String with the username
	 * @param setAsDefault
	 */
	void updateWidgetDefaultStatus(int widgetId, String username,
			boolean setAsDefault);

	/**
	 * Returns if the user has an association with the given widget (true) or if
	 * the asociation does not exist (false)
	 * 
	 * @param widgetsId
	 *            int with a valid widget id
	 * @param username
	 *            String with the username
	 * @return boolean
	 */
	boolean userHasWidget(int widgetId, String username);

	/**
	 * Returns the widget that matches the given id
	 * 
	 * @param widgetsId
	 *            int with a valid widget id
	 * @return Widget
	 */
	Widget getWidget(int widgetId);

	/**
	 * 
	 * @param widgetsId
	 *            int with a valid widget id
	 * @param username
	 *            String with the username
	 * @param categoryId
	 */
	void addOrRemoveWidgetToLibrary(int widgetId, String username,
			int categoryId);

	/**
	 * 
	 * @return List<WidgetCategory>
	 */
	List<WidgetCategory> findLibraryWidgetByCategory();
	

}
