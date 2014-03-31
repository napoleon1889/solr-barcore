package com.hugeinc.solr.search;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.stereotype.Component;

import com.hugeinc.web.form.DesiredGenderType;
import com.hugeinc.web.form.SearchForm;

@Component
public class BarCoreQueryFactoryImpl implements SolrQueryFactory<SearchForm>, BarCoreQueryFactory {

  public BarCoreQueryFactoryImpl() {
    super();
  }
  
  /* (non-Javadoc)
   * @see com.hugeinc.solr.search.BarCoreQueryFactory#createQuery(com.hugeinc.web.form.SearchForm)
   */
  @Override
  public SolrQuery createQuery(SearchForm searchForm) {
    SolrQuery query = new SolrQuery();
    query.setStart(1 + ((searchForm.getPageNumber() - 1) * 10));
    query.addField("*");
    query.addField("score");
    switch(searchForm.getBarSortType()) {
    case COURTSHIP_SCORE:
      if (searchForm.getDesiredGender() == DesiredGenderType.FEMALE) {
        query.addSort(new SolrQuery.SortClause("product(div(female, male), rating, unmarried)", ORDER.desc));
      } else {
        query.addSort(new SolrQuery.SortClause("product(div(male, female), rating, unmarried)", ORDER.desc));
      }
      break;
    case GOLD_DIGGER_SCORE:
      if (searchForm.getDesiredGender() == DesiredGenderType.FEMALE) {
          query.addSort(new SolrQuery.SortClause("product(div(female, male), rating, unmarried, income)", ORDER.desc));
      } else {
          query.addSort(new SolrQuery.SortClause("product(div(male, female), rating, unmarried, income)", ORDER.desc));
      }
      break;
    case RATING:
        query.addSort(new SolrQuery.SortClause("{!func}sum(votes, rating)", ORDER.desc));
      break;
    default:
        query.addSort(new SolrQuery.SortClause("product(female,unmarried)", ORDER.desc));
        
    }
    
    if (searchForm == null || StringUtils.isEmpty(searchForm.getQueryText())) {
      query.setQuery("*");      
    } else {
      query.setQuery(searchForm.getQueryText());
    }
    return query;
  }

}
