/**
 * 
 */
package com.ncr.ATMMonitoring.serviceFacade.imp;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncr.ATMMonitoring.pojo.Dashboard;
import com.ncr.ATMMonitoring.pojo.User;
import com.ncr.ATMMonitoring.pojo.Widget;
import com.ncr.ATMMonitoring.pojo.WidgetCategory;
import com.ncr.ATMMonitoring.service.DashboardService;
import com.ncr.ATMMonitoring.service.UserService;
import com.ncr.ATMMonitoring.service.WidgetService;
import com.ncr.ATMMonitoring.serviceFacade.DashboardWidgetFacade;

/**
 * @author Otto Abreu
 * 
 */
@Service
public class DashboardWidgetFacadeImp implements DashboardWidgetFacade {

	// autowired services
	@Autowired
	private DashboardService dashboardService;
	@Autowired
	private WidgetService widgetService;
	@Autowired
	private UserService userService;

	/* The logger. */
	static private Logger logger = Logger
			.getLogger(DashboardWidgetFacadeImp.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ncr.ATMMonitoring.serviceFacade.DashboardWidgetFacade#getDashboard
	 * (java.lang.String, boolean)
	 */
	@Override
	public Dashboard getDashboard(String username) {
		User loggedUser = this.getLoggedUser(username);
		Dashboard dashboard = dashboardService
				.findOrCreateDashboardByUser(loggedUser);
		return dashboard;
	}

/*
 * (non-Javadoc)
 * @see com.ncr.ATMMonitoring.serviceFacade.DashboardWidgetFacade#updateWidgetPosition(java.lang.String, int, int, int)
 */
	@Override
	public void updateWidgetPosition(String username ,int  widgetId, int oldPosition,
			int newPosition) {
		
		User loggedUser = this.getLoggedUser(username);
		Widget widget = this.widgetService.findWidgetById(widgetId);
		
		boolean userOwnsWidget = this.widgetService.isWidgetOwnedByUser(widget,
				loggedUser);
		
		if(userOwnsWidget && oldPosition != newPosition ){
			
			Dashboard dashboard = loggedUser.getDashboard();
			List<Widget> widgets = dashboard.getVisibleWidgets();
			
			logger.debug("Moving widget " + widget.getId()
					+ " from position " + oldPosition + " to "
					+ newPosition);
			
			logger.debug("old widget list: " + widgets);
			
			if (oldPosition < newPosition) {
				Collections.rotate(widgets.subList(oldPosition, newPosition + 1),
						-1);
			} else {
				Collections
						.rotate(widgets.subList(newPosition, oldPosition + 1), 1);
			}
			
			logger.debug("new widget list: " + widgets);
			
			for (int i = 0; i < widgets.size(); i++) {
				Widget widgetUpdating = (Widget) widgets.get(i);
				widgetUpdating.setOrder(i + 1);
				this.widgetService.saveWidget(widgetUpdating);
			}
		}

	}
	/*
	 * (non-Javadoc)
	 * @see com.ncr.ATMMonitoring.serviceFacade.DashboardWidgetFacade#hideShowWidget(int, java.lang.String, boolean)
	 */
	@Override
	public void hideShowWidget(int widgetId, String username, boolean hideShow) {
		
		User loggedUser = this.getLoggedUser(username);
		Widget widget = this.widgetService.findWidgetById(widgetId);
		int order = 0;
		
		boolean userOwnsWidget = this.widgetService.isWidgetOwnedByUser(widget,
				loggedUser);
		
		if (userOwnsWidget) {
			logger.debug("Showing widget: " + hideShow+"widget id: "+widget.getId());
			
			if (hideShow == VISIBLE) {

				List<Widget> widgets = loggedUser.getDashboard().getWidgets();
				int maxOrder = widgets.get(widgets.size() - 1).getOrder();
				order = maxOrder + 1;

			}
			widget.setVisible(hideShow);
			widget.setOrder(order);
			this.widgetService.saveWidget(widget);
		}

	}
	/*
	 * (non-Javadoc)
	 * @see com.ncr.ATMMonitoring.serviceFacade.DashboardWidgetFacade#changeDashboardColumms(java.lang.String, int)
	 */
	@Override
	public void changeDashboardColumms(String username, int columns ){
		
		User loggedUser = this.getLoggedUser(username);
		Dashboard dashboard = loggedUser.getDashboard();
		dashboard.setColumns(columns);
		this.dashboardService.saveDashboard(dashboard);
	}
	/*
	 * (non-Javadoc)
	 * @see com.ncr.ATMMonitoring.serviceFacade.DashboardWidgetFacade#getVisiblesAndInvisiblesWidgets(java.lang.String)
	 */
	@Override
	public Map<String, List<Widget>> getVisiblesAndInvisiblesWidgets(
			String username) {
		
		User loggedUser = this.getLoggedUser(username);
		Dashboard dashboard = loggedUser.getDashboard();
		List<Widget> visibleWidgets = dashboard.getVisibleWidgets();
		List<Widget> hiddenWidgets = dashboard.getWidgets();
		hiddenWidgets.removeAll(visibleWidgets);
		
		Map<String, List<Widget>> widgetList = new HashMap<String, List<Widget>>(2);
		widgetList.put(VISIBLE_WIDGETS, visibleWidgets);
		widgetList.put(INVISIBLE_WIDGETS, hiddenWidgets);
		return widgetList;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ncr.ATMMonitoring.serviceFacade.DashboardWidgetFacade#executeQueryForWidget(java.lang.String, int, java.util.Locale)
	 */
	@Override
	public List<?> executeQueryForWidget(String username,int widgetId, Locale locale){
		
		List<?> queryResults = null;
		User loggedUser = this.getLoggedUser(username);
		Widget widget = this.widgetService.findWidgetById(widgetId);
		boolean userOwnsWidget = this.widgetService.isWidgetOwnedByUser(widget,
				loggedUser);
		
		if (userOwnsWidget) {
			logger.debug("Found widget: " + widget.getId());
		
			 queryResults = widgetService.executeQuery(widget,
					locale);
		}
		return queryResults;
	}
	

	@Override
	public void addWidgetToUser(String username, List<Integer> widgetsId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Widget> findAndCopyDefaultWidgets(String username,
			boolean copyWidgets) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeWidgetFromUser(List<Integer> widgetsId, String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateWidgetDefaultStatus(int widgetId, String username,
			boolean setAsDefault) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean userHasWidget(int widgetId, String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Widget getWidget(int widgetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addOrRemoveWidgetToLibrary(int widgetId, String username,
			int categoryId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<WidgetCategory> findLibraryWidgetByCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the logged user
	 * 
	 * @param username
	 * @return
	 */
	private User getLoggedUser(String username) {
		return this.userService.getUserByUsername(username);
	}

	

}
