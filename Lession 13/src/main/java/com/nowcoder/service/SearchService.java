package com.nowcoder.service;

import com.nowcoder.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nowcoder on 2016/8/28.
 */
@Service
public class SearchService {
    private static final String SOLR_URL = "http://127.0.0.1:8983/solr/#/test";

    private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();

    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";

    public List<Question> searchQuestion(String keyword, int offset, int count,
                                         String hlPre, String hlPos) throws Exception {
        List<Question> questionList = new ArrayList<>();
        SolrQuery query = new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        // 后一个参数为什么连起来没看懂？？？:这里设置的是查找的field,用逗号隔开，没毛病
        query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
        // 获得响应
        QueryResponse response = client.query(query);
        // 知道为啥是这样的么？因为solr查询出来的形式就是这样，每个doc就是一个 Map<String, List<String>>，Keystring是field,ListString是查询出来的field的具体内容（用LIST装起来，即使只有一个）
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Question q = new Question();
            q.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0) {
                    q.setContent(contentList.get(0));
                }
            }
            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                if (titleList.size() > 0) {
                    q.setTitle(titleList.get(0));
                }
            }
            questionList.add(q);
        }
        return questionList;
    }

    // 另一个业务：将用户提出的新问题加入solr:即建立该document的索引,什么时候用这个业务呢？：增加问题的时候，即提问的时候（questionController而非questionService,
    // 在questionController中加入ADD_question这个时间交给addQuestionhandler处理：即建立对该问题的solr索引）
    public boolean indexQuestion(int qid, String title, String content) throws Exception {
        SolrInputDocument doc =  new SolrInputDocument();
        doc.setField("id", qid);
        doc.setField(QUESTION_TITLE_FIELD, title);
        doc.setField(QUESTION_CONTENT_FIELD, content);
        UpdateResponse response = client.add(doc, 1000);
        return response != null && response.getStatus() == 0;
    }

}
