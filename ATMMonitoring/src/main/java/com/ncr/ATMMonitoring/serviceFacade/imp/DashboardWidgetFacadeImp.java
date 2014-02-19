/**
 * 
 */
package com.ncr.ATMMonitoring.serviceFacade.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ncr.ATMMonitoring.pojo.Dashboard;
import com.ncr.ATMMonitoring.pojo.Widget;
import com.ncr.ATMMonitoring.pojo.WidgetCategory;
import com.ncr.ATMMonitoring.serviceFacade.DashboardWidgetFacade;

/**
 * @author Otto Abreu
 *
 */
@Service
public class DashboardWidgetFacadeImp implements DashboardWidgetFacade {

	/**
	 * 
	 */
	public DashboardWidgetFacadeImp() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dashboard getDashboard(String username, boolean createDefault) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateWidgetPosition(List<Widget> widgets, int oldPosition,
			int newPostion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideShowWidget(int widgetId, String username, boolean hideShow) {
		// TODO Auto-generated method stub
		
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

}
