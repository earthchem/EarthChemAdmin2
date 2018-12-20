package org.earthChem.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
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
import org.earthChem.model.Citation;

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
public class CitationRest {
	public static final String DOI_SERVER="http://dx.doi.org/";
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

	public Citation getCitationByDoi(String doi) throws InvalidDoiException {
		Citation result=new Citation();
		String json=this.getDataFromDoiServer(doi);

		if (json == null)
			return null;
		JSONObject jo = null;
		try
		{
			jo = new JSONObject(json.trim());
			if (jo.has(TITLE))
				result.setTitle(jo.getString(TITLE).toUpperCase());
			if (jo.has(DOI))
				result.setDoi(jo.getString(DOI));
			if (jo.has(VOLUME))
				result.setVolume(jo.getString(VOLUME));
			if (jo.has(ISSUE))
				result.setIssue(jo.getString(ISSUE));
			if (jo.has(CONTAINER_TITLE))
				result.setJournal(jo.getString(CONTAINER_TITLE).toUpperCase());
			if (jo.has(PUBLISHER))
				result.setPublisher(jo.getString(PUBLISHER));
			if (jo.has(PAGE))
			{
				String pages=jo.getString(PAGE);		
		/*		if(page.indexOf('-') != -1) {
				String startPage=page.substring(0, page.indexOf('-'));
				if(isNumeric(startPage)) result.setFirstPage(new Integer(startPage));	
				String endPage=page.substring(page.indexOf('-') + 1);
				if(isNumeric(endPage)) result.setLastPage(new Integer(endPage));
				} else result.setFirstPage(new Integer(page));
				*/
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
	

	/****
	 * to get the reference from doi server using http client
	 * 
	 * @param doi
	 * @return
	 * @throws InvalidDoiException 
	 */
	protected String getDataFromDoiServer(final String doi) throws InvalidDoiException
	{
		try {
			 
			//  @SuppressWarnings("deprecation")
			//  DefaultHttpClient  httpClient = new DefaultHttpClient();
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		//	HttpClient httpClient = HttpClientBuilder.create().build();	
			
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
			getRequest.addHeader("accept","application/vnd.citationstyles.csl+json;q=1.0");
			
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
		//	httpClient.getConnectionManager().shutdown();
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
}
