package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.earthChem.model.AuthorList;
import org.earthChem.model.Citation;
import org.earthChem.presentation.jsf.theme.Theme;
/*  query to generate table bai_mi
  
create table bai_mi as 
(select distinct m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM||'-'||decode(d.METHOD_COMMENT, null, 'N/A', d.METHOD_COMMENT) action, s.ITEM_MEASURED_NUM inum
from standard s, data_quality d, analysis a, METHOD m,  BATCH b 
where d.DATA_QUALITY_NUM = s.DATA_QUALITY_NUM and a.DATA_QUALITY_NUM = s.DATA_QUALITY_NUM and m.METHOD_NUM = d.METHOD_NUM and b.BATCH_NUM = a.BATCH_NUM 
union
select distinct m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM||'-'||decode(d.METHOD_COMMENT, null, 'N/A', d.METHOD_COMMENT) action, n.ITEM_MEASURED_num inum
from normalization n, NORMALIZATION_LIST nl, data_quality d, analysis a, BATCH b, method m
where b.BATCH_NUM = a.BATCH_NUM and m.METHOD_NUM = d.METHOD_NUM and a.DATA_QUALITY_NUM = d.DATA_QUALITY_NUM
and n.NORMALIZATION_NUM = nl.NORMALIZATION_NUM and d.DATA_QUALITY_NUM = nl.DATA_QUALITY_NUM
union
select distinct m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM||'-'||decode(d.METHOD_COMMENT, null, 'N/A', d.METHOD_COMMENT) action, s.ITEM_MEASURED_num inum
from method_precis s, data_quality d, analysis a, METHOD m,  BATCH b
where d.DATA_QUALITY_NUM = s.DATA_QUALITY_NUM and a.DATA_QUALITY_NUM = s.DATA_QUALITY_NUM 
and m.METHOD_NUM = d.METHOD_NUM and b.BATCH_NUM = a.BATCH_NUM 
union
select distinct m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM||'-'||decode(d.METHOD_COMMENT, null, 'N/A', d.METHOD_COMMENT) action, s.ITEM_MEASURED_num inum
from fract_correct s, fract_correct_list l, data_quality d, METHOD m, ITEM_MEASURED fi, BATCH b, analysis a 
where d.DATA_QUALITY_NUM = l.DATA_QUALITY_NUM  and b.BATCH_NUM = a.BATCH_NUM and a.DATA_QUALITY_NUM = d.DATA_QUALITY_NUM 
and m.METHOD_NUM = d.METHOD_NUM and  s.FRACT_CORRECT_NUM = l.FRACT_CORRECT_NUM 
and fi.ITEM_MEASURED_NUM = s.ITEM_FCORR_NUM 
)
*/

public class CitationDB {

	public static List<Citation> getCitationsWithStatus(String status) {
		List<Citation> list = new ArrayList<Citation>();
		String condition = "";
		if(!"ALL".equals(status)) condition = " and s.data_status='"+status+"'";
		String q = "select c.citation_num, e.citation_external_identifier,c.journal,substring(c.title from 1 for 27)||'...', s.data_status, s.internal_comment "+
				" from citation c "+
				" left join citation_external_identifier e on c.citation_num = e.citation_num "+
				" join  ec_status_info s on s.citation_num = c.citation_num "+
				" where c.status = 1 "+condition +" order by c.citation_num desc";

		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Citation c = new Citation();
			c.setCitationNum((Integer)arr[0]);
			c.setDoi((String)arr[1]);
			c.setJournal((String)arr[2]);
			c.setTitle((String)arr[3]);			
			c.setDataStatus((String)arr[4]);
			c.setInternalComment((String)arr[5]);
			list.add(c);
		}

