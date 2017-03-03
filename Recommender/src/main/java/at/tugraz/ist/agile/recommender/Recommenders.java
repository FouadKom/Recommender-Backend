package at.tugraz.ist.agile.recommender;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.tugraz.ist.agile.recommender.marketplaces.parsers.ParseGooglePlayStore;
import at.tugraz.ist.agile.recommender.marketplaces.parsers.ParseNodeRed;
import at.tugraz.ist.agile.recommender.models.*;


public class Recommenders {
	
	public static List<App> getAppRecommendations(GatewayProfile profile){
		
		String query = getQuery(profile);
		
		RecommendApps recommendApp = new RecommendApps();
	
		try {
			recommendApp.getRecommendation(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recommendApp.recommendedApps;
	}
	
	public static List<Device> getDevRecommendations(GatewayProfile profile){
		
		String query = getQuery(profile);
		
		RecommendDevices recommendDev = new RecommendDevices();
	
		try {
			recommendDev.getRecommendation(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recommendDev.recommendedDevices;
	}
	
	public static List<Workflow> getWorklowRecommendations(GatewayProfile profile){
		
		RecommendWorkFlow recommendWf = new RecommendWorkFlow();
		
		try {
			recommendWf.getRecommendation(getQuery(profile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recommendWf.recommendedWorkflows;
	}
	
	public static List<Cloud> getCloudRecommendation(GatewayProfile profile){
		RecommendCloud recommendCloud = new RecommendCloud();
		
		try {
			recommendCloud.getRecommendation(profile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recommendCloud.recommendedClouds;
	}
	
	private static String getRecommAppStr(String results, List<App> appList){
		
		int count =1;
		for (Iterator<App> it = appList.iterator(); it.hasNext(); ) {
		        App f = it.next();
		        results += "<a href=https://play.google.com/"+f.getHref().toString()+">"+count+"."+f.getTitle().toString()+"</a>"+"<br>";
		        count++;
		}
		return results;
		
	}
	
	private static String getRecommWfStr(String results, List<Workflow> workflowList){
		
		int count =1;
		for (Iterator<Workflow> it = workflowList.iterator(); it.hasNext(); ) {
				Workflow f = it.next();
		        results += "<a href=http://flows.nodered.org"+f.getHref().toString()+">"+count+"."+f.href.toString()+"</a>"+"<br>";
		        count++;
		}
		return results;
		
	}
	
	private static String getQuery(GatewayProfile profi){
		
		String query  = "iot";
		String query1 = "";
		String query2 = "";
		String query3 = "";
		
		if(profi.apps.length()>0){
			query1 = profi.apps.replace(" ", " OR ");
			query1 = query1.replace(",", " OR ");
			query += query+" OR "+ query1;
		}
		
		if(profi.wfs.length()>0){
			query2 = profi.wfs.replace(" ", " OR ");
			query2 = query2.replace(",", " OR ");
			query += query+" OR "+ query2;
		}
		
		if(profi.devices.length()>0){
			query3 = profi.devices.replace(" ", " OR ");
			query3 = query3.replace(",", " OR ");
			query += query+" OR "+ query3;
		}
		
		return query;
	}

}

