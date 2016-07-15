package at.tugraz.ist.agileRecommender.mahout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import at.tugraz.ist.agileRecommender.Recommenders;
import at.tugraz.ist.agileRecommender.lucene.app.App;
import at.tugraz.ist.agileRecommender.lucene.workflow.WorkFlow;
import at.tugraz.ist.agileRecommender.profile.Profile;

public class UserBased {

	public static List<App> getUBCFApps(long userID){
		List<App> recommendedApps = new ArrayList<App>();
		try {
			// load the data from the file with format "userID,itemID,value"
			File file = new File("resources/AppRatings");
			String absolutePath = file.getAbsolutePath();
			DataModel model = new FileDataModel(new File(absolutePath));
			
			//  compute the correlation coefficient between their interactions
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			
			// we'll use all that have a similarity greater than 0.1
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0, similarity, model);
			
			// all the pieces to create our recommender
			UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			
			//  get 3 items recommended for the user with userID 1
			List<RecommendedItem> recommendations = recommender.recommend(userID, 3);
			for (RecommendedItem recommendation : recommendations) {
				App a = new App();
				a.setHref(FileOperations.readFile("AppList", recommendation.getItemID()));
				a.setTitle(a.getHref());
				recommendedApps.add(a);
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recommendedApps;
	}
	
	public static List<WorkFlow> getUBCFWfs(long userID){
		List<WorkFlow> recommendedWfs = new ArrayList<WorkFlow>();
		try {
			// load the data from the file with format "userID,itemID,value"
			File file = new File("resources/WfRatings");
			String absolutePath = file.getAbsolutePath();
			DataModel model = new FileDataModel(new File(absolutePath));
			
			//  compute the correlation coefficient between their interactions
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			
			// we'll use all that have a similarity greater than 0.1
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0, similarity, model);
			
			// all the pieces to create our recommender
			UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			
			//  get 3 items recommended for the user with userID 1
			List<RecommendedItem> recommendations = recommender.recommend(userID, 3);
			for (RecommendedItem recommendation : recommendations) {
				WorkFlow a = new WorkFlow();
				a.setHref(FileOperations.readFile("WfList", recommendation.getItemID()));
				recommendedWfs.add(a);
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recommendedWfs;
	}
	

}