		return list;
	}
	
	public static String saveCitation(Citation c, boolean isNew) {
		List<String> queries =  new ArrayList<String>();
		Integer citationNum = c.getCitationNum();
		String title = DBUtil.StringValue(c.getTitle());
		String publisher = DBUtil.StringValue(c.getPublisher());
		Integer year = c.getPublicationYear();
		String journal = DBUtil.StringValue(c.getJournal());
		String issue = DBUtil.StringValue(c.getIssue());
		String volume = DBUtil.StringValue(c.getVolume());
		String pages = DBUtil.StringValue(c.getPages());
		String btitle = DBUtil.StringValue(c.getBookTitle());
		String editor = DBUtil.StringValue(c.getBookEditor());
		String dataStatus = DBUtil.StringValue(c.getDataStatus());
		String internalComment = DBUtil.StringValue(c.getInternalComment());
		String publicComment = DBUtil.StringValue(c.getPublicComment());
		String authors = c.getDoiAuthors();
		String doi = c.getDoi();
		if(authors != null && !"".equals(authors.trim())) {
			authors = DBUtil.StringValue(authors);
		} else authors = null;
		String q = null;
		if(isNew){
			q= "INSERT INTO citation (citation_num, title, publisher, publication_year, journal, "+
					"issue, volume, book_title, book_editor, authors, pages) VALUES "+
					"("+citationNum+","+title+","+publisher+","+year+","+journal+","+issue+","+volume+","+btitle+","+editor+","+authors+","+pages+")";
			queries.add(q);
			q = "insert into ec_status_info values (nextval('ec_status_info_ec_status_info_num_seq'),"+citationNum+","+dataStatus+
					","+publicComment+","+internalComment+")";
			queries.add(q);
			if(doi != null && !"".equals(doi)) {
				q ="INSERT INTO citation_external_identifier values (nextval('citation_external_identifier_bridge_num_seq'),"+
					citationNum+",3,'"+c.getDoi()+"','http://doi.org/"+c.getDoi()+"')";
				queries.add(q);
			}
		}
		else {
			q = "update citation set title = "+title+		
			",publisher ="+publisher+
			",publication_year ="+year+	
			",journal = "+journal+
			",issue = "+issue+
			",volume ="+volume+
			",pages ="+pages+
			",book_title ="+btitle+
			",book_editor = "+editor+
			",authors = "+authors+
			",status =1 where citation_num = "+citationNum;

			queries.add(q);		
			q = "UPDATE citation_external_identifier SET citation_external_identifier = '"+c.getDoi()+
					"',citation_external_identifier_uri='http://doi.org/"+c.getDoi()+"' WHERE citation_num = "+citationNum;
			queries.add(q);		
			q = "UPDATE ec_status_info SET data_status = "+dataStatus+", public_comment = "+publicComment+
				", internal_comment = "+internalComment+" WHERE citation_num = "+citationNum;
			queries.add(q);	
		}
		
		return DBUtil.updateList(queries);
	}
	
	public static String inactiveCitation(Integer num) {
		String q = "update citation set status = 0 where citation_num = "+num;
		return DBUtil.update(q);
	}
	
	public static SelectItem  getSelectItems(Integer cititationNum) {
		String q ="select author_list_num from author_list where citation_num = "+cititationNum;
		List<Object[]> list  = DBUtil.getECList(q);
		if(list== null || list.size() == 0) {
			q = "select concat(citation_num,' (author_list is not created yet), '), c.publication_year from citation c where c.status <> 0 and citation_num = "+cititationNum;
		} else {
			q = "select concat(p.last_name,', ',p.first_name,', '), c.publication_year "+ 
				" from citation c  left join author_list a on c.citation_num = a.citation_num "+
				" left join person  p on p.person_num = a.person_num "+
				" where c.status <> 0 and c.citation_num= "+cititationNum+" order by a.author_order"; 
		}
		list =  DBUtil.getECList(q);
		int i = 0;
		String s = "";
		SelectItem st = null;
		if(list.size() == 0) {
			s = "No data found!";
			st  = new SelectItem(null,s,s);
		} else {
			Integer year = null;
			for(Object[] a: list) {
				if(++i == 4) { 
					s += "et al, ";
					break;
				}
				else s += (String) a[0];
				year = (Integer)a[1];
			}
			s += year;		
			st  = new SelectItem(cititationNum,s,s);
		}		
		return st;		
	}
	
	
	public static SelectItem[] getSearchResult(String type, String value) {
		String q = null;
		SelectItem[] siArr = null;
		if("number".equals(type)) {
			siArr = new SelectItem[1];
			siArr[0] = getSelectItems(new Integer(value));
		} else if ("title".equals(type)) {
			q = "select distinct c.citation_num, substring(c.title from 1 for 50)||'..., '||publication_year from citation c where c.status <> 0 and upper(c.title) like '%"+value.toUpperCase()+"%' order by c.citation_num desc";
			List<Object[]> titles = DBUtil.getECList(q);
			siArr = new SelectItem[titles.size()]; 			
			int i = 0;
			for(Object[] obj :titles) {
				siArr[i++] = new SelectItem((Integer)obj[0],(String)obj[1],(String)obj[1]);
			}
		} 		
		else { 
			if("author_year".equals(type)) {
			String [] arr = value.split(",");
				arr[0] = arr[0].trim();
				String year = "";
				if(arr.length > 1) {
					arr[1] = arr[1].trim();
					year = " and c.publication_year ="+arr[1];
				}
				String lastName = ((String)arr[0]).toUpperCase();
				q = "select distinct c.citation_num "+
						" from citation c, author_list a, person  p "+
						" where c.citation_num = a.citation_num  and p.person_num = a.person_num and c.status <> 0 "+
						" and upper(p.last_name) like '"+lastName+"%'" +year+" order by c.citation_num"; 						
			} else {
				q = "select distinct c.citation_num from citation c, citation_external_identifier e "+
					" where e.citation_num = c.citation_num and c.status <> 0 and e.citation_external_identifier = '"+value+"'";
			}
			List<Integer> ns = DBUtil.getIntegerList(q);
			siArr = new SelectItem[ns.size()]; 
			int i = 0;
			for(Integer num:ns) {
				siArr[i++] = getSelectItems(new Integer(num));
			}
		}
		
		return siArr;		
	}
	
	
	public static Citation getCitationByDoi(String doi) {
		return null;
	}
	
	public static Citation getCitation(Integer citationNum) {
		Citation c = new Citation();  
		String q = "select title,publisher,publication_year,journal,issue,volume,book_title,book_editor,"+
				" e.citation_external_identifier, st.data_status, st.internal_comment, st.public_comment,  concat(p.last_name,', ', p.first_name), c.authors, c.pages from citation c "+
				" left join author_list a on a.citation_num = c.citation_num and a.author_order = 1 left join person p on p.person_num = a.person_num "+
				" left join citation_external_identifier e on  c.citation_num=e.citation_num "+
				" left join ec_status_info st on st.citation_num = c.citation_num where c.citation_num ="+citationNum;
		Object[] arr = DBUtil.uniqueResult(q);
		c.setCitationNum(citationNum);
		c.setTitle((String)arr[0]);
		c.setPublisher((String)arr[1]);
		c.setPublicationYear((Integer)arr[2]);
		c.setJournal((String)arr[3]);
		c.setIssue((String)arr[4]);
		c.setVolume((String)arr[5]);
	//	c.setFirstPage((Integer)arr[6]);
	//	c.setLastPage((Integer)arr[7]);
		c.setBookTitle((String)arr[6]);
		c.setBookEditor((String)arr[7]);
		c.setDoi((String)arr[8]);
		c.setDataStatus((String)arr[9]);
		c.setInternalComment((String)arr[10]);
		c.setPublicComment((String)arr[11]);
		c.setDoiAuthors((String)arr[13]);
		String authors = ((String)arr[12]).trim();
		if(",".equals(authors)) {
			authors = null;
			c.setViewAuthors("DOI: "+arr[13]);	
		} else c.setViewAuthors(authors);	
		c.setAuthors(authors);
		c.setPages(""+arr[14]);
		
		return c;
	}	
	
	public static List<AuthorList> getAuthorList(Integer citationNum) {
		List<AuthorList> alist = new ArrayList<AuthorList>();
		String q = "select a.author_list_num, concat(p.last_name, ', ',p.first_name, ', '||p.middle_name), a.author_order, p.person_num from author_list a, citation c, person p "+
			" where a.citation_num = c.citation_num and p.person_num = a.person_num and c.citation_num="+citationNum+" order by a.author_order";
		List<Object[]> list =  DBUtil.getECList(q);
		for(Object[] arr: list) {
			AuthorList a = new AuthorList();
			a.setCitationNum(citationNum);
			a.setAuthorListNum((Integer)arr[0]);
			a.setFullName((String)arr[1]);
			a.setAuthorOrder((Integer)arr[2]);	
			a.setPersonNum((Integer)arr[3]);
			alist.add(a);
		}
		return alist;
	}
	
	public static String addAuthorList(Integer citationNum, Integer personNum, Integer order) {
		String q = "INSERT INTO author_list values ("+
			"nextval('author_list_author_list_num_seq'),"+citationNum+","+personNum+","+order+")";
		return DBUtil.update(q);
	}
	
	public static String deleteAuthorList(Integer authorListNum) {
		String q = "DELETE FROM author_list WHERE author_list_num = "+authorListNum;
		return DBUtil.update(q);
	}
	
	public static String updateAuthorOrder(List<AuthorList> list) {
		List<String> queries = new ArrayList<String>();
		if(list != null && list.size() > 0) {
			String q = "update author_list set author_order = (author_order+100) where citation_num= "+list.get(0).getCitationNum();
			queries.add(q);
			int i = 0;
			for(AuthorList a: list) {
				q = "update author_list set author_order = "+(++i)+" where author_list_num = "+a.getAuthorListNum();
				queries.add(q);
			}
		}
		return DBUtil.updateList(queries);
	}
	
	public static String save(Citation ct) {
		String msg = null;
		
			return msg;
	}
	
}
