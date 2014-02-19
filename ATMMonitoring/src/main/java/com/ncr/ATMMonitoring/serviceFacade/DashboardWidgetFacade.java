/**
 * 
 */
package com.ncr.ATMMonitoring.serviceFacade;

import java.util.List;

import com.ncr.ATMMonitoring.pojo.Dashboard;
import com.ncr.ATMMonitoring.pojo.Widget;
import com.ncr.ATMMonitoring.pojo.WidgetCategory;
import com.ncr.ATMMonitoring.service.DashboardService;
import com.ncr.ATMMonitoring.service.UserService;
import com.ncr.ATMMonitoring.service.WidgetService;

/**
 * Service that is an implementation of the Facade pattern that provides a
 * simplified interface for the interaction between the services {@link DashboardService} ,
 * {@link WidgetService} and {@link UserService}
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
	 * CREATE_DEFAULT = true;
	 */
	boolean CREATE_DEFAULT = true;
	/**
	 * FIND_ACTUAL = false;
	 */
	boolean FIND_ACTUAL = false;
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
	 *  TODO finish the javadoc
	 * <br>
	 * To set the param createDefault please use
	 * {@link DashboardWidgetFacade#CREATE_DEFAULT} for creating a default
	 * dashboard, {@link DashboardWidgetFacade#FIND_ACTUAL} otherwise
	 * 
	 * @param username
	 *            String with the username
	 * @param createDefault
	 *            boolean indicate if
	 * @return Dashboard
	 */
	Dashboard getDashboard(String username, boolean createDefault);

	/**
	 *  TODO finish the javadoc
	 * @param widgets
	 *            List<Widget> to update the position
	 * @param oldPosition
	 *            int actual position
	 * @param newPostion
	 *            int the new position
	 */
	void updateWidgetPosition(List<Widget> widgets, int oldPosition,
			int newPostion);

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
	 * Associates the widgets that matches the given ids with the given user
	 * 
	 * @param username
	 *            String with the username
	 * @param widgetsId
	 *            List<Integer> with valid widget id
	 */
	void addWidgetToUser(String username, List<Integer> widgetsId);

	/**
	 * TODO finish the javadoc
	 * <br>
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
	 *  TODO finish the javadoc
	 * <br>
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
