package microsoftacademicgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

public class Batch_Inserter {
	
	static public String datasource = "MicrosoftAcademicGraph";
	static public HashMap<String, Integer> offset = new HashMap<String, Integer>();
	
//	public Batch_Inserter()
//	{
//		offset.put("Affiliations", 0);
//		offset.put("Conference", 19849);
//		offset.put("ConferenceInstances", 21124);
//		offset.put("FieldsOfStydy", 71326);
//		offset.put("Journals", 125160);
//		offset.put("Authors", 148728);
//		offset.put("Papers", 120040929);
//		
//	}
	
	static public HashMap<String, double[]> LoadCoordinates(String type)
	{
		HashMap<String, double[]> hm = new HashMap<String, double[]>();
		String filepath = "/home/yuhansun/Documents/share/Real_data/MicrosoftAcademicGraph/"+type;
		File file = null;
		BufferedReader reader = null;
		String str = null;
		try
		{
			file = new File(filepath);
			reader = new BufferedReader(new FileReader(file));
			while((str = reader.readLine())!=null)
			{
				String[] l = str.split("\t");
				String name = l[0];
				double[] location = new double[2];
				location[0] = Double.parseDouble(l[1]);
				location[1] = Double.parseDouble(l[2]);
				hm.put(name, location);
			}
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader!=null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		return hm;
	}
	

	static public void BatchAffiliation()
	{
		BatchInserter inserter = null;
		Map<String, String> config = new HashMap<String, String>();
		config.put("dbms.pagecache.memory", "6g");
		String db_path = "/home/yuhansun/Documents/Real_data/" + datasource + "/neo4j-community-2.2.3/data/graph.db";
		Label label = DynamicLabel.label("Affiliations");
		
		HashMap<String, double[]> hm = LoadCoordinates("Affiliations_coordinate.csv");
		String filepath = "/home/yuhansun/Documents/share/Real_data/MicrosoftAcademicGraph/Affiliations.txt";
		File file = null;
		BufferedReader reader = null;
		String str = null;
		long id = 0;
		long offset = 0;
		try
		{
			inserter = BatchInserters.inserter(new File(db_path).getAbsolutePath(), config);
			file = new File(filepath);
			reader = new BufferedReader(new FileReader(file));
			while((str = reader.readLine())!=null)
			{
				String[] l = str.split("\t");
				String ID = l[0];
				String name = l[1];
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("ID", ID);
				properties.put("name", name);
				if(hm.containsKey(name))
				{
					double[] location = hm.get(name);
					properties.put("latitude", location[0]);
					properties.put("longitude", location[1]);
				}
				inserter.createNode(id+offset, properties, label);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(str);
		}
		finally
		{
			if(inserter!=null)
				inserter.shutdown();
			if(reader!=null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	static public void BatchConference()
	{
		BatchInserter inserter = null;
		Map<String, String> config = new HashMap<String, String>();
		config.put("dbms.pagecache.memory", "6g");
		String db_path = "/home/yuhansun/Documents/Real_data/" + datasource + "/neo4j-community-2.2.3/data/graph.db";
		Label label = DynamicLabel.label("Conference");
		
		String filepath = "/home/yuhansun/Documents/share/Real_data/MicrosoftAcademicGraph/Conference.txt";
		long offset = 19849;
		File file = null;
		BufferedReader reader = null;
		String str = null;
		try
		{
			inserter = BatchInserters.inserter(new File(db_path).getAbsolutePath(), config);
			file = new File(filepath);
			reader = new BufferedReader(new FileReader(file));
			long id = 0;
			while((str = reader.readLine())!=null)
			{
				String[] l = str.split("\t");
				String ID = l[0];
				String shortname = l[1];
				String fullname = l[2];
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("ID", ID);
				properties.put("short_name", shortname);
				properties.put("full_name", fullname);
				
				inserter.createNode(id+offset, properties, label);
				id++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(str);
		}
		finally
		{
			if(inserter!=null)
				inserter.shutdown();
			if(reader!=null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	static public void BatchConferenceInstances()
	{
		ArrayList<String> property_name = new ArrayList<String>();
		property_name.add("conference_ID");
		property_name.add("conference_instance_ID");
		property_name.add("short_name");
		property_name.add("full_name");
		property_name.add("location");
		property_name.add("URL");
		property_name.add("start_date");
		property_name.add("end_date");
		property_name.add("abstract_registration_date");
		property_name.add("submission_deadline_date");
		property_name.add("notification_due_date");
		property_name.add("final_version_due_date");
		BatchInserter inserter = null;
		Map<String, String> config = new HashMap<String, String>();
		config.put("dbms.pagecache.memory", "6g");
		String db_path = "/home/yuhansun/Documents/Real_data/" + datasource + "/neo4j-community-2.2.3/data/graph.db";
		Label label = DynamicLabel.label("ConferenceInstances");
		
		HashMap<String, double[]> hm = LoadCoordinates("ConferenceInstances_coordinate.csv");
		String filepath = "/home/yuhansun/Documents/share/Real_data/MicrosoftAcademicGraph/ConferenceInstances.txt";
		File file = null;
		BufferedReader reader = null;
		String str = null;
		long id = 0;
		long offset = 21124;
		try
		{
			inserter = BatchInserters.inserter(new File(db_path).getAbsolutePath(), config);
			file = new File(filepath);
			reader = new BufferedReader(new FileReader(file));
			while((str = reader.readLine())!=null)
			{
				if(inserter.nodeExists(id+offset))
				{
					id++;
					continue;
				}
				Map<String, Object> properties = new HashMap<String, Object>();
				String[] l = str.split("\t");
				for(int i = 0;i<l.length;i++)
				{
					String value = l[i];
					if(!value.equals(""))
					{
						properties.put(property_name.get(i), value);
					}
				}
				
				String location_name = l[4];
				double[] location = hm.get(location_name);
				if(location != null)
				{
					properties.put("latitude", location[0]);
					properties.put("longitude", location[1]);
				}
				inserter.createNode(id+offset, properties, label);
				id++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(str);
		}
		finally
		{
			if(inserter!=null)
				inserter.shutdown();
			if(reader!=null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void BatchFieldsOfStudy()
	{
		BatchInserter inserter = null;
		Map<String, String> config = new HashMap<String, String>();
		config.put("dbms.pagecache.memory", "6g");
		String db_path = "/home/yuhansun/Documents/Real_data/" + datasource + "/neo4j-community-2.2.3/data/graph.db";
		Label label = DynamicLabel.label("FieldsOfStudy");
		
		String filepath = "/home/yuhansun/Documents/share/Real_data/MicrosoftAcademicGraph/FieldsOfStudy.txt";
		long offset = 71326;
		File file = null;
		BufferedReader reader = null;
		String str = null;
		try
		{
			inserter = BatchInserters.inserter(new File(db_path).getAbsolutePath(), config);
			file = new File(filepath);
			reader = new BufferedReader(new FileReader(file));
			long id = 0;
			while((str = reader.readLine())!=null)
			{
				String[] l = str.split("\t");
				String ID = l[0];
				String name = l[1];
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("ID", ID);
				properties.put("name", name);
				
				inserter.createNode(id+offset, properties, label);
				id++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(str);
		}
		finally
		{
			if(inserter!=null)
				inserter.shutdown();
			if(reader!=null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void BatchJournals()
	{
		BatchInserter inserter = null;
		Map<String, String> config = new HashMap<String, String>();
		config.put("dbms.pagecache.memory", "6g");
		String db_path = "/home/yuhansun/Documents/Real_data/" + datasource + "/neo4j-community-2.2.3/data/graph.db";
		Label label = DynamicLabel.label("Journals");
		
		String filepath = "/home/yuhansun/Documents/share/Real_data/MicrosoftAcademicGraph/Journals.txt";
		long offset = 125160;
		File file = null;
		BufferedReader reader = null;
		String str = null;
		try
		{
			inserter = BatchInserters.inserter(new File(db_path).getAbsolutePath(), config);
			file = new File(filepath);
			reader = new BufferedReader(new FileReader(file));
			long id = 0;
			while((str = reader.readLine())!=null)
			{
				String[] l = str.split("\t");
				String ID = l[0];
				String name = l[1];
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("ID", ID);
				properties.put("name", name);
				
				inserter.createNode(id+offset, properties, label);
				id++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(str);
		}
		finally
		{
			if(inserter!=null)
				inserter.shutdown();
			if(reader!=null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void BatchAuthors()
	{
		BatchInserter inserter = null;
		Map<String, String> config = new HashMap<String, String>();
		config.put("dbms.pagecache.memory", "6g");
		String db_path = "/home/yuhansun/Documents/Real_data/" + datasource + "/neo4j-community-2.2.3/data/graph.db";
		Label label = DynamicLabel.label("Authors");
		
		String filepath = "/home/yuhansun/Documents/share/Real_data/MicrosoftAcademicGraph/Authors.txt";
		long offset = 148728;
		File file = null;
		BufferedReader reader = null;
		String str = null;
		try
		{
			inserter = BatchInserters.inserter(new File(db_path).getAbsolutePath(), config);
			file = new File(filepath);
			reader = new BufferedReader(new FileReader(file));
			long id = 0;
			while((str = reader.readLine())!=null)
			{
				String[] l = str.split("\t");
				String ID = l[0];
				String name = l[1];
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("ID", ID);
				properties.put("name", name);
				
				inserter.createNode(id+offset, properties, label);
				id++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(str);
		}
		finally
		{
			if(inserter!=null)
				inserter.shutdown();
			if(reader!=null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		HashMap<String, double[]> hm = LoadCoordinates("Affiliations_coordinate.csv");
//		double[] location = hm.get("facultad de ciencias medicas");
//		System.out.println(hm.size());
//		System.out.println(location[0]);
//		System.out.println(location[1]);
		
//		BatchAffiliation();
//		BatchConference();
//		BatchConferenceInstances();
//		BatchFieldsOfStudy();
//		BatchJournals();
		BatchAuthors();
	}

}
