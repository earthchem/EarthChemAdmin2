package org.earthChem.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.earthChem.db.CitationDB;
import org.earthChem.db.DBUtil;
//import org.earthChem.domain.Citation;
import org.earthChem.db.postgresql.hbm.Citation;
//import org.earthChem.exception.InvalidDoiException;

/*****
 * 
 * @author 
 * 
 * Document: https://citation.crosscite.org/docs.html
 * 
 * 
 *
 */
@SuppressWarnings("deprecation")
public class BibcodeOrcidRest {

	public static final String DOI_SERVER="https://api.crossref.org/works/doi/";
	public static final String DOI_SERVER2="http://pid-dev.astromat.org/crossref/doi/";
	public static final String ADS_SERVER="http://pid-dev.astromat.org/ads/doi/";
	public static final String VOLUME="volume";
	public static final String ISSUE="issue";
	public static final String DOI="DOI";
	public static final String URL="URL";
	public static final String TITLE="title";
	public static final String CONTAINER_TITLE="container-title";
	public static final String PUBLISHER="publisher";
	public static final String ISSUED="issued";
	public static final String DATE="date-parts";
	public static final String AUTHOR="author";
	public static final String FAMILY_NAME="family";
	public static final String GIVEN_NAME="given";
	public static final String EDITOR="editor";
	public static final String PAGE="page";
	public static final String TYPE="type";
	public static final String RAW="raw";
	public static final String AFFILIATION="affiliation";
	public static final String BIBCODE="bibcode";	


	public void searchBibcodeByDoi() throws InvalidDoiException {	
		List<Citation> list = CitationDB.getDoiList();
		for(Citation c: list) {	
			String doi = c.getDoi();
			doi = doi.trim();
			doi = doi.replace("<", "%3C");
			doi = doi.replace(">", "%3E");
			doi = doi.replace("[", "%5B");
			doi = doi.replace("]", "%5D");
			doi = doi.replace(" ", "%20");
			String json=this.getDataFromServer(ADS_SERVER,doi);
			String bibcode = null;
			String type = null;
			System.out.println("bc-json: "+c.getCitationNum() +": "+json);
			
			try
			{
				JSONObject jo = null;
				JSONObject joAll = new JSONObject(json.trim());
				if(joAll.has("status") && !"fail".equals(joAll.getString("status"))) 
				{
					
		/*		System.out.println("bc-json2: "+joAll.getString("status"));
				System.out.println("bc-json4: "+joAll.getString("count"));
				if(joAll.has("count") && "0".equals(joAll.getString("count"))) {
					System.out.println("bc-json5: "+joAll.getString("count"));
					return;
				} */
					if(joAll != null && !"null".equals(joAll.getString("data"))) {
						if (joAll.has("data")) jo = joAll.getJSONObject("data");	
					}
					
					if (jo.has(BIBCODE)) 
						bibcode = jo.getString(BIBCODE);
					if (jo.has(TYPE)) 
						type = jo.getString(TYPE);
					if(bibcode != null && type != null) {
						String q = ""+ c.getCitationNum()+","+DBUtil.StringValue(bibcode)+","+DBUtil.StringValue(type);
						DBUtil.update("INSERT INTO bibcode values ("+q+")");
					}
				}
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	private String reformatDoi(String doi) {
		doi = doi.trim();
		doi = doi.replace("<", "%3C");
		doi = doi.replace(">", "%3E");
		doi = doi.replace("[", "%5B");
		doi = doi.replace("]", "%5D");
		doi = doi.replace(" ", "%20");
		return doi;
	}
	
	public void orcidUpdate() throws InvalidDoiException {	
		List<Citation> list = CitationDB.getAllDoiList();
		for(Citation c: list) {	
			String doi= reformatDoi(c.getDoi());
			String json=this.getDataFromServer( DOI_SERVER2,doi);
			String type = null;
			System.out.println("bc-json:: "+c.getCitationNum()+":"+json);
			try
			{
				JSONObject jo = null;
				JSONObject joAll = new JSONObject(json.trim());
				if(joAll.has("status") && !"fail".equals(joAll.getString("status"))) 
				{
					if(joAll != null && !"null".equals(joAll.getString("data"))) {
						if (joAll.has("data")) jo = joAll.getJSONObject("data");	
					}
					
					
					if(jo.has("authors")) {
						JSONArray jsonarray = new JSONArray(jo.getString("authors"));
							for (int i = 0; i < jsonarray.length(); i++) {
						
						    JSONObject jsonobject = jsonarray.getJSONObject(i);
						    String orcid = null;
								if(jsonobject.has("ORCID")) {	
									orcid = jsonobject.getString("ORCID");
									System.out.println("bc-o:: "+orcid);
								}
								    String family = jsonobject.getString("familyName");
								//	System.out.println("bc-f:: "+family);
								    String given = null;
								    if(jsonobject.has("givenName")) {
								    	given = jsonobject.getString("givenName");
								//	System.out.println("bc-g:: "+given);
								    }
									Integer order = jsonobject.getInt("sequence");
								//	System.out.println("bc-s:: "+sequence);
								if(orcid != null && given != null) {
									String q = ""+ c.getCitationNum()+","+DBUtil.StringValue(family)+","+DBUtil.StringValue(given)+","+DBUtil.StringValue(orcid)+","+order;
									DBUtil.update("INSERT INTO orcid (citation_num, family_name, given_name, orcid, author_order) values ("+q+")");
								}
					
						    }	
					}	
				}
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
	


	protected String getDataFromServer(String server, final String code) throws InvalidDoiException
	{
		try {
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		
			httpClientBuilder.setRedirectStrategy(new DefaultRedirectStrategy(){
			        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
			            boolean isRedirect=false;
			            try {
			                isRedirect = super.isRedirected(request, response, context );
			            } catch (org.apache.http.ProtocolException e) {
			                throw new RuntimeException(e);
			            }
			            if (!isRedirect) {
			                int responseCode = response.getStatusLine().getStatusCode();
			                if (responseCode == 301 || responseCode == 302) {
			                    return true;
			                }
			            }
			            return isRedirect;
			        }
			    });
			CloseableHttpClient httpClient = httpClientBuilder.build();
			System.out.println("bc-url: "+server +code);
			HttpGet getRequest = new HttpGet(server +code); 
			HttpResponse response = httpClient.execute(getRequest);			
			
			if (response.getStatusLine().getStatusCode() == 404) {
				throw new InvalidDoiException("Invalid DOI");
			}

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				   + response.getStatusLine().getStatusCode());
			}
	 
			BufferedReader br = new BufferedReader(
	                         new InputStreamReader((response.getEntity().getContent())));
	 
			StringBuffer buffer=new StringBuffer();
			String output;
			while ((output = br.readLine()) != null) {
				buffer.append(output);
			}
	 
			httpClient.close();
			return buffer.toString();
	 
		  } catch (ClientProtocolException e) {
	 
			  throw new RuntimeException(e);
	 
		  } catch (IOException e) {
	 
			  throw new RuntimeException(e);
		  }		
	}
	
	
	/*
	
	
	public Citation getCitationByDoi(String doi) throws InvalidDoiException {
		Citation result=new Citation();
		String json=this.getDataFromDoiServer(doi);
		System.out.println("bc-j "+json);
		if (json == null)
			return null;		
		
		try
		{
			
			JSONObject rjo = new JSONObject(json.trim());
			JSONObject jo = null;
			String json2 = rjo.getString("message");

			jo = new JSONObject(json2.trim());
			if (jo.has(TITLE)) {
				String tt = jo.getString(TITLE).toUpperCase();
				tt = tt.substring(2, tt.length()-2);
				result.setTitle(tt.toUpperCase());
			}
			if (jo.has(DOI))
				result.setDoi(jo.getString(DOI));
			if (jo.has(VOLUME))
				result.setVolume(jo.getString(VOLUME));
			if (jo.has(ISSUE))
				result.setIssue(jo.getString(ISSUE));
			if (jo.has(CONTAINER_TITLE)) {
				String tt = jo.getString(CONTAINER_TITLE).toUpperCase();
				if(tt.length() > 2) {
					tt = tt.substring(2, tt.length()-2);
					result.setJournal(tt.toUpperCase());
				}
			}
			if (jo.has(PUBLISHER))
				result.setPublisher(jo.getString(PUBLISHER));
			if (jo.has(PAGE))
			{
				String pages=jo.getString(PAGE);		
				result.setPages(pages);
			}
		
			if (jo.has(ISSUED))
			{
				JSONObject issuedJo=jo.getJSONObject(ISSUED);
				if (issuedJo != null) 
				{	
					if (issuedJo.has(DATE))
					{
						JSONArray dateParts=issuedJo.getJSONArray(DATE);
						if (dateParts != null)
						{	
							JSONArray yearArray=dateParts.getJSONArray(0);
							if (yearArray != null)
							{
								String year=yearArray.getString(0);
								if (year != null && year.isEmpty() == false)	
									{ if(isNumeric(year)) result.setPublicationYear(new Integer(year));}
							}
						}
					}
					else if (issuedJo.has(RAW))
					{
						String year=issuedJo.getString(RAW);
						if (year != null && year.isEmpty() == false)
							{ if(isNumeric(year)) result.setPublicationYear(new Integer(year));}						
					}
				}
			}
			
			if(jo.has(AUTHOR)) {
				JSONArray jsonarray = new JSONArray(jo.getString(AUTHOR));
					String authors = "";
					for (int i = 0; i < jsonarray.length(); i++) {
				
				    JSONObject jsonobject = jsonarray.getJSONObject(i);
				
				    String family = jsonobject.getString(FAMILY_NAME);
				 
				    String given = jsonobject.getString(GIVEN_NAME);
				    if((i+1) == jsonarray.length() && i!=0)  authors += " and "+given+" "+family;
				    else if(i != 0) authors += ", "+given+" "+family;
				    else authors += given+" "+family;
				    }	
					result.setDoiAuthors(authors);
			}	
			
			return result;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	protected String getDataFromDoiServer(final String doi) throws InvalidDoiException
	{
		try {
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		
			httpClientBuilder.setRedirectStrategy(new DefaultRedirectStrategy(){
			        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
			            boolean isRedirect=false;
			            try {
			                isRedirect = super.isRedirected(request, response, context );
			            } catch (org.apache.http.ProtocolException e) {
			                throw new RuntimeException(e);
			            }
			            if (!isRedirect) {
			                int responseCode = response.getStatusLine().getStatusCode();
			                if (responseCode == 301 || responseCode == 302) {
			                    return true;
			                }
			            }
			            return isRedirect;
			        }
			    });
			CloseableHttpClient httpClient = httpClientBuilder.build();
			
			
			HttpGet getRequest = new HttpGet(DOI_SERVER +URLEncoder.encode(doi, "UTF-8")); 


			HttpResponse response = httpClient.execute(getRequest);			
			
			if (response.getStatusLine().getStatusCode() == 404) {
				throw new InvalidDoiException("Invalid DOI");
			}

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				   + response.getStatusLine().getStatusCode());
			}
	 
			BufferedReader br = new BufferedReader(
	                         new InputStreamReader((response.getEntity().getContent())));
	 
			StringBuffer buffer=new StringBuffer();
			String output;
			while ((output = br.readLine()) != null) {
				buffer.append(output);
			}
	 
			httpClient.close();
			return buffer.toString();
	 
		  } catch (ClientProtocolException e) {
	 
			  throw new RuntimeException(e);
	 
		  } catch (IOException e) {
	 
			  throw new RuntimeException(e);
		  }		
	}
	
	public boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	}  
	*/
}
